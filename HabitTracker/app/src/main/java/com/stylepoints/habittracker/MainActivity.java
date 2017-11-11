package com.stylepoints.habittracker;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stylepoints.habittracker.repository.DatabaseInitUtil;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;


public class MainActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        // for now, initialize database with a couple of test entries
        DatabaseInitUtil.initializeDbWithTestData(db);

        // find the id of the habit with type "exercise"
        int habitId = HabitRepository.getInstance(db).getHabitIdFromType("exercise");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //HabitDetailsFragment fragment = HabitDetailsFragment.newInstance(habitId);

        HabitListFragment fragment = HabitListFragment.newInstance();
        ft.replace(R.id.main_activity_placeholder, fragment);
        ft.commit();
    }
}
