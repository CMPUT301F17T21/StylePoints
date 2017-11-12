package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HabitNewActivity extends AppCompatActivity {
    Intent intent;

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
                HabitEntity newHabit;
                try {
                    newHabit = new HabitEntity(edittext_habit_name.getText().toString(),
                            edittext_habit_reason.getText().toString(),
                            (new SimpleDateFormat(date_format)).parse(edittext_habit_start_date.getText().toString()),
                            "TT"
                            );
                    HabitRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext())).save(newHabit);
                } catch (ParseException exception) {
                    System.out.println("Incorrect date format");
                }

                intent = new Intent(HabitNewActivity.this, HabitsMainActivity.class);
                startActivity(intent);
            }

        });

    }

}
