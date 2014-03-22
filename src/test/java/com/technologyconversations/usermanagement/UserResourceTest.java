package com.technologyconversations.usermanagement;

import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;

import static com.technologyconversations.usermanagement.StatusEnum.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserResourceTest extends CommonTest {

    private static HttpServer server;
    private WebTarget target;
    private User user;
    private Client client;
    private UserDao userDao = UserDaoImpl.getInstance();
    private ObjectMapper objectMapper;
    private String userJson;
    private String userName = "vFarcic";
    private String fullName = "Viktor Farcic";
    private String password = "Password1";

    @BeforeClass
    public static void beforeUserResourceTestClass() {
        server = Server.startServer();
    }

    @Before
    public void beforeUserResourceTest() throws Exception {
        user = new User(userName);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setUpdated(new Date());
        client = ClientBuilder.newClient();
        target = client.target(Server.BASE_API_URI).path("users/user/" + userName + ".json");
        objectMapper = new ObjectMapper();
        userJson = objectMapper.writeValueAsString(user);
        target.request().put(Entity.text(userJson), String.class);
    }

    @After
    public void afterUserResourceTest() throws Exception {
        userDao.deleteAllUsers();
    }

    @AfterClass
    public static void afterUserResourceTestClass() {
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
        user.setUserName("newuser1");
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
    public void putUserShouldReturnStatusNokIfFullNameIsGreaterThan200Characters() throws Exception {
        String json = updateUserToBigFullName();
        User actual = objectMapper.readValue(json, User.class);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("Full name cannot have more than 200 characters"));
    }

    @Test
    public void putUserShouldNotUpdateDataIfUserStatusIsNok() throws Exception {
        updateUserToBigFullName();
        assertThat(userDao.getUser(userName).getFullName(), is(fullName));
    }

    @Test
    public void putUserShouldReturnStatusNokIfFullNameIsEmpty() throws Exception {
        user.setFullName("");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("Full name is mandatory"));
    }

    @Test
    public void putUserShouldReturnStatusNokIfFullNameHasCharactersOtherThanAlphaOrSpace() throws Exception {
        user.setFullName("Viktor Farcic 3");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("Full Name can contain only letters and space character"));
    }

    @Test
    public void putUserShouldReturnStatusNokIfUserNameIsGreaterThan20Characters() throws Exception {
        user.setUserName("ThisUserNameIsGreaterThan20Characters");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("User name cannot have more than 20 characters"));
    }

    @Test
    public void putUserShouldReturnStatusNokIfUserNameIsEmpty() throws Exception {
        user.setUserName("");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("User name is mandatory"));
    }

    @Test
    public void putUserShouldReturnStatusNokIfUserNameHasCharactersOtherThanAlphaNumeric() throws Exception {
        user.setUserName("v_farcic");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("User name can contain only alpha-numeric characters"));
    }

    @Test
    public void putUserShouldReturnStatusNokIfPasswordIsLessThan8Characters() throws Exception {
        user.setPassword("7_chars");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("Password must be at least 8 characters"));
    }

    @Test
    public void putUserShouldReturnStatusNokIfPasswordDoesNotHaveAtLeastOneCapitalLetter() throws Exception {
        user.setPassword("mysecret1");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("Password must contain at least one capital letter"));
    }

    @Test
    public void putUserShouldReturnStatusNokIfPasswordDoesNotHaveAtLeastOneNumber() throws Exception {
        user.setPassword("Mysecret");
        User actual = putUser(user);
        assertThat(actual.getStatus(), is(equalTo(NOK)));
        assertThat(actual.getStatusMessage(), is("Password must contain at least one number"));
    }

    @Test
    public void putUserShouldUseExistingPasswordIfItIsNotProvidedInJson() throws Exception {
        user.setPassword("");
        User actual = putUser(user);
        assertThat(actual.getPassword(), is(not(equalTo(""))));
    }

    @Test
    public void putUserShouldSaveEncryptedPassword() {
        target.request().put(Entity.text(userJson));
        assertThat(userDao.getUser(userName).getPassword(), is(not(equalTo(password))));
    }

    @Test
    public void getUserShouldNotReturnPassword() throws Exception {
        String json = target.request().get(String.class);
        User actual =  objectMapper.readValue(json, User.class);
        System.out.println(actual.getPassword());
        assertThat(actual.getPassword(), is(""));
    }

    private User putUser(User user) throws Exception {
        userJson = objectMapper.writeValueAsString(user);
        String json = target.request().put(Entity.text(userJson), String.class);
        return objectMapper.readValue(json, User.class);
    }

    private String updateUserToBigFullName() throws Exception {
        char[] charArray = new char[250];
        Arrays.fill(charArray, 'X');
        user.setFullName(new String(charArray));
        userJson = objectMapper.writeValueAsString(user);
        return target.request().put(Entity.text(userJson), String.class);
    }

}
