package com.Listeners;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.User.User;
import com.User.UserManager;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener{
    @Override
    public void sessionCreated(HttpSessionEvent evt) {

    }
    @Override
    public void sessionDestroyed(HttpSessionEvent evt) {
        User user = (User) evt.getSession().getAttribute("user");
        if (user != null) {
            user.removeSession(evt.getSession());
            if (!user.haveSession()) {
                UserManager userManager = (UserManager) evt.getSession().getServletContext().getAttribute("userManager");
                userManager.unloadUser(user);
            }
        }
    }
}
