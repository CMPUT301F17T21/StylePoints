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
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary.HabitEventListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary.HabitEventListViewModelFactory;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventEditActivity extends AppCompatActivity {
    static String TAG = "EventEditActivity";

    static final int CAM_REQUEST = 25647;
    static final int REQ_CODE_CAMERA = 157670;

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

    public List<HabitEntity> getHabitList() {
        return habitList;
    };

    private TextView textViewEventName;  // Change the this to a spinner later one, selecting from existing habits
    private TextView textViewDateOfOccurence;  // Change to calendar selector, like habits
    private EditText editTextEventComment;
    private ImageView imageViewEventPhoto;
    private CheckBox checkBoxAttachLocation;
    private Button buttonTakePicture;
    private Button buttonRemovePicture;
    private Button buttonSaveEvent;
    private Button buttonDeleteEvent;

    private List<HabitEntity> habitList;
    private ArrayAdapter<HabitEntity> habitArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        System.out.println("EventEditActivity");

        // Get required repo
        HabitRepository habitRepo = HabitRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        HabitListViewModelFactory habitFactory = new HabitListViewModelFactory(habitRepo);
        HabitListViewModel habitModel = ViewModelProviders.of(this, habitFactory).get(HabitListViewModel.class);

        HabitEventRepository eventRepo = HabitEventRepository.getInstance(AppDatabase.getAppDatabase(getApplicationContext()));
        HabitEventListViewModelFactory eventFactory = new HabitEventListViewModelFactory(eventRepo);
        HabitEventListViewModel eventModel = ViewModelProviders.of(this, eventFactory).get(HabitEventListViewModel.class);


        Intent inputIntent = getIntent();
        System.out.println(inputIntent);
        if (!inputIntent.hasExtra("EVENT_ID")) {
            // nothing to edit! return back to previous page
            Log.e(TAG, "eventId was not passed in with the intent");
            finish();
            return;
        }

        int eventId = inputIntent.getIntExtra("EVENT_ID", 0);
        Log.d(TAG, "passed in eventId: " + String.valueOf(eventId));
        if (eventId == 0) {
            // invalid ID
            Log.e(TAG, "eventId can not be 0");
            finish();
            return;
        }

        HabitEventEntity event = eventRepo.getEventSync(eventId);
        if (event == null) {
            Log.e(TAG, "Unable to find event with id: " + String.valueOf(eventId));
            finish();
            return;
        }

        // Inisitialise the activity to layout
        bindToUi();
        fillUi(event);

        // Initialise the data of occurence field
        Date date = new Date();
        textViewDateOfOccurence.setText((new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")).format(date));

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

        buttonSaveEvent.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setComment(editTextEventComment.getText().toString());
                if (imageViewEventPhoto.getDrawable() != null) {
                    event.setPhoto(((BitmapDrawable) imageViewEventPhoto.getDrawable()).getBitmap());
                } else {
                    event.setPhoto(null);
                }
//              event.setLocation();
                eventRepo.save(event);
                finish();
            }
        });

        // Deletion process
//        buttonDeleteEvent.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                eventRepo.delete(event);
//                finish();
//            }
//        });

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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAM_REQUEST);
            } else {
                Toast.makeText(this, "CannotAccessCamera", Toast.LENGTH_LONG).show();
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
        textViewEventName = (TextView) findViewById(R.id.textViewHabitName);
        textViewDateOfOccurence = (TextView) findViewById(R.id.textViewDateOfOccurence);
        editTextEventComment = (EditText) findViewById(R.id.editTextEventComment);
        imageViewEventPhoto = (ImageView) findViewById(R.id.imageViewEventPhoto);
        checkBoxAttachLocation = (CheckBox) findViewById(R.id.checkBoxAttachLocation);
        buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
        buttonRemovePicture = (Button) findViewById(R.id.buttonRemovePhoto);
        buttonSaveEvent = (Button) findViewById(R.id.buttonSaveEvent);
        buttonDeleteEvent = (Button) findViewById(R.id.buttonDeleteEvent);
    }


    private void fillUi(HabitEventEntity event) {
        textViewEventName.setText(event.getName());
        textViewDateOfOccurence.setText((new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")).format(event.getDate()));
        editTextEventComment.setText(event.getComment());
        imageViewEventPhoto.setImageBitmap(event.getPhoto());
    }
}
