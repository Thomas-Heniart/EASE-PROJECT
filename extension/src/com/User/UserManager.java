package com.User;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserManager {
    private List<User> userList;
    private HashMap<String, User> userEmailMap;

    public UserManager() {
        userList = new LinkedList<User>();
        userEmailMap = new HashMap<String, User>();
    }

    public User loadUser(String email) {
        User user = userEmailMap.get(email);
        if (user == null) {
            user = new User(email);
            userList.add(user);
            userEmailMap.put(email, user);
        }
        return user;
    }

    public void unloadUser(User user) {
        userList.remove(user);
        userEmailMap.remove(user.getLogin());
    }

    public User getUser(String email) {
        return userEmailMap.get(email);
    }
}