package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.HabitJsonSource;
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
    private final HabitJsonSource source;
    private ElasticSearch elastic;

    private HabitRepository(HabitJsonSource source) {
        this.source = source;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    private HabitRepository() {
        this.source = HabitJsonSource.getInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    public LiveData<Habit> getHabit(String habitId) {
        return source.getHabit(habitId);
    }

    public Habit getHabitSync(String habitId) {
        return source.getHabitSync(habitId);
    }

    public LiveData<List<Habit>> loadAll() {
        return source.getHabits();
    }

    /*
    public int getHabitIdFromType(String type) {
        return habitDao.findIdOfHabitType(type);
    }*/

    // TODO: change to off main thread
    public void save(Habit habit) {
        source.saveHabit(habit);
    }

    public LiveData<Habit> getRemoteHabit(String habitId) {
        final MutableLiveData<Habit> data = new MutableLiveData<>();
        elastic.getHabitById(habitId).enqueue(new Callback<ElasticResponse<Habit>>() {
            @Override
            public void onResponse(Call<ElasticResponse<Habit>> call, Response<ElasticResponse<Habit>> response) {
                data.setValue(response.body().getSource());
            }

            @Override
            public void onFailure(Call<ElasticResponse<Habit>> call, Throwable t) {
                // TODO: add failure case
            }
        });
        return data;
    }

    public LiveData<List<Habit>> getUsersHabits(String elasticUsername) {
        final MutableLiveData<List<Habit>> data = new MutableLiveData<>();
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

    public static HabitRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HabitRepository();
        }
        return INSTANCE;
    }
}
