package com.stylepoints.habittracker.viewmodel.HabitRelatedActivities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.stylepoints.habittracker.R;

import java.util.ArrayList;
import java.util.List;

public class HabitStatActivity extends AppCompatActivity {

    final int[] pieColors = {Color.BLUE, Color.RED};

    PieChart pieChartHabitStat;
    // Placeholder
    private List<PieEntry> pieChartEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_stat);

        pieChartHabitStat = (PieChart) findViewById(R.id.pieChartHabitStat);

        pieChartEntry = new ArrayList<PieEntry>();
        // Arbitrary demo values here, please change
        pieChartEntry.add(new PieEntry((float) 17, "Completed on Schedule"));
        pieChartEntry.add(new PieEntry((float) 55, "Not Completed on Schedule"));

        pieChartHabitStat.setRotationEnabled(true);

        PieDataSet pds = new PieDataSet(pieChartEntry, String.format("Completion Stats for %s", "Habit Name"));
        pds.setColors(pieColors);

        pieChartHabitStat.setData(new PieData(pds));

        pieChartHabitStat.getLegend().setPosition(Legend.LegendPosition.PIECHART_CENTER);
    }
}
