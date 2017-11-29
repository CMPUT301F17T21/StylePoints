package com.stylepoints.habittracker.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.stylepoints.habittracker.model.HabitEvent;

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

public class EventJsonSource {
    private Gson gson;
    private static EventJsonSource INSTANCE;
    private static String FILENAME = "events.json";
    @Expose
    private List<HabitEvent> eventList;

    public EventJsonSource(Gson gson) {
        eventList = new ArrayList<>();
        this.gson = gson;
    }

    public static EventJsonSource getInstance() {
        if (INSTANCE == null) {
            Gson gson = new GsonBuilder().create();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(FILENAME));
                INSTANCE = gson.fromJson(bufferedReader, EventJsonSource.class);
                INSTANCE.setGson(gson);
            } catch (FileNotFoundException e) {
                // return an empty list
                INSTANCE = new EventJsonSource(gson);
            }
        }
        return INSTANCE;
    }

    public LiveData<List<HabitEvent>> getEvents() {
        MutableLiveData<List<HabitEvent>> data = new MutableLiveData<>();
        data.setValue(eventList);
        return data;
    }

    public LiveData<HabitEvent> getEvent(String id) {
        MutableLiveData<HabitEvent> data = new MutableLiveData<>();
        if (eventList.contains(id)) {
            data.setValue(eventList.get(eventList.indexOf(id)));
        }
        return data;
    }

    public void deleteEvent(String id) {
        eventList.remove(id);
        saveToDisk();
    }

    public void updateEvent(String id, HabitEvent event) {
        event.setElasticId(id);
        eventList.remove(id);
        eventList.add(event);
        saveToDisk();
    }


    public void saveEvent(HabitEvent event) {
        eventList.add(event);
        saveToDisk();
    }

    private void saveToDisk() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILENAME));
            gson.toJson(INSTANCE, EventJsonSource.class, bufferedWriter);
        } catch (IOException e) {
            Log.e("DISK", "Error saving event list to disk");
            e.printStackTrace();
        }
    }

    private void setGson(Gson gson) {
        this.gson = gson;
    }
}
