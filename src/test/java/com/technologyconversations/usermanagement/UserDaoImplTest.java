package com.technologyconversations.usermanagement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UserDaoImplTest {

    private UserDao userDao;
    private User user;
    private String userName = "vfarcic";

    @Before
    public void beforeUserDaoImplTest() {
        userDao = new UserDaoImpl();
        user = new User();
        user.setUserName(userName);
        user.setPassword("password");
        user.setFullName("fullName");
        user.setUpdated(new Date());
    }

    @Test
    public void putUserShouldInsertNewRowIfUserDoesNotExist() {
        userDao.putUser(user);
        assertThat(userDao.getAllUsers().size(), is(1));
    }

    @Test
    public void getUserShouldRetrieveUser() {
        userDao.putUser(user);
        assertThat(userDao.getUser(userName), is(equalTo(user)));
    }

}
