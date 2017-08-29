package com.Ease.API.V1.Catalog;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 15/05/2017.
 */
@WebServlet("/api/v1/catalog/SearchTeamCatalogApps")
public class ServletSearchTeamCatalogApps extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer team_id = sm.getIntParam("team_id", true);
            String search = sm.getParam("q", true);
            sm.needToBeTeamUserOfTeam(team_id);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            JSONArray jsonArray = new JSONArray();
            if (search != null && !search.equals("")) {
                for (Website website : catalog.getWebsites()) {
                    if (website.isInCatalogForTeam(String.valueOf(team_id)) && website.getName().toLowerCase().startsWith(search.toLowerCase()) && website.isIntegrated())
                        jsonArray.add(website.getSearchJson());
                }
            }
            sm.setSuccess(jsonArray);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
