package com.stylepoints.habittracker;


import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;
import com.stylepoints.habittracker.repository.remote.ElasticTweetResponse;
import com.stylepoints.habittracker.repository.remote.Tweet;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class ElasticTest {
    @Test
    public void getTweetTest() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/testing/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ElasticSearch elastic = retrofit.create(ElasticSearch.class);

        // .execute() is sync, .enqueue() is async
        //Call<Tweet> call = elastic.getTweetById("kYpocdGpSUORYaxSHm6UZQ");
        //Response<Tweet> response = elastic.getTweetById("kYpocdGpSUORYaxSHm6UZQ").execute();
        //Tweet tweet = response.body();

        Response<ElasticResponse<Tweet>> response = elastic.getTweetById("kYpocdGpSUORYaxSHm6UZQ").execute();
        Tweet tweet = response.body().getSource();

        System.out.println(response);
        System.out.println(tweet);

        assertEquals("(update)Happy Valentines Day! (I am so lonely ;_;)", tweet.getMessage());
    }

    @Test
    public void searchTweetTest() throws Exception {
        // TODO: this only returns the first 10 results!

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/testing/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ElasticSearch elastic = retrofit.create(ElasticSearch.class);

        Response<ElasticTweetResponse> response = elastic.searchTweets("message:sad").execute();
        System.out.println(response);
        System.out.println(response.body());

        List<Tweet> tweetList = response.body().getList();
        for (Tweet tweet : tweetList) {
            System.out.println(tweet);
        }
        assertEquals(10, tweetList.size());
    }

    @Test
    public void addTweetTest() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/testing/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ElasticSearch elastic = retrofit.create(ElasticSearch.class);

        Tweet tweet = new Tweet(LocalDate.now().toString(), "test tweet!");

        Response<ElasticRequestStatus> response = elastic.saveTweet(tweet).execute();
        System.out.println(response);
        ElasticRequestStatus status = response.body();
        System.out.println(status);

        assertTrue(status.wasCreated());
        assertEquals("testing", status.getIndex());
        assertEquals(1, status.getVersion());
        assertEquals("tweet", status.getType());
    }
}
