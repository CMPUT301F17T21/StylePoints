package com.stylepoints.habittracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qikai on 2017-11-10.
 */

public class HabitController {
    // One-hot, do not raise both flag
    private boolean create_flag = false;
    private boolean destroy_flag = false;

    private static final HabitController instance = new HabitController();
    private Habit habit = null;

    private HabitController () {

    }

    public static HabitController getInstance () {
        return instance;
    }

    // Habit connection functions
    public void connectHabit (Habit habit) { // controller take required model
        if (this.habit == null) {
            this.habit = habit;
        } else {
            // raise some exception about a habit is still connected here?
        }
    }

    public Habit disconnectHabit () { // controller removes unnecessary model
        Habit dhabit = habit;
        habit = null;
        return dhabit;
    }

    public void createHabit () { // HabitController can double as factory; Convenient but is it proper?
        if (this.habit == null) {
            this.habit = new Habit();
        } else {
            // raise some exception about a habit is still connected here?
        }
    }

    public Boolean checkForConnectedHabit () { // check if some model is occupying the controller
        if (habit != null) {
            return true;
        }
        return false;
    }

    // Habit modification functions
    public String getHabitName() {
        return habit.getName();
    }

    public void setHabitName(String name) throws CommentTooLongException{
        habit.setName(name);
    }

    public String getHabitReason() {
        return habit.getReason();
    }

    public void setHabitReason(String reason) throws CommentTooLongException{
        habit.setReason(reason);
    }

    public Date getHabitStartDate() {
        return habit.getStartDate();
    }

    public void setHabitStartDate(String date_string, String date_format) throws ParseException {
        habit.setStartDate((new SimpleDateFormat(date_format)).parse(date_string));
    }

    public WeekSchedule getWeekSchedule() {
        return habit.getWeekSchedule();
    }

    public void setWeekSchedule(Boolean[] schedule_list) {
        habit.setWeekSchedule(new WeekSchedule(
                schedule_list[0],
                schedule_list[1],
                schedule_list[2],
                schedule_list[3],
                schedule_list[4],
                schedule_list[5],
                schedule_list[6]
        ));
    }

    // Statistics Function

    // Lower echelon communication
    public ArrayList<Event> getEventList() {
        return habit.getEventList();
    }

    public void setEventList(ArrayList<Event> eventList) {
        habit.setEventList(eventList);
    }

    public void addToEventList(Event event) {
    }

    public void removeFromEventList(Integer index) {
    }

    // Upper echelon communication
    public void raiseCreateFlage() { // declare creation of self habit
        this.create_flag = true;
    }
    public void raiseDestroyFlage() { // declare deletion of self habit
        this.destroy_flag = true;
    }
    public boolean takeCreateFlag() { // take will check and reset flag
        boolean flag = this.create_flag;
        this.create_flag = false;
        return flag;
    }
    public boolean takeDestroyFlag() { // take will check and reset flag
        boolean flag = this.destroy_flag;
        this.destroy_flag = false;
        return flag;
    }
}
