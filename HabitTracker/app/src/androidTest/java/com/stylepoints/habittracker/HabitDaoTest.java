package com.stylepoints.habittracker;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public class HabitDaoTest {
    private AppDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(), AppDatabase.class)
                .build();
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }

    @Test
    public void insertAndGetHabit() {
        HabitEntity habit = new HabitEntity("Exercise", "get healthy", LocalDate.now(), DayOfWeek.MONDAY);
        int habitId = (int) db.habitDao().save(habit);

        assertEquals("Habit not retrieved from repo", habit.getType(), db.habitDao().loadSync(habitId).getType());
    }

    @Test
    public void getHabitIdFromType() throws Exception{
        HabitEntity habit = new HabitEntity("Exercise", "get healthy", LocalDate.now(), DayOfWeek.MONDAY);
        int habitId = (int) db.habitDao().save(habit);

        int idLookup = db.habitDao().findIdOfHabitType("Exercise");

        assertEquals(habitId, idLookup);
    }

    @Test
    public void getHabitList() throws Exception {
        HabitEntity habit = new HabitEntity("Exercise", "get healthy", LocalDate.now(), DayOfWeek.MONDAY);
        db.habitDao().save(habit);

        HabitEntity habit2 = new HabitEntity("Exercise", "get healthy", LocalDate.now(), DayOfWeek.MONDAY);
        db.habitDao().save(habit2);

        List<HabitEntity> habits = LiveDataTestUtil.getValue(db.habitDao().loadAllHabits());
        assertEquals(habits.size(), 2);
    }
}
