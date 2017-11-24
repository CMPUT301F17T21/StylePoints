package com.stylepoints.habittracker.model;

import com.stylepoints.habittracker.repository.remote.Id;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.EnumSet;

/**
 * Created by mchauck on 11/9/17.
 */

public interface Habit extends Id {
    String getType();
    String getReason();
    LocalDate getStartDate();
    EnumSet<DayOfWeek> getDaysActive();
    boolean isActiveToday();
}
