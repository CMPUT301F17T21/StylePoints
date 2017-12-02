package com.stylepoints.habittracker.repository;

import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by nikosomos on 2017-12-01.
 */

public class AsyncUserLoader extends AsyncTask<String, Void, Integer> {

    private UserRepository uR;
    private Boolean userExists;

    public AsyncUserLoader(UserRepository uR){
        this.uR = uR;
    }

    @Override
    protected Integer doInBackground(String... str) {
        try {
            userExists = uR.checkUserExists(str[0]);
            uR.requestUser(str[0]);
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer i){
        if(userExists) {
            uR.loadUser();
        }
    }

}
