package com.stylepoints.habittracker.repository;

import android.util.Log;

import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;

import java.util.Date;

/**
 * Generate some test data for the database.
 *
 * Note: an int id can not be 0 in SQlite
 */
public class DatabaseInitUtil {
    public static void initializeDbWithTestData(AppDatabase db) {
        HabitEntity habit1 = new HabitEntity("exercise", "get healthy", new Date(), "MWF");
        habit1.setId(1);
        db.habitDao().save(habit1);
        

        HabitEventEntity event1 = new HabitEventEntity(1, new Date(), "ran 5km today");
        event1.setHabitId(1); // this habit event belongs to the habit with id 0 (the exercise habit above)
        db.habitEventDao().save(event1);

        HabitEventEntity event2 = new HabitEventEntity(2, new Date(), "went to gym");
        int habitId = db.habitDao().findIdOfHabitType("exercise"); // finding the id by string
        event2.setHabitId(habitId);
        db.habitEventDao().save(event2);
    }
}
