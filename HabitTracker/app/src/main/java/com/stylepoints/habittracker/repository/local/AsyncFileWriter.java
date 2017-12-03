package com.stylepoints.habittracker.repository.local;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * Helper class to write a List to a JSON file asynchronously.
 *
 * @author Mackenzie Hauck
 * @see AsyncFileWriterParams
 */
public class AsyncFileWriter extends AsyncTask<AsyncFileWriterParams, Void, Void> {
    private static final String TAG = "AsyncFileWriter";
    @Override
    protected Void doInBackground(AsyncFileWriterParams... params) {
        Gson gson = new Gson();
        gson.toJson(params[0].list, params[0].writer);
        try {
            params[0].writer.flush();
            params[0].fos.close();
        } catch (IOException e) {
            Log.w(TAG, "Unable to flush buffer");
        }
        return null;
    }
}
