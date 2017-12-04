package com.stylepoints.habittracker.repository;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class for dealing with the JobScheduler. Gives an easy way to
 * get a unique job id, and stores constants that define the type of
 * CRUD operation to perform
 * @author Mackenzie Hauck
 */
public class Util {
    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    public static final String API_BASE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/";

    /**
     * The Android JobScheduler keeps track of active and pending jobs by assigning
     * them an integer id. Since we want to have for example multiple Habit updates
     * we must use a unique id for each habit that we want to process.
     *
     * @author Mackenzie Hauck
     * @param jobScheduler the JobScheduler instance that we want an id for
     * @return the lowest possible unique integer id available in the JobScheduler
     */
    public static int getUniqueJobId(JobScheduler jobScheduler) {
        List<JobInfo> jobs = jobScheduler.getAllPendingJobs();

        // populate an array with all ids that are assigned
        int[] usedIds = new int[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            usedIds[i] = jobs.get(i).getId();
        }
        Arrays.sort(usedIds);

        // get the smallest unused id
        for (int i = 0; i < usedIds.length; i++) {
            if (usedIds[i] != i) {
                return i;
            }
        }
        return usedIds.length;
    }
}
