package com.stylepoints.habittracker.repository;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.HabitJsonSource;
import com.stylepoints.habittracker.repository.remote.ElasticHabitListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;
import com.stylepoints.habittracker.repository.remote.RemoteHabitJob;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A singleton class that acts as an abstraction layer between the user interface
 * and either the local Habit storage, or the remote Habit storage.
 *
 * The data returned to the UI are instances of LiveData, which is basically an observable
 * class that is also aware of android specific lifecycle information.
 */
public class HabitRepository {
    private static final String TAG = "HabitRepository";
    private static HabitRepository INSTANCE;
    private final HabitJsonSource source;
    private ElasticSearch elastic;
    private JobScheduler jobScheduler;
    private Context context;

    private HabitRepository(Context context) {
        this.context = context.getApplicationContext();
        this.source = HabitJsonSource.getInstance(this.context);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
        this.jobScheduler = (JobScheduler) this.context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    /**
     * Get observable Habit object with id.
     * @param habitId unique id for the habit we wish to get
     * @return an observable LiveData<Habit>
     */
    public LiveData<Habit> getHabit(String habitId) {
        if (source.contains(habitId)){
            return source.getHabit(habitId);
        } else {
            return getRemoteHabit(habitId);
        }
    }

    /**
     * Get a Habit synchronously
     * @param habitId unique id for habit we wish to get
     * @return the Habit with id habitId, or null if not found
     */
    public Habit getHabitSync(String habitId) {
        return source.getHabitSync(habitId);
    }

    /**
     * Get observable list of habits. Automatically updates when backend updates
     * @return An observable list of all local users habits
     */
    public LiveData<List<Habit>> loadAll() {
        return source.getHabits();
    }

    /**
     * Create a new Habit and save it both locally and remotely.
     * @param habit The habit we wish to add to local and remote storage
     * @throws NonUniqueException when a Habit with that `type` already exists
     */
    public void save(Habit habit) throws NonUniqueException {
        if (source.hasHabitWithType(habit.getType())) {
            throw new NonUniqueException();
        }
        // save habit locally
        source.saveHabit(habit);
        // save habit in elastic search async
        remoteOperation(habit.getElasticId(), Util.CREATE);
    }

    /**
     * Update the Habit with id to what is passed in in habit. Changes propagate
     * locally and remotely.
     * @param id unique habit id used to specify which habit we are updating
     * @param habit What the old habit should be updated to
     */
    public void update(String id, Habit habit) {
        // update local version
        source.updateHabit(id, habit);
        // update remote version
        remoteOperation(id, Util.UPDATE);
    }

    /**
     * Delete the Habit specified by id locally and remotely.
     * @param id unique habit id
     */
    public void delete(String id) {
        // delete from local
        source.deleteHabit(id);
        HabitEventRepository.getInstance(context).deleteEventsByHabitId(id);
        // delete from remote
        remoteOperation(id, Util.DELETE);
    }

    public void deleteAll(){
        source.deleteAllHabits();
    }

    /**
     * Bulk save habits to local storage
     * @param habitList list of habits to add to local storage
     */
    public void saveList(List<Habit> habitList){
        source.saveHabits(habitList);
    }

    /**
     * Get's a habit from the remote server
     * @param habitId unique id of a habit
     * @return an observable LiveData<Habit>
     */
    public LiveData<Habit> getRemoteHabit(String habitId) {
        final MutableLiveData<Habit> data = new MutableLiveData<>();
        elastic.getHabitById(habitId).enqueue(new Callback<ElasticResponse<Habit>>() {
            @Override
            public void onResponse(Call<ElasticResponse<Habit>> call, Response<ElasticResponse<Habit>> response) {
                data.setValue(response.body().getSource());
            }

            @Override
            public void onFailure(Call<ElasticResponse<Habit>> call, Throwable t) {
                // TODO: add failure case
            }
        });
        return data;
    }

    /**
     * Get all Habits for a specific username
     * @param elasticUsername which users habits we should fetch
     * @return observable list of habits, that updates when network request completes
     */
    public LiveData<List<Habit>> getUsersHabits(String elasticUsername) {
        final MutableLiveData<List<Habit>> data = new MutableLiveData<>();
        elastic.searchHabit("user:" + elasticUsername).enqueue(new Callback<ElasticHabitListResponse>() {
            @Override
            public void onResponse(Call<ElasticHabitListResponse> call, Response<ElasticHabitListResponse> response) {
                data.setValue(response.body().getList());
            }

            @Override
            public void onFailure(Call<ElasticHabitListResponse> call, Throwable t) {
                // TODO: add failure case
            }
        });
        return data;
    }

    /**
     * This function removes all previous RemoteHabitJob that have the same
     * elasticId. This must be done because the JobScheduler does not execute
     * jobs in order, so if we create a Habit offline then delete it offline
     * there is no guarantee that it will be done in that order, causing a
     * mismatch between the local and remote data.
     *
     * @author Mackenzie Hauck
     * @param id the elasticId of a Habit
     * @see RemoteHabitJob
     */
    private void removePreviousJobs(String id) {
        String jobHabitId = null;
        for (JobInfo job : jobScheduler.getAllPendingJobs()) {
            jobHabitId = job.getExtras().getString("HABIT_ID");
            if (jobHabitId != null && jobHabitId.equals(id)) {
                // remove previous job that was scheduled with this id
                // so we don't do something like "update" and then "create"
                jobScheduler.cancel(job.getId());
                // should only ever be one job with that habitId
                return;
            }
        }
    }

    /**
     * Schedules a RemoteHabitJob that guarantees that the network request will
     * be done. Persists across app closes as well
     * @param id the elasticId of the Habit we are doing an operation on
     * @param operation an integer representing the operation (Util.CREATE, Util.UPDATE, Util.DELETE)
     * @see RemoteHabitJob
     */
    private void remoteOperation(String id, int operation) {
        removePreviousJobs(id);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("HABIT_ID", id);
        bundle.putInt("OPERATION", operation);
        jobScheduler.schedule(new JobInfo.Builder(Util.getUniqueJobId(jobScheduler),
                new ComponentName(context, RemoteHabitJob.class))
                .setExtras(bundle) // send eventId, and operation (create, update, or delete)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // must have network
                .build()
        );
    }

    public static HabitRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HabitRepository(context);
        }
        return INSTANCE;
    }
}
