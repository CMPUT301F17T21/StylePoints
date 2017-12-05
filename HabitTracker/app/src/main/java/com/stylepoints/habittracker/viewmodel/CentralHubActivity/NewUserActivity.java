package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.UserRepository;

public class NewUserActivity extends AppCompatActivity implements UserAsyncCallback{

    private Button loginButton;
    private EditText usernameInput;

    private UserRepository userRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        loginButton = (Button) findViewById(R.id.userLoginButton);
        usernameInput = (EditText) findViewById(R.id.userNameInput);

        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());


        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                login();
            }
        });
    }

    public void login(){
        userRepo.logInUser(usernameInput.getText().toString(), this);
    }

    @Override
    public void setLoading() {
        loginButton.setEnabled(false);
        usernameInput.setEnabled(false);
    }

    @Override
    public void setError(Throwable t) {
        try {
            throw t;
        } catch (Throwable e) {
            Snackbar.make(findViewById(android.R.id.content), "Problem Contacting Server", Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void setSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }
}
