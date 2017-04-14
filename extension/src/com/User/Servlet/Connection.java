package com.User.Servlet;/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.Servlet.Servlet;
import com.User.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Connection")
public class Connection extends Servlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.needToBeNotConnected();
        String login = this.getParameter("login");
        User user = userManager.loadUser(login);
        this.user = user;
        session.setAttribute("user", user);
        user.addSession(session);
        response.getWriter().print("Connected.");
    }
}
