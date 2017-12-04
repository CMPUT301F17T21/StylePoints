package com.stylepoints.habittracker;

import com.stylepoints.habittracker.model.HabitEvent;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class HabitEventModelTest {

    @Test
    public void noNullInRequiredFields() {
        HabitEvent event = new HabitEvent("username", "habitId", "habitType");
        assert(event.getHabitId() != null);
        assert(event.getElasticId() != null);
        assert(event.getDate() != null);
        assert(event.getUsername() != null);
    }

    @Test
    public void testGetters() {
        HabitEvent event = new HabitEvent("username", "habitId", "habitType");

        assertEquals("username", event.getUsername());
        assertEquals("habitId", event.getHabitId());
        assert(event.getElasticId().length() > 0);
        assertEquals(LocalDate.now(), event.getDate());
    }
}
