package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Sso;
import com.Ease.Catalog.Website;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
            for (Website website : catalog.getWebsites()) {
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
                /* for (String team_id : website.getTeam_ids()) {
                    Team team = teamManager.getTeamWithId(Integer.valueOf(team_id));
                    JSONObject teamObj = new JSONObject();
                    teamObj.put("id", team.getDb_id());
                    teamObj.put("name", team.getName());
                    teams.add(teamObj);
                } */
                tmp.put("teams", teams);
                Sso sso = website.getSso();
                tmp.put("sso", (sso == null) ? -1 : sso.getDb_id());
                tmp.put("category_id", website.getCategory() == null ? -1 : website.getCategory().getDb_id());
                JSONArray connectWtih = new JSONArray();
                for (Website website1 : website.getConnectWith_websites())
                    connectWtih.add(website1.getDb_id());
                tmp.put("connectWith", connectWtih);
                res.add(tmp);
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
