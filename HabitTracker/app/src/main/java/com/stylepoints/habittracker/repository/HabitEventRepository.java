package com.stylepoints.habittracker.repository;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Log;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.local.EventJsonSource;
import com.stylepoints.habittracker.repository.remote.RemoteEventJob;

import java.util.List;

public class HabitEventRepository {
    private static final String TAG = "HabitEventRepository";
    private static HabitEventRepository INSTANCE;
    private final EventJsonSource source;
    private HabitRepository habitRepo;
    private JobScheduler jobScheduler;
    private Context context;

    private HabitEventRepository(EventJsonSource source) {
        this.source = source;
    }

    private HabitEventRepository(Context context) {
        this.source = EventJsonSource.getInstance(context);
        this.habitRepo = HabitRepository.getInstance(context);
        this.context = context.getApplicationContext();
        this.jobScheduler = (JobScheduler) this.context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public LiveData<List<HabitEvent>> getEventsByHabitId(String habitId) {
        LiveData<List<HabitEvent>> filtered = Transformations.map(getAllEvents(), eventList -> {
            for (HabitEvent event : eventList) {
                if (!event.getHabitId().equals(habitId)) {
                    eventList.remove(event);
                }
            }
            return eventList;
        });
        return filtered;
    }

    public LiveData<List<HabitEvent>> getAllEvents() {
        return source.getEvents();
    }

    public LiveData<HabitEvent> getEventById(String eventId) {
        return source.getEvent(eventId);
    }

    public HabitEvent getEventSync(String eventId) {
        return source.getEventSync(eventId);
    }

    public void saveEvent(HabitEvent event) {
        if (habitRepo.getHabitSync(event.getHabitId()) == null) {
            Log.d(TAG, "Could not find local habit with id: " + event.getHabitId());
            return;
        }
        source.saveEvent(event);
        remoteOperation(event.getElasticId(), Util.CREATE);
    }

    public void updateEvent(String eventId, HabitEvent event) {
        source.updateEvent(eventId, event);
        remoteOperation(eventId, Util.UPDATE);
    }

    public void deleteEvent(String eventId) {
        source.deleteEvent(eventId);
        remoteOperation(eventId, Util.DELETE);
    }

    public void deleteEventsByHabitId(String habitId) {
        for (String eventId : source.getEventIdsForHabitId(habitId)) {
            deleteEvent(eventId);
        }
    }

    private void remoteOperation(String id, int operation) {
        removePreviousJobs(id);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("EVENT_ID", id);
        bundle.putInt("OPERATION", operation);
        jobScheduler.schedule(new JobInfo.Builder(Util.getUniqueJobId(jobScheduler),
                new ComponentName(context, RemoteEventJob.class))
                .setExtras(bundle) // send eventId, and operation (create, update, or delete)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // must have network
                .build()
        );
    }

    private void removePreviousJobs(String id) {
        String jobEventId = null;
        for (JobInfo job : jobScheduler.getAllPendingJobs()) {
            jobEventId = job.getExtras().getString("EVENT_ID");
            if (jobEventId != null && jobEventId.equals(id)) {
                // remove previous job that was scheduled with this id
                // so we don't do something like "update" and then "create"
                jobScheduler.cancel(job.getId());
                // should only ever be one job with that habitId
                return;
            }
        }
    }

    public void saveList(List<HabitEvent> list){
        source.saveEvents(list);
    }

    public void deleteAll(){
        source.deleteAllEvents();
    }

    public static HabitEventRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HabitEventRepository(context);
        }
        return INSTANCE;
    }
}
