package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
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

@WebServlet("/api/v1/catalog/AddClassicAppSameAs")
public class ServletAddClassicAppSameAs extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer website_id = sm.getIntParam("website_id", true, false);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() >= 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            Integer app_id = sm.getIntParam("same_app_id", true, false);
            App same_app = user.getDashboardManager().getAppWithId(app_id);
            if (!same_app.isClassicApp())
                throw new HttpServletException(HttpStatus.BadRequest, "Please provide a classic app");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id);
            Profile profile = user.getDashboardManager().getProfileWithId(profile_id);
            App app = ClassicApp.createClassicAppSameAs(profile, profile.getApps().size(), name, website, (ClassicApp) same_app, sm.getDB(), user);
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
