package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/MoveApp")
public class ServletMoveApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Integer position = sm.getIntParam("position", true, false);
            User user = sm.getUser();
            App app = user.getApp(app_id, sm.getHibernateQuery());
            Profile old_profile = app.getProfile();
            Profile new_profile = user.getProfile(profile_id);
            if (old_profile == new_profile)
                old_profile.updateAppPositions(app, position, sm.getHibernateQuery());
            else {
                old_profile.removeAppAndUpdatePositions(app, sm.getHibernateQuery());
                new_profile.addAppAndUpdatePositions(app, position, sm.getHibernateQuery());
            }
            sm.setSuccess(app.getJson());
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
