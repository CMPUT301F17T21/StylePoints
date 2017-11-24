package com.stylepoints.habittracker.repository.local.converter;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

/**
 * Convert a `Date` class to a timestamp (long) so it can be stored in the local database
 * (sqlite)
 * from: https://github.com/googlesamples/android-architecture-components/blob/master/BasicSample/app/src/main/java/com/example/android/persistence/db/converter/DateConverter.java
 * 2017-11-10
 */

public class LocalDateConverter {
    @TypeConverter
    public static LocalDate toDate(Long timestamp) {
        if (timestamp == null) { return null; }
        return LocalDate.ofEpochDay(timestamp);

    }

    @TypeConverter
    public static Long toTimestamp(LocalDate date) {
        if (date == null) { return null; }
        return date.toEpochDay();
    }
}
