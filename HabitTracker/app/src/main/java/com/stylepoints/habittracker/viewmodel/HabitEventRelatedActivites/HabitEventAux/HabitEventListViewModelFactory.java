package com.stylepoints.habittracker.viewmodel.HabitEventRelatedActivites.HabitEventAux;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.stylepoints.habittracker.repository.HabitEventRepository;


public class HabitEventListViewModelFactory implements ViewModelProvider.Factory {
    private final HabitEventRepository eventRepo;

    public HabitEventListViewModelFactory(HabitEventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HabitEventListViewModel.class)) {
            return (T) new HabitEventListViewModel(eventRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
