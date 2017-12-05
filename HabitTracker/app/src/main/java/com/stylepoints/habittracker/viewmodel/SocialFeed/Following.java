package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.RelationshipRepository;
import com.stylepoints.habittracker.repository.UserRepository;

public class Following extends AppCompatActivity implements FollowingAsyncCallback, CheckUserExistsCallback{

    private Button searchButton;
    private EditText usernameInput;
    private ListView followingListView;

    private UserRepository userRepo;
    private RelationshipRepository relationRepo;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        searchButton = (Button) findViewById(R.id.searchButton);
        usernameInput = (EditText) findViewById(R.id.searchUser);
        followingListView = (ListView) findViewById(R.id.followingList);

        relationRepo = RelationshipRepository.getInstance(getApplicationContext());
        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

        relationRepo.getFollowing(userRepo.getUserName(), this).observe(this, followingList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, followingList);
            followingListView.setAdapter(adapter);
        });

        searchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUser();
            }
        });


    }

    public void followUser(){
        username = usernameInput.getText().toString();
        userRepo.checkUserExits(username, this);
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
            throw t;
        } catch (Throwable throwable) {
            Snackbar.make(findViewById(android.R.id.content), t.getMessage(), Snackbar.LENGTH_SHORT).show();
            throwable.printStackTrace();
        }

    }

    @Override
    public void userExists() {
        relationRepo.checkRelationshipExists(userRepo.getUserName(), username, this);
    }

    @Override
    public void userDoesNotExist() {
        Snackbar.make(findViewById(android.R.id.content), "User: " + username + " not found", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setError(Throwable t) {
        Snackbar.make(findViewById(android.R.id.content), t.getMessage(), Snackbar.LENGTH_SHORT).show();
        t.printStackTrace();
    }
}
