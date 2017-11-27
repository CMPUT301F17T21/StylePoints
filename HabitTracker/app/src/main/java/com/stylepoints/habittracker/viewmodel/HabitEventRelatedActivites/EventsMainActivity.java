package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;
// List duplication error
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;
import com.stylepoints.habittracker.viewmodel.CentralHubActivity.MainActivity;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary.HabitEventListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary.HabitEventListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModelFactory;

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

    public void setEventList(List<HabitEventEntity> eventList) {
        this.eventList = eventList;
    }

    public void setHabitList(List<HabitEntity> habitList) {
        this.habitList = habitList;
    }

    public void setFilteredEventList(List<HabitEventEntity> filteredEventList) {
        this.filteredEventList = filteredEventList;
    }

    public List<HabitEventEntity> getFilteredEventList() {
        return filteredEventList;
    }

    // Widgets
    private Button button_new_event;
    private Button button_filter_none;
    private Button button_filter_habit;
    private Button button_filter_keyword;
    private ListView listview_event_list;
    private Spinner spinner_habit_select;
    private EditText edittext_keyword;

    private List<HabitEventEntity> eventList;
    private List<HabitEventEntity> filteredEventList;
    private List<HabitEntity> habitList;

    private ArrayAdapter<HabitEventEntity> eventArrayAdapter; // adapter for the array of events
    private ArrayAdapter<HabitEntity> habitArrayAdapter; // adapter for the array of habits
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
        listview_event_list = (ListView) findViewById(R.id.eventListView);
        spinner_habit_select = (Spinner) findViewById(R.id.habitSelectSpinner);
        edittext_keyword = (EditText) findViewById(R.id.editTextKeyword);
    }

    private void initEventListAndFilteredEventList(HabitEventListViewModel eventModel) {
        eventModel.getEventList().observe(this, eventList -> {
            Collections.sort(eventList, new Comparator<HabitEventEntity>() {
                public int compare(HabitEventEntity e0, HabitEventEntity e1) {
                    return e1.getDate().compareTo(e0.getDate());
                }
            });
            EventsMainActivity.this.setEventList(eventList);
            EventsMainActivity.this.setFilteredEventList(eventList);
            ArrayAdapter<HabitEventEntity> adapter = new ArrayAdapter<HabitEventEntity>(
                    this, android.R.layout.simple_list_item_1, eventList);
            listview_event_list.setAdapter(adapter);
        });
    }

    private void initFilterButtons() {
        // no filter
        button_filter_none.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsMainActivity.this.setFilteredEventList(new ArrayList<HabitEventEntity>(EventsMainActivity.this.getEventList()));
                ArrayAdapter<HabitEventEntity> adapter = new ArrayAdapter<HabitEventEntity>(
                        EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                listview_event_list.setAdapter(adapter);
            }
        });

        // habit filter
        button_filter_habit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsMainActivity.this.setFilteredEventList(new ArrayList<HabitEventEntity>(EventsMainActivity.this.getEventList()));
                Iterator<HabitEventEntity> it = EventsMainActivity.this.getFilteredEventList().iterator();
                while(it.hasNext()) {
                    if (it.next().getHabitId() != ((HabitEntity) spinner_habit_select.getSelectedItem()).getId()) {
                        it.remove();
                    }
                }

                ArrayAdapter<HabitEventEntity> adapter = new ArrayAdapter<HabitEventEntity>(
                        EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                listview_event_list.setAdapter(adapter);
            }
        });

        // commentary filter
        button_filter_keyword.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsMainActivity.this.setFilteredEventList(new ArrayList<HabitEventEntity>(EventsMainActivity.this.getEventList()));
                Iterator<HabitEventEntity> it = EventsMainActivity.this.getFilteredEventList().iterator();
                while(it.hasNext()) {
                    HabitEventEntity hee = it.next();
                    if (!hee.getComment().contains(edittext_keyword.getText().toString())) {
                        it.remove();
                    }
                }

                ArrayAdapter<HabitEventEntity> adapter = new ArrayAdapter<HabitEventEntity>(
                        EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                listview_event_list.setAdapter(adapter);
            }
        });
    }

    private void initSpinnerHabitSelect(HabitListViewModel habitModel) {
        habitModel.getHabitList().observe(this, habitList -> {
            habitArrayAdapter = new ArrayAdapter<HabitEntity>(EventsMainActivity.this, android.R.layout.simple_list_item_1, habitList);
            spinner_habit_select.setAdapter(habitArrayAdapter);
        });
    }

    private void initListViewListener() {
        listview_event_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long l){
                int eventId = EventsMainActivity.this.getFilteredEventList().get(position).getId();
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
}
