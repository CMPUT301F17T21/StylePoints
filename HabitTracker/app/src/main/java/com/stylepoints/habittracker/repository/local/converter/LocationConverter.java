package com.stylepoints.habittracker.repository.local.converter;

import android.arch.persistence.room.TypeConverter;
import android.location.Location;


/**
 * Converts a Location class to a string holding latitude and longitude, comma separated
 */
public class LocationConverter {
    @TypeConverter
    public static Location toLocation(String loc) {
        if (loc == null) {
            return null;
        }
        String[] parts = loc.split(",");

        try {
            double lat = Double.parseDouble(parts[0]);
            double lon = Double.parseDouble(parts[1]);
            Location location = new Location("coordinates");
            location.setLatitude(lat);
            location.setLongitude(lon);
            return location;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toStringRepresentation(Location loc) {
        if (loc == null) {
            return null;
        }

        return String.valueOf(loc.getLatitude()) + "," + String.valueOf(loc.getLongitude());
    }
}
