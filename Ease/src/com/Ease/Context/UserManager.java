package com.Ease.Context;

import com.Ease.Dashboard.User.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserManager {
    protected List<User> users = new LinkedList<>();
    protected Map<String, User> idUserMap = new HashMap<>();
    protected Map<User, List<Integer>> sessionUserMap = new HashMap<>();

    public UserManager() {

    }

    public void addUser(User user, Integer sessionId) {
        this.users.add(user);
        this.idUserMap.put(user.getDBid(), user);
        List<Integer> sessionIds = this.sessionUserMap.get(user);
        if (sessionIds == null)
            sessionIds = new LinkedList<>();
        sessionIds.add(sessionId);
        this.sessionUserMap.put(user, sessionIds);
    }

    /* Logout from all sessions */
    public void removeUser(User user) {
        this.users.remove(user);
        this.idUserMap.remove(user.getDBid());
        this.sessionUserMap.remove(user);
    }

    /* Classic logout */
    public void removeUser(User user, Integer sessionId) {
        List<Integer> sessionIds = sessionUserMap.get(user);
        if (sessionIds == null)
            return;
        sessionIds.remove(sessionId);
        if (!sessionIds.isEmpty())
            return;
        this.users.remove(user);
        this.idUserMap.remove(user.getDBid());
        this.sessionUserMap.remove(user);
    }

    public User getUserById(String id) {
        return this.idUserMap.get(id);
    }
}
