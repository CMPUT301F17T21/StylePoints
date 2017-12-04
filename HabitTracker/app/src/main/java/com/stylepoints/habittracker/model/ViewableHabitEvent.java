package com.stylepoints.habittracker.model;

/**
 * Created by Main on 2017-12-03.
 */

public class ViewableHabitEvent {
    HabitEvent event;
    String type;

    public ViewableHabitEvent(HabitEvent event, String type) {
        this.event = event;
        this.type = type;
    }

    public void setEvent(HabitEvent event) {
        this.event = event;
    }

    public HabitEvent getEvent() {
        return this.event;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return type + "\n" + event;
    }
}
