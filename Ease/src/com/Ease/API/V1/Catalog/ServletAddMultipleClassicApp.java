package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/api/v1/catalog/AddMultipleClassicApp")
public class ServletAddMultipleClassicApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user == null)
                user = sm.getUserWithToken();
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = user.getDashboardManager().getProfileWithId(profile_id);
            JSONArray apps_to_add = sm.getArrayParam("apps_to_add", false, false);
            JSONArray apps = new JSONArray();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            /* @TODO decipher account information */
            List<App> appList = new LinkedList<>();
            for (Object app_to_add : apps_to_add) {
                JSONObject websiteAndName = (JSONObject) app_to_add;
                String name = (String) websiteAndName.get("name");
                Integer website_id = Math.toIntExact((Long) websiteAndName.get("website_id"));
                Website website = catalog.getPublicWebsiteWithId(website_id);
                appList.add(ClassicApp.createClassicApp(profile, profile.getApps().size(), name, website, website.getInformationNeeded(account_information), user, db));

            }
            db.commitTransaction(transaction);
            for (App app : appList) {
                profile.addApp(app);
                apps.add(app.getJson());
            }
            JSONObject res = new JSONObject();
            res.put("apps", apps);
            sm.setSuccess(res);
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
