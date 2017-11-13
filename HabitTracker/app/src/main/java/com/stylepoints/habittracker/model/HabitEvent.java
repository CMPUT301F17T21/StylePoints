package com.stylepoints.habittracker.model;

import android.location.Location;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by Mackenzie on 2017-11-10.
 */

public interface HabitEvent {
    Date getDate();
    @Nullable
    String getComment();
    @Nullable
    Location getLocation();

    // TODO: Add photograph support
}
