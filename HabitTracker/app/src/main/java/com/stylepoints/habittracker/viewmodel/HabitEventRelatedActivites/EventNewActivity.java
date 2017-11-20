package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;

import java.io.ByteArrayOutputStream;

public class EventNewActivity extends AppCompatActivity {
    static String TAG = "EventNewActivity";
    HabitRepository repo;

    static final int CAM_REQUEST = 2;
    static final int REQ_CODE_CAMERA = 157670;

    Intent intent;

    EditText editTextEventName;  // Change the this to a spinner later one, selecting from existing habits
    TextView textViewDateOfOccurence;  // Change to calendar selector, like habits
    EditText editTextEventComment;
    ImageView imageViewEventPhoto;
    CheckBox checkBoxAttachLocation;
    Button buttonTakePicture;
    Button buttonAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_new);

        // Inisitialise the activity to layout
        bindToUi();

        buttonAddEvent.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitEventEntity event = new HabitEventEntity();
            }
        });

        buttonTakePicture.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAM_REQUEST);
                } else {
                    String permissionRequest[] = {Manifest.permission.CAMERA};
                    requestPermissions(permissionRequest, REQ_CODE_CAMERA);
                }
            }
        });
    }


    private void bindToUi() {
        // Intialise variables
        editTextEventName = (EditText) findViewById(R.id.textViewEventName);
        textViewDateOfOccurence = (TextView) findViewById(R.id.textViewDateOfOccurence);
        editTextEventComment = (EditText) findViewById(R.id.editTextEventComment);
        imageViewEventPhoto = (ImageView) findViewById(R.id.imageViewEventPhoto);
        checkBoxAttachLocation = (CheckBox) findViewById(R.id.checkBoxAttachLocation);
        buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
        buttonAddEvent = (Button) findViewById(R.id.buttonAddEvent);
    }


    // Photo specific process
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_CAMERA ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

            ByteArrayOutputStream bp_byte = new ByteArrayOutputStream();

            bp.compress(Bitmap.CompressFormat.JPEG, 100, bp_byte);
            byte bytearray[] = bp_byte.toByteArray();

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
            imageViewEventPhoto.setImageBitmap(bitmap);
        }
        else if (resultCode == RESULT_CANCELED) {
            // Camera intent cancelled; do nothing here
        }

    }

}
