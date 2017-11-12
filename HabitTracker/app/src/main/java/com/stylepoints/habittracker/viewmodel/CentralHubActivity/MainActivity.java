package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.DatabaseInitUtil;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.EventsMainActivity;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitsMainActivity;


public class MainActivity extends AppCompatActivity {
    private AppDatabase db;

    Button habitButton;
    Button eventButton;
    Button profileButton;
    Button socialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        // for now, initialize database with a couple of test entries
        DatabaseInitUtil.initializeDbWithTestData(db);

        setupButtons();
    }

    private void setupButtons() {
        habitButton = (Button) findViewById(R.id.habitsMenuButton);
        eventButton = (Button) findViewById(R.id.eventsMenuButton);
        profileButton = (Button) findViewById(R.id.profileMenuButton);
        socialButton = (Button) findViewById(R.id.socialMenuButton);

        eventButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EventsMainActivity.class);
                startActivity(intent);
            }
        });

        habitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HabitsMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
