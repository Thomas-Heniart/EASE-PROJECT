package com.Ease.API.V1.Tracking;

import com.Ease.Metrics.EaseEvent;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/trackEvent")
public class ServletTracking extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String name = sm.getStringParam("name", true, false);
            JSONObject data = sm.getJsonParam("data", true, false);
            EaseEvent easeEvent = new EaseEvent();
            easeEvent.setName(name);
            easeEvent.setData(data.toString());
            easeEvent.setUser_id(sm.getUser().getDb_id());
            sm.getTrackingHibernateQuery().saveOrUpdateObject(easeEvent);
            sm.setSuccess("Success");
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
