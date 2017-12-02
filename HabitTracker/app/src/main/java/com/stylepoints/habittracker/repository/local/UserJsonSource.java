package com.stylepoints.habittracker.repository.local;

import com.google.gson.Gson;

/**
 * Created by nikosomos on 2017-11-28.
 */

public class UserJsonSource {

    private static final String TAG = "UserJsonSource";
    private Gson gson;
    private static UserJsonSource INSTANCE;
    private static String FILENAME = ".json";
}
