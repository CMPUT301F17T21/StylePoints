package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;

import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.dao.HabitEventDao;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;

import java.util.List;

public class HabitEventRepository {
    private static HabitEventRepository INSTANCE;
    private final HabitEventDao dao;

    private HabitEventRepository(HabitEventDao dao) {
        this.dao = dao;
    }

    public LiveData<List<HabitEventEntity>> getEventsByHabitId(int habitId) {
        return dao.loadEventsByHabitId(habitId);
    }
    public HabitEventEntity getEventSync(int eventId) {
        return dao.loadSync(eventId);
    }

    public LiveData<List<HabitEventEntity>> getAllEvents() {
        return dao.loadAllEvents();
    }

    public LiveData<HabitEventEntity> getEventById(int eventId) {
        return dao.load(eventId);
    }

    // TODO: change to off main thread
    public long save(HabitEventEntity event) {
        // TODO: make sure that the habit this references is actually in the database
        // TODO: verify data is correct (comment length is not too long)
        return dao.save(event);
    }

    public int delete(HabitEventEntity event) {
        // TODO: make sure that the habit this references is actually in the database
        // TODO: verify data is correct (comment length is not too long)
        return dao.delete(event);
    }

    public static HabitEventRepository getInstance(AppDatabase db) {
        if (INSTANCE == null) {
            INSTANCE = new HabitEventRepository(db.habitEventDao());
        }
        return INSTANCE;
    }
}
