package com.stylepoints.habittracker.viewmodel.SocialFeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stylepoints.habittracker.R;

public class Following extends AppCompatActivity {

    Button searchButton;
    EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        searchButton = (Button) findViewById(R.id.searchButton);
        usernameInput = (EditText) findViewById(R.id.searchUser);


        searchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(),"Searching " + username + "...", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                //run username against database, request to follow,
                //a follow request function...

            }
        });
    }
}
