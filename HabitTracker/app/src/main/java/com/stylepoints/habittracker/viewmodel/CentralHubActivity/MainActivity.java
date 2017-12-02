package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.EventsMainActivity;
import com.stylepoints.habittracker.viewmodel.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitsMainActivity;
import com.stylepoints.habittracker.viewmodel.Profile.ProfileMain;
import com.stylepoints.habittracker.viewmodel.SocialFeed.SocialFeed;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor prefEdit;
    static final int GET_USER_NAME = 1;

    Button habitButton;
    Button eventButton;
    Button profileButton;
    Button socialButton;

    TextView testTextView;

    ListView listView;
    ArrayAdapter<Habit> adapter;

    private List<Habit> habitList;
    private LiveData<List<Habit>> fullList;
    private HabitRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast toast = Toast.makeText(getApplicationContext(), "Welcome to Habit Tracker!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        //Added for getting the user name and ID
        pref = getPreferences(0);
        if (pref.contains("username") == false){
            //Go to Activity to get username. Should only be ran the first time
            Intent getUserNameIntent = new Intent(this, NewUserActivity.class);
            startActivityForResult(getUserNameIntent, GET_USER_NAME);
        }
        String username = pref.getString("username", "");
        Log.i("debug", pref.getString("username", ""));

        HabitRepository habitRepo = HabitRepository.getInstance(getApplicationContext());
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);


        bindToUi();



        habitModel.getHabitList().observe(this, habitList -> {
            ArrayAdapter<Habit> adapter = new ArrayAdapter<Habit>(
                    this, android.R.layout.simple_list_item_1, habitList);
            listView.setAdapter(adapter);
        });

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                String habitId = habitModel.getHabitList().getValue().get(position).getElasticId();
                Intent intent = new Intent(MainActivity.this, EventTodayNewActivity.class);
                intent.putExtra("HABIT_ID", habitId);
                startActivity(intent);
            }
        });
    }

//    private void subscribeToModel() {
//        fullList.observe(this, fullList -> {
//            if (fullList == null) { return; }
//            // TODO: do this a better way
//            habitList.clear();
//            for (Habit habit : fullList) {
//                if (habit.isActiveToday()) {
//                    habitList.add(habit);
//                }
//            }
//            adapter.notifyDataSetChanged();
//        });
//    }

    private void bindToUi() {
        listView = (ListView) findViewById(R.id.todaysHabitsList);
        habitButton = (Button) findViewById(R.id.habitsMenuButton);
        eventButton = (Button) findViewById(R.id.eventsMenuButton);
        profileButton = (Button) findViewById(R.id.profileMenuButton);
        socialButton = (Button) findViewById(R.id.socialMenuButton);

        eventButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EventsMainActivity.class);
                startActivity(intent);
            }
        });

        habitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HabitsMainActivity.class);
                startActivity(intent);
            }
        });
        socialButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SocialFeed.class);
                startActivity(intent);
            }
        });
      
        profileButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileMain.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case GET_USER_NAME:
                if (resultCode == RESULT_OK){
                    prefEdit = pref.edit();
                    prefEdit.putString("username", data.getStringExtra("username"));
                    prefEdit.commit();
                }
                break;
        }

    }
}
