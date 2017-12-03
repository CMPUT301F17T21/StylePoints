package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;

import java.util.List;

/**
 * The ViewModel for storing the data for a list of all Habits stored in the
 * local database.
 */

public class HabitEventListViewModel extends ViewModel {
    private LiveData<List<HabitEvent>> eventList;
    private HabitEventRepository eventRepo;

    public HabitEventListViewModel(HabitEventRepository eventRepo) {
        this.eventRepo = eventRepo;
        eventList = eventRepo.getAllEvents();
        System.out.println(eventList);
        // remote example...
        //habitList = habitRepo.getUsersHabits("mackenzie");
    }

    public LiveData<List<HabitEvent>> getHabitEventList() {
        return eventList;
    }
}
