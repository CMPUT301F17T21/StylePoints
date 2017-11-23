package com.stylepoints.habittracker.model;

import android.graphics.Bitmap;
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
    @Nullable
    Bitmap getPhoto();

    // TODO: Add photograph support
}
