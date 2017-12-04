package com.stylepoints.habittracker.repository.remote;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.google.gson.Gson;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handles creating, updating, and deleting HabitEvents on the server.
 *
 * Defines a Job that is executed by the Android JobScheduler which ensures that
 * offline activity is replicated to the server, even if we lose network connection.
 * The JobScheduler will execute the job the next time we get network.
 *
 * @author Mackenzie Hauck
 */
public class RemoteEventJob extends JobService {
    private static final String TAG = "RemoteEventJob";

    /**
     * Called on the main thread to kick off the job. Setup the job synchronously, then
     * the actual network call is done async.
     *
     * @param jobParameters contains all info about job (jobId, habitEventId, operation)
     * @return true if the job is still active, false if job is finished
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        String eventId = jobParameters.getExtras().getString("EVENT_ID");
        int operation = jobParameters.getExtras().getInt("OPERATION", -1);
        Log.d(TAG, "Job started: " + eventId + " operation: " + String.valueOf(operation) + " jobId: " + jobParameters.getJobId());

        if (eventId == null || operation == -1) {
            Log.e(TAG, "Got bad data from extras, aborting");
            return false;
        }

        HabitEvent event = HabitEventRepository.getInstance(getApplicationContext()).getEventSync(eventId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ElasticSearch elastic = retrofit.create(ElasticSearch.class);

        Callback<ElasticRequestStatus> cb = new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                Log.d(TAG, eventId + " success " + response.toString());
                jobFinished(jobParameters, false);
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "RemoteEventJob failed, rescheduling: " + t.getMessage());
                jobFinished(jobParameters, true);
            }
        };

        switch (operation) {
            case Util.CREATE:
                if (HabitRepository.getInstance(getApplicationContext()).getHabitSync(event.getHabitId()) == null) {
                    Log.w(TAG, "Can not create remote event if the local habit has been deleted");
                    jobFinished(jobParameters, false);
                }
                Log.d(TAG, "Attempting remote create: " + eventId);
                elastic.createEventWithId(eventId, event).enqueue(cb);
                break;
            case Util.UPDATE:
                if (HabitRepository.getInstance(getApplicationContext()).getHabitSync(event.getHabitId()) == null) {
                    Log.w(TAG, "Can not update remote event if the local habit has been deleted");
                    jobFinished(jobParameters, false);
                }
                Log.d(TAG, "Attempting remote update: " + eventId);
                elastic.updateEvent(eventId, event).enqueue(cb);
                break;
            case Util.DELETE:
                Log.d(TAG, "Attempting remote delete: " + eventId);
                elastic.deleteEvent(eventId).enqueue(cb);
                break;
            default:
                Log.d(TAG, "Got bad operation: " + String.valueOf(operation));
                break;
        }

        // return true to signal that we aren't done with the job yet
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        // reschedule the job if it is interrupted (lose network while in middle of job)
        return true;
    }
}
