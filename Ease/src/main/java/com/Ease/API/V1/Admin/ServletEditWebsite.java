package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Category;
import com.Ease.Catalog.Sso;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.AnyApp;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/api/v1/admin/EditWebsite")
public class ServletEditWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer id = sm.getIntParam("id", true, false);
            String name = sm.getStringParam("name", true, false);
            String folder = sm.getStringParam("folder", true, false);
            String login_url = sm.getStringParam("login_url", true, false);
            String landing_url = sm.getStringParam("landing_url", true, false);
            JSONArray teams = sm.getArrayParam("teams", false, false);
            JSONArray connectWith_ids = sm.getArrayParam("connectWith", false, false);
            Integer sso_id = Integer.valueOf(sm.getStringParam("sso_id", true, false));
            Integer category_id = sm.getIntParam("category_id", true, false);
            Boolean integrated = sm.getBooleanParam("integrated", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Website website = catalog.getWebsiteWithId(id, hibernateQuery);
            website.setName(name);
            website.setFolder(folder);
            website.setLogin_url(login_url);
            website.setWebsite_homepage(landing_url);
            website.getWebsiteAttributes().setIntegrated(integrated);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            for (Team team : website.getTeams()) {
                team.removeTeamWebsite(website);
                sm.saveOrUpdate(team);
            }
            website.setTeams(ConcurrentHashMap.newKeySet());
            for (int i = 0; i < teams.length(); i++) {
                Integer team_id = teams.getInt(i);
                Team team = teamManager.getTeam(team_id, sm.getHibernateQuery());
                team.addTeamWebsite(website);
                website.addTeam(team);
                sm.saveOrUpdate(team);
            }
            Sso sso = null;
            if (sso_id != -1)
                sso = catalog.getSsoWithId(sso_id, hibernateQuery);
            website.setSso(sso);
            Category category = null;
            if (category_id != -1) {
                category = catalog.getCategoryWithId(category_id, hibernateQuery);
                category.addWebsite(website);
            }
            website.setCategory(category);
            website.setConnectWith_websites(ConcurrentHashMap.newKeySet());
            for (int i = 0; i < connectWith_ids.length(); i++) {
                Integer connectWith_id = connectWith_ids.getInt(i);
                Website connectWith = catalog.getWebsiteWithId(connectWith_id, hibernateQuery);
                website.addConnectWith_website(connectWith);
                connectWith.addSignIn_website(website);
                sm.saveOrUpdate(connectWith);
            }
            sm.saveOrUpdate(website.getWebsiteAttributes());
            sm.saveOrUpdate(website);
            website.getTeamWebsiteCardSet().forEach(teamWebsiteCard -> {
                if (teamWebsiteCard.getName().equals(name)) {
                    teamWebsiteCard.setName(name);
                    sm.saveOrUpdate(teamWebsiteCard);
                }
            });
            website.getWebsiteAppSet().forEach(websiteApp -> {
                if (websiteApp.getAppInformation().getName().equals(name)) {
                    websiteApp.getAppInformation().setName(name);
                    sm.saveOrUpdate(websiteApp.getAppInformation());
                }
                if (websiteApp.isAnyApp() && website.getWebsiteAttributes().isIntegrated()) {
                    AnyApp anyApp = (AnyApp) websiteApp;
                    App tmp_app = new ClassicApp(anyApp.getAppInformation(), website, anyApp.getAccount());
                    tmp_app.setProfile(anyApp.getProfile());
                    tmp_app.setPosition(anyApp.getPosition());
                    sm.saveOrUpdate(tmp_app);
                    /* @TODO WebSocket message here */
                }
            });
            if (website.getWebsiteAttributes().isIntegrated()) {
                hibernateQuery.queryString("DELETE FROM AnyApp a WHERE a.website.db_id = :id");
                hibernateQuery.setParameter("id", website.getDb_id());
                hibernateQuery.executeUpdate();
            }
            sm.setSuccess("Catalog edited");
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
