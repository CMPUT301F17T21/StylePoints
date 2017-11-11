package com.stylepoints.habittracker.repository.local.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Convert a `Date` class to a timestamp (long) so it can be stored in the local database
 * (sqlite)
 * from: https://github.com/googlesamples/android-architecture-components/blob/master/BasicSample/app/src/main/java/com/example/android/persistence/db/converter/DateConverter.java
 * 2017-11-10
 */

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
