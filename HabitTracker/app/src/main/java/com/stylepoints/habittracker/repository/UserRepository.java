package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
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
import java.util.List;

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
    private ElasticSearch elastic;
    private HabitRepository hR;
    private HabitEventRepository hER;
    private List<Habit> habitList;
    private List<HabitEvent> eventList;

    public UserRepository(HabitRepository hR, HabitEventRepository hER) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
        this.hR = hR;
        this.hER = hER;
    }





    public Boolean checkUserExists(String username) throws IOException{
        Response<ElasticResponse<User>> response = elastic.getUserByName(username).execute();
        if (response.body().wasFound()){
            return true;
        } else {
            return false;
        }
    }

    public void loadUser(String username) throws IOException {

        if (checkUserExists(username)) {
            Response<ElasticHabitListResponse> responseHabits = elastic.searchHabit("user:" + username).execute();
            if (!responseHabits.isSuccessful()) {
                throw new IOException("Elastic Search Request Unsuccessful");
            }
            habitList = responseHabits.body().getList();

            Response<ElasticEventListResponse> responseEvents = elastic.searchEvent("user:" + username).execute();
            if (!responseEvents.isSuccessful()) {
                throw new IOException("Elastic Search Request Unsuccessful");
            }
            eventList = responseEvents.body().getList();

        } else {
            elastic.createUser(username).execute();
        }

    }

    public void deleteUser(){

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

    @Override
    protected void onPostExecute(Integer i){
        this.hR.saveList(this.habitList);
        this.hER.saveList(this.eventList);
    }

}
