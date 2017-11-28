package com.stylepoints.habittracker.repository.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.Nullable;

import com.stylepoints.habittracker.model.HabitEvent;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = HabitEntity.class,
                                  parentColumns = "id",
                                  childColumns = "habitId",
                                  onDelete = ForeignKey.CASCADE),
        tableName = "habit_events")
public class HabitEventEntity implements HabitEvent {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int habitId;
    private Date date;
    private String comment;
    private Location location;
    private Bitmap photo;
    // TODO: add photograph support

    public HabitEventEntity() {
    }

    public HabitEventEntity(int habitId, Date date, String comment) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
    }

    public HabitEventEntity(int habitId, Date date, String comment, Location location) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
        this.location = location;
    }

    public HabitEventEntity(int habitId, Date date, String comment, Location location, Bitmap photo) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
        this.location = location;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public int getHabitId() {
        return habitId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPhoto (Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return location;
    }

    @Nullable
    @Override
    public Bitmap getPhoto () {
        return photo;
    };


}
