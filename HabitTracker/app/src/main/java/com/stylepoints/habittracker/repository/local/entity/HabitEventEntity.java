package com.stylepoints.habittracker.repository.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.stylepoints.habittracker.model.HabitEvent;

import java.time.LocalDate;

@Entity(foreignKeys = @ForeignKey(entity = HabitEntity.class,
                                  parentColumns = "id",
                                  childColumns = "habitId",
                                  onDelete = ForeignKey.CASCADE),
        tableName = "habit_events")
public class HabitEventEntity implements HabitEvent {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int habitId;
    private String elasticId;

    @Expose
    private String user;
    @Expose
    private String habitElasticId;
    @Expose
    private LocalDate date;
    @Expose
    private String comment;
    @Expose
    private Location location;
    private Bitmap photo;
    // TODO: add photograph support

    public HabitEventEntity() {
    }

    public HabitEventEntity(int habitId, LocalDate date, String comment) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
    }

    public HabitEventEntity(int habitId, LocalDate date, String comment, Location location) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
        this.location = location;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHabitElasticId() {
        return habitElasticId;
    }

    public void setHabitElasticId(String habitElasticId) {
        this.habitElasticId = habitElasticId;
}

    public HabitEventEntity(int habitId, LocalDate date, String comment, Location location, Bitmap photo) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
        this.location = location;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getElasticId() {
        return elasticId;
    }

    @Override
    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
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

    public void setDate(LocalDate date) {
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
    public LocalDate getDate() {
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

    @Override
    public String toString() {
        return "HabitEventEntity{" +
                "user='" + user + '\'' +
                ", habitElasticId='" + habitElasticId + '\'' +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", location=" + location +
                '}';
    }

    @Nullable
    @Override
    public Bitmap getPhoto () {
        return photo;
    }
}
