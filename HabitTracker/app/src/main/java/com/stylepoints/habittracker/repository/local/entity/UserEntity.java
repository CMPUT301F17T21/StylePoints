package com.stylepoints.habittracker.repository.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by nikosomos on 2017-11-19.
 */

@Entity(tableName = "User")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;

    public UserEntity(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
