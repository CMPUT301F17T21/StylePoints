package com.stylepoints.habittracker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class HabitTest {

    @Test
    public void getters() {

        String type = "testType";
        String reason = "testReason";
        Date start = new GregorianCalendar(2017, 1, 1).getTime();
        WeekSchedule schedule = new WeekSchedule();

        Habit tester = new Habit(type, reason, start, schedule);


        //getters
        assertEquals("getType() must return correct type", tester.getType(), type);
        assertEquals("getReason() must return correct reason", tester.getReason(), reason);
        assertEquals("getStartDate() must return correct start date", tester.getStartDate(), start);
        assertEquals("getWeekSchedule() must return correct weekly schedule", tester.getWeekSchedule(), schedule);

        //setters
        tester.setType("newType");
        assertEquals("Type setter must set the right type", tester.getType(), "newType");
        tester.setReason("newReason");
        assertEquals("Reason setter must set the right reason", tester.getReason(), "newReason");

        Date newDate = new GregorianCalendar(2017, 1, 2).getTime();
        tester.setStartDate(newDate);
        assertEquals("Start date setter must set the right start date", tester.getStartDate(), newDate);

        WeekSchedule schedule2 = new WeekSchedule(true, true, true, false, false, false, false);
        tester.setWeekSchedule(schedule2);
        assertEquals("Weekly schedule setter must set the right weekly schedule", tester.getWeekSchedule(), schedule2);

    }

    @Test
    public void habitListTest() {
        String type = "testType";
        String reason = "testReason";
        Date start = new GregorianCalendar(2017, 1, 1).getTime();
        WeekSchedule schedule = new WeekSchedule();
        Habit habit = new Habit(type, reason, start, schedule);

        //list test
        List<HabitEvent> testList = new ArrayList<HabitEvent>();
        assertEquals("HabitList getter must get the right list", habit.getHabitEventList(), testList);

        List<HabitEvent> list2 = new ArrayList<HabitEvent>();
        habit.setHabitEventList(list2);
        assertEquals("Habit event list setter needs to set the right list", habit.getHabitEventList(), list2);
    }
}

