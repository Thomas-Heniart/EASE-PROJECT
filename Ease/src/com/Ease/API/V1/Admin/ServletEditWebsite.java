package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Category;
import com.Ease.Catalog.Sso;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessage;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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
            JSONArray teams = (JSONArray) sm.getParam("teams", false, false);
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
            for (Object team_id : teams) {
                Team team = teamManager.getTeam(Math.toIntExact((Long) team_id), sm.getHibernateQuery());
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
            for (Object connectWith_id : connectWith_ids) {
                Website connectWith = catalog.getWebsiteWithId(Math.toIntExact((Long) connectWith_id), hibernateQuery);
                website.addConnectWith_website(connectWith);
                connectWith.addSignIn_website(website);
                sm.saveOrUpdate(connectWith);
            }
            sm.saveOrUpdate(website.getWebsiteAttributes());
            sm.saveOrUpdate(website);
            List<WebSocketMessage> webSocketMessageList = new LinkedList<>();
            if (website.getWebsiteAttributes().isIntegrated()) {
                for (Team team : teamManager.getTeams(sm.getHibernateQuery())) {
                    /* for (ShareableApp shareableApp : team.getAppManager().getShareableApps().values()) {
                        App app = (App) shareableApp;
                        if (!app.isClassicApp() && !app.isEmpty())
                            continue;
                        WebsiteApp websiteApp = (WebsiteApp) app;
                        if (website != websiteApp.getSite())
                            continue;
                        String old_name = app.getName();
                        app.setName(website.getName(), db);
                        for (SharedApp sharedApp : shareableApp.getSharedApps().values()) {
                            App app1 = (App) sharedApp;
                            if (app1.getName().equals(old_name))
                                app1.setName(website.getName(), db);
                        }
                        JSONObject target = shareableApp.getOrigin();
                        target.put("team_id", team.getDb_id());
                        team.getWebSocketManager().sendObject(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), target));
                    } */
                    System.out.println(webSocketMessageList.size() + " messages send");
                    sm.getTeamWebSocketManager(team.getDb_id()).sendObjects(webSocketMessageList);
                    webSocketMessageList.clear();
                }
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
