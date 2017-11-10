package com.stylepoints.habittracker.repository.local;

import android.arch.persistence.room.Database;;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.stylepoints.habittracker.repository.local.converter.DateConverter;
import com.stylepoints.habittracker.repository.local.dao.HabitDao;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;


/**
 * https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
 *
 */
@Database(entities = {HabitEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract HabitDao habitDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "habit-database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
