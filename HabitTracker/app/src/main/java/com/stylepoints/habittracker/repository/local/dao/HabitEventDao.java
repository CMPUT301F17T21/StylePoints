package com.stylepoints.habittracker.repository.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;

import java.util.List;


@Dao
public interface HabitEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(HabitEventEntity event);

    @Delete
    int delete(HabitEventEntity event);

    @Query("SELECT * FROM habit_events WHERE id = :eventId")
    LiveData<HabitEventEntity> load(int eventId);

    @Query("SELECT * FROM habit_events WHERE id = :eventId")
    HabitEventEntity loadSync(int eventId);

    @Query("SELECT * FROM habit_events WHERE habitId = :habitId")
    LiveData<List<HabitEventEntity>> loadEventsByHabitId(int habitId);

    @Query("SELECT * FROM habit_events")
    LiveData<List<HabitEventEntity>> loadAllEvents();

    // TODO: add query for searching in comments

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    // 2017-11-10
    @Query("DELETE FROM habit_events")
    void nukeTable();
}
