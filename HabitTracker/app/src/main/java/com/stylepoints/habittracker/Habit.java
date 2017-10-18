package com.stylepoints.habittracker;

import java.util.Date;
import java.util.List;

/**
 * Created by nikosomos on 2017-10-18.
 */

public class Habit {

    private String type;
    private String reason;
    private Date startDate;


    // TODO: Choose which storage option we should use
    /*  Option 1 for storing days of the week
        Pro:    Easier to identify days inside class - Habit.saturday
                self-explanatory(no decoding needed)
        Con:    Lots of variables
     */
    private Boolean sunday;
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;

    /*  Option 2 for storing days of the week
        Pro:    Cleaner look, only one variable needed.
        Con:    Needs to be decoded before you can use it.
     */
    private Boolean[] dayOfWeek = new Boolean[7];


    // TODO: Decide whether to use ArrayList, or extend it to HabitList for more functionality
    private List<HabitEvent> habitEventList;

    // TODO: Build constructors and getters and setters

}
