package com.stylepoints.habittracker.repository;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.model.User;
import com.stylepoints.habittracker.repository.remote.ElasticEventListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticHabitListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;
import com.stylepoints.habittracker.viewmodel.CentralHubActivity.NewUserActivity;
import com.stylepoints.habittracker.viewmodel.CentralHubActivity.UserAsyncCallback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by nikosomos on 2017-11-28.
 */

public class UserRepository{

    private static final String TAG = "UserRepository";
    private ElasticSearch elastic;
    private HabitRepository habitRepo;
    private HabitEventRepository habitEventRepo;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserRepository(HabitRepository habitRepo, HabitEventRepository habitEventRepo, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
        this.habitRepo = habitRepo;
        this.habitEventRepo = habitEventRepo;
        this.context = context;
        this.pref = context.getSharedPreferences("stylepoints", 0);
        this.editor = pref.edit();
    }

    //Local
    public String getUserName(){
        return pref.getString("username", "");
    }

    public Boolean isUserNameSet(){
        return pref.contains("username");
    }

    public void setUserName(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public void logOutUser(){
        editor.remove("username");
        Intent getUserNameIntent = new Intent(context, NewUserActivity.class);
        context.startActivity(getUserNameIntent);
    }

    //Remote
    public void logInUser(String username, UserAsyncCallback callback) {
        callback.setLoading();
        habitRepo.deleteAll();
        habitEventRepo.deleteAll();
        setUserName(username);
        try {
            getRemoteUser(username, callback);
        } catch (IOException e) {
            callback.setError(e);
        }
    }


    public void deleteUser(String username, UserAsyncCallback callback){
        callback.setLoading();
        logOutUser();


    }



    public Float getHabitsPossiblePoints(String habitId){
        Habit habit = habitRepo.getHabitSync(habitId);
        LocalDate tmp = habit.getStartDate();
        LocalDate today = LocalDate.now();
        Float points = new Float(0);
        while (!tmp.equals(today)){
            if (habit.getDaysActive().contains(tmp.getDayOfWeek())){
                points += 1;
            }
            tmp = tmp.plusDays(1);
        }
        return points;
    }

    public Float getHabitsCompletePoints(String habitId){
        List<HabitEvent> events = habitEventRepo.getEventsByHabit(habitId);
        Habit habit = habitRepo.getHabitSync(habitId);
        Float points = new Float(0);
        if (events == null){
            return new Float(0);
        }
        for (HabitEvent e: events){
            if (habit.getDaysActive().contains(e.getDate().getDayOfWeek())){
                points += 1;
            }
        }
        return points;
    }

    public Float getHabitsPercent(String habitId){
        return getHabitsCompletePoints(habitId)/getHabitsPossiblePoints(habitId);
    }

    public Float getHabitsUncompletePoints(String habitId){
        return getHabitsPossiblePoints(habitId)-getHabitsCompletePoints(habitId);
    }


    //Get User after login
    private void getRemoteUser(String username, UserAsyncCallback callback) throws IOException{
        elastic.getUserByName(username).enqueue(new Callback<ElasticResponse<User>>() {
            @Override
            public void onResponse(Call<ElasticResponse<User>> call, Response<ElasticResponse<User>> response) {
                try {
                    if (response.isSuccessful()) {
                        loadRemoteUserHabits(username, callback);
                    } else {
                        createRemoteUser(username, callback);
                    }
                } catch (IOException e){
                    callback.setError(e);
                }
            }

            @Override
            public void onFailure(Call<ElasticResponse<User>> call, Throwable t) {
                callback.setError(t);
            }
        });
    }

    private void loadRemoteUserHabits(String username, UserAsyncCallback callback) throws IOException {
        elastic.searchHabit("username:" + username).enqueue(new Callback<ElasticHabitListResponse>() {
            @Override
            public void onResponse(Call<ElasticHabitListResponse> call, Response<ElasticHabitListResponse> response) {
                if (response.isSuccessful()) {
                    saveUserHabits(response.body().getList(), callback);
                }
                try {
                    loadRemoteUserEvents(username, callback);
                } catch (IOException e) {
                    callback.setError(e);
                }
            }

            @Override
            public void onFailure(Call<ElasticHabitListResponse> call, Throwable t) {
                callback.setError(t);
            }
        });
    }

    private void loadRemoteUserEvents(String username, UserAsyncCallback callback) throws IOException {

            elastic.searchEvent("username:" + username).enqueue(new Callback<ElasticEventListResponse>() {
                @Override
                public void onResponse(Call<ElasticEventListResponse> call, Response<ElasticEventListResponse> response) {
                    if (response.isSuccessful()){
                        saveUserEvents(response.body().getList(), callback);
                    }
                    callback.setSuccess();
                }

                @Override
                public void onFailure(Call<ElasticEventListResponse> call, Throwable t) {
                    callback.setError(t);
                }
            });
    }

    private void createRemoteUser(String username, UserAsyncCallback callback) throws IOException {
        elastic.createUser(username, new User(username)).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()){
                    callback.setSuccess();
                } else {
                    callback.setError(new Throwable("Elasticsearch create user failed"));
                }
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                callback.setError(t);
            }
        });
    }

    private void saveUserHabits(List<Habit> habits, UserAsyncCallback callback){
        habitRepo.saveList(habits);
    }

    private void saveUserEvents(List<HabitEvent> events, UserAsyncCallback callback){
        habitEventRepo.saveList(events);
    }



}
