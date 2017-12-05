package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.Relationship;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.RelationshipRepository;
import com.stylepoints.habittracker.repository.UserRepository;

public class FollowResponseActivity extends AppCompatActivity implements FollowingAsyncCallback{

    private Button acceptButton;
    private Button rejectButton;
    private TextView followerUsername;

    private UserRepository userRepo;
    private RelationshipRepository relationRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_response);

        acceptButton = (Button) findViewById(R.id.acceptButton);
        rejectButton = (Button) findViewById(R.id.rejectButton);
        followerUsername = (TextView) findViewById(R.id.followerUsername);

        Intent intent = getIntent();
        followerUsername.setText(intent.getStringExtra("username"));

        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

        relationRepo = RelationshipRepository.getInstance(getApplicationContext());


        acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                setRelationship(Relationship.FOLLOW_ACCEPTED);


                Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Accepted!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        rejectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                setRelationship(Relationship.FOLLOW_REJECTED);


                Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Rejected!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    public void setRelationship(String status){
        relationRepo.findRelationshipForResponse(userRepo.getUserName(), followerUsername.getText().toString(), this, status);
    }

    @Override
    public void setLoading() {

    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(FollowResponseActivity.this, Followers.class);
        startActivity(intent);
    }

    @Override
    public void onError(Throwable t) {

    }
}
