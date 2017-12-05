package com.stylepoints.habittracker.model;

import com.stylepoints.habittracker.repository.remote.Id;

/**
  Relationship model class. Essentially works as a gatekeeper for follower requests. 
  Status is controlled by an integer. Set status rejected/accepted is invoked by
  
 * @author Niko Somos
 */

public class Relationship implements Id{

    public static final String FOLLOW_REQUESTED = "0";
    public static final String FOLLOW_ACCEPTED = "1";
    public static final String FOLLOW_REJECTED = "2";

    // the id used with elastic search. i.e., /habit/{elasticId}
    private String elasticId;

    private String follower;
    private String followee;
    private String status;

    public Relationship(String follower, String followee){
        this.follower = follower;
        this.followee = followee;
        this.status = FOLLOW_REQUESTED;
    }

    public String getElasticId() {
        return elasticId;
    }

    public String getFollower() {
        return follower;
    }

    public String getFollowee() {
        return followee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatusAccepted(){
        this.status = FOLLOW_ACCEPTED;
    }

    public void setStatusRejected(){
        this.status = FOLLOW_REJECTED;
    }

    @Override
    public void setElasticId(String id) {
        this.elasticId = id;
    }
}
