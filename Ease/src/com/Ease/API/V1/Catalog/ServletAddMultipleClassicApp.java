package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.NewDashboard.*;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
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
import java.util.*;

@WebServlet("/api/v1/catalog/AddMultipleClassicApp")
public class ServletAddMultipleClassicApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user == null)
                user = sm.getUserWithToken();
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = user.getDashboardManager().getProfile(profile_id);
            JSONArray apps_to_add = sm.getArrayParam("apps_to_add", false, false);
            JSONArray apps = new JSONArray();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            DataBaseConnection db = sm.getDB();
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            String private_key = (String) sm.getContextAttr("privateKey");
            for (Object entry : account_information.entrySet()) {
                Map.Entry<String, String> accountInformation = (Map.Entry<String, String>) entry;
                account_information.put(accountInformation.getKey(), RSA.Decrypt(accountInformation.getValue(), private_key));
            }
            List<App> appList = new LinkedList<>();
            for (Object app_to_add : apps_to_add) {
                JSONObject websiteAndName = (JSONObject) app_to_add;
                String name = (String) websiteAndName.get("name");
                Integer website_id = Math.toIntExact((Long) websiteAndName.get("website_id"));
                Website website = catalog.getPublicWebsiteWithId(website_id);
                Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
                Set<AccountInformation> accountInformationSet = new HashSet<>();
                Map<String, String> information = website.getInformationNeeded(account_information);
                for (Map.Entry<String, String> entry : information.entrySet())
                    accountInformationSet.add(new AccountInformation(entry.getKey(), RSA.Encrypt(entry.getValue(), public_and_private_key.getKey()), entry.getValue()));
                Account account = new Account(0, public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), user.getKeys().getKeyUser()), accountInformationSet, public_and_private_key.getValue());
                accountInformationSet.stream().forEach(accountInformation -> accountInformation.setAccount(account));
                AppInformation appInformation = new AppInformation(name);
                com.Ease.NewDashboard.ClassicApp classicApp = new com.Ease.NewDashboard.ClassicApp(appInformation, website, account);
                classicApp.setProfile(profile);
                classicApp.setPosition(profile.getAppMap().size());
                sm.saveOrUpdate(classicApp);
                profile.addApp(classicApp);
                user.getDashboardManager().addApp(classicApp);
                appList.add(classicApp);

            }
            for (App app : appList)
                apps.add(app.getJson());
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
