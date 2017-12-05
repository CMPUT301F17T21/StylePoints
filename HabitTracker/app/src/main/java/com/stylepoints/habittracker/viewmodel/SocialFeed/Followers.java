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

import java.util.Observer;

public class Followers extends AppCompatActivity implements FollowingAsyncCallback{


    private Button acceptButton;
    private Button rejectButton;
    private ListView followersListView;
    private ListView requestedListView;

    private String requestStatus;
    private String highlightedRequest;

    private RelationshipRepository relationRepo;
    private UserRepository userRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        acceptButton = (Button) findViewById(R.id.acceptRequest);
        rejectButton = (Button) findViewById(R.id.rejectRequest);
        followersListView = (ListView) findViewById(R.id.followersList);
        requestedListView = (ListView) findViewById(R.id.requestsList);

        relationRepo = RelationshipRepository.getInstance(getApplicationContext());
        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

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

        requestedListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        requestedListView.setSelection(0);

        requestedListView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
            }
        });



        acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = (String) ((ListView) findViewById(R.id.requestsList)).getSelectedItem();
                requestStatus = Relationship.FOLLOW_ACCEPTED;
                //relationRepo.findRelationshipForResponse();
                //accept request function
                Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Accepted!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        rejectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestStatus = Relationship.FOLLOW_REJECTED;

                //reject request function
                Toast toast = Toast.makeText(getApplicationContext(), "Follow Request Rejected!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });

    }

    @Override
    public void setLoading() {
        acceptButton.setEnabled(false);
        rejectButton.setEnabled(false);
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
}
