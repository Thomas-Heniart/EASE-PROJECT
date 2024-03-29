package com.Ease.API.V1.Common;

import com.Ease.Metrics.EaseEvent;
import com.Ease.Metrics.EaseEventFactory;
import com.Ease.User.Options;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/SetBackgroundPicture")
public class ServletSetBackgroundPicture extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Boolean active = sm.getBooleanParam("active", true, false);
            Options options = sm.getUser().getOptions();
            options.setBackground_picked(active);
            sm.saveOrUpdate(options);
            EaseEvent easeEvent = EaseEventFactory.getInstance().createBackgroundPictureEvent(sm.getUser().getDb_id(), "Dashboard", active);
            sm.getTrackingHibernateQuery().saveOrUpdateObject(easeEvent);
            sm.setSuccess("Background picture edited");
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
