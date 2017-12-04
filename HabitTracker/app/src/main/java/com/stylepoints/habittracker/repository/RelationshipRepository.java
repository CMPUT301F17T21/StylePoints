package com.stylepoints.habittracker.repository;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;

import com.stylepoints.habittracker.model.Relationship;
import com.stylepoints.habittracker.repository.local.HabitJsonSource;
import com.stylepoints.habittracker.repository.remote.ElasticRelationshipListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;
import com.stylepoints.habittracker.repository.remote.RelationshipUpdateStatus;
import com.stylepoints.habittracker.repository.remote.RemoteHabitJob;
import com.stylepoints.habittracker.viewmodel.SocialFeed.FollowingAsyncCallback;
import com.stylepoints.habittracker.viewmodel.SocialFeed.RelationshipExists;
import com.stylepoints.habittracker.viewmodel.SocialFeed.UserNotFound;

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

    public void followRequest(String follower, String followee, FollowingAsyncCallback callback){
        callback.setLoading();
        checkRelationshipExists(follower, followee, callback);
    }

    public void getFollowers(String username, FollowingAsyncCallback callback){
        callback.setLoading();
        elastic.searchRelationship("followee:" + username + "status:" + Relationship.FOLLOW_ACCEPTED).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){

                } else {

                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getFollowing(String username, FollowingAsyncCallback callback){
        callback.setLoading();
        elastic.searchRelationship("follower:" + username + "&status:" + Relationship.FOLLOW_ACCEPTED).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){

                } else {

                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getFollowRequests(String username, FollowingAsyncCallback callback){
        callback.setLoading();
        elastic.searchRelationship("followee:" + username + "&status:" + Relationship.FOLLOW_REQUESTED).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){

                } else {

                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void checkRelationshipExists(String follower, String followee, FollowingAsyncCallback callback){
        elastic.searchRelationship("follower:" + follower + "&followee:" + followee + "&status:" + Relationship.FOLLOW_ACCEPTED).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){
                    callback.onError(new RelationshipExists());
                } else {
                    newRelationship(follower, followee, callback);
                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void newRelationship(String follower, String followee, FollowingAsyncCallback callback){
        elastic.createRelationship(new Relationship(follower, followee)).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()){
                    callback.onSuccess();
                } else{
                    callback.onError(new UserNotFound());
                }
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void findRelationshipForResponse(String follower, String followee, FollowingAsyncCallback callback, Integer status){
        elastic.searchRelationship("follower:" + follower + "&followee:" + followee + "&status:" + Relationship.FOLLOW_REQUESTED).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){
                    callback.onError(new RelationshipExists());
                } else {
                    respondToRelationshipRequest(response.body().getList().get(0).getElasticId(), status);
                    // Here could be a bug. Get could be 1 indexed instead of 0
                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void respondToRelationshipRequest(String id, Integer status){
        RelationshipUpdateStatus rUS = new RelationshipUpdateStatus(status);
        elastic.updateRelationshipStatus(id, rUS).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {

            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {

            }
        });
    }

    public void deleteRelationship(String id){
        elastic.deleteRelationship(id).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                //
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                //TODO: Do something for failure
            }
        });
    }

    public static RelationshipRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RelationshipRepository(context);
        }
        return INSTANCE;
    }


}
