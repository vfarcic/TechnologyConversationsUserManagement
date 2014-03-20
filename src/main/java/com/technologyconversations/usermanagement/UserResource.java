package com.technologyconversations.usermanagement;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.technologyconversations.usermanagement.StatusEnum.*;

@Path("user/{userName}.json")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


    @GET
    public String getUser(@PathParam("userName") String userName) throws Exception {
        User user = getUserOrNok(UserDaoImpl.getInstance().getUser(userName));
        return new ObjectMapper().writeValueAsString(user);
    }

    @DELETE
    public String deleteUser(@PathParam("userName") String userName) throws Exception {
        User user = getUserOrNok(UserDaoImpl.getInstance().deleteUser(userName));
        return new ObjectMapper().writeValueAsString(user);
    }

    @PUT
    public String putUser(String userJson) throws Exception {
        User user = new ObjectMapper().readValue(userJson, User.class);
        UserDaoImpl.getInstance().putUser(user);
        return new ObjectMapper().writeValueAsString(user);
    }

    private User getUserOrNok(User user) {
        if (user == null) {
            user = new User();
            user.setStatus(NOK);
        }
        return user;
    }

}