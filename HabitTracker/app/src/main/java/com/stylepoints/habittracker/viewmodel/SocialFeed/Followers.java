package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.Relationship;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.RelationshipRepository;
import com.stylepoints.habittracker.repository.UserRepository;
import com.stylepoints.habittracker.viewmodel.CentralHubActivity.EventTodayNewActivity;
import com.stylepoints.habittracker.viewmodel.CentralHubActivity.MainActivity;

import java.util.Observer;

public class Followers extends AppCompatActivity implements FollowingAsyncCallback{


    private static final Integer REFRESH_PAGE = 0;

    private ListView followersListView;
    private ListView requestedListView;


    private RelationshipRepository relationRepo;
    private UserRepository userRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        followersListView = (ListView) findViewById(R.id.followersList);
        requestedListView = (ListView) findViewById(R.id.requestsList);

        relationRepo = RelationshipRepository.getInstance(getApplicationContext());
        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

        refreshPage();

        requestedListView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String username = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(Followers.this, FollowResponseActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, REFRESH_PAGE);
            }
        });


    }

    public void refreshPage(){
        relationRepo.getFollowers(userRepo.getUserName(), this).observe(this, followerList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, followerList);
            followersListView.setAdapter(adapter);
        });

        relationRepo.getFollowRequests(userRepo.getUserName(), this).observe(this, requestList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, requestList);
            requestedListView.setAdapter(adapter);
        });
    }

    @Override
    public void setLoading() {
        //Toast toast = Toast.makeText(getApplicationContext(),"Searching " + username + "...", Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER,0,0);
        //toast.show();


    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REFRESH_PAGE){
                refreshPage();
            }
        }
    }
}
