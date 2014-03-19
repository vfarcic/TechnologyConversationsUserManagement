package com.technologyconversations.usermanagement;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UserTest {

    private User user;
    private final String userName = "vfarcic";
    private final String password = "secret";
    private final String fullName = "Viktor Farcic";
    private final Date updated = new Date();

    @Before
    public void beforeUserTest() {
        user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setUpdated(updated);
    }

    @Test
    public void userNameShouldHaveGetter() {
        assertThat(user.getUserName(), is(userName));
    }

    @Test
    public void userNameShouldHaveSetter() {
        String actual = "myUsername";
        user.setUserName(actual);
        assertThat(user.getUserName(), is(actual));
    }

    @Test
    public void passwordShouldHaveGetter() {
        assertThat(user.getPassword(), is(password));
    }

    @Test
    public void passwordShouldHaveSetter() {
        String actual = "myPassword";
        user.setUserName(actual);
        assertThat(user.getUserName(), is(actual));
    }

    @Test
    public void fullNameShouldHaveGetter() {
        assertThat(user.getFullName(), is(fullName));
    }

    @Test
    public void fullNameShouldHaveSetter() {
        String actual = "My Name";
        user.setFullName(actual);
        assertThat(user.getFullName(), is(actual));
    }

    @Test
    public void updatedShouldHaveGetter() {
        assertThat(user.getUpdated(), is(updated));
    }

    @Test
    public void updatedShouldHaveSetter() {
        Date actual = new Date();
        user.setUpdated(actual);
        assertThat(user.getUpdated(), is(actual));
    }

    @Test
    public void equalsShouldFailIfUsernameIsNotTheSame() {
        User actual = new User();
        actual.setUserName("something");
        actual.setPassword(password);
        actual.setFullName(fullName);
        actual.setUpdated(updated);
        assertThat(actual, not(equalTo(user)));
    }

    @Test
    public void equalsShouldFailIfPasswordIsNotTheSame() {
        User actual = new User();
        actual.setUserName(userName);
        actual.setPassword("something");
        actual.setFullName(fullName);
        actual.setUpdated(updated);
        assertThat(actual, not(equalTo(user)));
    }

    @Test
    public void equalsShouldFailIfFullNameIsNotTheSame() {
        User actual = new User();
        actual.setUserName(userName);
        actual.setPassword(password);
        actual.setFullName("something");
        actual.setUpdated(updated);
        assertThat(actual, not(equalTo(user)));
    }

    @Test
    public void equalsShouldNotFailIfUserNamePasswordAndFullNameAreTheSame() {
        User actual = new User();
        actual.setUserName(userName);
        actual.setPassword(password);
        actual.setFullName(fullName);
        actual.setUpdated(updated);
        assertThat(actual, equalTo(user));
    }

}
