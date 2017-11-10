package com.stylepoints.habittracker.repository.local.entity;

import com.stylepoints.habittracker.model.Habit;

import java.util.Date;

/**
 * Created by mchauck on 11/9/17.
 */

public class HabitEntity implements Habit {
    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getReason() {
        return null;
    }

    @Override
    public Date getStartDate() {
        return null;
    }

    @Override
    public String getSchedule() {
        return null;
    }
}
