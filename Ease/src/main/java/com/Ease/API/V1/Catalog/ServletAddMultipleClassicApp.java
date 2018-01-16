package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.AppFactory;
import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/catalog/AddMultipleClassicApp")
public class ServletAddMultipleClassicApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user == null)
                user = sm.getUserWithToken();
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = user.getProfile(profile_id);
            JSONArray apps_to_add = sm.getArrayParam("apps_to_add", false, false);
            JSONArray apps = new JSONArray();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            DataBaseConnection db = sm.getDB();
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            String private_key = (String) sm.getContextAttr("privateKey");
            for (Object object : account_information.keySet()) {
                String key = (String) object;
                String value = account_information.getString(key);
                account_information.put(key, RSA.Decrypt(value, private_key));
            }
            List<App> appList = new LinkedList<>();
            for (int i = 0; i < apps_to_add.length(); i++) {
                JSONObject websiteAndName = apps_to_add.getJSONObject(i);
                String name = (String) websiteAndName.get("name");
                Integer website_id = Math.toIntExact((Long) websiteAndName.get("website_id"));
                Website website = catalog.getPublicWebsiteWithId(website_id, sm.getHibernateQuery(), user.getTeams());
                Map<String, String> information = website.getInformationNeeded(account_information);
                String keyUser = sm.getKeyUser();
                App app = AppFactory.getInstance().createClassicApp(name, website, keyUser, information, 0, sm.getHibernateQuery());
                app.setProfile(profile);
                app.setPosition(profile.getSize());
                sm.saveOrUpdate(app);
                profile.addApp(app);
                appList.add(app);
            }
            for (App app : appList)
                apps.put(app.getJson());
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
