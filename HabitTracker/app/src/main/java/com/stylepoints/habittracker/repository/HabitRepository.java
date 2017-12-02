package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.HabitJsonSource;
import com.stylepoints.habittracker.repository.remote.ElasticHabitListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
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
    private static final String TAG = "HabitRepository";
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

    private HabitRepository(Context context) {
        this.source = HabitJsonSource.getInstance(context);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    public LiveData<Habit> getHabit(String habitId) {
        if (source.contains(habitId)){
            return source.getHabit(habitId);
        } else {
            return getRemoteHabit(habitId);
        }
    }

    public Habit getHabitSync(String habitId) {
        return source.getHabitSync(habitId);
    }

    public LiveData<List<Habit>> loadAll() {
        return source.getHabits();
    }

    public void save(Habit habit) {
        // save habit locally
        source.saveHabit(habit);
        // save habit in elastic search async
        elastic.createHabitWithId(habit.getElasticId(), habit).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "create habit network request success");
                    if (response.body().wasCreated()) {
                        Log.i(TAG, "Habit successfully stored in remote " + habit.getElasticId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "network request failed saving habit " + habit.getElasticId());
            }
        });
    }

    public void update(String id, Habit habit) {
        // update local version
        source.updateHabit(id, habit);
        // update remote version
        elastic.updateHabit(id, habit).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "habit update network request success");
                    if (response.body().wasCreated()) {
                        Log.i(TAG, "Habit successfully stored in remote " + habit.getElasticId());
                    }
                }
            }
            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "network request failed to update habit " + habit.getElasticId());
            }
        });
    }

    public void delete(String id) {
        // delete from local
        source.deleteHabit(id);
        // delete from remote
        elastic.deleteHabit(id).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "habit delete network request success");
                    Log.d(TAG, response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "network request failed for habit delete " + id);
            }
        });
    }

    public void deleteAll(){
        source.deleteAllHabits();
    }

    public void saveList(List<Habit> habitList){
        source.saveHabits(habitList);
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

    public static HabitRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HabitRepository(context);
        }
        return INSTANCE;
    }
}
