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

    private String requestStatus;

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
                requestStatus = Relationship.FOLLOW_ACCEPTED;
                setRelationship(requestStatus);

            }
        });
        rejectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStatus = Relationship.FOLLOW_REJECTED;
                setRelationship(requestStatus);

            }
        });
    }

    public void setRelationship(String status){
        relationRepo.findRelationshipForResponse(followerUsername.getText().toString(), userRepo.getUserName(),  this, status);
    }

    public void acceptedToast(){
        Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Accepted!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public void rejectedToast(){
        Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Rejected!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void setLoading() {

    }

    @Override
    public void onSuccess() {
        if (requestStatus == Relationship.FOLLOW_ACCEPTED){
            acceptedToast();
        } else if (requestStatus == Relationship.FOLLOW_REJECTED){
            rejectedToast();
        }
        Intent result = new Intent(this, Following.class);
        setResult(RESULT_OK, result);
        finish();

    }

    @Override
    public void onError(Throwable t) {
        Toast toast = Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
        Intent result = new Intent(this, Following.class);
        setResult(RESULT_OK, result);
        finish();
    }
}
