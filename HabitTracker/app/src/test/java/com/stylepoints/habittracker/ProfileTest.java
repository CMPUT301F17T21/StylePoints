package com.stylepoints.habittracker;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by mchauck on 10/21/17.
 */
public class ProfileTest {
    Profile profile;

    @Before
    public void setUp() throws Exception {
        profile = new Profile("testUserName");
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals("testUserName", profile.getUsername());
    }

    @Test
    public void setUsername() throws Exception {
        profile.setUsername("newUserName");
        assertEquals("newUserName", profile.getUsername());
    }

    @Test
    public void addHabit() throws Exception {
        assertEquals(profile.numHabits(), 0);
        Habit habit = new Habit("habitType", "reason", new Date(), new WeekSchedule());
        profile.addHabit(habit);
        assertEquals(profile.numHabits(), 1);
    }

    @Test
    public void removeHabit() throws Exception {
        Habit habit = new Habit("habitType", "reason", new Date(), new WeekSchedule());
        profile.addHabit(habit);
        assertEquals(profile.numHabits(), 1);
        profile.removeHabit(habit);
        assertEquals(profile.numHabits(), 0);
    }

    @Test
    public void findHabit() throws Exception {
        Habit habit0 = new Habit("habitType", "reason", new Date(), new WeekSchedule());
        Habit habit1 = new Habit("habitType", "reason", new Date(), new WeekSchedule());
        Habit habit2 = new Habit("habitType", "reason", new Date(), new WeekSchedule());
        profile.addHabit(habit0);
        profile.addHabit(habit1);
        profile.addHabit(habit2);

        assertEquals(profile.findHabit(habit1), 1);
    }

}