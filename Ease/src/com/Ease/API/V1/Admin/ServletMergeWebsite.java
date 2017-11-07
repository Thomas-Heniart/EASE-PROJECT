package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/admin/MergeWebsite")
public class ServletMergeWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer id = Integer.parseInt(sm.getStringParam("id", true, false));
            Integer id_to_merge = sm.getIntParam("id_to_merge", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(id);
            Website website_to_merge = catalog.getWebsiteWithId(id_to_merge);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            List<WebSocketMessage> webSocketMessageList = new LinkedList<>();
            DataBaseConnection db = sm.getDB();
            Map<String, User> userMap = (Map<String, User>) sm.getContextAttr("users");
            int transaction2 = db.startTransaction();
            for (Team team : teamManager.getTeams()) {
                System.out.println(webSocketMessageList.size() + " messages send");
                team.getWebSocketManager().sendObjects(webSocketMessageList);
                webSocketMessageList.clear();
            }
            for (User user : userMap.values()) {
                for (App app : user.getDashboardManager().getAppMap().values()) {
                    if (!app.isWebsiteApp())
                        continue;
                    WebsiteApp websiteApp = (WebsiteApp) app;
                    if (!website_to_merge.equals(websiteApp.getWebsite()))
                        continue;
                    websiteApp.setWebsite(website);
                    sm.saveOrUpdate(app);
                }
            }
            DatabaseRequest databaseRequest = db.prepareRequest("UPDATE websiteApps SET website_id = ? WHERE website_id = ?;");
            databaseRequest.setInt(website.getDb_id());
            databaseRequest.setInt(website_to_merge.getDb_id());
            databaseRequest.set();
            catalog.removeWebsite(website_to_merge.getDb_id());
            sm.deleteObject(website_to_merge);
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
