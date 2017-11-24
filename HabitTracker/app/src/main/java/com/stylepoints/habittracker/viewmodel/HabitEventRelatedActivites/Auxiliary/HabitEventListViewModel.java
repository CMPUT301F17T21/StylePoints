package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;

import java.util.List;

/**
 * The ViewModel for storing the data for a list of all Habits stored in the
 * local database.
 */

public class HabitEventListViewModel extends ViewModel {
    private LiveData<List<HabitEventEntity>> eventList;
    private HabitEventRepository eventRepo;

    public HabitEventListViewModel(HabitEventRepository eventRepo) {
        this.eventRepo = eventRepo;
        eventList = eventRepo.getAllEvents();
    }

    public LiveData<List<HabitEventEntity>> getEventList() {
        return eventList;
    }
}
