package com.stylepoints.habittracker.repository.local.entity;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.Nullable;

import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.HabitRepository;
import com.stylepoints.habittracker.repository.local.AppDatabase;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModel;
import com.stylepoints.habittracker.viewmodel.HabitRelatedActivities.Auxiliary.HabitListViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = HabitEntity.class,
                                  parentColumns = "id",
                                  childColumns = "habitId",
                                  onDelete = ForeignKey.CASCADE),
        tableName = "habit_events")
public class HabitEventEntity implements HabitEvent {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int habitId;
    private String name;
    private Date date;
    private String comment;
    private Location location;
    private Bitmap photo;
    // TODO: add photograph support

    public HabitEventEntity() {
    }

    public HabitEventEntity(int habitId, String name, Date date, String comment) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
    }

    public HabitEventEntity(int habitId, String name, Date date, String comment, Location location) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
        this.location = location;
    }

    public HabitEventEntity(int habitId, String name, Date date, String comment, Location location, Bitmap photo) {
        this.habitId = habitId;
        this.date = date;
        this.comment = comment;
        this.location = location;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public int getHabitId() {
        return habitId;
    }

    public String getName() {
        return name;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPhoto (Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return location;
    }

    @Nullable
    @Override
    public Bitmap getPhoto () {
        return photo;
    };

    public String toString() {
        return "Habit: " + this.getName() + "\n" +
                "StartDate: " + (new SimpleDateFormat("yyyy/MM/dd").format(this.getDate())) + "\n";
    }

}
