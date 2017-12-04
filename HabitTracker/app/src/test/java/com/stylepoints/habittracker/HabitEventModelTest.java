package com.stylepoints.habittracker;

import com.stylepoints.habittracker.model.HabitEvent;

import org.junit.Test;

import static org.junit.Assert.*;

public class HabitEventModelTest {

    @Test
    public void noNullInRequiredFields() {
        HabitEvent event = new HabitEvent("username", "habitId", "habitType");
        assert(event.getHabitId() != null);
        assert(event.getElasticId() != null);
        assert(event.getDate() != null);
        assert(event.getType() != null);
        assert(event.getUsername() != null);
    }
}
