package com.stylepoints.habittracker.repository;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;

import java.util.Arrays;
import java.util.List;


public class Util {
    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;


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
