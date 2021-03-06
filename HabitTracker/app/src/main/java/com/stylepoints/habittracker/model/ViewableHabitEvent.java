package com.stylepoints.habittracker.model;

/**
 * Includes getters and setters for viewable versions of habit events, ie. not hidden by the
 * LiveData wrapper in the repository class. This is the regular observable counterpart to the
 * lifecycle-aware HabitEvent object.
 *
 * Includes methods for setting and getting the HabitEvent associated with a given instance of this
 * class, getting and setting the event type as a string and returning the type and event as a
 * string.
 *
 * @author StylePoints
 * @see com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitsMainActivity
 */

// For Viewing an event (displaying habit ID is not very useful for users)
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
