package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class HabitNewActivity extends AppCompatActivity {
    Intent intent;
    HabitController habitcontroller;
    final String date_format = "yyyy/MM/dd";

    Button button_add_habit;
    EditText edittext_habit_name;
    EditText edittext_habit_reason;
    EditText edittext_habit_start_date;
    CheckBox checkbox_monday;
    CheckBox checkbox_tuesday;
    CheckBox checkbox_wednesday;
    CheckBox checkbox_thursday;
    CheckBox checkbox_friday;
    CheckBox checkbox_saturday;
    CheckBox checkbox_sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_new);

        habitcontroller = HabitController.getInstance();

        button_add_habit = (Button) findViewById(R.id.buttonAddHabit);
        edittext_habit_name = (EditText) findViewById(R.id.editTextHabitName);
        edittext_habit_reason = (EditText) findViewById(R.id.editTextReason);
        edittext_habit_start_date = (EditText) findViewById(R.id.editTextStartingDate);
        checkbox_monday = (CheckBox) findViewById(R.id.checkBoxMonday);
        checkbox_tuesday = (CheckBox) findViewById(R.id.checkBoxTuesday);
        checkbox_wednesday = (CheckBox) findViewById(R.id.checkBoxWednesday);
        checkbox_thursday = (CheckBox) findViewById(R.id.checkBoxThursday);
        checkbox_friday = (CheckBox) findViewById(R.id.checkBoxFriday);
        checkbox_saturday = (CheckBox) findViewById(R.id.checkBoxSaturday);
        checkbox_sunday = (CheckBox) findViewById(R.id.checkBoxSunday);

        button_add_habit.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                habitcontroller.connectHabit(new Habit());

                try {
                    habitcontroller.setHabitName(edittext_habit_name.getText().toString());
                } catch (CommentTooLongException exception) {
                    System.out.println(edittext_habit_name.getText().toString());
                    habitcontroller.disconnectHabit(); // Forced eviction of habit if exception occurs
                    // something something ...
                }

                try {
                    habitcontroller.setHabitReason(edittext_habit_reason.getText().toString());
                } catch (CommentTooLongException exception) {
                    System.out.println(edittext_habit_reason.getText().toString());
                    habitcontroller.disconnectHabit(); // Forced eviction of habit if exception occurs
                // something something ...
                }
                try {
                    habitcontroller.setHabitStartDate(edittext_habit_start_date.getText().toString(), date_format);
                } catch (ParseException exception) {
                    System.out.println(edittext_habit_start_date.getText().toString() + "__"); // Dash line so something gets printed out if there is nothing
                    habitcontroller.disconnectHabit(); // Forced eviction of habit if exception occurs
                    // Yaddi Yadda
                }
                Boolean[] schedule_list = {
                        checkbox_sunday.isChecked(),
                        checkbox_monday.isChecked(),
                        checkbox_tuesday.isChecked(),
                        checkbox_wednesday.isChecked(),
                        checkbox_thursday.isChecked(),
                        checkbox_friday.isChecked(),
                        checkbox_saturday.isChecked()
                };
                habitcontroller.setWeekSchedule(schedule_list);

                intent = new Intent(HabitNewActivity.this, HabitsMainActivity.class);
                startActivity(intent);
            }

        });

    }
}
