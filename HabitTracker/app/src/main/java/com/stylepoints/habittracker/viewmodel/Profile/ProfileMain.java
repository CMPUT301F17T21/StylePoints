package com.stylepoints.habittracker.viewmodel.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.UserRepository;
import com.stylepoints.habittracker.viewmodel.SocialFeed.MapEvents;
import com.stylepoints.habittracker.viewmodel.SocialFeed.SocialFeed;

public class ProfileMain extends AppCompatActivity {

    Button achievements;
    Button question;
    Button logout;

    private Intent intent;

    private UserRepository userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        achievements = (Button) findViewById(R.id.achievements);
        question = (Button) findViewById(R.id.question);
        logout = (Button) findViewById(R.id.logOut);

        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

        achievements.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileMain.this, Achievements.class);
                startActivity(intent);
            }
        });
        question.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),"Complete events to level up!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        logout.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                userRepo.logOutUser();
            }
        });

    }
}

