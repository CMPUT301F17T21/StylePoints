package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;

import android.arch.lifecycle.ViewModelProviders;
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
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary.HabitEventListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary.HabitEventListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class EventsMainActivity extends AppCompatActivity {

    // Internals
    private Intent intent;

    // Widgets
    private Button button_new_event;
    private ListView listview_event_list;
    private Spinner spinner_event_filter;

    // Test properties, will be replaced
    List<HabitEventEntity> eventList;
    List<HabitEntity> habitList;
    private ArrayAdapter<HabitEventEntity> eventArrayAdapter; // adapter for the array of events
    private ArrayAdapter<String> filtersArrayAdapter; // adapter for the array of filters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);

        HabitRepository habitRepo = HabitRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        HabitEventRepository eventRepo = HabitEventRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
        HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);

        button_new_event = (Button) findViewById(R.id.addNewEventButton);
        listview_event_list = (ListView) findViewById(R.id.eventListView);
        spinner_event_filter = (Spinner) findViewById(R.id.eventFilterSpinner);

        List<String> filterlist = new ArrayList<String>();
        filterlist.add("Reverse Chronology");
        filterlist.add("Key Word in Comment");
        // Need to add all habit names
        filtersArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterlist);
        spinner_event_filter.setAdapter(filtersArrayAdapter);
        habitModel.getHabitList().observe(this, habitList -> {
            for (HabitEntity habit : habitList) {
                filterlist.add(habit.getType());
            }
            ArrayAdapter<HabitEntity> adapter = new ArrayAdapter<HabitEntity>(
                    this, android.R.layout.simple_list_item_2, habitList);
        });









        listview_event_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long l){
                int eventId = eventModel.getEventList().getValue().get(position).getId();
                intent = new Intent(EventsMainActivity.this, EventEditActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });

        button_new_event.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(EventsMainActivity.this, EventNewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void subscribeToModel(HabitEventListViewModel eventModel) {
        eventModel.getEventList().observe(this, eventList -> {
            ArrayAdapter<HabitEventEntity> adapter = new ArrayAdapter<HabitEventEntity>(
                    this, android.R.layout.simple_list_item_1, eventList);
            listview_event_list.setAdapter(adapter);
        });
    }
}
