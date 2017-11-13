package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Social menu protocol. Redirects to habit and today activity menus
 * (later social and profile menus).
 *
 * @author StylePoints
 * @see TodayMainActivity,HabitsMainActivity
 * @version 2017/10/18
 */

public class SocialMainActivity extends BottomMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_main);
        addMenuListeners();
    }

    @Override
    public void goToToday() {
        Intent intent = new Intent(this, TodayMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToHabits() {
        Intent intent = new Intent(this, HabitsMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToSocial() {

    }

    @Override
    public void goToProfile() {
    }
}
