package com.stylepoints.habittracker.viewmodel.HabitRelatedActivities;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.stylepoints.habittracker.DatePickerFragment;
import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.EnumSet;

public class HabitNewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static String TAG = "HabitNewActivity";
    HabitRepository repo;

    LocalDate date;

    Button button_add_habit;
    Button button_set_date;
    EditText edittext_habit_name;
    EditText edittext_habit_reason;
    TextView textview_habit_start_date;
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
        bindToUi();

        repo = HabitRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        date = LocalDate.now();
        textview_habit_start_date.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        button_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "date");
            }
        });

        button_add_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyFields()) {
                    HabitEntity habit = new HabitEntity();

                    habit.setReason(edittext_habit_reason.getText().toString());
                    habit.setType(edittext_habit_name.getText().toString());

                    // https://stackoverflow.com/questions/22929237/convert-java-time-localdate-into-java-util-date-type
                    // 2017-11-12
                    Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    habit.setStartDate(d);

                    EnumSet<DayOfWeek> schedule = EnumSet.noneOf(DayOfWeek.class);
                    if (checkbox_monday.isChecked()) { schedule.add(DayOfWeek.MONDAY); }
                    if (checkbox_tuesday.isChecked()) { schedule.add(DayOfWeek.TUESDAY); }
                    if (checkbox_wednesday.isChecked()) { schedule.add(DayOfWeek.WEDNESDAY); }
                    if (checkbox_thursday.isChecked()) { schedule.add(DayOfWeek.THURSDAY); }
                    if (checkbox_friday.isChecked()) { schedule.add(DayOfWeek.FRIDAY); }
                    if (checkbox_saturday.isChecked()) { schedule.add(DayOfWeek.SATURDAY); }
                    if (checkbox_sunday.isChecked()) { schedule.add(DayOfWeek.SUNDAY); }
                    habit.setDaysActive(schedule);

                    repo.save(habit);
                    finish();
                }
            }
        });

    }

    private boolean verifyFields() {
        boolean valid = true;

        // TODO: check if type is empty string

        if (edittext_habit_reason.getText().toString().length() > 30) {
            edittext_habit_reason.setError("Reason must be less than 30 characters");
            valid = false;
        }

        return valid;
    }

    private void bindToUi() {
        button_add_habit = (Button) findViewById(R.id.buttonAddHabit);
        button_set_date = (Button) findViewById(R.id.button_set_date);
        edittext_habit_name = (EditText) findViewById(R.id.editTextHabitName);
        edittext_habit_reason = (EditText) findViewById(R.id.editTextReason);
        textview_habit_start_date = (TextView) findViewById(R.id.textViewDate);
        checkbox_monday = (CheckBox) findViewById(R.id.checkBoxMonday);
        checkbox_tuesday = (CheckBox) findViewById(R.id.checkBoxTuesday);
        checkbox_wednesday = (CheckBox) findViewById(R.id.checkBoxWednesday);
        checkbox_thursday = (CheckBox) findViewById(R.id.checkBoxThursday);
        checkbox_friday = (CheckBox) findViewById(R.id.checkBoxFriday);
        checkbox_saturday = (CheckBox) findViewById(R.id.checkBoxSaturday);
        checkbox_sunday = (CheckBox) findViewById(R.id.checkBoxSunday);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        date = LocalDate.of(year, month + 1, day);
        textview_habit_start_date.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
