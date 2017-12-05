package com.stylepoints.habittracker;

import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;

import retrofit2.Call;
import retrofit2.http.DELETE;

/**
 * Created by mchauck on 12/4/17.
 */

public interface ElasticAdminCalls {
    @DELETE("habit")
    Call<ElasticRequestStatus> deleteEveryonesHabits();

    @DELETE("event")
    Call<ElasticRequestStatus> deleteEveryonesEvents();

    @DELETE("user")
    Call<ElasticRequestStatus> deleteAllUsers();
}
