package com.stylepoints.habittracker;


import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
    public static final String TEST_SERVER_BASE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints_test/";

    public static ElasticSearch getElasticInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.TEST_SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ElasticSearch.class);
    }
}
