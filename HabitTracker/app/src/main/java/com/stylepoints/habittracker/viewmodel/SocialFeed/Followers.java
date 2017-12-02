package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stylepoints.habittracker.R;

public class Followers extends AppCompatActivity {


    Button acceptButton;
    Button rejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        acceptButton = (Button) findViewById(R.id.acceptRequest);
        rejectButton = (Button) findViewById(R.id.rejectRequest);
        acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //accept request function
                Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Accepted!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        rejectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reject request function
                Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Rejected!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });

    }
}
