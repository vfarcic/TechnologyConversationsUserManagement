package com.technologyconversations.usermanagement;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static com.technologyconversations.usermanagement.StatusEnum.*;

@Path("users.json")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @GET
    public String getUsers() throws Exception {
        return new ObjectMapper().writeValueAsString(UserDaoImpl.getInstance().getAllUsers());
    }

    @DELETE
    public String deleteUsers() throws Exception {
        UserDaoImpl.getInstance().deleteAllUsers();
        return new ObjectMapper().writeValueAsString(new Status(OK));
    }

}