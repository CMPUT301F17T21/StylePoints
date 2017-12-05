package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;

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

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventEditActivity extends AppCompatActivity {
    static String TAG = "EventEditActivity";

    static final int CAM_REQUEST = 25647; // request code for camera use
    static final int REQ_CODE_CAMERA = 157670; // request code to acquire camera permission
    static final int REQ_LISTENER = 282194; // request permission for location listener

    /* field getters, used if lambda or inner functions cannot access private fields */

    public TextView getTextViewEventName() {
        return textViewEventName;
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

    public Button getButtonSaveEvent() {
        return buttonSaveEvent;
    }

    public Button getButtonDeleteEvent() {
        return buttonDeleteEvent;
    }

    private TextView textViewEventName;  // Fixed type name of event, associated to habit
    private TextView textViewDateOfOccurence;  // Date field, is fixed.
    private EditText editTextEventComment; // Event comment field, up to 30 char (reinforced in .xml)
    private ImageView imageViewEventPhoto; // Photo display, if null, no photo is found
    private CheckBox checkBoxAttachLocation; // checkBox is attached
    private Button buttonTakePicture; // Take picture
    private Button buttonRemovePicture; // Delete Picture
    private Button buttonSaveEvent; // Save Modifications to Events
    private Button buttonDeleteEvent; // Delete current event
    private Button buttonClearEventLocation; // Clear location of event

    private HabitRepository habitRepo; // habit repository, need to be accessed by inner classes
    HabitEventRepository eventRepo; // event Repository
    private Location loc; // location field

    /**
     * Initialise all fields required for tha activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        // Get required repository
        habitRepo = HabitRepository.getInstance(getApplicationContext());
        eventRepo = HabitEventRepository.getInstance(getApplicationContext());

        /* Get event by passed eventId*/
        /////////////////////////////////////////////////////////////////
        Intent inputIntent = getIntent();
        System.out.println(inputIntent);
        if (!inputIntent.hasExtra("EVENT_ID")) {
            // nothing to edit! return back to previous page
            Log.e(TAG, "eventId was not passed in with the intent");
            finish();
            return;
        }

        String eventId = inputIntent.getStringExtra("EVENT_ID");
        Log.d(TAG, "passed in eventId: " + String.valueOf(eventId));
        if (eventId == null) {
            // invalid ID
            Log.e(TAG, "eventId can not be 0");
            finish();
            return;
        }

        HabitEvent event = eventRepo.getEventSync(eventId);
        if (event == null) {
            Log.e(TAG, "Unable to find event with id: " + String.valueOf(eventId));
            finish();
            return;
        }
        /////////////////////////////////////////////////////////////////////

        // Inisitialise the activity to layout
        bindToUi();
        // Set fields with pre-existing information for viewing
        fillUi(event);

        // Initialise button to take picture
        buttonTakePicture.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                // If camera permission is already given, proceed, else request for permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAM_REQUEST);
                } else { // This chain of code performs properly, but camera must be first withfrawn from before reattempt at taking pictures
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
                Toast.makeText(EventEditActivity.this, "Picture Deleted.", Toast.LENGTH_LONG).show();
            }
        });

        // Initialise button to clear location
        buttonClearEventLocation.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                event.setLocation(null);
                Toast.makeText(EventEditActivity.this, "Location Deleted.", Toast.LENGTH_LONG).show();
            }
        });

        // Initialise operation to save events
        buttonSaveEvent.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setComment(editTextEventComment.getText().toString());
                if (imageViewEventPhoto.getDrawable() != null) { // null check for photo
                    event.setPhoto(((BitmapDrawable) imageViewEventPhoto.getDrawable()).getBitmap());
                } else {
                    event.setPhoto(null);
                }
                if (checkBoxAttachLocation.isChecked() && event.getLocation() == null) { // update only if previous location is cleared
                    event.setLocation(loc);
                }
                eventRepo.updateEvent(event.getElasticId(), event);
                finish();
            }
        });

        // Initialise operation for event deletion process
        buttonDeleteEvent.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventRepo.deleteEvent(event.getElasticId());
                finish();
            }
        });

        // Initialise location listener if permission is acquired (!runtimePermissions)
        if (!runtimePermissions()) {
            setLocListener();
        }
    }

    /**
     * Establish listen for location
     */
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

    /**
     * check if runtimePermission for location listener to operate
     * @return boolean
     */
    private boolean runtimePermissions() {
        // from https://www.youtube.com/watch?v=QNb_3QKSmMk
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LISTENER);
            return true;
        }
        return false;
    }

    /**
     *  Request permission operation
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LISTENER) { // Location Listener
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setLocListener();
            } else {
                runtimePermissions(); // Forgot what this does... leave it for now
            }
        } else if (requestCode == REQ_CODE_CAMERA ) { // Camera
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("granted");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAM_REQUEST);
            } else {
                Toast.makeText(this, "Cannot Access Camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * for getting camera result
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * UI init
     */
    private void bindToUi() {
        // Intialise variables
        textViewEventName = (TextView) findViewById(R.id.textViewHabitName);
        textViewDateOfOccurence = (TextView) findViewById(R.id.textViewDateOfOccurence);
        editTextEventComment = (EditText) findViewById(R.id.editTextEventComment);
        imageViewEventPhoto = (ImageView) findViewById(R.id.imageViewEventPhoto);
        checkBoxAttachLocation = (CheckBox) findViewById(R.id.checkBoxAttachLocation);
        buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
        buttonRemovePicture = (Button) findViewById(R.id.buttonRemovePhoto);
        buttonSaveEvent = (Button) findViewById(R.id.buttonSaveEvent);
        buttonDeleteEvent = (Button) findViewById(R.id.buttonDeleteEvent);
        buttonClearEventLocation = (Button) findViewById(R.id.buttonClearEvent);
    }

    /**
     * fill Ui (declare after UI init)
     * @param event
     */
    private void fillUi(HabitEvent event) {
        textViewEventName.setText(habitRepo.getHabitSync(event.getHabitId()).getType());
        textViewDateOfOccurence.setText(event.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        editTextEventComment.setText(event.getComment());
        imageViewEventPhoto.setImageBitmap(event.getPhoto());
        if (event.getLocation() != null) {
            checkBoxAttachLocation.setChecked(true);
        }
    }
}
