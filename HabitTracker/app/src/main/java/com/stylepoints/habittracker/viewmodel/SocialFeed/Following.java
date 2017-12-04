package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.RelationshipRepository;
import com.stylepoints.habittracker.repository.UserRepository;

public class Following extends AppCompatActivity implements FollowingAsyncCallback{

    Button searchButton;
    EditText usernameInput;

    private UserRepository userRepo;
    private RelationshipRepository relationRepo;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        searchButton = (Button) findViewById(R.id.searchButton);
        usernameInput = (EditText) findViewById(R.id.searchUser);

        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

        relationRepo = RelationshipRepository.getInstance(getApplicationContext());

        searchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUser();
                //run username against database, request to follow,
                //a follow request function...

            }
        });
    }

    public void followUser(){
        username = usernameInput.getText().toString();
        relationRepo.newRelationship(userRepo.getUserName(), username, this);
    }

    @Override
    public void setLoading() {
        searchButton.setEnabled(false);
        usernameInput.setEnabled(false);
        Toast toast = Toast.makeText(getApplicationContext(),"Searching " + username + "...", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void onSuccess() {
        searchButton.setEnabled(true);
        usernameInput.setEnabled(true);
        Toast toast = Toast.makeText(getApplicationContext(), "Follow Requested Sent to: " + username, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void onError(Throwable t) {
        searchButton.setEnabled(true);
        usernameInput.setEnabled(true);
        try {
            throw t.getCause();
        } catch (UserNotFound userNotFound) {
            Toast toast = Toast.makeText(getApplicationContext(), "User: " + username + " not found", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } catch (Throwable throwable) {
            Snackbar.make(findViewById(android.R.id.content), "Problem Contacting Server", Snackbar.LENGTH_LONG).show();
            throwable.printStackTrace();
        }

    }
}
