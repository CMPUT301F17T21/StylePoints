package com.stylepoints.habittracker.repository.local;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

import com.stylepoints.habittracker.repository.local.dao.HabitDao;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

@Database(entities = {HabitEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();

}
