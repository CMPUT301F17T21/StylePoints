package com.stylepoints.habittracker.model;

import android.graphics.Bitmap;
import android.location.Location;

import com.stylepoints.habittracker.repository.remote.Id;

import java.time.LocalDate;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mackenzie on 2017-11-10.
 */

public class HabitEvent implements Id {

    private String elasticId;
    private String habitId;
    private String type;

    private String username;
    private String comment;
    private LocalDate date;
    private Location location;
    private Bitmap photo;

    public HabitEvent(String username, String habitId, String type) {
        elasticId = UUID.randomUUID().toString();
        this.habitId = habitId;
        this.type = type;
        this.username = username;
        this.date = LocalDate.now();
    }


    public HabitEvent(String username, String habitId, String type, String comment) {
        this(username, habitId, type);
        this.comment = comment;
    }

    public HabitEvent(String username, String habitId, String type, String comment, LocalDate date) {
        this(username, habitId, type, comment);
        this.date = date;
    }

    public HabitEvent(String username, String habitId, String type, String comment, LocalDate date, Location location) {
        this(username, habitId, type, comment, date);
        this.location = location;
    }

    public String getElasticId() {
        return elasticId;
    }

    public String getHabitId() {
        return habitId;
    }

    @Override
    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    // new code
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Type: " + type + "\nDate: " + date;
//        return "HabitEvent{" +
//                "elasticId='" + elasticId + '\'' +
//                ", username='" + username + '\'' +
//                ", comment='" + comment + '\'' +
//                ", date=" + date +
//                ", location=" + location +
//                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HabitEvent that = (HabitEvent) o;

        if (!elasticId.equals(that.elasticId)) return false;
        if (!username.equals(that.username)) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return location != null ? location.equals(that.location) : that.location == null;
    }

    @Override
    public int hashCode() {
        int result = elasticId.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
