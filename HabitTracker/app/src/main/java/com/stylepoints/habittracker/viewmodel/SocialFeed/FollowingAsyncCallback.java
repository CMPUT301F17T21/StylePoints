package com.stylepoints.habittracker.viewmodel.SocialFeed;

/**
 * Created by nikosomos on 2017-12-04.
 */

public interface FollowingAsyncCallback {
    void setLoading();
    void onSuccess();
    void onError(Throwable t);
}
