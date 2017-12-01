package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.view.View;
import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.local.AppDatabase;

public class SocialFeed extends AppCompatActivity {
    private AppDatabase db;

    // Internals
    private Intent intent;

    // Widgets
    private Button button_mapEvents;
    private Button button_followers;
    private Button button_following;
    private ListView listview_socialFeed;
    private Spinner spinner_social_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_feed);

        bindToUi();

    }
    private void bindToUi() {
        button_mapEvents = (Button) findViewById(R.id.mapEventsButton);
        button_followers = (Button) findViewById(R.id.followersButton);
        button_following = (Button) findViewById(R.id.followingButton);

        button_mapEvents.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialFeed.this, MapEvents.class);
                startActivity(intent);
            }
        });

        button_followers.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialFeed.this, Followers.class);
                startActivity(intent);
            }
        });
        button_following.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialFeed.this, Following.class);
                startActivity(intent);
            }
        });
    }

}
