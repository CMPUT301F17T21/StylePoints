package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.MutableLiveData;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.User;
import com.stylepoints.habittracker.repository.local.HabitJsonSource;
import com.stylepoints.habittracker.repository.local.UserJsonSource;
import com.stylepoints.habittracker.repository.remote.ElasticHabitListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by nikosomos on 2017-11-28.
 */

public class UserRepository {

    private static UserRepository INSTANCE = new UserRepository();
    private ElasticSearch elastic;

    private UserRepository(UserJsonSource source) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    private UserRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    public Boolean checkNewUser(String username){
        try {
            Response<ElasticResponse<User>> response = elastic.getUserByName(username).execute();
            if (response.isSuccessful()){
                return response.body().wasFound();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadUser(String username){
        try {
            if (checkNewUser(username)) {
                Response<ElasticHabitListResponse> response = elastic.searchHabit("user:" + username).execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(){

    }
}
