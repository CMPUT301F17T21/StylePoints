package com.stylepoints.habittracker.model;

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
