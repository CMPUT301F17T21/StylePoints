package com.stylepoints.habittracker.repository.remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mchauck on 11/23/17.
 */

public interface ElasticSearch {
    @GET("tweet/{id}")
    Call<ElasticResponse<Tweet>> getTweetById(@Path("id") String id);

    @GET("tweet/_search")
    Call<ElasticTweetResponse> searchTweets(@Query("q") String fieldAndTerms);

    @POST("tweet/")
    Call<ElasticRequestStatus> saveTweet(@Body Tweet tweet);

    @PUT("tweet/{id}")
    Call<ElasticRequestStatus> updateTweet(@Path("id") String id, @Body Tweet tweet);
}
