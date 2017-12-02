package com.stylepoints.habittracker.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.stylepoints.habittracker.repository.HabitRepository;


public class HabitListViewModelFactory implements ViewModelProvider.Factory {
    private final HabitRepository habitRepo;

    public HabitListViewModelFactory(HabitRepository habitRepo) {
        this.habitRepo = habitRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HabitListViewModel.class)) {
            return (T) new HabitListViewModel(habitRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
