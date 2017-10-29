package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HabitsMainActivity extends AppCompatActivity {

    Intent intent;
    Button new_habit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_main);

        new_habit_button = (Button) findViewById(R.id.addNewHabitButton);


        new_habit_button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){
                intent = new Intent(HabitsMainActivity.this, HabitNewActivity.class);
                startActivity(intent);
            }
        });


    }
}