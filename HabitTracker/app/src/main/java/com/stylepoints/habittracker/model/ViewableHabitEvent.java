package com.stylepoints.habittracker.model;

/**
 * Created by Main on 2017-12-03.
 */

// For Vieing an event (displayin ghabit ID is not very useful for users)
public class ViewableHabitEvent {
    HabitEvent event;
    String type;

    /**
     * Constructor
     * @param event
     * @param type
     */
    public ViewableHabitEvent(HabitEvent event, String type) {
        this.event = event;
        this.type = type;
    }

    // field getter and setter
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

    // conversion to string
    @Override
    public String toString() {
        return type + "\n" + event;
    }
}
