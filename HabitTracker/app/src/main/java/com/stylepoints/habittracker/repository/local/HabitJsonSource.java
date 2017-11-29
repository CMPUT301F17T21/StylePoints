package com.stylepoints.habittracker.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.stylepoints.habittracker.model.Habit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mchauck on 11/28/17.
 */

public class HabitJsonSource {
    private Gson gson;
    private static HabitJsonSource INSTANCE;
    private static String FILENAME = "habits.json";
    @Expose
    private List<Habit> habitList;

    private HabitJsonSource(Gson gson) {
        this.habitList = new ArrayList<>();
        this.gson = gson;
    }

    public static HabitJsonSource getInstance() {
        if (INSTANCE == null) {
            Gson gson = new GsonBuilder().create();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(FILENAME));
                INSTANCE = gson.fromJson(bufferedReader, HabitJsonSource.class);
                INSTANCE.setGson(gson);
            } catch (FileNotFoundException e) {
                // return an empty list
                INSTANCE = new HabitJsonSource(gson);
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Habit>> getHabits() {
        MutableLiveData<List<Habit>> data = new MutableLiveData<>();
        data.setValue(habitList);
        return data;
    }

    public LiveData<Habit> getHabit(String id) {
        MutableLiveData<Habit> data = new MutableLiveData<>();

        if (habitList.contains(id)) {
            data.setValue(habitList.get(habitList.indexOf(id)));
        }
        return data;
    }

    public Habit getHabitSync(String id) {
        if (habitList.contains(id)) {
            return habitList.get(habitList.indexOf(id));
        }
        return null;
    }

    public void deleteHabit(String id) {
        habitList.remove(id);
        saveToDisk();
    }

    public void updateHabit(String id, Habit habit) {
        habit.setElasticId(id);
        habitList.remove(id);
        habitList.add(habit);
        saveToDisk();
    }

    public void saveHabit(Habit habit) {
        habitList.add(habit);
        saveToDisk();
    }

    private void saveToDisk() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILENAME));
            gson.toJson(INSTANCE, HabitJsonSource.class, bufferedWriter);
        } catch (IOException e) {
            Log.e("DISK", "Error saving habit list to disk");
            e.printStackTrace();
        }
    }

    private void setGson(Gson gson) {
        this.gson = gson;
    }
}
