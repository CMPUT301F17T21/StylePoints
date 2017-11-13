package com.stylepoints.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Main activity for a habit.
 * Initializes habit oontroller on creation.
 *
 * @author StylePoints
 * @see Habit,HabitController
 * @version 2017/11/13
 */

public class HabitsMainActivity extends AppCompatActivity {

    // Internals
    private Intent intent;
    private HabitController habitcontroller;

    // Widgets
    private Button button_new_habit;
    private ListView listview_habit_list;

    // Test properties, will be replaced
    private ArrayList<Habit> habit_list = new ArrayList<Habit>();
    private ArrayAdapter<Habit> arrayadapter_habit_list; // adapter for the array of counters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_main);

        // Initialise here
        habitcontroller = HabitController.getInstance();

        button_new_habit = (Button) findViewById(R.id.addNewHabitButton);
        listview_habit_list = (ListView) findViewById(R.id.habitListView);

        arrayadapter_habit_list = new ArrayAdapter<Habit>(this, android.R.layout.simple_list_item_1, habit_list);

        // Define listener for selecting counter on listview of counters
        listview_habit_list.setAdapter(arrayadapter_habit_list);

        if (habitcontroller.checkForConnectedHabit() == true) {
            habit_list.add(habitcontroller.disconnectHabit());
        }

        listview_habit_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long l){
                if (habitcontroller.checkForConnectedHabit() == false) {
                    habitcontroller.connectHabit((Habit) HabitsMainActivity.this.listview_habit_list.getItemAtPosition(position));
                }
                intent = new Intent(HabitsMainActivity.this, HabitEditActivity.class);
                startActivity(intent);
            }
        });


        button_new_habit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                intent = new Intent(HabitsMainActivity.this, HabitNewActivity.class);
                startActivity(intent);
            }
        });




    }
}