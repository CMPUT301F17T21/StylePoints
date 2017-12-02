package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;
////////////////////////////Work on this urgent
import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux.HabitEventListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux.HabitEventListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitListViewModelFactory;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventNewActivity extends AppCompatActivity {
    static String TAG = "EventNewActivity";

    static final int CAM_REQUEST = 25647;
    static final int REQ_CODE_CAMERA = 157670;

    public Spinner getSpinnerHabitName() {
        return spinnerHabitName;
    }

    public TextView getTextViewDateOfOccurence() {
        return textViewDateOfOccurence;
    }

    public EditText getEditTextEventComment() {
        return editTextEventComment;
    }

    public ImageView getImageViewEventPhoto() {
        return imageViewEventPhoto;
    }

    public Button getButtonTakePicture() {
        return buttonTakePicture;
    }

    public Button getButtonAddEvent() {
        return buttonAddEvent;
    }

    public List<Habit> getHabitList() {
        return habitList;
    };

    private Spinner spinnerHabitName;  // Change the this to a spinner later one, selecting from existing habits
    private TextView textViewDateOfOccurence;  // Change to calendar selector, like habits
    private EditText editTextEventComment;
    private ImageView imageViewEventPhoto;
    private CheckBox checkBoxAttachLocation;
    private Button buttonTakePicture;
    private Button buttonRemovePicture;
    private Button buttonAddEvent;

    private List<Habit> habitList;
    private ArrayAdapter<Habit> habitArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_new);

        System.out.println("EventNewActivity");

        // Get required repo
        HabitRepository habitRepo = HabitRepository.getInstance(getApplicationContext());
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        HabitEventRepository eventRepo = HabitEventRepository.getInstance(getApplicationContext());
        HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
        HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);


        System.out.println(getIntent());
        // Inisitialise the activity to layout
        bindToUi();

        // Initialise the data of occurence field
        LocalDate date = LocalDate.now();
        textViewDateOfOccurence.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        // Initialise the spinner for habit input
        habitList = new ArrayList<Habit>();
        habitModel.getHabitList().observe(this, habitList -> {
            for (Habit habit : habitList) {
                EventNewActivity.this.getHabitList().add(habit);
            }
            habitArrayAdapter = new ArrayAdapter<Habit>(EventNewActivity.this, android.R.layout.simple_list_item_1, EventNewActivity.this.getHabitList());
            spinnerHabitName.setAdapter(habitArrayAdapter);
        });

        // Initialise imageView for photos
        imageViewEventPhoto.setImageDrawable(null);

        // Initialise button to take picture
        buttonTakePicture.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAM_REQUEST);
                } else {
                    String permissionRequest[] = {Manifest.permission.CAMERA};
                    requestPermissions(permissionRequest, REQ_CODE_CAMERA);
                }
            }
        });

        // Initialise button to remove picture
        buttonRemovePicture.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (imageViewEventPhoto.getDrawable() != null) {
                    imageViewEventPhoto.setImageDrawable(null);
                }
            }
        });

        buttonAddEvent.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerHabitName.getSelectedItem() != null) {
                    String habitId = ((Habit) spinnerHabitName.getSelectedItem()).getElasticId();
                    String type = ((Habit) spinnerHabitName.getSelectedItem()).getType();
                    HabitEvent event = new HabitEvent("username", habitId, type);
                    event.setDate(date);
                    event.setComment(editTextEventComment.getText().toString());
                    if (imageViewEventPhoto.getDrawable() != null) {
                        event.setPhoto(((BitmapDrawable) imageViewEventPhoto.getDrawable()).getBitmap());
                    }
                    //              event.setLocation();
                    eventRepo.saveEvent(event);
                    finish();
                } else {
                    Toast.makeText(EventNewActivity.this, "No associated habit (event name) is provided.", Toast.LENGTH_LONG).show();
                }
            }
        });

//        checkBoxAttachLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    // Photo specific process
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_CAMERA ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("granted");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAM_REQUEST);
            } else {
                Toast.makeText(this, "Cannot Access Camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            imageViewEventPhoto.setImageBitmap(bp);
        } else if (resultCode == RESULT_CANCELED) {
            // Camera intent cancelled; do nothing here
        }
    }


    // UI init
    private void bindToUi() {
        // Intialise variables
        spinnerHabitName = (Spinner) findViewById(R.id.textViewEventName);
        textViewDateOfOccurence = (TextView) findViewById(R.id.textViewDateOfOccurence);
        editTextEventComment = (EditText) findViewById(R.id.editTextEventComment);
        imageViewEventPhoto = (ImageView) findViewById(R.id.imageViewEventPhoto);
        checkBoxAttachLocation = (CheckBox) findViewById(R.id.checkBoxAttachLocation);
        buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
        buttonRemovePicture = (Button) findViewById(R.id.buttonRemovePhoto);
        buttonAddEvent = (Button) findViewById(R.id.buttonAddEvent);
    }

}
