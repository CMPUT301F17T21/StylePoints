package com.stylepoints.habittracker;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitReasonTooLongException;
import com.stylepoints.habittracker.model.HabitTypeTooLongException;

import org.junit.Test;

import static org.junit.Assert.*;

public class HabitModelTest {

    @Test
    public void testReasonTooLong() {
        String testReason = "this is a long test reason that is more than the maximum characters";
        assertTrue(testReason.length() > Habit.MAX_REASON_LENGTH);
        try {
            Habit habit = new Habit("t", testReason, "username");
            fail("Exception not thrown for a reason that is too long");
        } catch (HabitTypeTooLongException e) {
            fail("Type is too long for this test");
        } catch (HabitReasonTooLongException e) {

        }
    }

    @Test
    public void testTypeTooLong() {
        String type = "this is a long type reason that is more than the maximum characters";
        assertTrue(type.length() > Habit.MAX_TYPE_LENGTH);
        try {
            Habit habit = new Habit(type, "", "username");
            fail("Exception not thrown for a reason that is too long");
        } catch (HabitTypeTooLongException e) {

        } catch (HabitReasonTooLongException e) {
            fail("Reason is too long for this test");
        }
    }

    @Test
    public void noNullInRequiredFields() throws Exception {
        Habit habit = new Habit("testType", "testReason", "testUsername");
        assertEquals("testType", habit.getType());
        assertEquals("testReason", habit.getReason());
        assert(habit.getElasticId() != null);
        assert(habit.getDaysActive() != null);
        assert(habit.getStartDate() != null);
    }
}
