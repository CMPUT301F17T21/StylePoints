package com.stylepoints.habittracker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;

import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;


public class HabitEventDaoTest {
    private AppDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase.class).build();
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }

    @Test
    public void insertAndGetEvent() throws Exception {
        HabitEntity habit = new HabitEntity("Exercise", "get healthy", LocalDate.now(), DayOfWeek.MONDAY);
        int habitId = (int) db.habitDao().save(habit);

        HabitEventEntity event = new HabitEventEntity(habitId, LocalDate.now(), "ran 5km");
        db.habitEventDao().save(event);
        db.habitEventDao().loadAllEvents();

        List<HabitEventEntity> eventList = LiveDataTestUtil.getValue(db.habitEventDao().loadAllEvents());

        assertEquals(eventList.get(0).getComment(), event.getComment());
    }

    @Test
    public void getEventsByHabitId() throws Exception {
        HabitEntity habit = new HabitEntity("Exercise", "get healthy", LocalDate.now(), DayOfWeek.MONDAY);
        int habitId = (int) db.habitDao().save(habit);

        HabitEventEntity event = new HabitEventEntity(habitId, LocalDate.now(), "ran 5km");
        db.habitEventDao().save(event);

        HabitEventEntity event2 = new HabitEventEntity(habitId, LocalDate.now(), "ran 6km");
        db.habitEventDao().save(event2);

        List<HabitEventEntity> eventList = LiveDataTestUtil.getValue(db.habitEventDao().loadEventsByHabitId(habitId));
        assertEquals(eventList.size(), 2);
    }
}
