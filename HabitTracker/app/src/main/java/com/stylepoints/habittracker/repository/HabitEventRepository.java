package com.stylepoints.habittracker.repository;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Log;

import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.local.EventJsonSource;
import com.stylepoints.habittracker.repository.remote.RemoteEventJob;

import java.util.List;

/**
 * A singleton class that acts as an abstraction layer between the user interface
 * and either the local HabitEvent storage, or the remote HabitEvent storage.
 *
 * The data returned to the UI are instances of LiveData, which is basically an observable
 * class that is also aware of android specific lifecycle information.
 */
public class HabitEventRepository {
    private static final String TAG = "HabitEventRepository";
    private static HabitEventRepository INSTANCE;
    private final EventJsonSource source;
    private HabitRepository habitRepo;
    private JobScheduler jobScheduler;
    private Context context;

    private HabitEventRepository(Context context) {
        this.context = context.getApplicationContext();
        this.source = EventJsonSource.getInstance(this.context);
        this.habitRepo = HabitRepository.getInstance(this.context);
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

    /**
     * Get observable list of habit events. Automatically updates when backend updates
     * @return Observable LiveData list of events
     */
    public LiveData<List<HabitEvent>> getAllEvents() {
        return source.getEvents();
    }

    /**
     * Get HabitEvent with id
     * @param eventId unique id representing the event to be fetched
     * @return an event wrapped in LiveData to make it observable
     */
    public LiveData<HabitEvent> getEventById(String eventId) {
        return source.getEvent(eventId);
    }

    /**
     * Get an event synchronously
     * @param eventId unique id representing the event to be fetched
     * @return the HabitEvent with id eventId
     */
    public HabitEvent getEventSync(String eventId) {
        return source.getEventSync(eventId);
    }

    /**
     * Save the HabitEvent locally and remotely. Checks that the Habit it references
     * exists.
     *
     * @param event the HabitEvent to be saved
     * @see HabitRepository
     */
    public void saveEvent(HabitEvent event) {
        if (habitRepo.getHabitSync(event.getHabitId()) == null) {
            System.out.println(TAG + " no event");
            Log.d(TAG, "Could not find local habit with id: " + event.getHabitId());
            return;
        }
        System.out.println(TAG + " got event");
        source.saveEvent(event);
        remoteOperation(event.getElasticId(), Util.CREATE);
    }

    /**
     * Update the HabitEvent with eventId to what is passed in in event. Changes propagate
     * locally and remotely.
     *
     * @param eventId unique event id to specify which event we need to update
     * @param event what the event should be updated to
     */
    public void updateEvent(String eventId, HabitEvent event) {
        source.updateEvent(eventId, event);
        remoteOperation(eventId, Util.UPDATE);
    }

    /**
     * Delete the HabitEvent locally and remotely
     * @param eventId unique id representing a HabitEvent
     */
    public void deleteEvent(String eventId) {
        Log.d(TAG, "Deleting " + eventId);
        source.deleteEvent(eventId);
        remoteOperation(eventId, Util.DELETE);
    }

    /**
     * Delete all events that are related to the habit with habitId.
     * Deletes locally and remotely.
     *
     * @param habitId unique id representing a habit
     */
    public void deleteEventsByHabitId(String habitId) {
        for (String eventId : source.getEventIdsForHabitId(habitId)) {
            deleteEvent(eventId);
        }
    }

    /**
     * Schedules a RemoteEventJob that guarantees that the network request will
     * be done. Persists across app closes as well
     * @param id the elasticId of the HabitEvent we are doing an operation on
     * @param operation an integer representing the operation (Util.CREATE, Util.UPDATE, Util.DELETE)
     * @see RemoteEventJob
     */
    private void remoteOperation(String id, int operation) {
        removePreviousJobs(id);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("EVENT_ID", id);
        bundle.putInt("OPERATION", operation);
        jobScheduler.schedule(new JobInfo.Builder(Util.getUniqueJobId(jobScheduler),
                new ComponentName(context, RemoteEventJob.class))
                .setExtras(bundle) // send eventId, and operation (create, update, or delete)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // must have network
                .setOverrideDeadline(5000)
                .build()
        );
    }

    /**
     * This function removes all previous RemoteEventJob that have the same
     * elasticId. This must be done because the JobScheduler does not execute
     * jobs in order, so if we create a HabitEvent offline then delete it offline
     * there is no guarantee that it will be done in that order, causing a
     * mismatch between the local and remote data.
     *
     * @author Mackenzie Hauck
     * @param id the elasticId of a HabitEvent
     * @see RemoteEventJob
     */
    private void removePreviousJobs(String id) {
        String jobEventId = null;
        for (JobInfo job : jobScheduler.getAllPendingJobs()) {
            jobEventId = job.getExtras().getString("EVENT_ID");
            if (jobEventId != null && jobEventId.equals(id)) {
                // remove previous job that was scheduled with this id
                // so we don't do something like "update" and then "create"
                Log.d(TAG, "Removing job with id: " + String.valueOf(job.getId()));
                jobScheduler.cancel(job.getId());
                // should only ever be one job with that habitId
                return;
            }
        }
    }

    /**
     * Bulk save HabitEvents to local persistent storage
     * @param list the HabitEvents to be saved
     */
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
