package com.stylepoints.habittracker.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.local.EventJsonSource;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HabitEventRepository {
    private static final String TAG = "HabitEventRepository";
    private static HabitEventRepository INSTANCE;
    private final EventJsonSource source;
    private ElasticSearch elastic; // new code

    private HabitEventRepository(EventJsonSource source) {
        this.source = source;
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    private HabitEventRepository(Context context) {
        this.source = EventJsonSource.getInstance(context);
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        this.elastic = retrofit.create(ElasticSearch.class);
    }

    public LiveData<List<HabitEvent>> getEventsByHabitId(String habitId) {
        LiveData<List<HabitEvent>> filtered = Transformations.map(getAllEvents(), eventList -> {
            for (HabitEvent event : eventList) {
                if (!event.getHabitId().equals(habitId)) {
                    eventList.remove(event);
                }
            }
            return eventList;
        });
        return filtered;
    }

    public LiveData<List<HabitEvent>> getAllEvents() {
        return source.getEvents();
    }

    public LiveData<HabitEvent> getEventById(String eventId) {
        return source.getEvent(eventId);
    }

    // TODO: change to off main thread
    public void saveEvent(HabitEvent event) {
        // TODO: make sure that the habit this references is actually in the database
        // TODO: verify data is correct (comment length is not too long)
        source.saveEvent(event);
        /// new code elastic
        elastic.createEventWithId(event.getElasticId(), event).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "create event network request success");
                    if (response.body().wasCreated()) {
                        Log.i(TAG, "Event successfully stored in remote " + event.getElasticId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "network request failed saving event " + event.getElasticId());
            }
        });
    }

    public static HabitEventRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HabitEventRepository(context);
        }
        return INSTANCE;
    }

    public HabitEvent getEventSync(String eventId) {
        return source.getEventSync(eventId);
    }

    /// new code
    public void updateEvent(String id, HabitEvent event) {
        // update local version
        source.updateEvent(id, event);
        // update remote version
        elastic.updateEvent(id, event).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "event update network request success");
                    if (response.body().wasCreated()) {
                        Log.i(TAG, "Event successfully stored in remote " + event.getElasticId());
                    }
                }
            }
            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "network request failed to update event " + event.getElasticId());
            }
        });
    }

    /// new code
    public void deleteEvent(String id) {
        // delete from local
        source.deleteEvent(id);
        // delete from remote
        elastic.deleteEvent(id).enqueue(new Callback<ElasticRequestStatus>() {
            @Override
            public void onResponse(Call<ElasticRequestStatus> call, Response<ElasticRequestStatus> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "event delete network request success");
                    Log.d(TAG, response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<ElasticRequestStatus> call, Throwable t) {
                Log.d(TAG, "network request failed for event delete " + id);
            }
        });
    }
}
