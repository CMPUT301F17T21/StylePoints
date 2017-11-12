package com.stylepoints.habittracker.repository;

import android.util.Log;

import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;
import com.stylepoints.habittracker.repository.local.entity.WeekSchEntity;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

/**
 * Generate some test data for the database.
 *
 * Note: an int id can not be 0 in SQlite
 */
public class DatabaseInitUtil {
    public static void initializeDbWithTestData(AppDatabase db) {
        // clear out the tables so they are empty
        clearDb(db);

//        Boolean[] hb1Sch = {false, true, false, true, false, true, false};
//        HabitEntity habit1 = new HabitEntity("exercise", "get healthy", new Date(), (new WeekSchEntity(Arrays.asList(hb1Sch))).toString());
        HabitEntity habit1 = new HabitEntity("exercise", "get healthy", new Date(), "Monday, Wednesday");
        habit1.setId(1);
        db.habitDao().save(habit1);

//        Boolean[] hb2Sch = {false, false, false, false, false, false, false};
//        HabitEntity habit2 = new HabitEntity("floss", "make dentist happy", new Date(), (new WeekSchEntity(Arrays.asList(hb2Sch))).toString());
        HabitEntity habit2 = new HabitEntity("floss", "make dentist happy", new Date(), "");
        habit2.setId(2);
        db.habitDao().save(habit2);


        HabitEventEntity event1 = new HabitEventEntity(1, new Date(), "ran 5km today");
        event1.setHabitId(1); // this habit event belongs to the habit with id 0 (the exercise habit above)
        db.habitEventDao().save(event1);

        HabitEventEntity event2 = new HabitEventEntity(2, new Date(), "went to gym");
        int habitId = db.habitDao().findIdOfHabitType("exercise"); // finding the id by string
        event2.setHabitId(habitId);
        db.habitEventDao().save(event2);
    }

    private static void clearDb(AppDatabase db) {
        db.habitDao().nukeTable();
        db.habitEventDao().nukeTable();
    }
}
