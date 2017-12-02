package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.User;
import com.stylepoints.habittracker.repository.local.HabitJsonSource;
import com.stylepoints.habittracker.repository.local.UserJsonSource;
import com.stylepoints.habittracker.repository.remote.ElasticEventListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticHabitListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import java.io.IOException;
import java.lang.reflect.Parameter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by nikosomos on 2017-11-28.
 */

public class UserRepository extends AsyncTask<String, Void, Integer>{

    private static final String TAG = "UserRepository";
    private static UserRepository INSTANCE = new UserRepository();
    private ElasticSearch elastic;
    private HabitRepository hR;
    private HabitEventRepository hER;

    private UserRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    public void sethR(HabitRepository hR) {
        this.hR = hR;
    }

    public void sethER(HabitEventRepository hER) {
        this.hER = hER;
    }





    public Boolean checkUserExists(String username) throws IOException{
        Response<ElasticResponse<User>> response = elastic.getUserByName(username).execute();
        if (response.isSuccessful()){
            return response.body().wasFound();
        }
        throw new IOException("Elastic Search Request Unsuccessful");
    }

    public void loadUser(String username) throws IOException {
        if (checkUserExists(username)) {
            Response<ElasticHabitListResponse> responseHabits = elastic.searchHabit("user:" + username).execute();
            Response<ElasticEventListResponse> responseEvents = elastic.searchEvent("user:" + username).execute();
            if (responseHabits.isSuccessful()) {
                throw new IOException("Elastic Search Request Unsuccessful");
            }
            this.hR.saveList(responseHabits.body().getList());
            if (responseEvents.isSuccessful()) {
                throw new IOException("Elastic Search Request Unsuccessful");
            }
            this.hER.saveList(responseEvents.body().getList());
        } else {
            elastic.createUser(username).execute();
        }

    }

    public void deleteUser(){

    }

    public static UserRepository getINSTANCE(){
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }

    @Override
    protected Integer doInBackground(String... str) {
        try {
            loadUser(str[0]);
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
