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
 * Created by mchauck on 11/9/17.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 *
 * singleton class
 */

public class HabitRepository {
    private static final String TAG = "HabitRepository";
    private static HabitRepository INSTANCE;
    private final HabitJsonSource source;
    private ElasticSearch elastic;
    private JobScheduler jobScheduler;
    private Context context;

    private HabitRepository(HabitJsonSource source) {
        this.source = source;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    private HabitRepository(Context context) {
        this.source = HabitJsonSource.getInstance(context);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
        this.context = context.getApplicationContext();
        this.jobScheduler = (JobScheduler) context.getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public LiveData<Habit> getHabit(String habitId) {
        if (source.contains(habitId)){
            return source.getHabit(habitId);
        } else {
            return getRemoteHabit(habitId);
        }
    }

    public Habit getHabitSync(String habitId) {
        return source.getHabitSync(habitId);
    }

    public LiveData<List<Habit>> loadAll() {
        return source.getHabits();
    }

    public void save(Habit habit) {
        // save habit locally
        source.saveHabit(habit);

        // save habit in elastic search async
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("HABIT_ID", habit.getElasticId());
        bundle.putInt("OPERATION", Util.CREATE);
        jobScheduler.schedule(new JobInfo.Builder(Util.getUniqueJobId(jobScheduler),
                new ComponentName(context, RemoteHabitJob.class))
                .setExtras(bundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        );
    }

    public void update(String id, Habit habit) {
        // update local version
        source.updateHabit(id, habit);
        // update remote version
        removePreviousJobs(id);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("HABIT_ID", habit.getElasticId());
        bundle.putInt("OPERATION", Util.UPDATE);
        jobScheduler.schedule(new JobInfo.Builder(Util.getUniqueJobId(jobScheduler),
                new ComponentName(context, RemoteHabitJob.class))
                .setExtras(bundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        );
    }

    public void delete(String id) {
        // delete from local
        source.deleteHabit(id);
        // delete from remote
        removePreviousJobs(id);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("HABIT_ID", id);
        bundle.putInt("OPERATION", Util.DELETE);
        jobScheduler.schedule(new JobInfo.Builder(Util.getUniqueJobId(jobScheduler),
                new ComponentName(context, RemoteHabitJob.class))
                .setExtras(bundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        );
    }

    public void saveList(List<Habit> habitList){
        for (Habit h: habitList){
            save(h);
        }
    }

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

    public static HabitRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HabitRepository(context);
        }
        return INSTANCE;
    }
}
