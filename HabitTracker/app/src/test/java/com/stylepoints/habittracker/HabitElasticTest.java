package com.stylepoints.habittracker;


import com.stylepoints.habittracker.repository.local.entity.HabitEntity;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class HabitElasticTest {
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
    public void getHabitTest() throws Exception {
        // .execute() is sync, .enqueue() is async

        Response<ElasticResponse<HabitEntity>> response = elastic.getHabitById("AV_sN6rwT651_e3dy3Dl").execute();
        HabitEntity habit = response.body().getSource();

        System.out.println(response);
        System.out.println(response.body());

        assertEquals("run", habit.getType());
    }

    @Test
    public void saveAndDeleteHabitTest() throws Exception {
        HabitEntity habit = new HabitEntity("run", "", LocalDate.now(), DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
        habit.setUser("mackenzie");

        Response<ElasticRequestStatus> response = elastic.saveHabit(habit).execute();
        assert(response.isSuccessful());
        System.out.println(response);
        ElasticRequestStatus status = response.body();
        System.out.println(status);

        assertTrue(status.wasCreated());
        assertEquals("cmput301f17t21_stylepoints", status.getIndex());
        assertEquals(1, status.getVersion());
        assertEquals("habit", status.getType());

        // delete the habit we just added
        Response<ElasticRequestStatus> delResponse = elastic.deleteHabit(status.getId()).execute();
        assert(delResponse.isSuccessful());

        ElasticRequestStatus delStatus = delResponse.body();
        System.out.println(delStatus);
        assertEquals("habit", delStatus.getType());
        assertEquals(status.getId(), delStatus.getId());
    }
}
