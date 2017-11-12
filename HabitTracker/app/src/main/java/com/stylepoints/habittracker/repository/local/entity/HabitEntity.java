package com.stylepoints.habittracker.repository.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.stylepoints.habittracker.model.Habit;

import java.text.SimpleDateFormat;
import java.util.Date;


@Entity(tableName = "habits")
public class HabitEntity implements Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String reason;
    private Date startDate;
    private String schedule;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String toString () {
        return "Type: " + this.getType() + "\n" +
                "Reason: " + this.getReason() + "\n" +
                "StartDate: " + (new SimpleDateFormat("yyyy/MM/dd").format(this.getStartDate())) + "\n" +
                "Schedule: " + getSchedule();
    }
}
