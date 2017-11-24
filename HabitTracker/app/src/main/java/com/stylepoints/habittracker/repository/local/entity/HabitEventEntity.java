package com.stylepoints.habittracker.repository.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.stylepoints.habittracker.model.HabitEvent;

import java.time.LocalDate;
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

    @Expose
    private String elasticId;
    @Expose
    private LocalDate date;
    @Expose
    private String comment;
    @Expose
    private Location location;
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




}
