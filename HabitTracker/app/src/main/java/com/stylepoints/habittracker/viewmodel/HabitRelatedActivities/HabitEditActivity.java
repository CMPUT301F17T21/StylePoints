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
import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitReasonTooLongException;
import com.stylepoints.habittracker.model.HabitTypeTooLongException;
import com.stylepoints.habittracker.repository.HabitRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

public class HabitEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static String TAG = "HabitEditActivity";
    HabitRepository repo;

    LocalDate date;

    Button button_save_habit;
    Button button_delete_habit;
    Button button_habit_stats;
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
        setContentView(R.layout.activity_habit_edit);
        bindToUi();

        repo = HabitRepository.getInstance(getApplicationContext());

        Intent inputIntent = getIntent();
        if (!inputIntent.hasExtra("HABIT_ID")) {
            // nothing to edit! return back to previous page
            Log.e(TAG, "habitId was not passed in with the intent");
            finish();
            return;
        }

        String habitId = inputIntent.getStringExtra("HABIT_ID");
        Log.d(TAG, "passed in habitId: " + String.valueOf(habitId));
        if (habitId.equals("")) {
            // invalid ID
            Log.e(TAG, "habitId can not be empty string");
            finish();
            return;
        }

        Habit habit = repo.getHabitSync(habitId);
        if (habit == null) {
            Log.e(TAG, "Unable to find habit with id: " + String.valueOf(habitId));
            finish();
            return;
        }

        fillUi(habit);

        button_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "date");
            }
        });

        button_habit_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitEditActivity.this, HabitStatActivity.class);
                intent.putExtra("habitId", habit.getElasticId());
                startActivity(intent);
            }
        });

        button_save_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyFields()) {
                    try {
                        habit.setReason(edittext_habit_reason.getText().toString());
                    } catch (HabitReasonTooLongException e) {
                        e.printStackTrace();
                    }
                    try {
                        habit.setType(edittext_habit_name.getText().toString());
                    } catch (HabitTypeTooLongException e) {
                        e.printStackTrace();
                    }

                    habit.setStartDate(date);

                    EnumSet<DayOfWeek> schedule = EnumSet.noneOf(DayOfWeek.class);
                    if (checkbox_monday.isChecked()) { schedule.add(DayOfWeek.MONDAY); }
                    if (checkbox_tuesday.isChecked()) { schedule.add(DayOfWeek.TUESDAY); }
                    if (checkbox_wednesday.isChecked()) { schedule.add(DayOfWeek.WEDNESDAY); }
                    if (checkbox_thursday.isChecked()) { schedule.add(DayOfWeek.THURSDAY); }
                    if (checkbox_friday.isChecked()) { schedule.add(DayOfWeek.FRIDAY); }
                    if (checkbox_saturday.isChecked()) { schedule.add(DayOfWeek.SATURDAY); }
                    if (checkbox_sunday.isChecked()) { schedule.add(DayOfWeek.SUNDAY); }
                    habit.setDaysActive(schedule);

                    repo.update(habit.getElasticId(), habit);
                    finish();
                }
            }
        });

        button_delete_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.delete(habit.getElasticId());
                finish();
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


    /* Initialisation procedures */
    // Assign widgets to specified variables
    private void bindToUi() {
        button_save_habit = (Button) findViewById(R.id.buttonSaveHabit);
        button_delete_habit = (Button) findViewById(R.id.buttonDeleteHabit);
        button_habit_stats = (Button) findViewById(R.id.buttonHabitStats);
        button_set_date = (Button) findViewById(R.id.buttonSetDate);
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

    // Fill the initialised UI with required values
    private void fillUi(Habit habit) {
        date = habit.getStartDate();

        edittext_habit_name.setText(habit.getType());
        edittext_habit_reason.setText(habit.getReason());
        textview_habit_start_date.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        checkbox_monday.setChecked(habit.getDaysActive().contains(DayOfWeek.MONDAY));
        checkbox_tuesday.setChecked(habit.getDaysActive().contains(DayOfWeek.TUESDAY));
        checkbox_wednesday.setChecked(habit.getDaysActive().contains(DayOfWeek.WEDNESDAY));
        checkbox_thursday.setChecked(habit.getDaysActive().contains(DayOfWeek.THURSDAY));
        checkbox_friday.setChecked(habit.getDaysActive().contains(DayOfWeek.FRIDAY));
        checkbox_saturday.setChecked(habit.getDaysActive().contains(DayOfWeek.SATURDAY));
        checkbox_sunday.setChecked(habit.getDaysActive().contains(DayOfWeek.SUNDAY));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        date = LocalDate.of(year, month + 1, day);
        textview_habit_start_date.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
