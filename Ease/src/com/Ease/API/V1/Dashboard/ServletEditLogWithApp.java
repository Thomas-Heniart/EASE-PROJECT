package com.Ease.API.V1.Dashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.LogWithApp;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/EditLogWithApp")
public class ServletEditLogWithApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            User user = sm.getUser();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            App app = user.getApp(app_id, hibernateQuery);
            if (app == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This app does not exist");
            Profile profile = app.getProfile();
            if (app.getTeamCardReceiver() != null || (profile != null && !profile.getUser().equals(user)))
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!app.isLogWithApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            LogWithApp logWithApp = (LogWithApp) app;
            Integer logWith_app_id = sm.getIntParam("logWithApp_id", true, false);
            App connectWith_app = user.getApp(logWith_app_id, hibernateQuery);
            Profile profile1 = app.getProfile();
            if ((profile1 != null && !profile1.getUser().equals(user)))
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!connectWith_app.isWebsiteApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            if (((WebsiteApp) connectWith_app).getWebsite() != logWithApp.getLogWith_website())
                logWithApp.setLogWith_website(((WebsiteApp) connectWith_app).getWebsite());
            logWithApp.setLoginWith_app((WebsiteApp) connectWith_app);
            sm.saveOrUpdate(logWithApp);
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
