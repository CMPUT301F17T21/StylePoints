package com.stylepoints.habittracker.repository.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.stylepoints.habittracker.model.Habit;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by mchauck on 11/9/17.
 */

@Dao
public interface HabitDao {
    @Insert(onConflict = REPLACE)
    void save(Habit habit);

    @Query("SELECT * FROM habit WHERE id = :habitId")
    LiveData<Habit> load(String habitId);
}
