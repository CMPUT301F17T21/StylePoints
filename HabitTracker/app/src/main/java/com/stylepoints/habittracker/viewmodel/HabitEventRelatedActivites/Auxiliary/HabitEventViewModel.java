package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.Auxiliary;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.local.entity.HabitEventEntity;


/**
 * The viewmodel for viewing the details of a SINGLE Habit
 */

public class HabitEventViewModel extends ViewModel {
    private LiveData<HabitEventEntity> event;
    private HabitEventRepository eventRepo;


    /// might need something to grab event by event first
    public HabitEventViewModel(HabitEventRepository eventRepo, int eventId) {
        this.eventRepo = eventRepo;
        this.event = eventRepo.getEventById(eventId);
    }

    public HabitEventViewModel(HabitEventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public void init(int eventId) {
        if (this.event != null) {
            return;
        }
        event = eventRepo.getEventById(eventId);
    }

    public LiveData<HabitEventEntity> getEvent() {
        return event;
    }

    // temporary saving stuff
    public void saveEvent(HabitEventEntity event) {
        eventRepo.save(event);
    }
}
