package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HabitsMainActivity extends BottomMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_main);
        addMenuListeners();
    }

    @Override
    public void goToToday() {
        Intent intent = new Intent(this, TodayMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToHabits() {

    }

    @Override
    public void goToSocial() {
        Intent intent = new Intent(this, SocialMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToProfile() {
    }
}
