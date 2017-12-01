package com.stylepoints.habittracker.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.stylepoints.habittracker.model.Habit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mchauck on 11/28/17.
 */

public class HabitJsonSource {
    private static final String TAG = "HabitJsonSource";
    private Gson gson;
    private Context context;
    private static HabitJsonSource INSTANCE;
    private static String FILENAME = "habits.json";
    //@Expose
    private List<Habit> habitList;
    private MutableLiveData<List<Habit>> liveHabits;

    private HabitJsonSource(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new Gson();

        try {
            FileInputStream fis = this.context.openFileInput(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();
            habitList = gson.fromJson(bufferedReader, listType);

            if (habitList == null) {
                Log.w(TAG, "Gson returned a null list!");
                // problem reading file?
                habitList = new ArrayList<>();
                File file = new File(this.context.getFilesDir(), FILENAME);
                if(file.delete()) {
                    Log.i(TAG, "Corrupt habit json file deleted");
                } else {
                    Log.i(TAG, "Habit json file not deleted");
                }
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "habit json file not found, using empty list");
            habitList = new ArrayList<>();
        }

        liveHabits = new MutableLiveData<>();
        liveHabits.setValue(this.habitList);
    }

    public static HabitJsonSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HabitJsonSource(context);
        }
        return INSTANCE;
    }

    public LiveData<List<Habit>> getHabits() {
        liveHabits.setValue(habitList);
        return liveHabits;
    }

    public LiveData<Habit> getHabit(String id) {

        return Transformations.map(getHabits(), hList -> {
            for (Habit habit : hList) {
                if (habit.getElasticId().equals(id)) {
                    return habit;
                }
            }
            // not found!
            return null;
        });
    }

    public Habit getHabitSync(String id) {
        for (Habit habit : habitList) {
            if (habit.getElasticId().equals(id)) {
                return habit;
            }
        }
        return null;
    }

    public void deleteHabit(String id) {
        for (Habit habit : habitList) {
            if (habit.getElasticId().equals(id)) {
                habitList.remove(habit);
                break;
            }
        }
        saveToDisk();
    }

    public void updateHabit(String id, Habit habit) {
        habit.setElasticId(id);
        for (Habit h : habitList) {
            if (h.getElasticId().equals(id)) {
                habitList.remove(h);
                habitList.add(habit);
                //h = habit;
                break;
            }
        }
        saveToDisk();
    }

    public void saveHabit(Habit habit) {
        habitList.add(habit);
        saveToDisk();
    }

    private void saveToDisk() {
        try {
            FileOutputStream fos = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
            gson.toJson(this.habitList, bufferedWriter);
            bufferedWriter.flush();
            fos.close();
            Log.d(TAG, "saved habitList to disk");
        } catch (IOException e) {
            Log.e(TAG, "Error saving habit list to disk" + e.toString());
            e.printStackTrace();
        }
        // update our livedata, so our observers are notified of the change
        liveHabits.setValue(habitList);
    }
}
