package com.User.Servlet;/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.Servlet.Servlet;
import com.User.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Logout")
public class Logout extends Servlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.needToBeConnected();
        user.removeSession(request.getSession());
        session.removeAttribute("user");
        if (!user.haveSession()) {
            UserManager userManager = (UserManager) request.getSession().getServletContext().getAttribute("userManager");
            userManager.unloadUser(user);
        }
        response.getWriter().print("Logged out.");
    }
}
