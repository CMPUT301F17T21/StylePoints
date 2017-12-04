package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;
// List duplication error

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux.HabitEventListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux.HabitEventListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitListViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EventsMainActivity extends AppCompatActivity {

    //constants labels
    private static final String REV_CHRON = "Reverse Chronology";
    private static final String HABIT_SEL = "Selected Habit";
    private static final String KEYWORD = "Key Word in Comment";

    // Internals
    private Intent intent;

    public Button getButton_new_event() {
        return button_new_event;
    }

    public ListView getListview_event_list() {
        return listview_event_list;
    }

    public Spinner getSpinner_habit_select() {
        return spinner_habit_select;
    }

    public List<HabitEvent> getEventList() {
        return eventList;
    }

    public List<Habit> getHabitList() {
        return habitList;
    }

    public ArrayAdapter<HabitEvent> getEventArrayAdapter() {
        return eventArrayAdapter;
    }

    public ArrayAdapter<Habit> getHabitArrayAdapter() {
        return habitArrayAdapter;
    }

    public ArrayAdapter<String> getFiltersArrayAdapter() {
        return filtersArrayAdapter;
    }

    public void setEventList(List<HabitEvent> eventList) {
        this.eventList = eventList;
    }

    public void setHabitList(List<Habit> habitList) {
        this.habitList = habitList;
    }

    public void setFilteredEventList(List<HabitEvent> filteredEventList) {
        this.filteredEventList = filteredEventList;
    }

    public List<HabitEvent> getFilteredEventList() {
        return filteredEventList;
    }

    // Widgets
    private Button button_new_event;
    private Button button_filter_none;
    private Button button_filter_habit;
    private Button button_filter_keyword;
    private Button button_map_events_listed;
    private ListView listview_event_list;
    private Spinner spinner_habit_select;
    private EditText edittext_keyword;

    private List<HabitEvent> eventList;
    private List<HabitEvent> filteredEventList;
    private List<Habit> habitList;

    private ArrayAdapter<HabitEvent> eventArrayAdapter; // adapter for the array of events
    private ArrayAdapter<Habit> habitArrayAdapter; // adapter for the array of habits
    private ArrayAdapter<String> filtersArrayAdapter; // adapter for the array of filters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);

        // get required model
        HabitRepository habitRepo = HabitRepository.getInstance(getApplicationContext());
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        HabitEventRepository eventRepo = HabitEventRepository.getInstance(getApplicationContext());
        HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
        HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);

        // Initialise widgets
        bindToUI();

        // Intialise eventList and filterList
        initEventListAndFilteredEventList(eventModel);

        // Intialise filter buttons
        initFilterButtons();

        // Initialise habit select spinner
        initSpinnerHabitSelect(habitModel);

        // Initialise event listView listener
        initListViewListener();

        // Initialise new event button
        initButtonAddEvent();

        // Initialise map listed events button
        initButtonMapListedEvents();
    }

    protected void onResume() {
        super.onResume();
        System.out.println();
    }

    private void bindToUI () {
        button_new_event = (Button) findViewById(R.id.addNewEventButton);
        button_filter_none = (Button) findViewById(R.id.buttonFilterNone);
        button_filter_habit = (Button) findViewById(R.id.buttonFilterHabit);
        button_filter_keyword = (Button) findViewById(R.id.buttonFilterKeyword);
        button_map_events_listed = (Button) findViewById(R.id.buttonMapEventList);
        listview_event_list = (ListView) findViewById(R.id.eventListView);
        spinner_habit_select = (Spinner) findViewById(R.id.habitSelectSpinner);
        edittext_keyword = (EditText) findViewById(R.id.editTextKeyword);
    }

    private void initEventListAndFilteredEventList(HabitEventListViewModel eventModel) {
        eventModel.getHabitEventList().observe(this, eventList -> {
            Collections.sort(eventList, new Comparator<HabitEvent>() {
                public int compare(HabitEvent e0, HabitEvent e1) {
                    return e0.getDate().compareTo(e1.getDate());
                }
            });
            EventsMainActivity.this.setEventList(eventList);
            EventsMainActivity.this.setFilteredEventList(eventList);
            ArrayAdapter<HabitEvent> adapter = new ArrayAdapter<HabitEvent>(
                    this, android.R.layout.simple_list_item_1, eventList);
            listview_event_list.setAdapter(adapter);
        });
    }

    private void initFilterButtons() {
        // no filter
        button_filter_none.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsMainActivity.this.setFilteredEventList(new ArrayList<HabitEvent>(EventsMainActivity.this.getEventList()));
                ArrayAdapter<HabitEvent> adapter = new ArrayAdapter<HabitEvent>(
                        EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                listview_event_list.setAdapter(adapter);
            }
        });

        // habit filter
        button_filter_habit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit filter_habit = (Habit) spinner_habit_select.getSelectedItem();
                if (filter_habit != null) {
                    EventsMainActivity.this.setFilteredEventList(new ArrayList<HabitEvent>(EventsMainActivity.this.getEventList()));
                    Iterator<HabitEvent> it = EventsMainActivity.this.getFilteredEventList().iterator();
                    while (it.hasNext()) {
                        if (!(it.next().getHabitId()).equals(filter_habit.getElasticId())) {
                            it.remove();
                        }
                    }
                    ArrayAdapter<HabitEvent> adapter = new ArrayAdapter<HabitEvent>(
                            EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                    listview_event_list.setAdapter(adapter);
                }
            }
        });

        // commentary filter
        button_filter_keyword.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsMainActivity.this.setFilteredEventList(new ArrayList<HabitEvent>(EventsMainActivity.this.getEventList()));
                Iterator<HabitEvent> it = EventsMainActivity.this.getFilteredEventList().iterator();
                while(it.hasNext()) {
                    if (!it.next().getComment().contains(edittext_keyword.getText().toString())) {
                        it.remove();
                    }
                }

                ArrayAdapter<HabitEvent> adapter = new ArrayAdapter<HabitEvent>(
                        EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                listview_event_list.setAdapter(adapter);
            }
        });
    }

    private void initSpinnerHabitSelect(HabitListViewModel habitModel) {
        habitModel.getHabitList().observe(this, habitList -> {
            habitArrayAdapter = new ArrayAdapter<Habit>(EventsMainActivity.this, android.R.layout.simple_list_item_1, habitList);
            spinner_habit_select.setAdapter(habitArrayAdapter);
        });
    }

    private void initListViewListener() {
        listview_event_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long l){
                String eventId = EventsMainActivity.this.getFilteredEventList().get(position).getElasticId();
                intent = new Intent(EventsMainActivity.this, EventEditActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });
    }

    private void initButtonAddEvent() {
        button_new_event.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(EventsMainActivity.this, EventNewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initButtonMapListedEvents() {
        button_map_events_listed.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EventsMainActivity.this, EventsMapActivity.class);
                startActivity(intent);
            }
        });
    }
}
