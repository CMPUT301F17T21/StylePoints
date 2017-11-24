package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.dao.HabitDao;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.remote.ElasticHabitListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mchauck on 11/9/17.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 *
 * singleton class
 */

public class HabitRepository {
    private static HabitRepository INSTANCE;
    private final HabitDao habitDao;
    private ElasticSearch elastic;

    private HabitRepository(HabitDao habitDao) {
        this.habitDao = habitDao;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    public LiveData<HabitEntity> getHabit(int habitId) {
        return habitDao.load(habitId);
    }

    public HabitEntity getHabitSync(int habitId) {
        return habitDao.loadSync(habitId);
    }

    public LiveData<List<HabitEntity>> loadAll() {
        return habitDao.loadAllHabits();
    }

    public int getHabitIdFromType(String type) {
        return habitDao.findIdOfHabitType(type);
    }

    // TODO: change to off main thread
    public long save(HabitEntity habit) {
        return habitDao.save(habit);
    }

    public LiveData<HabitEntity> getRemoteHabit(String habitId) {
        final MutableLiveData<HabitEntity> data = new MutableLiveData<>();
        elastic.getHabitById(habitId).enqueue(new Callback<ElasticResponse<HabitEntity>>() {
            @Override
            public void onResponse(Call<ElasticResponse<HabitEntity>> call, Response<ElasticResponse<HabitEntity>> response) {
                data.setValue(response.body().getSource());
            }

            @Override
            public void onFailure(Call<ElasticResponse<HabitEntity>> call, Throwable t) {
                // TODO: add failure case
            }
        });
        return data;
    }

    public LiveData<List<HabitEntity>> getUsersHabits(String elasticUsername) {
        final MutableLiveData<List<HabitEntity>> data = new MutableLiveData<>();
        elastic.searchHabit("user:" + elasticUsername).enqueue(new Callback<ElasticHabitListResponse>() {
            @Override
            public void onResponse(Call<ElasticHabitListResponse> call, Response<ElasticHabitListResponse> response) {
                data.setValue(response.body().getList());
            }

            @Override
            public void onFailure(Call<ElasticHabitListResponse> call, Throwable t) {
                // TODO: add failure case
            }
        });
        return data;
    }

    public static HabitRepository getInstance(AppDatabase db) {
        if (INSTANCE == null) {
            INSTANCE = new HabitRepository(db.habitDao());
        }
        return INSTANCE;
    }
}
