package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.EventsMainActivity;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitEditActivity;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitsMainActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private AppDatabase db;

    Button habitButton;
    Button eventButton;
    Button profileButton;
    Button socialButton;

    ListView listView;
    ArrayAdapter<HabitEntity> adapter;

//    private List<HabitEntity> habitList;
//    private LiveData<List<HabitEntity>> fullList;
//    private HabitRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HabitRepository habitRepo = HabitRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        bindToUi();


        habitModel.getHabitList().observe(this, habitList -> {
            ArrayAdapter<HabitEntity> adapter = new ArrayAdapter<HabitEntity>(
                    this, android.R.layout.simple_list_item_1, habitList);
            listView.setAdapter(adapter);
        });

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                int habitId = habitModel.getHabitList().getValue().get(position).getId();
                Intent intent = new Intent(MainActivity.this, EventTodayNewActivity.class);
                intent.putExtra("HABIT_ID", habitId);
                startActivity(intent);
            }
        });

//        subscribeToModel();

//        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
//        // for now, initialize database with a couple of test entries
//        // DatabaseInitUtil.initializeDbWithTestData(db);
//
//        repo = HabitRepository.getInstance(db);
//        fullList = repo.loadAll();
//        habitList = new ArrayList<>();

//        bindToUi();
//        subscribeToModel();

//        adapter = new ArrayAdapter<HabitEntity>(
//                this, android.R.layout.simple_list_item_1, habitList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?>adapter, View v, int position, long l){
//                int habitId = repo.getHabitList().getValue().get(position).getId();
//                intent = new Intent(HabitsMainActivity.this, HabitEditActivity.class);
//                intent.putExtra("HABIT_ID", habitId);
//                startActivity(intent);
//            }
//        });
    }

//    private void subscribeToModel() {
//        fullList.observe(this, fullList -> {
//            if (fullList == null) { return; }
//            // TODO: do this a better way
//            habitList.clear();
//            for (HabitEntity habit : fullList) {
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
    }
}
