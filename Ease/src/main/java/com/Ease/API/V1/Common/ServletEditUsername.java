package com.Ease.API.V1.Common;

import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/EditUsername")
public class ServletEditUsername extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String username = sm.getStringParam("username", true, false);
            username = username.toLowerCase();
            if (!Regex.isValidUsername(username))
                throw new HttpServletException(HttpStatus.BadRequest, "You must provide a valid username.");
            sm.getUser().setUsername(username);
            sm.saveOrUpdate(sm.getUser());
            sm.getUser().getCookies().forEach(response::addCookie);
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.updateUserData(sm.getUser());
            sm.setSuccess("Username edited");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
