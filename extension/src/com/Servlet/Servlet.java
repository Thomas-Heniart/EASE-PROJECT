package com.Servlet;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.Servlet.Exception.ServletErrorException;
import com.Servlet.Exception.ServletNoPermissionException;
import com.Servlet.Exception.ServletNotConnectedException;
import com.Servlet.Exception.ServletWrongParameterException;
import com.User.User;
import com.User.UserManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class Servlet extends HttpServlet {

    protected ServletContext context;
    protected UserManager userManager;

    protected HttpSession session;
    protected User user;

    HttpServletRequest request;
    HttpServletResponse response;

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        context = conf.getServletContext();
        userManager = (UserManager) context.getAttribute("userManager");

    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        session = req.getSession();
        user = (User)session.getAttribute("user");
        request = req;
        response = resp;
        try {
            super.service(req, resp);
        } catch (ServletWrongParameterException e) {
            e.printStackTrace();
            resp.resetBuffer();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(e.getMessage());
        } catch (ServletNotConnectedException e) {
            e.printStackTrace();
            resp.resetBuffer();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print(e.getMessage());
        } catch (ServletNoPermissionException e) {
            e.printStackTrace();
            resp.resetBuffer();
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().print(e.getMessage());
        } catch (ServletErrorException e) {
            e.printStackTrace();
            resp.resetBuffer();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void needToBeAdmin() throws ServletNoPermissionException {
        if (!user.isAdmin()) {
            throw new ServletNoPermissionException("You have not he permission.");
        }
    }

    protected void needToBeConnected() throws ServletNotConnectedException {
        if (user == null) {
            throw new ServletNotConnectedException("You must be connected.");
        }
    }

    protected void needToBeNotConnected() throws ServletNotConnectedException {
        if (user != null) {
            throw new ServletNotConnectedException("You must be not connected.");
        }
    }

    protected String getParameter(String key) throws ServletWrongParameterException {
        String param = null;
        if ((param = request.getParameter(key)) == null) {
            throw new ServletWrongParameterException("Parameter '" + key + "' is not defined.");
        } else {
            return param;
        }
    }
}
