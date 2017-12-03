package com.stylepoints.habittracker.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stylepoints.habittracker.model.HabitEvent;

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
 * A local data store for HabitEvents. Backed by a json file that stores
 * the list of HabitEvents. Saving to file is done async.
 *
 * @author Mackenzie Hauck
 * @see HabitEvent
 */
public class EventJsonSource {
    private static final String TAG = "EventJsonSource";
    private static EventJsonSource INSTANCE;
    private static String FILENAME = "events.json";

    private Gson gson;
    private Context context;
    private List<HabitEvent> eventList;
    private MutableLiveData<List<HabitEvent>> liveEvents;

    private EventJsonSource(Context context) {
        // This must use getApplicationContext() to help prevent memory leaks
        this.context = context.getApplicationContext();
        this.gson = new Gson();

        try {
            FileInputStream fis = this.context.openFileInput(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            Type listType = new TypeToken<ArrayList<HabitEvent>>(){}.getType();
            eventList = gson.fromJson(bufferedReader, listType);

            if (eventList == null) {
                Log.w(TAG, "Gson returned a null list");
                eventList = new ArrayList<>();
                File file = new File(this.context.getFilesDir(), FILENAME);
                if(file.delete()) {
                    Log.i(TAG, "Corrupt event json file deleted");
                } else {
                    Log.i(TAG, "Event json file not deleted!");
                }
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Event json file not found, using empty list");
            eventList = new ArrayList<>();
        }

        liveEvents = new MutableLiveData<>();
        liveEvents.setValue(eventList);
    }

    public static EventJsonSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new EventJsonSource(context);
        }
        return INSTANCE;
    }

    public LiveData<List<HabitEvent>> getEvents() {
        liveEvents.setValue(eventList);
        return liveEvents;
    }

    public LiveData<HabitEvent> getEvent(String id) {
        return Transformations.map(getEvents(), eList -> {
            for (HabitEvent event : eList) {
                if (event.getElasticId().equals(id)) {
                    return event;
                }
            }
            // couldn't find that event in our list
            return null;
        });
    }

    public HabitEvent getEventSync(String id) {
        for (HabitEvent event : eventList) {
            if (event.getElasticId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public List<String> getEventIdsForHabitId(String habitId) {
        List<String> list = new ArrayList<>();
        for (HabitEvent event : eventList) {
            if (event.getHabitId().equals(habitId)) {
                list.add(event.getElasticId());
            }
        }
        return list;
    }

    public void deleteEvent(String id) {
        for (HabitEvent event : eventList) {
            if (event.getElasticId().equals(id)) {
                eventList.remove(event);
                break;
            }
        }
        saveToDisk();
    }

    public void deleteAllEvents(){
        eventList.clear();
        saveToDisk();
    }

    public void updateEvent(String id, HabitEvent event) {
        Log.i(TAG, "Updating HabitEvent " + id);
        event.setElasticId(id);
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getElasticId().equals(id)) {
                eventList.set(i, event);
                break;
            }
        }
        saveToDisk();
    }


    public void saveEvent(HabitEvent event) {
        Log.d(TAG, event.toString());
        eventList.add(event);
        saveToDisk();
    }

    public void saveEvents(List<HabitEvent> events){
        eventList.addAll(events);
        saveToDisk();
    }

    private void saveToDisk() {
        liveEvents.setValue(eventList);
        try {
            FileOutputStream fos = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            AsyncFileWriter task = new AsyncFileWriter();
            task.doInBackground(new AsyncFileWriterParams(eventList, fos));
        } catch (FileNotFoundException e) {
            // should never get this exception, openFileOutput will create the file if
            // it is not found, this exception is only thrown when the directory can't be found
            Log.e(TAG, "Could not find application directory");
        }
    }
}
