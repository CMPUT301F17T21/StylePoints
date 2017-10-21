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

    // TODO: Build constructors and getters and setters

    public Profile(String username) {
        this.username = username;
        this.habitList = new ArrayList();
    }
}
