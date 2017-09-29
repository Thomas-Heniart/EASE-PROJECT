package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
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
import java.util.Map;

@WebServlet("/api/v1/teams/ShareEnterpriseApp")
public class ServletShareEnterpriseApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            Integer app_id = sm.getIntParam("app_id", true, false);
            Integer team_user_id = sm.getIntParam("team_user_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            App app = (App) shareableApp;
            if (!app.isEmpty())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamUser teamUser_tenant = team.getTeamUserWithId(team_user_id);
            Channel channel = shareableApp.getChannel();
            if (!channel.getTeamUsers().contains(teamUser_tenant))
                throw new HttpServletException(HttpStatus.Forbidden, "You can only share this app with people in this room.");
            if (shareableApp.getTeamUser_tenants().contains(teamUser_tenant))
                throw new HttpServletException(HttpStatus.BadRequest, "This user already have this app.");
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            SharedApp sharedApp;
            JSONObject params = new JSONObject();
            JSONObject account_information = sm.getJsonParam("account_information", false, true);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            if (account_information == null && shareableApp.getPendingTeamUsers().contains(teamUser_tenant)) {
                DatabaseRequest databaseRequest = db.prepareRequest("SELECT information_name, information_value FROM enterpriseAppRequests JOIN pendingJoinAppRequests ON enterpriseAppRequests.request_id = pendingJoinAppRequests.id WHERE shareable_app_id = ? AND team_user_id = ?;");
                databaseRequest.setInt(app.getDBid());
                databaseRequest.setInt(teamUser_tenant.getDb_id());
                DatabaseResult rs = databaseRequest.get();
                account_information = new JSONObject();
                while (rs.next())
                    account_information.put(rs.getString(1), AES.decrypt(rs.getString(2), teamUser_connected.getDeciphered_teamKey()));
                params.put("account_information", account_information);
                sharedApp = shareableApp.share(teamUser_tenant, team, params, sm);
                shareableApp.removePendingTeamUser(teamUser_tenant, db);
                if (teamUser_connected != teamUser_tenant) {
                    String url = channel.getDb_id() + "?app_id=" + app.getDBid();
                    teamUser_tenant.addNotification(teamUser_connected.getUsername() + " approved your access to " + ((App) shareableApp).getName() + " in #" + channel.getName(), url, ((App) shareableApp).getLogo(), sm.getTimestamp(), sm.getDB());
                }
                //sharedApp.accept(db);
            } else {
                String privateKey = (String) sm.getContextAttr("privateKey");
                for (Object entry : account_information.entrySet()) {
                    Map.Entry<String, String> key_value = (Map.Entry<String, String>) entry;
                    account_information.put(key_value.getKey(), RSA.Decrypt(key_value.getValue(), privateKey));
                }
                params.put("account_information", account_information);
                sharedApp = shareableApp.share(teamUser_tenant, team, params, sm);
                if (shareableApp.getPendingTeamUsers().contains(teamUser_tenant))
                    shareableApp.removePendingTeamUser(teamUser_tenant, db);
                if (teamUser_tenant == teamUser_connected)
                    sharedApp.accept(db);
                else {
                    String url = channel.getDb_id() + "?app_id=" + app.getDBid();
                    teamUser_tenant.addNotification(teamUser_connected.getUsername() + " sent you " + app.getName() + " in #" + channel.getName(), url, app.getLogo(), sm.getTimestamp(), db);
                }
            }
            db.commitTransaction(transaction);
            shareableApp.addSharedApp(sharedApp);
            team.getAppManager().addSharedApp(sharedApp);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, app.getShareableJson(), app.getOrigin()));
            sm.setSuccess(sharedApp.getSharedJSON());
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
