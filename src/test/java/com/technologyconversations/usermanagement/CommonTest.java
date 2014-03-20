package com.technologyconversations.usermanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonTest {

    protected List<String> insertUsers(int count) {
        List<String> userNames = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String userName = "someUser" + i;
            User user = new User(userName);
            user.setPassword("password");
            user.setFullName("Some Name");
            user.setUpdated(new Date());
            UserDaoImpl.getInstance().putUser(user);
            userNames.add(userName);
        }
        return userNames;
    }

}
