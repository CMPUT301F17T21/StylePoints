package com.stylepoints.habittracker.viewmodel.HabitRelatedActivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.stylepoints.habittracker.R;
import com.stylepoints.habittracker.repository.HabitEventRepository;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class HabitStatActivity extends AppCompatActivity {

    final int[] pieColors = {Color.BLUE, Color.RED};

    PieChart pieChartHabitStat;
    // Placeholder
    private List<PieEntry> pieChartEntry;

    private UserRepository userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_stat);

        pieChartHabitStat = (PieChart) findViewById(R.id.pieChartHabitStat);

        Intent intent = getIntent();
        String habitId = intent.getStringExtra("habitId");

        userRepo = new UserRepository(HabitRepository.getInstance(getApplicationContext()),
                HabitEventRepository.getInstance(getApplicationContext()),
                getApplicationContext());

        pieChartEntry = new ArrayList<PieEntry>();
        // Arbitrary demo values here, please change

        Float a = userRepo.getHabitsCompletePoints(habitId);
        Float b = userRepo.getHabitsIncompletePoints(habitId);


        System.out.println(userRepo.getHabitsCompletePoints(habitId));
        System.out.println(userRepo.getHabitsIncompletePoints(habitId));
        pieChartEntry.add(new PieEntry(userRepo.getHabitsCompletePoints(habitId), "Completed on Schedule"));
        pieChartEntry.add(new PieEntry(userRepo.getHabitsIncompletePoints(habitId), "Not Completed on Schedule"));

        pieChartHabitStat.setRotationEnabled(true);

        PieDataSet pds = new PieDataSet(pieChartEntry, String.format("Completion Stats for %s", "Habit Name"));
        pds.setColors(pieColors);

        pieChartHabitStat.setData(new PieData(pds));

        pieChartHabitStat.getLegend().setPosition(Legend.LegendPosition.PIECHART_CENTER);
    }
}
