package com.stylepoints.habittracker.repository.remote;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.model.Relationship;
import com.stylepoints.habittracker.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The interface that defines how to call Elastic Search.
 * Uses the Retrofit2 library
 */
public interface ElasticSearch {
    // ====== Habits =====
    @GET("habit/{id}")
    Call<ElasticResponse<Habit>> getHabitById(@Path("id") String id);

    @GET("habit/_search")
    Call<ElasticHabitListResponse> searchHabit(@Query("q") String fieldAndTerm);

    @GET("habit/_search")
    Call<ElasticHabitListResponse> searchHabit(@Query("q") String fieldAndTerm1, @Query("q") String fieldAndTerm2);

    @POST("habit/")
    Call<ElasticRequestStatus> saveHabit(@Body Habit habit);

    @PUT("habit/{id}")
    Call<ElasticRequestStatus> createHabitWithId(@Path("id") String id, @Body Habit habit);

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

    @PUT("event/{id}")
    Call<ElasticRequestStatus> createEventWithId(@Path("id") String id, @Body HabitEvent event);

    @DELETE("event/{id}")
    Call<ElasticRequestStatus> deleteEvent(@Path("id") String id);

    @GET("event/_search")
    Call<ElasticEventListResponse> searchEvent(@Query("q") String fieldAndTerms);

    // ===== User =====
    @GET("user/{username}")
    Call<ElasticResponse<User>> getUserByName(@Path("username") String username);

    @PUT("user/{username}")
    Call<ElasticRequestStatus> createUser(@Path("username") String username, @Body User user);

    // ===== Relationship =====
    @POST("relationship/")
    Call<ElasticRequestStatus> createRelationship(@Body Relationship relationship);

    @PUT("relationship/{id}")
    Call<ElasticRequestStatus> editRelationship(@Path("id") String id, @Body Relationship relationship);

    @DELETE("relationship/{id}")
    Call<ElasticRequestStatus> deleteRelationship(@Path("id") String id);
}
