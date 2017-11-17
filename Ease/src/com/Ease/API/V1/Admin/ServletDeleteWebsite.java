package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/api/v1/admin/DeleteWebsite")
public class ServletDeleteWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer website_id = sm.getIntParam("id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Map<String, User> userMap = (Map<String, User>) sm.getContextAttr("users");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Website website = catalog.getWebsiteWithId(website_id, sm.getHibernateQuery());
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            website.setTeams(ConcurrentHashMap.newKeySet());
            for (Team team : teamManager.getTeams(sm.getHibernateQuery())) {
                /* for (ShareableApp shareableApp : team.getAppManager().getShareableApps().values()) {
                    App app = (App) shareableApp;
                    if (app.isLinkApp())
                        continue;
                    WebsiteApp websiteApp = (WebsiteApp) app;
                    if (websiteApp.getSite() != website)
                        continue;
                    Integer app_id = app.getDBid();
                    JSONObject target = shareableApp.getOrigin();
                    target.put("team_id", team.getDb_id());
                    team.getAppManager().removeShareableApp(shareableApp, db);
                    team.getWebSocketManager().sendObject(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.REMOVED, app_id, target));
                } */
            }
            for (Team team : website.getTeams())
                team.removeTeamWebsite(website);
            for (User user : userMap.values()) {
                /* Set<App> appSet = new HashSet<>();
                for (App app : user.getDashboardManager().getApps()) {
                    if (app.isLinkApp())
                        continue;
                    WebsiteApp websiteApp = (WebsiteApp) app;
                    if (websiteApp.getSite() != website)
                        continue;
                    appSet.add(app);
                }
                for (App app : appSet)
                    user.getDashboardManager().removeAppWithId(app.getDBid(), db); */
            }
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT * FROM websiteApps WHERE website_id = ?;");
            databaseRequest.setInt(website_id);
            DatabaseResult rs = databaseRequest.get();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                databaseRequest = db.prepareRequest("SELECT account_id FROM classicApps WHERE website_app_id = ?");
                databaseRequest.setInt(id);
                DatabaseResult rs2 = databaseRequest.get();
                databaseRequest = db.prepareRequest("DELETE FROM classicApps WHERE website_app_id = ?");
                databaseRequest.setInt(id);
                databaseRequest.set();
                while (rs2.next()) {
                    databaseRequest = db.prepareRequest("DELETE FROM accountsInformations WHERE account_id = ?");
                    databaseRequest.setInt(rs2.getInt("account_id"));
                    databaseRequest.set();
                    databaseRequest = db.prepareRequest("DELETE FROM accounts WHERE id = ?");
                    databaseRequest.setInt(rs2.getInt("account_id"));
                    databaseRequest.set();
                }
                databaseRequest = db.prepareRequest("DELETE FROM logWithApps WHERE website_app_id = ?");
                databaseRequest.setInt(id);
                databaseRequest.set();
                databaseRequest = db.prepareRequest("DELETE FROM websiteApps WHERE id = ?");
                databaseRequest.setInt(id);
                databaseRequest.set();
                Integer app_id = rs.getInt("app_id");
                databaseRequest = db.prepareRequest("SELECT app_info_id FROM apps WHERE id = ?;");
                databaseRequest.setInt(app_id);
                rs2 = databaseRequest.get();
                databaseRequest = db.prepareRequest("DELETE FROM profileAndAppMap WHERE app_id = ?");
                databaseRequest.setInt(app_id);
                databaseRequest.set();
                databaseRequest = db.prepareRequest("DELETE FROM apps WHERE id = ?");
                databaseRequest.setInt(app_id);
                databaseRequest.set();
                while (rs2.next()) {
                    databaseRequest = db.prepareRequest("DELETE FROM appsInformations WHERE id = ?");
                    databaseRequest.setInt(rs2.getInt("app_info_id"));
                    databaseRequest.set();
                }
            }
            db.commitTransaction(transaction);
            sm.deleteObject(website);
            sm.setSuccess("Done");
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
