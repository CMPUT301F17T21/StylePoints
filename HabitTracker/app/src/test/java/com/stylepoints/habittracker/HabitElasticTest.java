package com.stylepoints.habittracker;


import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.repository.remote.ElasticHabitListResponse;
import com.stylepoints.habittracker.repository.remote.ElasticRequestStatus;
import com.stylepoints.habittracker.repository.remote.ElasticResponse;
import com.stylepoints.habittracker.repository.remote.ElasticSearch;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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

        Response<ElasticResponse<Habit>> response = elastic.getHabitById("8dfc7b93-9418-4ce0-b9e6-dd7ede287c1b").execute();
        System.out.println(response);
        Habit habit = response.body().getSource();
        System.out.println(habit);

        assertEquals("run", habit.getType());
    }

    @Test
    public void saveAndDeleteHabitTest() throws Exception {
        Habit habit = new Habit("run", "", "testusername", LocalDate.now(), DayOfWeek.MONDAY, DayOfWeek.TUESDAY);

        Response<ElasticRequestStatus> response = elastic.createHabitWithId(habit.getElasticId(), habit).execute();
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

    @Test
    public void getUsersHabitsTest() throws Exception {
        Response<ElasticHabitListResponse> response = elastic.searchHabit("user:mackenzie").execute();
        assert(response.isSuccessful());
        List<Habit> habitList = response.body().getList();
        assert(habitList != null);

        System.out.println("Number of hits: " + response.body().getNumHits());

        for (Habit habit : habitList) {
            System.out.println(habit);
        }
        assert(habitList.size() > 0);
    }
}
