package com.stylepoints.habittracker.model;

import com.stylepoints.habittracker.repository.local.entity.WeekSchEntity;

import java.util.Date;

/**
 * Created by mchauck on 11/9/17.
 */

public interface Habit {
    String getType();
    String getReason();
    Date getStartDate();
    String getSchedule();
}
