package com.Ease.API.V1.Admin;

import com.Ease.Catalog.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/GetWebsites")
public class ServletGetWebsites extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            JSONArray res = new JSONArray();
            for (Website website : catalog.getWebsites(sm.getHibernateQuery())) {
                JSONObject tmp = new JSONObject();
                tmp.put("id", website.getDb_id());
                tmp.put("name", website.getName());
                tmp.put("logo", website.getLogo());
                tmp.put("folder", website.getFolder());
                tmp.put("login_url", website.getLogin_url());
                tmp.put("landing_url", website.getWebsite_homepage());
                tmp.put("public", website.getWebsiteAttributes().isPublic_website());
                tmp.put("integrated", website.getWebsiteAttributes().isIntegrated());
                JSONArray teams = new JSONArray();
                for (Team team : website.getTeams()) {
                    JSONObject teamObj = new JSONObject();
                    teamObj.put("id", team.getDb_id());
                    teamObj.put("name", team.getName());
                    teams.put(teamObj);
                }
                tmp.put("teams", teams);
                Sso sso = website.getSso();
                tmp.put("sso", (sso == null) ? -1 : sso.getDb_id());
                tmp.put("category_id", website.getCategory() == null ? -1 : website.getCategory().getDb_id());
                JSONArray connectWith = new JSONArray();
                for (Website website1 : website.getConnectWith_websites())
                    connectWith.put(website1.getDb_id());
                tmp.put("connectWith", connectWith);
                JSONObject website_credentials = null;
                for (WebsiteCredentials websiteCredentials : website.getWebsiteCredentials())
                    website_credentials = websiteCredentials.getJson();
                if (website_credentials != null)
                    tmp.put("website_credentials", website_credentials);
                JSONArray alternative_urls = new JSONArray();
                website.getWebsiteAlternativeUrlSet().forEach(websiteAlternativeUrl -> alternative_urls.put(websiteAlternativeUrl.getJson()));
                res.put(tmp);
            }
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
