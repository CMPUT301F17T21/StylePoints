package com.stylepoints.habittracker.viewmodel.HabitRelatedActivities;

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
import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.viewmodel.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitListViewModelFactory;

import java.util.List;

public class HabitsMainActivity extends AppCompatActivity {
    // Internals
    private Intent intent;

    // Widgets
    private Button button_new_habit;
    private ListView listview_habit_list;

    // Test properties, will be replaced
    List<Habit> habitList;
    private ArrayAdapter<Habit> habitArrayAdapter; // adapter for the array of counters


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_main);

        HabitRepository repo = HabitRepository.getInstance(getApplicationContext());
        HabitListViewModelFactory factory = new HabitListViewModelFactory(repo);
        HabitListViewModel model = ViewModelProviders.of(this, factory).get(HabitListViewModel.class);

        button_new_habit = (Button) findViewById(R.id.addNewHabitButton);
        listview_habit_list = (ListView) findViewById(R.id.habitListView);

        // handler for what to do when an item is clicked on in the list.
        // in this case, switch to the edit activity, and pass in the habitId
        // so that the edit activity can populate the fields
        listview_habit_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long l){
                String habitId = model.getHabitList().getValue().get(position).getElasticId();
                intent = new Intent(HabitsMainActivity.this, HabitEditActivity.class);
                intent.putExtra("HABIT_ID", habitId);
                startActivity(intent);
            }
        });

        // Add new habit button, start the activity to add a habit
        button_new_habit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(HabitsMainActivity.this, HabitNewActivity.class);
                startActivity(intent);
            }
        });

        subscribeToModel(model);
    }

    private void subscribeToModel(HabitListViewModel model) {

        model.getHabitList().observe(this, habitList -> {
            ArrayAdapter<Habit> adapter = new ArrayAdapter<Habit>(
                    this, android.R.layout.simple_list_item_1, habitList);
            listview_habit_list.setAdapter(adapter);
        });
    }
}
