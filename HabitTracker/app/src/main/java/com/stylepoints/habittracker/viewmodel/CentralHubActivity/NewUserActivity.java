package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.AsyncUserLoader;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.UserRepository;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class NewUserActivity extends AppCompatActivity {

    String username;

    Button loginButton;
    EditText usernameInput;

    HabitRepository hR;
    HabitEventRepository hER;

    AsyncUserLoader userLoader;

    SharedPreferences.Editor prefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        loginButton = (Button) findViewById(R.id.userLoginButton);
        usernameInput = (EditText) findViewById(R.id.userNameInput);

        hR = HabitRepository.getInstance(getApplicationContext());
        hER = HabitEventRepository.getInstance(getApplicationContext());

        hR.deleteAll();
        hER.deleteAll();

        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    userLoader = new AsyncUserLoader(new UserRepository(hR, hER));
                    username = usernameInput.getText().toString();

                    Integer result = userLoader.execute(username).get();
                    if (result.equals(0)){
                        throw new IOException("Problem Contacting Server");
                    }
                    getPreferences(0).edit().putString("username", username).commit();
                    Intent intent = new Intent(NewUserActivity.this, MainActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (IOException e) {
                    Snackbar.make(view, "Problem Contacting Server", Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
