package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;

import java.util.ArrayList;

public class EventsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HabitEventRepository eventRepo;
    private HabitRepository habitRepo;

    /**
     * Setup the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        eventRepo = HabitEventRepository.getInstance(getApplicationContext()); // get eventRepo
        habitRepo = HabitRepository.getInstance(getApplicationContext()); // get habitRepo

        Intent i = getIntent();
        ArrayList<String> eventIds = i.getStringArrayListExtra("eventIds"); // pass eventid from filtered list
        for (String eventId : eventIds) { // for any events that have an attached location, create a marker with the specified longitude and latitude
            HabitEvent event = eventRepo.getEventSync(eventId);
            Location loc = event.getLocation();
            if (loc != null) {
                LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(ll).title(habitRepo.getHabitSync(event.getHabitId()).getType()));
            }
        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(34, 151);
//        LatLng aust = new LatLng(34.1, 152);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.addMarker(new MarkerOptions().position(aust).title("Marker in London"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
