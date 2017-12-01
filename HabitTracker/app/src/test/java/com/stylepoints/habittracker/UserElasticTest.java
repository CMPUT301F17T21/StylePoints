package com.stylepoints.habittracker;

import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import org.junit.Before;
import org.junit.Test;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nikosomos on 2017-11-28.
 */

public class UserElasticTest {
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
    public void testNewUser(){

    }

    @Test
    public void testLoadUser(){

    }

    @Test
    public void testDeleteUser(){

    }

}
