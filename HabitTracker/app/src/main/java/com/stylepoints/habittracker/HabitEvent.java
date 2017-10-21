package com.stylepoints.habittracker;

import android.graphics.Picture;
import android.location.Location;

import java.util.Date;

/**
 * Created by nikosomos on 2017-10-18.
 */

public class HabitEvent {

    private String comment;
    // TODO: Need to find storage class for this field
    private Picture picture;
    private Date eventDate;
    // TODO: Need to find storage class for this field
    private Location location;

    HabitEvent() {
        this.eventDate = new Date();
    }

    HabitEvent(String comment) throws CommentTooLongException {
        setComment(comment);
        this.eventDate = new Date();
    }

    HabitEvent(String comment, Date date, Picture picture, Location location) throws CommentTooLongException {
        setComment(comment);
        this.eventDate = date;
        this.picture = picture;
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() > 30) {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
