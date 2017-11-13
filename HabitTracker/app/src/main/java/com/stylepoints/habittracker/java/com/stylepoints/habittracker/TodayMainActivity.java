package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TodayMainActivity extends BottomMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_main);
        addMenuListeners();
    }

    @Override
    public void goToToday() {

    }

    @Override
    public void goToHabits() {
        Intent intent = new Intent(this, HabitsMainActivity.class);
        startActivity(intent);
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
