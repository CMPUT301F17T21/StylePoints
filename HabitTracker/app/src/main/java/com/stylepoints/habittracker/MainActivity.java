package com.stylepoints.habittracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.viewmodel.HabitViewModel;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private HabitViewModel viewModel;

    TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getAppDatabase(getApplicationContext());

        testText = (TextView) findViewById(R.id.tv_test);
        insertTestData();

        viewModel = ViewModelProviders.of(this).get(HabitViewModel.class);
        subscribeToModel(viewModel);
    }

    private void subscribeToModel(final HabitViewModel model) {
        // observe the habit data
        model.init("exercise");
        model.getHabit().observe(this, new Observer<HabitEntity>() {
            @Override
            public void onChanged(@Nullable HabitEntity habitEntity) {
                testText.setText(habitEntity.getReason());
            }


        });
    }

    private void insertTestData() {
        HabitEntity h1 = new HabitEntity("exercise", "healthy", new Date(), "MWF");
        db.habitDao().save(h1);
    }
}
