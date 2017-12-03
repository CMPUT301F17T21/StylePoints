package com.stylepoints.habittracker.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.stylepoints.habittracker.repository.remote.Id;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.UUID;

/**
 * Created by mchauck on 11/9/17.
 */

public class Habit implements Id {
    public static transient final int MAX_REASON_LENGTH = 30;
    public static transient final int MAX_TYPE_LENGTH = 20;

    // the id used with elastic search. i.e., /habit/{elasticId}
    private String elasticId;
    // the habit type. e.g., "exercise". must be less than 20 chars
    private String type;
    // reason for the habit. e.g., "get healthy". must be less than 30 chars
    private String reason;
    // the username of the owner of this habit as it is in elastic search
    private String username;
    private LocalDate startDate;
    private EnumSet<DayOfWeek> daysActive;

    public Habit(String type, String reason, String username) throws HabitReasonTooLongException, HabitTypeTooLongException {
        this.elasticId = UUID.randomUUID().toString();
        setType(type);
        setReason(reason);
        this.username = username;
        this.startDate = LocalDate.now();
        this.daysActive = EnumSet.noneOf(DayOfWeek.class);
    }

    public Habit(String type, String reason, String username, LocalDate startDate, DayOfWeek... daysActive) throws HabitTypeTooLongException, HabitReasonTooLongException {
        // generate a random id
        this.elasticId = UUID.randomUUID().toString();

        setType(type);
        setReason(reason);
        this.username = username;
        this.startDate = startDate;
        setDaysActive(daysActive);
    }

    public Habit(String type, String reason, String username, LocalDate startDate, EnumSet<DayOfWeek> daysActive) throws HabitTypeTooLongException, HabitReasonTooLongException {
        // generate a random id
        this.elasticId = UUID.randomUUID().toString();

        setType(type);
        setReason(reason);
        this.username = username;
        this.startDate = startDate;
        this.daysActive = daysActive;
    }

    public boolean isActiveToday() {
        return daysActive.contains(LocalDate.now().getDayOfWeek());
    }

    public String getElasticId() {
        return elasticId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws HabitTypeTooLongException {
        if (type.length() > MAX_TYPE_LENGTH) {
            throw new HabitTypeTooLongException();
        }
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) throws HabitReasonTooLongException {
        if (reason.length() > MAX_REASON_LENGTH) {
            throw new HabitReasonTooLongException();
        }
        this.reason = reason;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public EnumSet<DayOfWeek> getDaysActive() {
        return daysActive;
    }

    public void setDaysActive(EnumSet<DayOfWeek> daysActive) {
        this.daysActive = daysActive;
    }

    public void setDaysActive(DayOfWeek... days) {
        daysActive = EnumSet.noneOf(DayOfWeek.class);
        daysActive.addAll(Arrays.asList(days));
    }

    @Override
    public void setElasticId(String id) {
        elasticId = id;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "elasticId='" + elasticId + '\'' +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", username='" + username + '\'' +
                ", startDate=" + startDate +
                ", daysActive=" + daysActive +
                '}';
    }
}
