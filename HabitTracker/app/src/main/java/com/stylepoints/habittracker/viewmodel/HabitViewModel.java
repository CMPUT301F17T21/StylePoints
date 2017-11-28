package com.stylepoints.habittracker.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;


/**
 * The viewmodel for viewing the details of a SINGLE Habit
 */

public class HabitViewModel extends ViewModel {
    private LiveData<HabitEntity> habit;
    private HabitRepository habitRepo;

    public HabitViewModel(HabitRepository habitRepo, int habitId) {
        this.habitRepo = habitRepo;
        this.habit = habitRepo.getHabit(habitId);
    }

    public HabitViewModel(HabitRepository habitRepo) {
        this.habitRepo = habitRepo;
    }

    public void init(int habitId) {
        if (this.habit != null) {
            return;
        }
        habit = habitRepo.getHabit(habitId);
    }

    public LiveData<HabitEntity> getHabit() {
        return habit;
    }

    // temporary saving stuff
    public void saveHabit(HabitEntity habit) {
        habitRepo.save(habit);
    }
}
