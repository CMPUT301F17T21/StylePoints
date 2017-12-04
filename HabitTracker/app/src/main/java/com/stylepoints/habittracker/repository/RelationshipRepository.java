package com.stylepoints.habittracker.repository;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import com.stylepoints.habittracker.model.Relationship;
import com.stylepoints.habittracker.repository.local.HabitJsonSource;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;
import com.stylepoints.habittracker.repository.remote.RemoteHabitJob;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nikosomos on 2017-12-03.
 */

public class RelationshipRepository {

    private static RelationshipRepository INSTANCE;
    private ElasticSearch elastic;
    private JobScheduler jobScheduler;
    private Context context;

    private RelationshipRepository(Context context){
        this.context = context.getApplicationContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
        this.jobScheduler = (JobScheduler) this.context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public void newRelationship(String follower, String followee){
        elastic.createRelationship(new Relationship(follower, followee)).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {

            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {

            }
        });
    }

    public void acceptRelationship(String id){

    }

    public void deleteRelationship(String id){

    }

    public static RelationshipRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RelationshipRepository(context);
        }
        return INSTANCE;
    }
}
