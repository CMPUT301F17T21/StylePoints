package com.stylepoints.habittracker.repository.local.converter;

import android.arch.persistence.room.TypeConverter;

import java.time.DayOfWeek;
import java.util.EnumSet;

/**
 * Created by Mackenzie on 2017-11-12.
 * https://stackoverflow.com/questions/2199399/storing-enumset-in-a-database
 * 2017-11-12
 *
 * WARNING: this is dependent on the ordering of the enums remaining the same,
 * and no new enums being added. Since this is just a day of week enum in the
 * standard library, this *should* never happen. If it does, the stored values in
 * the database would be incorrect.
 */

public class ScheduleConverter {
    @TypeConverter
    public static EnumSet<DayOfWeek> toEnumSet(int bitset) {
        EnumSet<DayOfWeek> schedule = EnumSet.noneOf(DayOfWeek.class);

        DayOfWeek[] values = DayOfWeek.values();
        while (bitset != 0) {
            int ordinal = Integer.numberOfTrailingZeros(bitset); // read in the flag
            bitset ^= Integer.lowestOneBit(bitset); // clear that flag
            schedule.add(values[ordinal]);
        }
        return schedule;
    }

    @TypeConverter
    public static int toInt(EnumSet<DayOfWeek> schedule) {
        int bitset = 0;

        for (DayOfWeek day : schedule) {
            // Bitwise-OR each ordinal value together to encode as single int.
            bitset |= (1 << day.ordinal());
        }
        return bitset;
    }
}
