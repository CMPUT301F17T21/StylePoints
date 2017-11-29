package com.stylepoints.habittracker.repository.remote;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mchauck on 11/23/17.
 */

public interface ElasticSearch {
    // ====== Habits =====
    @GET("habit/{id}")
    Call<ElasticResponse<Habit>> getHabitById(@Path("id") String id);

    @GET("habit/_search")
    Call<ElasticHabitListResponse> searchHabit(@Query("q") String fieldAndTerm);

    @POST("habit/")
    Call<ElasticRequestStatus> saveHabit(@Body Habit habit);

    @PUT("habit/{id}")
    Call<ElasticRequestStatus> updateHabit(@Path("id") String id, @Body Habit habit);

    @DELETE("habit/{id}")
    Call<ElasticRequestStatus> deleteHabit(@Path("id") String id);

    // ====== Events =====
    @GET("event/{id}")
    Call<ElasticResponse<HabitEvent>> getEventById(@Path("id") String id);

    @POST("event/")
    Call<ElasticRequestStatus> saveEvent(@Body HabitEvent habit);

    @PUT("event/{id}")
    Call<ElasticRequestStatus> updateEvent(@Path("id") String id, @Body HabitEvent habit);

    @DELETE("event/{id}")
    Call<ElasticRequestStatus> deleteEvent(@Path("id") String id);

    @GET("event/_search")
    Call<ElasticEventListResponse> searchEvent(@Query("q") String fieldAndTerms);
}
