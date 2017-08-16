package com.Ease.API.V1.Common;

import com.Ease.Notification.NotificationManager;
import com.Ease.Utils.DataBaseConnection;
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
            NotificationManager notificationManager = sm.getUser().getNotificationManager();
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            int limit = offset + 5;
            notificationManager.loadNextNotifications(limit, db);
            db.commitTransaction(transaction);
            sm.setSuccess(notificationManager.getJson(offset, limit));
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
