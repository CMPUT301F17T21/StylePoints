package com.stylepoints.habittracker.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.repository.local.entity.HabitEntity;

import java.util.List;

/**
 * The ViewModel for storing the data for a list of all Habits stored in the
 * local database.
 */

public class HabitListViewModel extends ViewModel {
    private LiveData<List<HabitEntity>> habitList;
    private HabitRepository habitRepo;

    public HabitListViewModel(HabitRepository habitRepo) {
        this.habitRepo = habitRepo;
        habitList = habitRepo.loadAll();

        // remote example...
        //habitList = habitRepo.getUsersHabits("mackenzie");
    }

    public LiveData<List<HabitEntity>> getHabitList() {
        return habitList;
    }
}
