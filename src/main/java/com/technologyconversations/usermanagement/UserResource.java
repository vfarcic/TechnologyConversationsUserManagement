package com.technologyconversations.usermanagement;

import org.codehaus.jackson.map.ObjectMapper;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.regex.Pattern;

import static com.technologyconversations.usermanagement.StatusEnum.*;

@Path("users/user/{userName}.json")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    public String getUser(@PathParam("userName") String userName) throws Exception {
        User user = getUserOrNok(UserDaoImpl.getInstance().getUser(userName));
        user.setPassword("");
        return new ObjectMapper().writeValueAsString(user);
    }

    @DELETE
    public String deleteUser(@PathParam("userName") String userName) throws Exception {
        User user = getUserOrNok(UserDaoImpl.getInstance().deleteUser(userName));
        return new ObjectMapper().writeValueAsString(user);
    }

    @PUT
    public String putUser(String userJson) throws Exception {
        User user = getUserFromJson(userJson);
        if (user.getStatus() == OK) {
            UserDaoImpl.getInstance().putUser(user);
        }
        return new ObjectMapper().writeValueAsString(user);
    }

    private User getUserFromJson(String userJson) throws Exception {
        User user = new ObjectMapper().readValue(userJson, User.class);
        verifyFullName(user);
        verifyUserName(user);
        verifyPassword(user);
        if (user.getStatusMessage() != null && !user.getStatusMessage().isEmpty()) {
            user.setStatus(NOK);
        }
        return user;
    }

    private void verifyFullName(User user) {
        if (user.getFullName().length() > 200) {
            user.setStatusMessage("Full name cannot have more than 200 characters");
        } else if (user.getFullName() == null || user.getFullName().isEmpty()) {
            user.setStatusMessage("Full name is mandatory");
        } else if (Pattern.compile("[^a-zA-Z ]").matcher(user.getFullName()).find()) {
            user.setStatusMessage("Full Name can contain only letters and space character");
        }
    }

    private void verifyUserName(User user) {
        if (user.getUserName().length() > 20) {
            user.setStatusMessage("User name cannot have more than 20 characters");
        } else if (user.getUserName() == null || user.getUserName().isEmpty()) {
            user.setStatusMessage("User name is mandatory");
        } else if (Pattern.compile("[^a-zA-Z0-9]").matcher(user.getUserName()).find()) {
            user.setStatusMessage("User name can contain only alpha-numeric characters");
        }
    }

    private void verifyPassword(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            User existingUser = UserDaoImpl.getInstance().getUser(user.getUserName());
            if (existingUser != null && !existingUser.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            }
        } else {
            if (user.getPassword().length() < 8) {
                user.setStatusMessage("Password must be at least 8 characters");
            } else if (!Pattern.compile("[A-Z]").matcher(user.getPassword()).find()) {
                user.setStatusMessage("Password must contain at least one capital letter");
            } else if (!Pattern.compile("[0-9]").matcher(user.getPassword()).find()) {
                user.setStatusMessage("Password must contain at least one number");
            } else {
                user.setPassword(new StrongPasswordEncryptor().encryptPassword(user.getPassword()));
            }
        }
    }

    private User getUserOrNok(User user) {
        if (user == null) {
            user = new User();
            user.setStatus(NOK);
        }
        return user;
    }

}