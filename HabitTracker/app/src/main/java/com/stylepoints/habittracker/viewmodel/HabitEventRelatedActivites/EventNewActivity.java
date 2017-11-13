package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
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
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitNewActivity;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.HabitsMainActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EventNewActivity extends AppCompatActivity {

    static final int CAM_REQUEST = 1;
    static final int requestCodeCamera = 157649;

    Intent intent;

    TextView textViewEventName;
    TextView textViewDateOfOccurence;
    EditText editTextEventComment;
    ImageView imageViewEventPhoto;
    CheckBox checkBoxAttachLocation;
    Button buttonTakePicture;
    Button buttonSaveEvent;
    Button buttonDeleteEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_new);

        // Intialise variables
        textViewEventName = (TextView) findViewById(R.id.textViewEventName);
        textViewDateOfOccurence = (TextView) findViewById(R.id.textViewDateOfOccurence);
        editTextEventComment = (EditText) findViewById(R.id.editTextEventComment);
        imageViewEventPhoto = (ImageView) findViewById(R.id.imageViewEventPhoto);
        checkBoxAttachLocation = (CheckBox) findViewById(R.id.checkBoxAttachLocation);
        buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
        buttonSaveEvent = (Button) findViewById(R.id.buttonSaveEvent);
        buttonDeleteEvent = (Button) findViewById(R.id.buttonDeleteEvent);


        System.out.println("FFFAAA");

        buttonTakePicture.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("FFFE");
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("EEEE");
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                    String imageFileName = "ImageTest.jpg";
//                    File imageFile = new File(dir ,imageFileName);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//                    System.out.println("AAAAAAAAAAAAAAAAAAAA");
                    startActivityForResult(intent, CAM_REQUEST);
                } else {
                    String permissionRequest[] = {Manifest.permission.CAMERA};
                    requestPermissions(permissionRequest, requestCodeCamera);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCodeCamera) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("DDD");
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                System.out.println("CCCC");
//                String imageFileName = "ImageTest.jpg";
//                File imageFile = new File(dir ,imageFileName);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//                System.out.println("BBBBB");
                startActivityForResult(intent, CAM_REQUEST);
            } else {
                Toast.makeText(this, "CannotAccessCamera", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        imageViewEventPhoto.setImageBitmap(bp);
    }
}
