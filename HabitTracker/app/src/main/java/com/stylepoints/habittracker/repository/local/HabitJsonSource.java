package com.stylepoints.habittracker.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stylepoints.habittracker.model.Habit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A local data store for Habits. Backed by a json file that stores
 * the list of Habits. Saving to file is done async.
 *
 * @author Mackenzie Hauck
 * @see Habit
 */
public class HabitJsonSource {
    private static final String TAG = "HabitJsonSource";
    private Gson gson;
    private Context context;
    private static HabitJsonSource INSTANCE;
    private static String FILENAME = "habits.json";
    private List<Habit> habitList;
    private MutableLiveData<List<Habit>> liveHabits;

    private HabitJsonSource(Context context) {
        // This must use getApplicationContext() to help prevent memory leaks
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

    public void deleteAllHabits(){
        habitList.clear();
        saveToDisk();
    }

    public void updateHabit(String id, Habit habit) {
        Log.i(TAG, "Updating habit " + id);
        habit.setElasticId(id);
        for (int i = 0; i < habitList.size(); i++) {
            if (habitList.get(i).getElasticId().equals(id)) {
                Log.d(TAG, "Found habit to replace");
                habitList.set(i, habit);
                break;
            }
        }
        saveToDisk();
    }

    public void saveHabit(Habit habit) {
        Log.d(TAG, "Adding a habit with id: " + habit.getElasticId());
        habitList.add(habit);
        saveToDisk();
    }

    public void saveHabits(List<Habit> habits) {
        habitList.addAll(habits);
        saveToDisk();
    }

    public boolean contains(String id) {
        for (Habit habit : habitList) {
            if (habit.getElasticId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHabitWithType(String type) {
        for (Habit habit : habitList) {
            if (habit.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private void saveToDisk() {
        liveHabits.setValue(habitList);
        try {
            FileOutputStream fos = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            AsyncFileWriter task = new AsyncFileWriter();
            task.doInBackground(new AsyncFileWriterParams(habitList, fos));
        } catch (FileNotFoundException e) {
            // should never get this exception, openFileOutput will create the file if
            // it is not found, this exception is only thrown when the directory can't be found
            Log.e(TAG, "Could not find application directory");
        }
    }
}
