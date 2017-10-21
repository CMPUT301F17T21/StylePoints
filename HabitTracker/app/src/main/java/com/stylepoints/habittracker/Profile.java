package com.stylepoints.habittracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikosomos on 2017-10-18.
 */

public class Profile {

    private String username;

    // TODO: Decide whether to use ArrayList, or extend it to HabitList for more functionality
    private List<Habit> habitList;

    Profile(String username) {
        this.username = username;
        habitList = new ArrayList<Habit>();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addHabit(Habit habit) {
        habitList.add(habit);
    }

    public void removeHabit(Habit habit) {
        habitList.remove(habit);
    }

    public int findHabit(Habit habit) {
        return habitList.indexOf(habit);
    }

    public int numHabits() {
        return habitList.size();
    }

}