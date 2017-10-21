package com.stylepoints.habittracker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class HabitTest extends Habit{

    public HabitTest(String type, String reason, Date startDate, WeekSchedule schedule) {
        super(type, reason, startDate, schedule);
    }

    @Test
    public void Test1() {

        super

        Habit tester = new Habit(type, reason, start, schedule);


        //getters
        assertEquals("getType() must return correct type", tester.getType(), tester.type);
        assertEquals("getReason() must return correct reason", tester.getReason(), tester.reason);
        assertEquals("getStartDate() must return correct start date", tester.getStartDate(), tester.start);
        assertEquals("getWeekSchedule() must return correct weekly schedule", tester.getWeekSchedule(), tester.schedule);

        //setters
        tester.setType(testtype);
        assertEquals("Type setter must set the right type", tester.type, testtype);
        tester.setReason(testreason);
        assertEquals("Reason setter must set the right reason", tester.reason, testreason);
        tester.setStartDate(testdate);
        assertEquals("Start date setter must set the right start date", tester.start, testdate);
        tester.setWeekSchedule(testschedule);
        assertEquals("Weekly schedule setter must set the right weekly schedule", tester.schedule, testschedule);

        //list test
        List<HabitEvent> testList = new ArrayList<HabitEvent>;
        assertEquals("HabitList getter must get the right list", testList.getHabitEventList()), testList.habitEventList);
        testList.setHabitEventList(listsettertest);
        assertEquals("Habit event list setter needs to set the right list", testList.getHabitEventList()), listsettertest);
    }
}

