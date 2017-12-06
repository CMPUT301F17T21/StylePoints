package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import android.view.View;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.Relationship;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.RelationshipRepository;
import com.stylepoints.habittracker.repository.UserRepository;

public class SocialFeed extends AppCompatActivity {


    // Internals
    private Intent intent;

    private UserRepository userRepo;
    private RelationshipRepository relationRepo;
    private HabitRepository habitRepo;

    // Widgets
    private Button button_mapEvents;
    private Button button_followers;
    private Button button_following;
    private ListView socialFeed_list;
    private Spinner spinner_social_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_feed);

        bindToUi();

        habitRepo = HabitRepository.getInstance(getApplicationContext());
        relationRepo = RelationshipRepository.getInstance(getApplicationContext());
        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());




        spinner_social_filter = (Spinner) findViewById(R.id.sort_by_spinner);
        spinner_social_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String testing = spinner_social_filter.getItemAtPosition(i).toString();
                Toast toast = Toast.makeText(getApplicationContext(), "Showing " + testing + "...", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                // the three different filtering methods here
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast toast = Toast.makeText(getApplicationContext(), "make a selection!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });

    }
    private void bindToUi() {
        button_mapEvents = (Button) findViewById(R.id.mapEventsButton);
        button_followers = (Button) findViewById(R.id.followersButton);
        button_following = (Button) findViewById(R.id.followingButton);
        socialFeed_list = (ListView) findViewById(R.id.socialList);
        spinner_social_filter = (Spinner) findViewById(R.id.sort_by_spinner);

        button_mapEvents.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialFeed.this, MapEvents.class);
                startActivity(intent);
            }
        });

        button_followers.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialFeed.this, Followers.class);
                startActivity(intent);
            }
        });
        button_following.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialFeed.this, Following.class);
                startActivity(intent);
            }
        });
    }
}
