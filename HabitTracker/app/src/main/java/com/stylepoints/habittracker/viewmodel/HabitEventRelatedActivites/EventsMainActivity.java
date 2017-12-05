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
import com.stylepoints.habittracker.model.ViewableHabitEvent;
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

    // filter labels
    private static final String REV_CHRON = "Reverse Chronology";
    private static final String HABIT_SEL = "Selected Habit";
    private static final String KEYWORD = "Key Word in Comment";

    // getter of private fields (for lambda or inner functions)
    public Button getButton_new_event() {
        return button_new_event;
    }

    public ListView getListview_event_list() {
        return listview_event_list;
    }

    public Spinner getSpinner_habit_select() {
        return spinner_habit_select;
    }

    public List<ViewableHabitEvent> getEventList() {
        return eventList;
    }

    public List<Habit> getHabitList() {
        return habitList;
    }

    public ArrayAdapter<Habit> getHabitArrayAdapter() {
        return habitArrayAdapter;
    }

    public List<ViewableHabitEvent> getFilteredEventList() {
        return filteredEventList;
    }

    public HabitRepository getHabitRepo() {
        return habitRepo;
    }

    // set private fields (innner and lambda function)
    public void setEventList(List<ViewableHabitEvent> eventList) {
        this.eventList = eventList;
    }

    public void setHabitList(List<Habit> habitList) {
        this.habitList = habitList;
    }

    public void setFilteredEventList(List<ViewableHabitEvent> filteredEventList) {
        this.filteredEventList = filteredEventList;
    }

    // Widgets
    private Button button_new_event; // button linked to EventNewActivity
    private Button button_filter_none; // unfiltered list, date-reverse-sorted
    private Button button_filter_habit; // filtered by habit, maintains data order
    private Button button_filter_keyword; // filtered by keyword, maintains date order
    private Button button_map_events_listed; // button linked to EventsMapActivity
    private ListView listview_event_list; // presents the events after filtering
    private Spinner spinner_habit_select; // habit selection for filtering purposes
    private EditText edittext_keyword; // keyword input for filtering by comment purposes

    private List<ViewableHabitEvent> eventList; // date-sorted, but unfiltered eventList
    private List<ViewableHabitEvent> filteredEventList; // filtered list (by no filter, habit, or date)
    private List<Habit> habitList; // list of habit set for spinner

    private ArrayAdapter<Habit> habitArrayAdapter; // adapter for the array of habits

    // required repository
    private HabitRepository habitRepo;
    HabitEventRepository eventRepo;

    // SHould this be here?
    private Intent intent;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);

        // get required repository
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        habitRepo = HabitRepository.getInstance(getApplicationContext());
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        eventRepo = HabitEventRepository.getInstance(getApplicationContext());
        HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
        HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);
        /////////////////////////////////////////////////////////////////////////////////////////////////////

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

    protected void onResume() { // not sure what this is for
        super.onResume();
    }

    /**
     * bind activity to layout
     */
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

    /**
     * Get eventList (unfiltered) and display it as filterList
     * @param eventModel
     */
    private void initEventListAndFilteredEventList(HabitEventListViewModel eventModel) {
        eventModel.getHabitEventList().observe(this, eventList -> {
//            Collections.sort(eventList, new Comparator<HabitEvent>() {
//                public int compare(HabitEvent e0, HabitEvent e1) {
//                    return e0.getDate().compareTo(e1.getDate());
//                }
//            });

            Collections.reverse(eventList);

            ArrayList<ViewableHabitEvent> viewableEventList = new ArrayList<ViewableHabitEvent>();
            for (HabitEvent event : eventList) {
                viewableEventList.add(new ViewableHabitEvent(event, EventsMainActivity.this.getHabitRepo().getHabitSync(event.getHabitId()).getType()));
            }

            EventsMainActivity.this.setEventList(viewableEventList);
            EventsMainActivity.this.setFilteredEventList(viewableEventList);
            ArrayAdapter<ViewableHabitEvent> adapter = new ArrayAdapter<ViewableHabitEvent>(
                    this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getEventList());
            listview_event_list.setAdapter(adapter);

            Collections.reverse(eventList); // sort back
        });
    }

    // Manage filter buttons (use iterator to determine which event goes where)
    private void initFilterButtons() {
        // no filter
        button_filter_none.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsMainActivity.this.setFilteredEventList(new ArrayList<ViewableHabitEvent>(EventsMainActivity.this.getEventList()));
                ArrayAdapter<ViewableHabitEvent> adapter = new ArrayAdapter<ViewableHabitEvent>(
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
                    EventsMainActivity.this.setFilteredEventList(new ArrayList<ViewableHabitEvent>(EventsMainActivity.this.getEventList()));
                    Iterator<ViewableHabitEvent> it = EventsMainActivity.this.getFilteredEventList().iterator();
                    while (it.hasNext()) {
                        if (!(it.next().getEvent().getHabitId()).equals(filter_habit.getElasticId())) {
                            it.remove();
                        }
                    }
                    ArrayAdapter<ViewableHabitEvent> adapter = new ArrayAdapter<ViewableHabitEvent>(
                            EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                    listview_event_list.setAdapter(adapter);
                }
            }
        });

        // commentary filter
        button_filter_keyword.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsMainActivity.this.setFilteredEventList(new ArrayList<ViewableHabitEvent>(EventsMainActivity.this.getEventList()));
                Iterator<ViewableHabitEvent> it = EventsMainActivity.this.getFilteredEventList().iterator();
                while(it.hasNext()) {
                    if (!it.next().getEvent().getComment().contains(edittext_keyword.getText().toString())) {
                        it.remove();
                    }
                }

                ArrayAdapter<ViewableHabitEvent> adapter = new ArrayAdapter<ViewableHabitEvent>(
                        EventsMainActivity.this, android.R.layout.simple_list_item_1, EventsMainActivity.this.getFilteredEventList());
                listview_event_list.setAdapter(adapter);
            }
        });
    }

    /**
     * Place habitList in spinner
     * @param habitModel
     */
    private void initSpinnerHabitSelect(HabitListViewModel habitModel) {
        habitModel.getHabitList().observe(this, habitList -> {
            habitArrayAdapter = new ArrayAdapter<Habit>(EventsMainActivity.this, android.R.layout.simple_list_item_1, habitList);
            spinner_habit_select.setAdapter(habitArrayAdapter);
        });
    }

    /**
     * For selecting events from the ListView to update or delete
     */
    private void initListViewListener() {
        listview_event_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long l){
                String eventId = EventsMainActivity.this.getFilteredEventList().get(position).getEvent().getElasticId();
                intent = new Intent(EventsMainActivity.this, EventEditActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });
    }

    /**
     * link specified button to EventNewActivity
     */
    private void initButtonAddEvent() {
        button_new_event.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(EventsMainActivity.this, EventNewActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * link the filtered list to EventsMapActivity
     */
    private void initButtonMapListedEvents() {
        button_map_events_listed.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EventsMainActivity.this, EventsMapActivity.class);
                ArrayList<String> selId = new ArrayList<String>();
                for (ViewableHabitEvent viewableEvent : filteredEventList){
                    selId.add(viewableEvent.getEvent().getElasticId());
                    System.out.println(selId);
                }
                intent.putStringArrayListExtra("eventIds", selId);
                startActivity(intent);
            }
        });
    }
}
