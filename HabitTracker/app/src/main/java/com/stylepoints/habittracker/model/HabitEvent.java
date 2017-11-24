package com.stylepoints.habittracker.model;

import android.location.Location;
import android.support.annotation.Nullable;

import com.stylepoints.habittracker.repository.remote.Id;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Mackenzie on 2017-11-10.
 */

public interface HabitEvent extends Id {
    LocalDate getDate();
    @Nullable
    String getComment();
    @Nullable
    Location getLocation();

    // TODO: Add photograph support
}
