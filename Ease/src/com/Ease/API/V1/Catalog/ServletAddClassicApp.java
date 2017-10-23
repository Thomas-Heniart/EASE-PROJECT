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
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/catalog/AddClassicApp")
public class ServletAddClassicApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user == null)
                user = sm.getUserWithToken();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String name = sm.getStringParam("name", true, false);
            if (name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Name too long");
            Integer website_id = sm.getIntParam("website_id", true, false);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Website website = catalog.getPublicWebsiteWithId(website_id);
            Profile profile = user.getDashboardManager().getProfileWithId(profile_id);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            /* @TODO decipher account information */
            Map<String, String> information = website.getInformationNeeded(account_information);
            App app = ClassicApp.createClassicApp(profile, profile.getApps().size(), name, website, information, user, sm.getDB());
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
