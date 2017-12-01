package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.local.AppDatabase;

public class Following extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
    }
}
