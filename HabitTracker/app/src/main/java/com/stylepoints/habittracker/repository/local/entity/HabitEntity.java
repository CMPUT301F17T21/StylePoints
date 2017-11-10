package com.stylepoints.habittracker.repository.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.stylepoints.habittracker.model.Habit;

import java.util.Date;


@Entity(tableName = "habits")
public class HabitEntity implements Habit {

    @PrimaryKey
    @NonNull
    private String type;
    private String reason;
    @Ignore
    private Date startDate;
    private String schedule;

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public String getSchedule() {
        return schedule;
    }

    public HabitEntity() {

    }

    public HabitEntity(String type, String reason, Date startDate, String schedule) {
        this.type = type;
        this.reason = reason;
        this.startDate = startDate;
        this.schedule = schedule;
    }
}
