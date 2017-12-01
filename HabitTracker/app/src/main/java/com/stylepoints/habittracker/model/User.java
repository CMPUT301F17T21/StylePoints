package com.stylepoints.habittracker.model;

import com.stylepoints.habittracker.repository.remote.Id;

import java.time.LocalDate;

/**
 * Created by nikosomos on 2017-11-27.
 */

public class User implements Id {

    private String elasticId;
    private String username;
    private LocalDate dateCreated;
    //Maybe achievements here so that others can see your achievments

    public User(String username){
        this.username = username;
        this.dateCreated = LocalDate.now();
    }

    public String getUsername(){
        return username;
    }

    public LocalDate getDateCreated(){
        return dateCreated;
    }

    @Override
    public void setElasticId(String id) {
        elasticId = id;
    }
}
