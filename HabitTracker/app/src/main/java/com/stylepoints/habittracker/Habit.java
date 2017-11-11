package com.stylepoints.habittracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nikosomos on 2017-10-18.
 */

public class Habit {

    private final int name_len = 20;
    private final int reason_len = 30;

    private String name;
    private String reason;
    private Date startDate;
    private WeekSchedule weekSchedule;

    // TODO: Decide whether to use ArrayList, or extend it to HabitList for more functionality
    // ArrayList it is
    private ArrayList<Event> eventList;

    public Habit() { // empty habit, can only be used to connect to HabitConnector; must be loaded with actual data before use
        this.name = null;
        this.reason = null;
        this.startDate = null;
        this.weekSchedule = null;
        this.eventList = new ArrayList<Event>();
    }
//    This one is off, missing reason
//    public Habit(String name, WeekSchedule schedule) {
//        this. name = name;
//        this.startDate = new Date();
//        this.weekSchedule = schedule;
//        this.eventList = new ArrayList<Event>();
//    }

    public Habit(String name, String reason, WeekSchedule schedule) {
        this.name = name;
        this.reason = reason;
        this.startDate = new Date();
        this.weekSchedule = schedule;
        this.eventList = new ArrayList<Event>();
    }

    public Habit(String name, Date startDate, WeekSchedule schedule) {
        this.name = name;
        this.startDate = startDate;
        this.weekSchedule = schedule;
        this.eventList = new ArrayList<Event>();
    }

    public Habit(String name, String reason, Date startDate, WeekSchedule schedule) {
        this.name = name;
        this.reason = reason;
        this.startDate = startDate;
        this.weekSchedule = schedule;
        this.eventList = new ArrayList<Event>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws CommentTooLongException {
        if (name.length() > name_len) {
            throw new CommentTooLongException();
        }
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) throws CommentTooLongException {
        if (name.length() > reason_len) {
            throw new CommentTooLongException();
        }
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

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    public String toString () {
        return "Name: " + this.getName() + "\n" +
                "Reason: " + this.getReason() + "\n" +
                "StartDate: " + (new SimpleDateFormat("yyyy/MM/dd").format(this.getStartDate())) + "\n" +
                "Schedule: " + weekSchedule.toString();
    }
}
