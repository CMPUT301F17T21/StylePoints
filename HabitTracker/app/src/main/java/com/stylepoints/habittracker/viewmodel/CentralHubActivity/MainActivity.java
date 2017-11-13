package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.DatabaseInitUtil;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.EventsMainActivity;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitsMainActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private AppDatabase db;

    Button habitButton;
    Button eventButton;
    Button profileButton;
    Button socialButton;

    ListView listViewHabitToday;

    private LiveData<List<HabitEntity>> habitList;
    private HabitRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        // for now, initialize database with a couple of test entries
        DatabaseInitUtil.initializeDbWithTestData(db);

        repo = HabitRepository.getInstance(db);
        habitList = repo.loadAll();

        bindToUi();
        subscribeToModel();
    }

    private void subscribeToModel() {
        habitList.observe(this, habitList -> {
            if (habitList == null) { return; }
            // TODO: do this a better way
            for (HabitEntity habit : habitList) {
                if (!habit.isActiveToday()) {
                    habitList.remove(habit);
                }
            }

            ArrayAdapter<HabitEntity> adapter = new ArrayAdapter<HabitEntity>(
                    this, android.R.layout.simple_list_item_1, habitList);
            listViewHabitToday.setAdapter(adapter);
        });
    }

    private void bindToUi() {
        listViewHabitToday = (ListView) findViewById(R.id.todaysHabitsList);
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

        listViewHabitToday.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long l){ // Be aware that the subsequent class is different from the EventAddNewActivity
                Intent intent = new Intent(MainActivity.this, EventTodayNewActivity.class);
                startActivity(intent);
            }
        });
    }
}
