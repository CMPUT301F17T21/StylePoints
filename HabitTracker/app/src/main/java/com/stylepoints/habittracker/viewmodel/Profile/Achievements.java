package com.stylepoints.habittracker.viewmodel.Profile;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.model.ViewableHabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.UserRepository;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.EventsMainActivity;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux.HabitEventListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux.HabitEventListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitViewModel;
import com.stylepoints.habittracker.viewmodel.HabitViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;

        public class Achievements extends AppCompatActivity {

            TextView mostCommonVal;
            TextView username;
            ListView tenPlusVal;
            ListView hundredPlusVal;
            TextView level;

            private HabitEventRepository eventRepo;
            private HabitRepository habitRepo;
            private Button question;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_achievements);

                mostCommonVal = (TextView) findViewById(R.id.mostCommonVal);
                tenPlusVal = (ListView) findViewById(R.id.tenPlusVal);
                hundredPlusVal = (ListView) findViewById(R.id.hundredPlusVal);
                username = (TextView) findViewById(R.id.username);

                level = (TextView) findViewById(R.id.levelVal);
                level.setText("1");

                habitRepo = HabitRepository.getInstance(getApplicationContext());
                HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
                HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

                eventRepo = HabitEventRepository.getInstance(getApplicationContext());
                HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
                HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);

                UserRepository userRepo = new UserRepository(habitRepo, eventRepo, getApplicationContext());

                username.setText(userRepo.getUserName());

                habitModel.getHabitList().observe(this, habitList -> {

                    ArrayList<Habit> tenPlusList = new ArrayList<Habit>();
                    ArrayList<Habit> hundredPlusList = new ArrayList<Habit>();
                    Habit mostCommon = null;

                    for (Habit habit : habitList) {
                        Float point = userRepo.getHabitsCompletePoints(habit.getElasticId());
                        if (point > 10) {
                            tenPlusList.add(habit);
                        } else if (point > 100) {
                            hundredPlusList.add(habit);
                        }
                        if (mostCommon == null || point > userRepo.getHabitsCompletePoints(mostCommon.getElasticId())) {
                            mostCommon = habit;
                        }
                    }
                    if (mostCommon != null) {
                        mostCommonVal.setText(mostCommon.getType());
                    }
                    ArrayAdapter<Habit> tenPlus = new ArrayAdapter<Habit>(this, android.R.layout.simple_list_item_1, tenPlusList);
                    tenPlusVal.setAdapter(tenPlus);
                    ArrayAdapter<Habit> hundredPlus = new ArrayAdapter<Habit>(this, android.R.layout.simple_list_item_2, hundredPlusList);
                    hundredPlusVal.setAdapter(hundredPlus);
                });


                question = (Button) findViewById(R.id.question);
                question.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Complete events to level up!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                });
            }
        }

