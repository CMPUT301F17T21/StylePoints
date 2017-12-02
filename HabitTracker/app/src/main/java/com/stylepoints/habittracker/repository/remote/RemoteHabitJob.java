package com.stylepoints.habittracker.repository.remote;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RemoteHabitJob extends JobService {
    private static final String TAG = "RemoteHabitJob";
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // called when job is started. runs on main thread!
        String habitId = jobParameters.getExtras().getString("HABIT_ID");
        int operation = jobParameters.getExtras().getInt("OPERATION", -1);

        if (habitId == null || operation == -1) {
            Log.e(TAG, "Got bad data from extras");
            // signal the job is 'done'
            return false;
        }

        Habit habit = HabitRepository.getInstance(getApplicationContext()).getHabitSync(habitId);
        if (habit == null && operation != Util.DELETE) {
            Log.w(TAG, "Got a null habit");
            return false;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ElasticSearch elastic = retrofit.create(ElasticSearch.class);

        Callback<ElasticRequestStatus> cb = new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                Log.d(TAG, habitId + " success: " + response.toString());
                // we should be finished our job, since the server responded to us?
                jobFinished(jobParameters, false);
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "RemoteHabitJob failed, rescheduling");
                // something went wrong, reschedule the job
                jobFinished(jobParameters, true);
            }
        };

        switch (operation) {
            case Util.CREATE:
                Log.d(TAG, "Attempting remote create: " + habitId);
                elastic.createHabitWithId(habitId, habit).enqueue(cb);
                break;
            case Util.UPDATE:
                Log.d(TAG, "Attempting remote update: " + habitId);
                elastic.updateHabit(habitId, habit).enqueue(cb);
                break;
            case Util.DELETE:
                Log.d(TAG, "Attempting remote delete: " + habitId);
                elastic.deleteHabit(habitId).enqueue(cb);
                break;
            default:
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
