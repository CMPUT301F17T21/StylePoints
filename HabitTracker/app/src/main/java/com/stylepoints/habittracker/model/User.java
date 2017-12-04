package com.stylepoints.habittracker.model;


import com.stylepoints.habittracker.repository.remote.Id;

import java.time.LocalDate;

/**
 * The model class for a User. Implements the Id interface so that it can be
 * serialized and uploaded to Elastic Search. This class is automatically serialized
 * by gson into JSON when uploaded to the remote server
 *
 * In elastic search the users are keyed on the username field
 *
 * @author Niko Somos
 */
public class User implements Id {
    private String username;
    private LocalDate dateCreated;

    /**
     * Constructor
     * @param username a unique username
     */
    public User(String username){
        this.username = username;
        this.dateCreated = LocalDate.now();
    }

    /**
     * @return the unique username of this user
     */
    public String getUsername(){
        return username;
    }

    /**
     * @return the date this account was created
     */
    public LocalDate getDateCreated(){
        return dateCreated;
    }

    /**
     * @param id the unique username of this user
     */
    @Override
    public void setElasticId(String id) {
        this.username = id;
    }

}
