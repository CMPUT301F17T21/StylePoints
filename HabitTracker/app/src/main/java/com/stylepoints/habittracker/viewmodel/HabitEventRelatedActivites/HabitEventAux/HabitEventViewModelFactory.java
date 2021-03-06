package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.stylepoints.habittracker.repository.HabitEventRepository;

/**
 * Created by Mackenzie on 2017-11-10.
 * https://proandroiddev.com/architecture-components-modelview-livedata-33d20bdcc4e9
 */

public class HabitEventViewModelFactory implements ViewModelProvider.Factory {
    private final HabitEventRepository eventRepo;
    private final String eventId;

    public HabitEventViewModelFactory(HabitEventRepository eventRepo, String eventId) {
        this.eventRepo = eventRepo;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HabitEventViewModel.class)) {
            return (T) new HabitEventViewModel(eventRepo, eventId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
