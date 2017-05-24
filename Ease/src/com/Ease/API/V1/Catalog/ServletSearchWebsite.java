package com.Ease.API.V1.Catalog;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.AccountInformation;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by thomas on 15/05/2017.
 */
@WebServlet("/api/v1/catalog/SearchWebsite")
public class ServletSearchWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String team_id = sm.getServletParam("team_id", true);
            String search = sm.getServletParam("search", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null.");
            sm.needToBeTeamUserOfTeam(team_id);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            JSONArray jsonArray = new JSONArray();
            if (search != null && !search.equals("")) {
                for (Website website : catalog.getWebsitesAlphabetically()) {
                    if (website.isInCatalogForTeam(team_id) && website.getName().toLowerCase().startsWith(search.toLowerCase()) && website.work()) {
                        jsonArray.add(website.getSimpleJson());
                        /* Get apps from dashboard with this websites */
                        List<ClassicApp> dashboardApps = user.getDashboardManager().findClassicAppsWithWebsite(website);
                        for (ClassicApp classicApp : dashboardApps) {
                            JSONObject tmp = website.getSimpleJson();
                            tmp.put("profileName", classicApp.getProfile().getName());
                            JSONArray accountInformationArray = new JSONArray();
                            for (AccountInformation accountInformation : classicApp.getAccount().getAccountInformations())
                                accountInformationArray.add(accountInformation.getJson());
                            tmp.put("accountInformation", accountInformationArray);
                            jsonArray.add(tmp);
                        }
                    }
                }
            }
            sm.setResponse(ServletManager.Code.Success, jsonArray.toString());
            sm.setLogResponse("Search done");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
