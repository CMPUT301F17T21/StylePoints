package com.stylepoints.habittracker.model;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.EnumSet;

/**
 * Created by mchauck on 11/9/17.
 */

public interface Habit {
    String getType();
    String getReason();
    Date getStartDate();
    EnumSet<DayOfWeek> getDaysActive();
    boolean isActiveToday();
}
