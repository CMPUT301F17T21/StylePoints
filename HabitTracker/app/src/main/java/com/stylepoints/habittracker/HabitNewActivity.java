package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class HabitNewActivity extends AppCompatActivity {
    Intent intent;

    Button button_add_habit;
    EditText edittext_habit_name;
    EditText edittext_habit_reason;
    EditText edittext_habit_starting_date;
    RadioButton radiobutton_monday;
    RadioButton radiobutton_tuesday;
    RadioButton radiobutton_wednesday;
    RadioButton radiobutton_thursday;
    RadioButton radiobutton_friday;
    RadioButton radiobutton_saturday;
    RadioButton radiobutton_sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_new);

        button_add_habit = (Button) findViewById(R.id.buttonAddHabit);
        edittext_habit_name = (EditText) findViewById(R.id.editTextHabitName);
        edittext_habit_reason = (EditText) findViewById(R.id.editTextReason);
        edittext_habit_starting_date = (EditText) findViewById(R.id.editTextStartingDate);
        radiobutton_monday = (RadioButton) findViewById(R.id.radioButtonMonday);
        radiobutton_tuesday = (RadioButton) findViewById(R.id.radioButtonTuesday);
        radiobutton_wednesday = (RadioButton) findViewById(R.id.radioButtonWednesday);
        radiobutton_thursday = (RadioButton) findViewById(R.id.radioButtonThursday);
        radiobutton_friday = (RadioButton) findViewById(R.id.radioButtonFriday);
        radiobutton_saturday = (RadioButton) findViewById(R.id.radioButtonSaturday);
        radiobutton_sunday = (RadioButton) findViewById(R.id.radioButtonSunday);

        button_add_habit.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){
                intent = new Intent(HabitNewActivity.this, HabitsMainActivity.class);
                startActivity(intent);
            }
        });

    }
}
