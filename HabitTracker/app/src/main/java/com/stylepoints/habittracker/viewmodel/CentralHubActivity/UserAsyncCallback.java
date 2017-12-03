package com.stylepoints.habittracker.viewmodel.CentralHubActivity;

/**
 * Created by nikosomos on 2017-12-02.
 */

public interface UserAsyncCallback {
    void setLoading();
    void setError(Throwable t);
    void setSuccess();
}
