package com.stylepoints.habittracker;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stylepoints.habittracker.repository.local.AppDatabase;


public class MainActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HabitDetailsFragment fragment = HabitDetailsFragment.newInstance(2);
        ft.replace(R.id.main_activity_placeholder, fragment);
        ft.commit();
    }
}
