package com.technologyconversations.usermanagement;

import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;

import static com.technologyconversations.usermanagement.StatusEnum.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserResourceTest extends CommonTest {

    private HttpServer server;
    private WebTarget target;
    private User user;
    private Client client;
    private UserDao userDao = UserDaoImpl.getInstance();
    private ObjectMapper objectMapper;
    private String userJson;
    private String userName = "vfarcic";

    @Before
    public void beforeMyResourceTest() throws Exception {
        user = new User(userName);
        user.setPassword("password");
        user.setFullName("Viktor Farcic");
        user.setUpdated(new Date());
        userDao.putUser(user);
        server = Server.startServer();
        client = ClientBuilder.newClient();
        target = client.target(Server.BASE_API_URI).path("users/user/vfarcic.json");
        objectMapper = new ObjectMapper();
        userJson = objectMapper.writeValueAsString(user);
    }

    @After
    public void afterMyResourceTest() throws Exception {
        userDao.deleteAllUsers();
        server.shutdown();
    }

    @Test
    public void userJsonShouldReturnStatus200() {
        assertThat(target.request().head().getStatus(), is(200));
    }

    @Test
    public void getUserShouldReturnStatus200() {
        assertThat(target.request().get().getStatus(), is(200));
    }

    @Test
    public void getUserShouldReturnTypeApplicationJson() {
        assertThat(target.request().get().getMediaType().toString(), is("application/json"));
    }

    @Test
    public void getUserShouldReturnSpecifiedUser() throws Exception {
        String json = target.request().get(String.class);
        User actual =  objectMapper.readValue(json, User.class);
        assertThat(actual, is(equalTo(user)));
    }

    @Test
    public void getUserShouldReturnStatusNokIfSpecifiedUserDoesNotExist() throws Exception {
        target = client.target(Server.BASE_API_URI).path("users/user/non_existent_user.json");
        String json = target.request().get(String.class);
        User actual =  objectMapper.readValue(json, User.class);
        assertThat(actual.getStatus(), is(NOK));
    }

    @Test
    public void deleteUserShouldReturnStatus200() {
        assertThat(target.request().delete().getStatus(), is(200));
    }

    @Test
    public void deleteUserShouldReturnTypeApplicationJson() {
        assertThat(target.request().delete().getMediaType().toString(), is("application/json"));
    }

    @Test
    public void deleteUserShouldReturnStatusOkIfSpecifiedUserExists() throws Exception {
        String json = target.request().delete(String.class);
        User actual = objectMapper.readValue(json, User.class);
        assertThat(actual.getStatus(), is(equalTo(StatusEnum.OK)));
    }

    @Test
    public void deleteUserShouldDeleteSpecifiedUser() throws Exception {
        insertUsers(3);
        int countUsers = userDao.getAllUsers().size();
        target.request().delete(String.class);
        assertThat(userDao.getAllUsers().size(), is(countUsers - 1));
    }

    @Test
    public void deleteUserShouldReturnStatusNokIfSpecifiedUserDoesNotExist() throws Exception {
        target = client.target(Server.BASE_API_URI).path("users/user/non_existent_user.json");
        String json = target.request().delete(String.class);
        User actual =  objectMapper.readValue(json, User.class);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
    }

    @Test
    public void putUserShouldReturnStatus200() throws Exception {
        Response actual = target.request().put(Entity.text(userJson));
        assertThat(actual.getStatus(), is(200));
    }

    @Test
    public void putUserShouldReturnTypeApplicationJson() {
        Response actual = target.request().put(Entity.text(userJson));
        assertThat(actual.getMediaType().toString(), is("application/json"));
    }

    @Test
    public void putUserShouldReturnStatusOk() throws Exception {
        String json = target.request().put(Entity.text(userJson), String.class);
        User actual = objectMapper.readValue(json, User.class);
        assertThat(actual.getStatus(), is(equalTo(OK)));
    }

    @Test
    public void putUserShouldCreateNewUserIfItDoesNotAlreadyExist() throws Exception {
        int countUsers = userDao.getAllUsers().size();
        user.setUserName("new_user");
        userJson = objectMapper.writeValueAsString(user);
        target.request().put(Entity.text(userJson), String.class);
        assertThat(userDao.getAllUsers().size(), is(countUsers + 1));
    }

    @Test
    public void putUserShouldUpdateExistingUserIfItAlreadyExists() throws Exception {
        String fullName = "This is my new Full Name";
        user.setFullName(fullName);
        userJson = objectMapper.writeValueAsString(user);
        target.request().put(Entity.text(userJson), String.class);
        assertThat(userDao.getUser(userName).getFullName(), is(fullName));
    }

    @Test
    public void putUserShouldReturnStatusNokIfFullNameIsGreaterThan200Characters() {
        char[] charArray = new char[250];
        Arrays.fill(charArray, 'X');
        String fullName = new String(charArray);
    }

}
