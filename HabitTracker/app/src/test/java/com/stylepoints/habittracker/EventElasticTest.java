package com.stylepoints.habittracker;

import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.remote.ElasticEventListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;


public class EventElasticTest {
    private ElasticSearch elastic;

    @Before
    public void initElastic() throws Exception {
        elastic = Util.getElasticInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.TEST_SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ElasticAdminCalls adminElastic = retrofit.create(ElasticAdminCalls.class);
        adminElastic.deleteEveryonesHabits().execute();
        adminElastic.deleteEveryonesEvents().execute();
        Thread.sleep(100);
    }

    @Test
    public void saveAndDeleteEventTest() throws Exception {
        HabitEvent event = new HabitEvent("testusername", "8dfc7b93-9418-4ce0-b9e6-dd7ede287c1b", "testcomment");

        Response<ElasticRequestStatus> response = elastic.createEventWithId(event.getElasticId(), event).execute();
        assert(response.isSuccessful());
        ElasticRequestStatus status = response.body();
        System.out.println(status);

        assertEquals("event", status.getType());
        assert(status.wasCreated());

        // get the event from elastic
        Response<ElasticResponse<HabitEvent>> getResponse = elastic.getEventById(status.getId()).execute();
        assert(getResponse.isSuccessful());
        System.out.println(getResponse);

        HabitEvent event2 = getResponse.body().getSource();
        assert(event2 != null);
        assertEquals(event.getElasticId(), event2.getElasticId());

        // delete the event we just made
        Response<ElasticRequestStatus> delResponse = elastic.deleteEvent(status.getId()).execute();
        assert(delResponse.isSuccessful());

        ElasticRequestStatus delStatus = delResponse.body();
        assertEquals("event", delStatus.getType());
        assertEquals(status.getId(), delStatus.getId());
    }

    @Test
    public void searchCommentsTest() throws Exception {
        // save a temporary event to elastic
        HabitEvent event = new HabitEvent("testusername", "AV_sN6rwT651_e3dy3Dl", "testcomment");

        Response<ElasticRequestStatus> saveResponse = elastic.createEventWithId(event.getElasticId(), event).execute();
        assert(saveResponse.isSuccessful());
        System.out.println(saveResponse.body());

        // search for something in our temporary event
        Response<ElasticEventListResponse> response = elastic.searchEvent("comment:testcomment").execute();
        assert(response.isSuccessful()); // http response OK

        System.out.println(response);
        System.out.println(response.body());

        System.out.println(response.body().getNumHits());

        List<HabitEvent> eventList = response.body().getList();
        assert(eventList != null);
        for (HabitEvent e : eventList) {
            System.out.println(e);
        }
        assert(response.body().getNumHits() > 0);
    }
}