package com.User;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

public class User {
    private String login;
    private Boolean admin;
    private List<HttpSession> sessions;

    public User(String login) {
        this.login = login;
        this.admin = true;
        this.sessions = new LinkedList<HttpSession>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String email) {
        this.login = email;
    }

    public Boolean isAdmin() {return admin; }

    public void addSession(HttpSession session) {
        sessions.add(session);
    }

    public void removeSession(HttpSession session) {
        sessions.remove(session);
    }

    public boolean haveSession() {
        return sessions.size() > 0;
    }

    public String getExtensionVersion() {
        return ("0.0.1");
    }

}