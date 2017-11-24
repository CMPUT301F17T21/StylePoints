package com.stylepoints.habittracker;

import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import org.junit.Before;
import org.junit.Test;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mackenzie on 2017-11-23.
 */

public class EventElasticTest {
    private ElasticSearch elastic;

    @Before
    public void initElastic() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        elastic = retrofit.create(ElasticSearch.class);
    }

    @Test
    public void getEventTest() throws Exception {

    }
}