package com.stylepoints.habittracker.repository.local.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by qikai on 2017-11-11.
 */

public class WeekSchEntity {
    private final String[] weekday_names = {"Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday"};
    private final int weekLen = 7;

    private List<Boolean> weekdaysSelect;

    public WeekSchEntity (List<Boolean> weekdaysSelect) {
        if (weekdaysSelect.size() == weekLen) {
            this.weekdaysSelect = weekdaysSelect;
        } else {
            // throw exception?
        }
    }
    public void changeWeekdaysSelect (List<Boolean> weekdaysSelect) {
        if (weekdaysSelect.size() == weekLen) {
            this.weekdaysSelect = weekdaysSelect;
        } else {
            // throw exception?
        }
    }

    public Boolean checkSunday() {
        return weekdaysSelect.get(0);
    }
    public Boolean checkMonday() {
        return weekdaysSelect.get(1);
    }
    public Boolean checkTuesday() {
        return weekdaysSelect.get(2);
    }
    public Boolean checkWednesday() {
        return weekdaysSelect.get(3);
    }
    public Boolean checkThursday() {
        return weekdaysSelect.get(4);
    }
    public Boolean checkFriday() {
        return weekdaysSelect.get(5);
    }
    public Boolean checkSaturday() {
        return weekdaysSelect.get(6);
    }

    public void changeSunday(Boolean b){
        weekdaysSelect.set(0, b);
    }

    public void setMonday(Boolean b){
        weekdaysSelect.set(1, b);
    }

    public void changeTuesday(Boolean b){
        weekdaysSelect.set(2, b);
    }

    public void changeWednesday(Boolean b){
        weekdaysSelect.set(3, b);
    }

    public void changeThursday(Boolean b){
        weekdaysSelect.set(4, b);
    }

    public void changeFriday(Boolean b){
        weekdaysSelect.set(5, b);
    }

    public void changeSaturday(Boolean b){
        weekdaysSelect.set(6, b);
    }


    public String toString() {
        String selectedDateString = "";
        for (int i = 0; i < weekLen; i++) {
            if (weekdaysSelect.get(i)) {
                selectedDateString += weekday_names[i] + ", ";
            }
        }
        if (selectedDateString.length() > 0) {
            return selectedDateString.substring(0, selectedDateString.length() - 2);
        } else {
            return "None Selected";
        }
    }
}
