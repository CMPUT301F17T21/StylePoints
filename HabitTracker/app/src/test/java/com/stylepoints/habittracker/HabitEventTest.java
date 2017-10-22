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
    public void testCommentTooLong() {
        String testComment = "this is a test comment that is more than 30 characters long";
        assertTrue(testComment.length() > 30);
        try {
            event.setComment(testComment);
            fail("Exception was not thrown for a comment that is too long");
        } catch (CommentTooLongException e) {

        }
    }

    @Test
    public void testPictureClass() throws Exception {
        event.setPicture(new Picture());
        assertEquals(event.getPicture().getClass(), Picture.class);
    }

    @Test
    public void dateGetAndSet() throws Exception {
        Date date = new GregorianCalendar(2017, 1, 1).getTime();
        event.setEventDate(date);
        assertEquals(date, event.getEventDate());
    }

    @Test
    public void testLocationClass() throws Exception {
        event.setLocation(new Location("test"));
        assertEquals(event.getLocation().getClass(), Location.class);
    }

}