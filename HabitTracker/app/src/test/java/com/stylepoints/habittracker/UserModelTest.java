package com.stylepoints.habittracker;

import com.stylepoints.habittracker.model.User;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class UserModelTest {
    @Test
    public void noRequiredFieldsNull() {
        User user = new User("test_username");
        assert(user.getUsername() != null);
        assert(user.getDateCreated() != null);
    }

    @Test
    public void testGetters() {
        User user = new User("test_username");
        assertEquals("test_username", user.getUsername());
        assertEquals(LocalDate.now(), user.getDateCreated());
    }
}
