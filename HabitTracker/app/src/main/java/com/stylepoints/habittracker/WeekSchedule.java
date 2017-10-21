package com.stylepoints.habittracker;

/**
 * Created by nikosomos on 2017-10-19.
 */

public class WeekSchedule {
    private Boolean[] dayOfWeek = new Boolean[7];

    public WeekSchedule(Boolean sunday, Boolean monday, Boolean tuesday, Boolean wednesday, Boolean thursday, Boolean friday, Boolean saturday) {
        this.dayOfWeek[0] = sunday;
        this.dayOfWeek[1] = monday;
        this.dayOfWeek[2] = tuesday;
        this.dayOfWeek[3] = wednesday;
        this.dayOfWeek[4] = thursday;
        this.dayOfWeek[5] = friday;
        this.dayOfWeek[6] = saturday;
    }

    public Boolean getSunday(){
        return dayOfWeek[0];
    }

    public Boolean getMonday(){
        return dayOfWeek[1];
    }

    public Boolean getTuesday(){
        return dayOfWeek[2];
    }

    public Boolean getWednesday(){
        return dayOfWeek[3];
    }

    public Boolean getThursday(){
        return dayOfWeek[4];
    }

    public Boolean getFriday(){
        return dayOfWeek[5];
    }

    public Boolean getSaturday(){
        return dayOfWeek[6];
    }

    public Boolean setSunday(Boolean b){
        return dayOfWeek[0] = b;
    }

    public Boolean setMonday(Boolean b){
        return dayOfWeek[1] = b;
    }

    public Boolean setTuesday(Boolean b){
        return dayOfWeek[2] = b;
    }

    public Boolean setWednesday(Boolean b){
        return dayOfWeek[3] = b;
    }

    public Boolean setThursday(Boolean b){
        return dayOfWeek[4] = b;
    }

    public Boolean setFriday(Boolean b){
        return dayOfWeek[5] = b;
    }

    public Boolean setSaturday(Boolean b){
        return dayOfWeek[6] = b;
    }


}
