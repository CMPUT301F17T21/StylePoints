package com.stylepoints.habittracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public abstract class BottomMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void addMenuListeners(){
        Button todayButton = (Button) findViewById(R.id.todayMenuButton);
        Button habitButton = (Button) findViewById(R.id.habitsMenuButton);
        Button socialButton = (Button) findViewById(R.id.socialMenuButton);
        Button profileButton = (Button) findViewById(R.id.profileMenuButton);

        todayButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){
                goToToday();
            }
        });

        habitButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){
                goToHabits();
            }
        });

        socialButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){
                goToSocial();
            }
        });

        profileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){
                goToProfile();
            }
        });
    }

    public abstract void goToToday();
    public abstract void goToHabits();
    public abstract void goToSocial();
    public abstract void goToProfile();

}
