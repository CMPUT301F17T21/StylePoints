package com.stylepoints.habittracker.viewmodel.Profile;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.RelationshipRepository;
import com.stylepoints.habittracker.repository.UserRepository;
import com.stylepoints.habittracker.viewmodel.SocialFeed.FollowingAsyncCallback;
import com.stylepoints.habittracker.viewmodel.SocialFeed.MapEvents;
import com.stylepoints.habittracker.viewmodel.SocialFeed.SocialFeed;

import java.util.List;

public class ProfileMain extends AppCompatActivity implements FollowingAsyncCallback {

    Button achievements;
    Button question;
    Button logout;
    TextView level;
    TextView username;
    TextView followers;
    TextView following;

    private Intent intent;

    LiveData<List<String>> followersList;
    LiveData<List<String>> followingList;

    private UserRepository userRepo;
    private RelationshipRepository relationRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        achievements = (Button) findViewById(R.id.achievements);
        question = (Button) findViewById(R.id.question);
        logout = (Button) findViewById(R.id.logOut);
        level = (TextView) findViewById(R.id.levelVal);
        level.setText("1");

        username = (TextView) findViewById(R.id.username);



        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

        relationRepo = RelationshipRepository.getInstance(getApplicationContext());

        username.setText(userRepo.getUserName());
        followersList = relationRepo.getFollowers(userRepo.getUserName(),this);
        followingList = relationRepo.getFollowing(userRepo.getUserName(),this);

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

    @Override
    public void setLoading() {

    }

    @Override
    public void onSuccess() {
        followers = (TextView) findViewById(R.id.followerVal);
        following = (TextView) findViewById(R.id.followingVal);
        try {
            followers.setText("" + followersList.getValue().size());
        } catch (Exception e){
            followers.setText("0");
        }
        try {
            following.setText("" + followingList.getValue().size());
        } catch (Exception e){
            following.setText("0");
        }
    }

    @Override
    public void onError(Throwable t) {

    }
}

