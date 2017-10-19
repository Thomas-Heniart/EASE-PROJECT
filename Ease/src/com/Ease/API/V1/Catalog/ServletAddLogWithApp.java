package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
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
            User user = sm.getUser();
            if (user == null)
                user = sm.getUserWithToken();
            String name = sm.getStringParam("name", true, false);
            Integer website_id = sm.getIntParam("website_id", true, false);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Integer logWith_app_id = sm.getIntParam("logWith_app_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getPublicWebsiteWithId(website_id);
            Profile profile = user.getDashboardManager().getProfileWithId(profile_id);
            App logWithApp = user.getDashboardManager().getAppWithId(logWith_app_id);
            if (logWithApp.isEmpty() || logWithApp.isLinkApp())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot login with this app.");
            WebsiteApp websiteApp = (WebsiteApp) logWithApp;
            if (!website.getConnectWith_websites().contains(websiteApp.getSite()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot login with this app.");
            App app = LogwithApp.createLogwithApp(profile, profile.getApps().size(), name, website, websiteApp, sm.getDB());
            profile.addApp(app);
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
