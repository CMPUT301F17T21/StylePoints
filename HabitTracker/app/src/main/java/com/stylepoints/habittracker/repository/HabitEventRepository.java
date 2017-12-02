package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.local.EventJsonSource;

import java.util.List;

public class HabitEventRepository {
    private static HabitEventRepository INSTANCE;
    private final EventJsonSource source;

    private HabitEventRepository(EventJsonSource source) {
        this.source = source;
    }

    private HabitEventRepository(Context context) {
        this.source = EventJsonSource.getInstance(context);
    }

    public LiveData<List<HabitEvent>> getEventsByHabitId(String habitId) {
        LiveData<List<HabitEvent>> filtered = Transformations.map(getAllEvents(), eventList -> {
            for (HabitEvent event : eventList) {
                if (!event.getHabitId().equals(habitId)) {
                    eventList.remove(event);
                }
            }
            return eventList;
        });
        return filtered;
    }

    public LiveData<List<HabitEvent>> getAllEvents() {
        return source.getEvents();
    }

    public LiveData<HabitEvent> getEventById(String eventId) {
        return source.getEvent(eventId);
    }

    // TODO: change to off main thread
    public void saveEvent(HabitEvent event) {
        // TODO: make sure that the habit this references is actually in the database
        // TODO: verify data is correct (comment length is not too long)
        source.saveEvent(event);
    }

    public void saveList(List<HabitEvent> list){
        source.saveEvents(list);
    }

    public void deleteAll(){
        source.deleteAllEvents();
    }

    public static HabitEventRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HabitEventRepository(context);
        }
        return INSTANCE;
    }
}
