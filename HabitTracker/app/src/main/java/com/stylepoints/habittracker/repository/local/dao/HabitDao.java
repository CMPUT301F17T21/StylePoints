package com.stylepoints.habittracker.repository.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

import java.util.List;


/**
 * Created by mchauck on 11/9/17.
 */

@Dao
public interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void save(HabitEntity habit);

    @Query("SELECT * FROM habits WHERE type = :habitId")
    LiveData<HabitEntity> load(String habitId);

    @Query("SELECT * FROM habits")
    LiveData<List<HabitEntity>> loadAllHabits();
}
