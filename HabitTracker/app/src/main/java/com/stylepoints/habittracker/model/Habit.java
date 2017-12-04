package com.stylepoints.habittracker.model;

import com.stylepoints.habittracker.repository.remote.Id;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.UUID;

/**
 * The model class for a Habit. Implements the Id interface so that it can be
 * serialized and uploaded to Elastic Search. This class is automatically serialized
 * by gson into JSON which is then stored locally, and uploaded to the server.
 *
 * Fields marked with transient are ignored by the serializer.
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

    /**
     *
     * @param type the type of habit. E.g., "Exercise"
     * @param reason reason for doing the habit. E.g., "to get healthy"
     * @param username username that this habit belongs to
     * @throws HabitTypeTooLongException when the type is longer than the allowed max
     * @throws HabitReasonTooLongException when the reason is longer than the allowed max
     */
    public Habit(String type, String reason, String username) throws HabitReasonTooLongException, HabitTypeTooLongException {
        this.elasticId = UUID.randomUUID().toString();
        setType(type);
        setReason(reason);
        this.username = username;
        this.startDate = LocalDate.now();
        this.daysActive = EnumSet.noneOf(DayOfWeek.class);
    }

    /**
     * Constructor.
     * @param type the type of habit. E.g., "Exercise"
     * @param reason reason for doing the habit. E.g., "to get healthy"
     * @param username username that this habit belongs to
     * @param startDate day to start working on this habit
     * @param daysActive days of the week to do this habit
     * @throws HabitTypeTooLongException when the type is longer than the allowed max
     * @throws HabitReasonTooLongException when the reason is longer than the allowed max
     */
    public Habit(String type, String reason, String username, LocalDate startDate, DayOfWeek... daysActive) throws HabitTypeTooLongException, HabitReasonTooLongException {
        this.elasticId = UUID.randomUUID().toString();

        setType(type);
        setReason(reason);
        this.username = username;
        this.startDate = startDate;
        setDaysActive(daysActive);
    }

    /**
     * Constructor.
     * @param type the type of habit. E.g., "Exercise"
     * @param reason reason for doing the habit. E.g., "to get healthy"
     * @param username username that this habit belongs to
     * @param startDate day to start working on this habit
     * @param daysActive the days of the week that we are scheduled to do this habit
     * @throws HabitTypeTooLongException when the type is longer than the allowed max
     * @throws HabitReasonTooLongException when the reason is longer than the allowed max
     */
    public Habit(String type, String reason, String username, LocalDate startDate, EnumSet<DayOfWeek> daysActive) throws HabitTypeTooLongException, HabitReasonTooLongException {
        this.elasticId = UUID.randomUUID().toString();

        setType(type);
        setReason(reason);
        this.username = username;
        this.startDate = startDate;
        this.daysActive = daysActive;
    }

    /**
     * Check if the habit is scheduled for this day of the week
     * @return true if the habit is scheduled for today
     */
    public boolean isActiveToday() {
        if (daysActive == null) {
            daysActive = EnumSet.noneOf(DayOfWeek.class);
        }
        return daysActive.contains(LocalDate.now().getDayOfWeek());
    }

    /**
     * @return the id that will be used to uniquely identify this Habit
     */
    public String getElasticId() {
        return elasticId;
    }

    /**
     * @return the type of habit. E.g., "Exercise"
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type of habit. E.g., "Exercise"
     * @throws HabitTypeTooLongException when type is too long
     */
    public void setType(String type) throws HabitTypeTooLongException {
        if (type.length() > MAX_TYPE_LENGTH) {
            throw new HabitTypeTooLongException();
        }
        this.type = type;
    }

    /**
     * @return reason for the habit. null if nothing set
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason for doing this habit. E.g., "to get healthy"
     * @throws HabitReasonTooLongException when the reason is longer than the maximum
     */
    public void setReason(String reason) throws HabitReasonTooLongException {
        if (reason.length() > MAX_REASON_LENGTH) {
            throw new HabitReasonTooLongException();
        }
        this.reason = reason;
    }

    /**
     * @return the username of the creator of this habit
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username of who created this habit
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the day that we want to start doing this habit
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the day that we want to start doing this habit
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the days of the week that we are scheduled to do this habit
     */
    public EnumSet<DayOfWeek> getDaysActive() {
        return daysActive;
    }

    /**
     * @param daysActive the days of the week that we are scheduled to do this habit
     */
    public void setDaysActive(EnumSet<DayOfWeek> daysActive) {
        this.daysActive = daysActive;
    }

    /**
     * @param days the days of the week that we are scheduled to do this habit
     */
    public void setDaysActive(DayOfWeek... days) {
        daysActive = EnumSet.noneOf(DayOfWeek.class);
        daysActive.addAll(Arrays.asList(days));
    }

    /**
     * This function is used to ensure that habits that are deserialized actually
     * have all of the required fields. Makes sure the Habit is not corrupted
     * @return true if all required fields are not null
     */
    public boolean hasValues() {
        return elasticId != null && type != null && username != null;
    }

    /**
     * @param id the id that is used in elastic search. E.g., api/habit/{id}
     */
    @Override
    public void setElasticId(String id) {
        elasticId = id;
    }

    @Override
    public String toString() {
        return "type:" + type;
//        return "Habit{" +
//                "elasticId='" + elasticId + '\'' +
//                ", type='" + type + '\'' +
//                ", reason='" + reason + '\'' +
//                ", username='" + username + '\'' +
//                ", startDate=" + startDate +
//                ", daysActive=" + daysActive +
//                '}';
    }
}
