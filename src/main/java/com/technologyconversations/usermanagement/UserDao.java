package com.technologyconversations.usermanagement;

import java.util.List;

public interface UserDao {

    abstract List<User> getAllUsers();

    abstract void putUser(User user);

    abstract User getUser(String userName);

    abstract void deleteAllUsers();

    abstract void deleteUser(String userName);

}
