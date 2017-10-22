package com.stylepoints.habittracker;

import android.graphics.Picture;
import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by mchauck on 10/21/17.
 */
public class HabitEventTest {
    HabitEvent event;

    @Before
    public void setUp() throws Exception {
        try {
            event = new HabitEvent("test comment");
        } catch (CommentTooLongException e) {
            fail("HabitEvent comment too long in setUp()");
        }
    }

    @Test
    public void getComment() throws Exception {
        assertEquals("test comment", event.getComment());
    }

    @Test
    public void setComment() throws Exception {
        assertEquals("test comment", event.getComment());
        try {
            event.setComment("new comment");
        } catch (CommentTooLongException e) {
            fail("new comment is too long to set");
        }
        assertEquals("new comment", event.getComment());
    }

    @Test
    public void getPicture() throws Exception {
        fail("not implemented");
    }

    @Test
    public void setPicture() throws Exception {
        fail("not implemented");
    }

    @Test
    public void dateGetAndSet() throws Exception {
        Date date = new GregorianCalendar(2017, 1, 1).getTime();
        event.setEventDate(date);
        assertEquals(date, event.getEventDate());
    }

    @Test
    public void getLocation() throws Exception {
        fail("not implemented");
    }

    @Test
    public void setLocation() throws Exception {
        fail("not implemented");
    }

}