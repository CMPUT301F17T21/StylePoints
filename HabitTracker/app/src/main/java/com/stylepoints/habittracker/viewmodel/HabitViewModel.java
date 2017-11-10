package com.stylepoints.habittracker.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

import javax.inject.Inject;


/**
 * The viewmodel for viewing the details of a Habit
 */

public class HabitViewModel extends ViewModel {
    private LiveData<HabitEntity> habit;
    private HabitRepository habitRepo;

    @Inject
    public HabitViewModel(HabitRepository habitRepo) {
        this.habitRepo = habitRepo;
    }

    public void init(String habitId) {
        if (this.habit != null) {
            return;
        }
        habit = habitRepo.getHabit(habitId);
    }

    public LiveData<HabitEntity> getHabit() {
        return habit;
    }
}
