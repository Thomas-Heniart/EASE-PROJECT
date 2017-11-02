package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.NewDashboard.*;
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

@WebServlet("/api/v1/catalog/AddLogWithApp")
public class ServletAddLogWithApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String name = sm.getStringParam("name", true, false);
            if (name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Name too long");
            Integer website_id = sm.getIntParam("website_id", true, false);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Integer logWith_app_id = sm.getIntParam("logWith_app_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getPublicWebsiteWithId(website_id);
            Profile profile = user.getDashboardManager().getProfile(profile_id);
            App logWithApp = user.getDashboardManager().getApp(logWith_app_id);
            if (!logWithApp.isWebsiteApp())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot login with this app.");
            WebsiteApp websiteApp = (WebsiteApp) logWithApp;
            if (!website.getConnectWith_websites().contains(websiteApp.getWebsite()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot login with this app.");
            AppInformation appInformation = new AppInformation(name);
            LogWithApp app = new LogWithApp(appInformation, website, websiteApp);
            app.setProfile(profile);
            app.setPosition(profile.getAppMap().size());
            sm.saveOrUpdate(app);
            profile.addApp(app);
            user.getDashboardManager().addApp(app);
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
