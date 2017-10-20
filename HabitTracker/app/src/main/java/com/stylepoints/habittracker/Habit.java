package com.stylepoints.habittracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nikosomos on 2017-10-18.
 */

public class Habit {

    private String type;
    private String reason;
    private Date startDate;
    private WeekSchedule weekSchedule;

    // TODO: Decide whether to use ArrayList, or extend it to HabitList for more functionality
    private List<HabitEvent> habitEventList;

    public Habit(String type, WeekSchedule schedule) {
        this.type = type;
        this.startDate = new Date();
        this.weekSchedule = schedule;
        this.habitEventList = new ArrayList<HabitEvent>();
    }

    public Habit(String type, String reason, WeekSchedule schedule) {
        this.type = type;
        this.reason = reason;
        this.startDate = new Date();
        this.weekSchedule = schedule;
        this.habitEventList = new ArrayList<HabitEvent>();
    }

    public Habit(String type, Date startDate, WeekSchedule schedule) {
        this.type = type;
        this.startDate = startDate;
        this.weekSchedule = schedule;
        this.habitEventList = new ArrayList<HabitEvent>();
    }

    public Habit(String type, String reason, Date startDate, WeekSchedule schedule) {
        this.type = type;
        this.reason = reason;
        this.startDate = startDate;
        this.weekSchedule = schedule;
        this.habitEventList = new ArrayList<HabitEvent>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public WeekSchedule getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(WeekSchedule weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public List<HabitEvent> getHabitEventList() {
        return habitEventList;
    }

    public void setHabitEventList(List<HabitEvent> habitEventList) {
        this.habitEventList = habitEventList;
    }
}
