package com.stylepoints.habittracker.viewmodel.SocialFeed;

/**
 * Created by nikosomos on 2017-12-04.
 */

public interface CheckUserExistsCallback {
    void userExists();
    void userDoesNotExist();
    void setError(Throwable t);
}
