package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Team.Team;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;

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
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            JSONArray jsonArray = new JSONArray();
            if (search == null)
                search = "";
            for (Website website : catalog.getWebsites(sm.getHibernateQuery())) {
                if (website.getWebsiteInformationList().isEmpty())
                    continue;
                if (search.equals("") || (website.getName().toLowerCase().startsWith(search.toLowerCase()) && website.getWebsiteAttributes().isIntegrated())) {
                    if (team.getTeamWebsites().contains(website) || website.getWebsiteAttributes().isPublic_website())
                        jsonArray.put(website.getSimpleJson());
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
