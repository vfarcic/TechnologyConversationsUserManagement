package com.technologyconversations.usermanagement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UserDaoImplTest extends CommonTest {

    private UserDao userDao;
    private User user;
    private String userName = "vfarcic";
    private String fullName = "Viktor Farcic";

    @Before
    public void beforeUserDaoImplTest() {
        userDao = UserDaoImpl.getInstance();
        user = new User(userName);
        user.setPassword("password");
        user.setFullName(fullName);
        user.setUpdated(new Date());
    }

    @After
    public void afterUserDaoImplTest() {
        userDao.deleteAllUsers();
    }

    @Test
    public void putUserShouldInsertNewUserIfUserNameDoesNotAlreadyExist() {
        userDao.putUser(user);
        assertThat(userDao.getAllUsers().size(), is(1));
    }

    @Test
    public void putUserShouldUpdateExistingUserIfUserNameAlreadyExists() {
        String actual = "Viktor Gulliksen";
        userDao.putUser(user);
        user.setFullName(actual);
        userDao.putUser(user);
        assertThat(userDao.getUser("vfarcic").getFullName(), is(actual));
    }

    @Test
    public void deleteAllUsersShouldDeleteAllUsersFromTheTable() {
        for (int i = 1; i <= 3; i++) {
            user.setUserName(userName + i);
            userDao.putUser(user);
        }
        userDao.deleteAllUsers();
        assertThat(userDao.getAllUsers().size(), is(0));
    }

    @Test
    public void getUserShouldRetrieveUser() {
        userDao.putUser(user);
        assertThat(userDao.getUser(userName), is(equalTo(user)));
    }

    @Test
    public void getAllUsersShouldRetrieveAllUsers() {
        insertUsers(3);
        assertThat(userDao.getAllUsers().size(), is(3));
    }

    @Test
    public void getAllUsersShouldContainUserName() {
        insertUsers(1);
        assertThat(userDao.getAllUsers().get(0).getUserName(), is(not(nullValue())));
        assertThat(userDao.getAllUsers().get(0).getUserName(), is(not(equalTo(""))));
    }

    @Test
    public void getAllUsersShouldContainFullName() {
        insertUsers(1);
        assertThat(userDao.getAllUsers().get(0).getFullName(), is(not(nullValue())));
        assertThat(userDao.getAllUsers().get(0).getFullName(), is(not(equalTo(""))));
    }

    @Test
    public void deleteUserShouldDeleteSpecifiedUser() {
        List<String> userNames = insertUsers(3);
        userDao.deleteUser(userNames.get(1));
        assertThat(userDao.getAllUsers().size(), is(2));
    }

    @Test
    public void deleteUserShouldReturnDeletedUser() {
        userDao.putUser(user);
        User actual = userDao.deleteUser(user.getUserName());
        assertThat(actual, is(equalTo(user)));
    }

}
