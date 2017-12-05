package com.stylepoints.habittracker.repository;

import android.app.job.JobScheduler;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.stylepoints.habittracker.model.Relationship;
import com.stylepoints.habittracker.repository.remote.ElasticRelationshipListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;
import com.stylepoints.habittracker.repository.remote.RelationshipUpdateStatus;
import com.stylepoints.habittracker.viewmodel.SocialFeed.FollowingAsyncCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public LiveData<List<String>> getFollowers(String username, FollowingAsyncCallback callback){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();
        String query = "followee:" + username +  " AND status:" + Relationship.FOLLOW_ACCEPTED;
        elastic.searchRelationship(query).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){
                    List<String> usernames = new ArrayList<>();
                    for (Relationship r: response.body().getList()){
                        usernames.add(r.getFollower());
                    }
                    data.setValue(usernames);
                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
        return data;
    }

    public LiveData<List<String>> getFollowing(String username, FollowingAsyncCallback callback){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();
        String query = "follower:" + username + " AND status:" + Relationship.FOLLOW_ACCEPTED;
        elastic.searchRelationship(query).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){
                    List<String> usernames = new ArrayList<>();
                    for (Relationship r: response.body().getList()){
                        usernames.add(r.getFollowee());
                    }
                    data.setValue(usernames);
                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
        return data;
    }

    public LiveData<List<String>> getFollowRequests(String username, FollowingAsyncCallback callback){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();
        String query = "followee:" + username + " AND status:" + Relationship.FOLLOW_REQUESTED;
        elastic.searchRelationship(query).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()){
                    List<String> usernames = new ArrayList<>();
                    for (Relationship r: response.body().getList()){
                        usernames.add(r.getFollower());
                    }
                    data.setValue(usernames);
                }
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
        return data;
    }

    public void checkRelationshipExists(String follower, String followee, FollowingAsyncCallback callback){
        if (follower.equals(followee)){
            callback.onError(new Throwable("Cannot follow self"));
            return;
        }
        String query = "followee:" + followee + " AND follower:" + follower;
        elastic.searchRelationship(query).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getNumHits() > 0) {
                        callback.onError(new Throwable("Relationship Exists"));
                        return;
                    }
                }
                newRelationship(follower, followee, callback);
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    private void newRelationship(String follower, String followee, FollowingAsyncCallback callback){
        elastic.createRelationship(new Relationship(follower, followee)).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()){
                    Log.i("aaa", response.toString());
                    callback.onSuccess();
                } else{
                    callback.onError(new IOException("Error in contacting server"));
                }
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void findRelationshipForResponse(String follower, String followee, FollowingAsyncCallback callback, String status){
        String query = "followee:" + followee + " AND follower:" + follower + " AND status:" + Relationship.FOLLOW_REQUESTED;
        elastic.searchRelationship(query).enqueue(new Callback<ElasticRelationshipListResponse>() {
            @Override
            public void onResponse(Call<ElasticRelationshipListResponse> call, Response<ElasticRelationshipListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getNumHits() != 0) {
                        respondToRelationshipRequest(response.body().getList().get(0).getElasticId(), status, callback);
                        return;
                    }
                }
                callback.onError(new Throwable("Relationship does not exist"));
            }

            @Override
            public void onFailure(Call<ElasticRelationshipListResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void respondToRelationshipRequest(String id, String status, FollowingAsyncCallback callback){
        RelationshipUpdateStatus rUS = new RelationshipUpdateStatus(status);
        elastic.updateRelationshipStatus(id, rUS).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()){
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                callback.onError(t);
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
