package com.stylepoints.habittracker.repository.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.stylepoints.habittracker.model.Habit;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;


@Entity(tableName = "habits")
public class HabitEntity implements Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String reason;
    private Date startDate;
    private EnumSet<DayOfWeek> daysActive;

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

    public void setDaysActive(DayOfWeek... days) {
        daysActive = EnumSet.noneOf(DayOfWeek.class);
        daysActive.addAll(Arrays.asList(days));
    }

    public void setDaysActive(EnumSet<DayOfWeek> daysActive) {
        this.daysActive = daysActive;
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
    public EnumSet<DayOfWeek> getDaysActive() {
        return daysActive;
    }

    @Override
    public boolean isActiveToday() {
        LocalDate date = LocalDate.now();
        return daysActive.contains(date.getDayOfWeek());
    }

    public HabitEntity() {
        daysActive = EnumSet.noneOf(DayOfWeek.class);
    }

    public HabitEntity(String type, String reason, Date startDate, DayOfWeek... daysActive) {
        this.type = type;
        this.reason = reason;
        this.startDate = startDate;
        this.setDaysActive(daysActive);
    }

    public HabitEntity(String type, String reason, Date startDate, EnumSet<DayOfWeek> daysActive) {
        this.type = type;
        this.reason = reason;
        this.startDate = startDate;
        this.daysActive = daysActive;
    }

    public String toString () {
        return "Type: " + this.getType() + "\n" +
                "Reason: " + this.getReason() + "\n" +
                "StartDate: " + (new SimpleDateFormat("yyyy/MM/dd").format(this.getStartDate())) + "\n" +
                "Schedule: " + daysActive.toString();
    }
}
