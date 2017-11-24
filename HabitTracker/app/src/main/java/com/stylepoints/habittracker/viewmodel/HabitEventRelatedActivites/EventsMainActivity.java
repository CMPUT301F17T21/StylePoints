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

    public Button getButton_new_event() {
        return button_new_event;
    }

    public ListView getListview_event_list() {
        return listview_event_list;
    }

    public Spinner getSpinner_event_filter() {
        return spinner_event_filter;
    }

    public Spinner getSpinner_habit_select() {
        return spinner_habit_select;
    }

    public List<String> getFilterList() {
        return filterList;
    }

    public List<HabitEventEntity> getEventList() {
        return eventList;
    }

    public List<HabitEntity> getHabitList() {
        return habitList;
    }

    public ArrayAdapter<HabitEventEntity> getEventArrayAdapter() {
        return eventArrayAdapter;
    }

    public ArrayAdapter<HabitEntity> getHabitArrayAdapter() {
        return habitArrayAdapter;
    }

    public ArrayAdapter<String> getFiltersArrayAdapter() {
        return filtersArrayAdapter;
    }

    // Widgets
    private Button button_new_event;
    private ListView listview_event_list;
    private Spinner spinner_event_filter;
    private Spinner spinner_habit_select;

    private List<String> filterList;
    private List<HabitEventEntity> eventList;
    private List<HabitEntity> habitList;

    private ArrayAdapter<HabitEventEntity> eventArrayAdapter; // adapter for the array of events
    private ArrayAdapter<HabitEntity> habitArrayAdapter; // adapter for the array of events
    private ArrayAdapter<String> filtersArrayAdapter; // adapter for the array of filters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);

        // get required model
        HabitRepository habitRepo = HabitRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        HabitEventRepository eventRepo = HabitEventRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
        HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);

        // Initialise widgets
        bindToUI();

        // Initialise filter spinner
        filterList = new ArrayList<String>();
        filterList.add("Reverse Chronology");
        filterList.add("Selected Habit");
        filterList.add("Key Word in Comment");
        filtersArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterList);
        spinner_event_filter.setAdapter(filtersArrayAdapter);
        spinner_event_filter.setSelection(0);
        spinner_event_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        // Initialise habit select spinner
        habitList = new ArrayList<HabitEntity>();
        habitModel.getHabitList().observe(this, habitList -> {
            for (HabitEntity habit : habitList) {
                EventsMainActivity.this.getHabitList().add(habit);
            }
            habitArrayAdapter = new ArrayAdapter<HabitEntity>(EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getHabitList());
            spinner_habit_select.setAdapter(habitArrayAdapter);
        });

        spinner_habit_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        listview_event_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long l){
                int eventId = eventModel.getEventList().getValue().get(position).getId();
//                int eventId = eventModel.getEventList().getValue().get(position).getId();
//                intent = new Intent(EventsMainActivity.this, EventEditActivity.class);
//                intent.putExtra("EVENT_ID", eventId);
//                startActivity(intent);
            }
        });

        // Initialise event listView
        eventList = new ArrayList<HabitEventEntity>();
        eventModel.getEventList().observe(this, eventList -> {
            for (HabitEventEntity event : eventList) {
                EventsMainActivity.this.getEventList().add(event);
            }
            eventArrayAdapter = new ArrayAdapter<HabitEventEntity>(EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getEventList());
            listview_event_list.setAdapter(habitArrayAdapter);
        });


        button_new_event.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(EventsMainActivity.this, EventNewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bindToUI () {
        button_new_event = (Button) findViewById(R.id.addNewEventButton);
        listview_event_list = (ListView) findViewById(R.id.eventListView);
        spinner_event_filter = (Spinner) findViewById(R.id.eventFilterSpinner);
        spinner_habit_select = (Spinner) findViewById(R.id.habitSelectSpinner);
    }










    private void subscribeToModel(HabitEventListViewModel eventModel) {
        eventModel.getEventList().observe(this, eventList -> {
            ArrayAdapter<HabitEventEntity> adapter = new ArrayAdapter<HabitEventEntity>(
                    this, android.R.layout.simple_list_item_1, eventList);
            listview_event_list.setAdapter(adapter);
        });
    }
}
