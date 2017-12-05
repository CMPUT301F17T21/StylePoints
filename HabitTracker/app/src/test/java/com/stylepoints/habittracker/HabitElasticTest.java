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
    public void saveAndDeleteHabitTest() throws Exception {
        Habit habit = new Habit("run", "", "testusername", LocalDate.now(), DayOfWeek.MONDAY, DayOfWeek.TUESDAY);

        Response<ElasticRequestStatus> response = elastic.createHabitWithId(habit.getElasticId(), habit).execute();
        assert(response.isSuccessful());
        System.out.println(response);
        ElasticRequestStatus status = response.body();
        System.out.println(status);

        assertTrue(status.wasCreated());
        assertEquals("cmput301f17t21_stylepoints_test", status.getIndex());
        assertEquals(1, status.getVersion());
        assertEquals("habit", status.getType());

        // get the habit we just saved
        Response<ElasticResponse<Habit>> getResponse = elastic.getHabitById(status.getId()).execute();
        assert(getResponse.isSuccessful());
        System.out.println(getResponse);

        Habit habit2 = getResponse.body().getSource();
        assert(habit2 != null);
        assertEquals(habit.getElasticId(), habit2.getElasticId());

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
        final int NUM_HABITS = 3;
        // first, upload several habits
        for (int i = 0; i < NUM_HABITS; i++) {
            Habit habit = new Habit(String.valueOf(i), "reason for: " + i, "testusername");
            Response<ElasticRequestStatus> resp = elastic.createHabitWithId(habit.getElasticId(), habit).execute();
            System.out.println(resp);
            assert(resp.isSuccessful());
        }

        // wait for elastic to finish making the habits
        Thread.sleep(500);

        Response<ElasticHabitListResponse> response = elastic.searchHabit("username:testusername").execute();
        assert(response.isSuccessful());

        List<Habit> habitList = response.body().getList();
        assert(habitList != null);

        for (Habit habit : habitList) {
            System.out.println(habit);
        }

        System.out.println("Number of hits: " + response.body().getNumHits());
        assertEquals(NUM_HABITS, response.body().getNumHits());
    }

    @Test
    public void searchHabits() throws Exception {
        Habit habit = new Habit("type1", "test", "user1");
        Habit habit2 = new Habit("type2", "test", "user2");
        Response<ElasticRequestStatus> resp = elastic.createHabitWithId(habit.getElasticId(), habit).execute();
        Response<ElasticRequestStatus> resp2 = elastic.createHabitWithId(habit2.getElasticId(), habit2).execute();

        assert(resp.isSuccessful());
        assert(resp2.isSuccessful());

        Thread.sleep(500); // wait for elastic to finish

        Response<ElasticHabitListResponse> respSearch1 = elastic.searchHabit("reason:test").execute();
        assert(respSearch1.isSuccessful());
        assertEquals(2, respSearch1.body().getNumHits());

        Response<ElasticHabitListResponse> respSearch2 = elastic.searchHabit("reason:test", "username:user1").execute();
        assert(respSearch2.isSuccessful());
        assertEquals(1, respSearch2.body().getNumHits());
    }
}
