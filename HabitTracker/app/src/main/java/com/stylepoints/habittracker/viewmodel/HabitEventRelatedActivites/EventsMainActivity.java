package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;
import com.stylepoints.habittracker.viewmodel.CentralHubActivity.EventTodayNewActivity;

import java.util.ArrayList;
import java.util.List;

public class EventsMainActivity extends AppCompatActivity {

    // Internals
    private Intent intent;

    // Widgets
    private Button button_new_habit;
    private ListView listview_habit_list;
    private Spinner spinner_event_filter;

    // Test properties, will be replaced
    List<HabitEventEntity> eventList;
    private ArrayAdapter<HabitEventEntity> eventArrayAdapter; // adapter for the array of events
    private ArrayAdapter<String> filtersArrayAdapter; // adapter for the array of filters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);

        HabitEventRepository repo = HabitEventRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));

        button_new_habit = (Button) findViewById(R.id.addNewEventButton);
        listview_habit_list = (ListView) findViewById(R.id.eventListView);
        spinner_event_filter = (Spinner) findViewById(R.id.eventFilterSpinner);



        List<String> filterlist = new ArrayList<String>();
        filterlist.add("Reverse Chronology");
        filterlist.add("Key Word in Comment");
        filtersArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterlist);
        spinner_event_filter.setAdapter(filtersArrayAdapter);


        listview_habit_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long l){
                intent = new Intent(EventsMainActivity.this, EventTodayNewActivity.class);
                startActivity(intent);
            }
        });

        button_new_habit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(EventsMainActivity.this, EventNewActivity.class);
                startActivity(intent);
            }
        });
    }
}
