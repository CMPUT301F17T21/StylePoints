package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stylepoints.habittracker.R;

public class NewUserActivity extends AppCompatActivity {

    Button loginButton;
    EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        loginButton = (Button) findViewById(R.id.userLoginButton);
        usernameInput = (EditText) findViewById(R.id.userNameInput);

        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(NewUserActivity.this, MainActivity.class);
                intent.putExtra("username", usernameInput.getText().toString());
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
