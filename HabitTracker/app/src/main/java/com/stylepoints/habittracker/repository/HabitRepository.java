package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.dao.HabitDao;

import javax.inject.Singleton;

/**
 * Created by mchauck on 11/9/17.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

@Singleton
public class HabitRepository {
    private final HabitDao habitDao;

    public HabitRepository(HabitDao habitDao) {
        this.habitDao = habitDao;
    }

    public LiveData<Habit> getHabit(String habitId) {
        return habitDao.load(habitId);
    }
}
