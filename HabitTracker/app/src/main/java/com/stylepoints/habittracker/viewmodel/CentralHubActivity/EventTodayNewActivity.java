package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Date;
import java.util.List;

public class EventTodayNewActivity extends AppCompatActivity {
    static String TAG = "EventTodayNewActivity";

    static final int CAM_REQUEST = 25648;
    static final int REQ_CODE_CAMERA = 157671;

    public TextView getTextViewHabitName() {
        return textViewHabitName;
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

    private TextView textViewHabitName;
    private TextView textViewDateOfOccurence;  // Change to calendar selector, like habits
    private EditText editTextEventComment;
    private ImageView imageViewEventPhoto;
    private CheckBox checkBoxAttachLocation;
    private Button buttonTakePicture;
    private Button buttonRemovePicture;
    private Button buttonAddEvent;

    private List<Habit> habitList;
    private ArrayAdapter<Habit> habitArrayAdapter;
    private Location loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_today_new);

        // Get required repo
        HabitRepository habitRepo = HabitRepository.getInstance(getApplicationContext());
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        HabitEventRepository eventRepo = HabitEventRepository.getInstance(getApplicationContext());
        HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
        HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);

        Intent inputIntent = getIntent();
        if (!inputIntent.hasExtra("HABIT_ID")) {
            // nothing to edit! return back to previous page
            Log.e(TAG, "habitId was not passed in with the intent");
            finish();
            return;
        }

        String habitId = inputIntent.getStringExtra("HABIT_ID");
        Log.d(TAG, "passed in habitId: " + String.valueOf(habitId));
        if (habitId == null) {
            // invalid ID
            Log.e(TAG, "habitId can not be 0");
            finish();
            return;
        }

        Habit habit = habitRepo.getHabitSync(habitId);
        if (habit == null) {
            Log.e(TAG, "Unable to find habit with id: " + String.valueOf(habitId));
            finish();
            return;
        }

        // Inisitialise the activity to layout
        bindToUi();

        // Initialise the habit field
        textViewHabitName.setText(habit.getType());

        // Initialise the data of occurence field
        LocalDate date = LocalDate.now();
        textViewDateOfOccurence.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));

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
                String habitId = habit.getElasticId();
                HabitEvent event = new HabitEvent("username", habitId);
                event.setDate(date);
                event.setComment(editTextEventComment.getText().toString());
                if (imageViewEventPhoto.getDrawable() != null) {
                    event.setPhoto(((BitmapDrawable) imageViewEventPhoto.getDrawable()).getBitmap());
                }
                if (checkBoxAttachLocation.isChecked()) {
                    event.setLocation(loc);
                }
                eventRepo.saveEvent(event);
                finish();
            }
        });

//        checkBoxAttachLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        if (!runtimePermissions()) {
            setLocListener();
        }
    }
    private void setLocListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                location.getLatitude();
                location.getLongitude();

                loc = location;

                //I make a log to see the results
                System.out.println(loc);

            }

            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            public void onProviderEnabled(String s) {

            }

            public void onProviderDisabled(String s) {

            }
        };
        //noInspect MissingPermissions
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
    }


    private boolean runtimePermissions() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    // Photo specific process
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setLocListener();
            } else {
                runtimePermissions();
            }
        }
        else if (requestCode == REQ_CODE_CAMERA ) {
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
        textViewHabitName = (TextView) findViewById(R.id.textViewEventName);
        textViewDateOfOccurence = (TextView) findViewById(R.id.textViewDateOfOccurence);
        editTextEventComment = (EditText) findViewById(R.id.editTextEventComment);
        imageViewEventPhoto = (ImageView) findViewById(R.id.imageViewEventPhoto);
        checkBoxAttachLocation = (CheckBox) findViewById(R.id.checkBoxAttachLocation);
        buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
        buttonRemovePicture = (Button) findViewById(R.id.buttonRemovePhoto);
        buttonAddEvent = (Button) findViewById(R.id.buttonAddEvent);
    }

}
