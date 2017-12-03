package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitEventRepository;

/**
 * The viewmodel for viewing the details of a SINGLE Habit
 */

public class HabitEventViewModel extends ViewModel {
    private LiveData<HabitEvent> event;
    private HabitEventRepository eventRepo;

    public HabitEventViewModel(HabitEventRepository eventRepo, String eventId) {
        this.eventRepo = eventRepo;
        this.event = eventRepo.getEventById(eventId);
    }

    public HabitEventViewModel(HabitEventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public void init(String eventId) {
        if (this.event != null) {
            return;
        }
        event = eventRepo.getEventById(eventId);
    }

    public LiveData<HabitEvent> getEvent() {
        return event;
    }

    // temporary saving stuff
    public void saveEvent(HabitEvent event) {
        eventRepo.saveEvent(event);
    }
}
