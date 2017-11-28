package com.stylepoints.habittracker.repository.local;

import android.arch.persistence.room.Database;;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.stylepoints.habittracker.repository.local.converter.LocalDateConverter;
import com.stylepoints.habittracker.repository.local.converter.BitmapConverter;
import com.stylepoints.habittracker.repository.local.converter.LocationConverter;
import com.stylepoints.habittracker.repository.local.converter.ScheduleConverter;
import com.stylepoints.habittracker.repository.local.dao.HabitDao;
import com.stylepoints.habittracker.repository.local.dao.HabitEventDao;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;


/**
 * https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
 *
 */
@Database(entities = {HabitEntity.class, HabitEventEntity.class}, version = 7)
@TypeConverters({LocalDateConverter.class, LocationConverter.class, ScheduleConverter.class, BitmapConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract HabitDao habitDao();
    public abstract HabitEventDao habitEventDao();

    // TODO: move database operations off main thread!
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "habit-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
