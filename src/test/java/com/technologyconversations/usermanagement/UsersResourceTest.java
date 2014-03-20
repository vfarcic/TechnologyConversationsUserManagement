package com.technologyconversations.usermanagement;

import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Date;
import java.util.List;

import static com.technologyconversations.usermanagement.StatusEnum.*;

public class UsersResourceTest {

    private HttpServer server;
    private WebTarget target;
    ObjectMapper mapper;

    @Before
    public void beforeMyResourceTest() throws Exception {
        User user = new User();
        user.setPassword("password");
        user.setFullName("Viktor Farcic");
        user.setUpdated(new Date());
        for (int index = 1; index <= 3; index++) {
            user.setUserName("vfarcic" + index);
            UserDaoImpl.getInstance().putUser(user);
        }
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI).path("users/all.json");
        mapper = new ObjectMapper();
    }

    @After
    public void afterMyResourceTest() throws Exception {
        server.shutdown();
    }

    @Test
    public void usersAllJsonShouldReturnStatus200() {
        assertThat(target.request().head().getStatus(), is(200));
    }

    @Test
    public void getAllUsersShouldReturnTypeApplicationJson() {
        assertThat(target.request().get().getMediaType().toString(), is("application/json"));
    }


    @Test
    public void getAllUsersShouldReturnStatus200() {
        assertThat(target.request().get().getStatus(), is(200));
    }

    @Test
    public void getAllUsersShouldReturnTheListOfAllUsers() throws Exception {
        String json = target.request().get(String.class);
        List<User> users = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        assertThat(users.size(), is(3));
    }

    @Test
    public void deleteAllUsersShouldReturnStatus200() {
        assertThat(target.request().delete().getStatus(), is(200));
    }

    @Test
    public void deleteAllUsersShouldReturnTypeApplicationJson() {
        assertThat(target.request().delete().getMediaType().toString(), is("application/json"));
    }

    @Test
    public void deleteAllUsersShouldReturnStatusOK() throws Exception {
        String json = target.request().delete(String.class);
        Status status = mapper.readValue(json, Status.class);
        assertThat(status.getStatus(), is(OK));
    }

    @Test
    public void deleteAllUsersShouldDeleteAllUsers() throws Exception {
        target.request().delete(String.class);
        assertThat(UserDaoImpl.getInstance().getAllUsers().size(), is(0));
    }


}
