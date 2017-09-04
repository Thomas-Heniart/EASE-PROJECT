package com.Ease.API.V1.Admin;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessage;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
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

@WebServlet("/api/v1/admin/MergeWebsite")
public class ServletMergeWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer id = Integer.parseInt(sm.getStringParam("id", true));
            Integer id_to_merge = sm.getIntParam("id_to_merge", true);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(id);
            Website website_to_merge = catalog.getWebsiteWithId(id_to_merge);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            List<WebSocketMessage> webSocketMessageList = new LinkedList<>();
            DataBaseConnection db = sm.getDB();
            int transaction2 = db.startTransaction();
            for (Team team : teamManager.getTeams()) {
                int transaction = db.startTransaction();
                for (ShareableApp shareableApp : team.getAppManager().getShareableApps()) {
                    App app = (App) shareableApp;
                    if (!app.isClassicApp() && !app.isEmpty())
                        continue;
                    WebsiteApp websiteApp = (WebsiteApp) app;
                    if (website_to_merge != websiteApp.getSite())
                        continue;
                    websiteApp.setWebsite(website, db);
                    JSONObject target = shareableApp.getOrigin();
                    target.put("team_id", team.getDb_id());
                    webSocketMessageList.add(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), target));
                }
                db.commitTransaction(transaction);
                System.out.println(webSocketMessageList.size() + " messages send");
                team.getWebSocketManager().sendObjects(webSocketMessageList);
                webSocketMessageList.clear();
            }
            DatabaseRequest databaseRequest = db.prepareRequest("UPDATE websiteApps SET website_id = ? WHERE website_id = ?;");
            databaseRequest.setInt(website.getDb_id());
            databaseRequest.setInt(website_to_merge.getDb_id());
            databaseRequest.set();
            catalog.removeWebsite(website_to_merge.getDb_id(), db);
            db.commitTransaction(transaction2);
            sm.setSuccess("Websites merged");
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
