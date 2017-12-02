package com.stylepoints.habittracker.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.HabitRepository;

/**
 * The viewmodel for viewing the details of a SINGLE Habit
 */

public class HabitViewModel extends ViewModel {
    private LiveData<Habit> habit;
    private HabitRepository habitRepo;

    public HabitViewModel(HabitRepository habitRepo, String habitId) {
        this.habitRepo = habitRepo;
        this.habit = habitRepo.getHabit(habitId);
    }

    public HabitViewModel(HabitRepository habitRepo) {
        this.habitRepo = habitRepo;
    }

    public void init(String habitId) {
        if (this.habit != null) {
            return;
        }
        habit = habitRepo.getHabit(habitId);
    }

    public LiveData<Habit> getHabit() {
        return habit;
    }

    // temporary saving stuff
    public void saveHabit(Habit habit) {
        habitRepo.save(habit);
    }

}
