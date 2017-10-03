package com.Ease.API.V1.Admin;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Sso;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessage;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
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
            Integer sso_id = Integer.valueOf(sm.getStringParam("sso_id", true, false));
            Boolean integrated = sm.getBooleanParam("integrated", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(id);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            website.setName(name, db);
            website.setFolder(folder, db);
            website.setLoginUrl(login_url, db);
            website.setLandingUrl(landing_url, db);
            website.setIntegrated(integrated, db);
            website.setTeams(teams, db);
            Sso sso = null;
            if (sso_id != -1)
                sso = catalog.getSsoWithDbId(sso_id);
            website.setSso(sso, db);
            List<WebSocketMessage> webSocketMessageList = new LinkedList<>();
            if (website.isIntegrated()) {
                TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
                for (Team team : teamManager.getTeams()) {
                    int transaction2 = db.startTransaction();
                    for (ShareableApp shareableApp : team.getAppManager().getShareableApps().values()) {
                        App app = (App) shareableApp;
                        if (!app.isClassicApp() && !app.isEmpty())
                            continue;
                        WebsiteApp websiteApp = (WebsiteApp) app;
                        if (website != websiteApp.getSite())
                            continue;
                        JSONObject target = shareableApp.getOrigin();
                        target.put("team_id", team.getDb_id());
                        webSocketMessageList.add(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), target));
                    }
                    db.commitTransaction(transaction2);
                    System.out.println(webSocketMessageList.size() + " messages send");
                    team.getWebSocketManager().sendObjects(webSocketMessageList);
                    webSocketMessageList.clear();
                }
            }
            db.commitTransaction(transaction);
            sm.setSuccess("Website edited");
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
