package com.stylepoints.habittracker.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.stylepoints.habittracker.repository.HabitRepository;

/**
 * Created by Mackenzie on 2017-11-10.
 * https://proandroiddev.com/architecture-components-modelview-livedata-33d20bdcc4e9
 */

public class HabitViewModelFactory implements ViewModelProvider.Factory {
    private final HabitRepository habitRepo;
    private final int habitId;

    public HabitViewModelFactory(HabitRepository habitRepo, int habitId) {
        this.habitRepo = habitRepo;
        this.habitId = habitId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HabitViewModel.class)) {
            return (T) new HabitViewModel(habitRepo, habitId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
