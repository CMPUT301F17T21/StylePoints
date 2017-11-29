package com.stylepoints.habittracker;

import com.stylepoints.habittracker.model.HabitEvent;
import com.stylepoints.habittracker.repository.remote.ElasticEventListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;


public class EventElasticTest {
    private ElasticSearch elastic;

    @Before
    public void initElastic() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cmput301.softwareprocess.es:8080/cmput301f17t21_stylepoints/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        elastic = retrofit.create(ElasticSearch.class);
    }

    @Test
    public void getEventTest() throws Exception {
        HabitEvent event = new HabitEvent("testusername", "testcomment", LocalDate.now(), "AV_sN6rwT651_e3dy3Dl");

        Response<ElasticRequestStatus> response = elastic.saveEvent(event).execute();
        assert(response.isSuccessful());
        ElasticRequestStatus status = response.body();

        assertEquals("event", status.getType());
        assert(status.wasCreated());

        Response<ElasticResponse<HabitEventEntity>> habitResponse = elastic.getEventById(status.getId()).execute();
        assert(habitResponse.isSuccessful());
        HabitEventEntity event2 = habitResponse.body().getSource();

        assertEquals("mackenzie", event2.getUser());
        assertEquals("testcomment", event2.getComment());
    }

    @Test
    public void saveAndDeleteEventTest() throws Exception {
        HabitEventEntity event = new HabitEventEntity(1, LocalDate.now(), "testcomment");
        event.setUser("mackenzie");
        event.setHabitElasticId("AV_sN6rwT651_e3dy3Dl");

        Response<ElasticRequestStatus> response = elastic.saveEvent(event).execute();
        assert(response.isSuccessful());
        ElasticRequestStatus status = response.body();

        assertEquals("event", status.getType());
        assert(status.wasCreated());

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
        HabitEventEntity event = new HabitEventEntity(1, LocalDate.now(), "testcomment");
        event.setUser("mackenzie");
        event.setHabitElasticId("AV_sN6rwT651_e3dy3Dl");
        Response<ElasticRequestStatus> saveResponse = elastic.saveEvent(event).execute();
        assert(saveResponse.isSuccessful());
        System.out.println(saveResponse.body());

        // search for something in our temporary event
        Response<ElasticEventListResponse> response = elastic.searchEvent("comment:testcomment").execute();
        assert(response.isSuccessful()); // http response OK

        System.out.println(response);
        System.out.println(response.body());

        System.out.println(response.body().getNumHits());

        List<HabitEventEntity> eventList = response.body().getList();
        assert(eventList != null);
        for (HabitEventEntity e : eventList) {
            System.out.println(e);
        }
        assert(response.body().getNumHits() > 0);

        elastic.deleteEvent(saveResponse.body().getId());
    }
}