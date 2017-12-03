package com.stylepoints.habittracker.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;

import com.stylepoints.habittracker.repository.remote.Id;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.UUID;

/**
 * The model class for an Event. Implements the Id interface so that it can be
 * serialized and uploaded to Elastic Search. This class is automatically serialized
 * by gson into JSON which is then stored locally, and uploaded to the server.
 *
 * A HabitEvent represents an occurrence of a Habit.
 *
 * Fields marked with transient are ignored by the serializer.
 */
public class HabitEvent implements Id {

    private String elasticId;
    private String habitId;
    private String type;
    private String username;
    private String comment;
    private LocalDate date;
    private Location location;
    private String photo;

    /**
     * Constructor
     * @param username username that this event belongs to
     * @param habitId the id of the habit that this event belongs to
     * @param type the type of the habit
     */
    public HabitEvent(String username, String habitId, String type) {
        elasticId = UUID.randomUUID().toString();
        this.habitId = habitId;
        this.type = type;
        this.username = username;
        this.date = LocalDate.now();
    }

    /**
     * Constructor
     * @param username username that this event belongs to
     * @param habitId the id of the habit that this event belongs to
     * @param type the type of the habit
     * @param comment comment for this event
     */
    public HabitEvent(String username, String habitId, String type, String comment) {
        this(username, habitId, type);
        this.comment = comment;
    }

    /**
     * Constructor
     * @param username username that this event belongs to
     * @param habitId the id of the habit that this event belongs to
     * @param type the type of the habit
     * @param comment comment for this event
     * @param date the date this event was done on
     */
    public HabitEvent(String username, String habitId, String type, String comment, LocalDate date) {
        this(username, habitId, type, comment);
        this.date = date;
    }

    /**
     * Constructor
     * @param username username that this event belongs to
     * @param habitId the id of the habit that this event belongs to
     * @param type the type of the habit
     * @param comment comment for this event
     * @param date the date this event was done on
     * @param location the location this was done at
     */
    public HabitEvent(String username, String habitId, String type, String comment, LocalDate date, Location location) {
        this(username, habitId, type, comment, date);
        this.location = location;
    }

    /**
     * @return the unique id of this event, also the id used for elastic search
     */
    public String getElasticId() {
        return elasticId;
    }

    /**
     * @return the id of the habit this event belongs to
     */
    public String getHabitId() {
        return habitId;
    }

    /**
     * @param elasticId the id to uniquely identify this event in local and remote storage
     */
    @Override
    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    /**
     * @param habitId the id of the habit this event belongs to
     */
    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    /**
     * @return the username of the user that this event belongs to
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username of the user that this event belongs to
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the comment of this event. Null if not set
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment that the event should have
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the date this event was done
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date the date this was done on
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the location where the event took place. Null if not set
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the location where the event took place
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Convert the internal representation of string to a bitmap for display
     * @author qikai
     * @return the photo for the event. null if not set
     */
    public Bitmap getPhoto() {
        // http://practiceonandroid.blogspot.ca/2013/03/convert-string-to-bitmap-and-bitmap-to.html
        try{
            byte [] encodeByte = Base64.decode(this.photo, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    /**
     * Convert and set the photo to internal representation as base64 string
     * @param photo the photo for the event
     */
    public void setPhoto(Bitmap photo) {
        // http://practiceonandroid.blogspot.ca/2013/03/convert-string-to-bitmap-and-bitmap-to.html
        ByteArrayOutputStream ByteStream=new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b = ByteStream.toByteArray();
        this.photo = Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * @return the type of the Habit that this event belongs to
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type of the Habit that this event belongs to
     */
    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
//        return "HabitEvent{" +
//                "elasticId='" + elasticId + '\'' +
//                ", username='" + username + '\'' +
//                ", comment='" + comment + '\'' +
//                ", date=" + date +
//                ", location=" + location +
//                '}';
        return "Type: " + type + "\nDate: " + date;
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
