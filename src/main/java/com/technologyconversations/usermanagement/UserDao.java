package com.technologyconversations.usermanagement;

import java.util.List;

public interface UserDao {

    abstract List<User> getAllUsers();

    abstract void deleteAllUsers();

    abstract User getUser(String userName);

    abstract User deleteUser(String userName);

    abstract void putUser(User user);

}
