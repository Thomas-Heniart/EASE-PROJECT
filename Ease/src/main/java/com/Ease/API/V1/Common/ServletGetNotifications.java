package com.Ease.API.V1.Common;

import com.Ease.User.NotificationManager;
import com.Ease.User.User;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/GetNotifications")
public class ServletGetNotifications extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer offset = sm.getIntParam("offset", true);
            User user = sm.getUser();
            NotificationManager notificationManager = sm.getUserNotificationManager(user.getDb_id());
            int limit = offset + 10;
            sm.setSuccess(notificationManager.getJson(offset, limit, user, sm.getHibernateQuery()));
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
