package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.dao.HabitDao;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by mchauck on 11/9/17.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 *
 * singleton class
 */

public class HabitRepository {
    private static HabitRepository INSTANCE;
    private final HabitDao habitDao;

    private HabitRepository(HabitDao habitDao) {
        this.habitDao = habitDao;
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

    public static HabitRepository getInstance(AppDatabase db) {
        if (INSTANCE == null) {
            INSTANCE = new HabitRepository(db.habitDao());
        }
        return INSTANCE;
    }
}
