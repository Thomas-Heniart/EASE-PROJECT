package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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

@WebServlet("/api/v1/teams/JoinEnterpriseApp")
public class ServletJoinEnterpriseApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            Integer app_id = sm.getIntParam("app_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            App app = (App) shareableApp;
            if (!app.isEmpty())
                throw new HttpServletException(HttpStatus.Forbidden);
            Channel channel = shareableApp.getChannel();
            if (!channel.getTeamUsers().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.Forbidden, "You are not of channel of this app.");
            if (shareableApp.getTeamUser_tenants().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.BadRequest, "You already have this app.");
            if (shareableApp.getPendingTeamUsers().containsKey(teamUser_connected.getDb_id()))
                throw new HttpServletException(HttpStatus.BadRequest, "You already ask to join this app.");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            String privateKey = (String) sm.getContextAttr("privateKey");
            for (Object entry : account_information.entrySet()) {
                Map.Entry<String, String> key_value = (Map.Entry<String, String>) entry;
                account_information.put(key_value.getKey(), RSA.Decrypt(key_value.getValue(), privateKey));
            }
            JSONObject params = new JSONObject();
            params.put("account_information", account_information);
            params.put("team_key", teamUser_connected.getDeciphered_teamKey());
            shareableApp.addPendingTeamUser(teamUser_connected, params, sm.getDB());
            channel.getRoom_manager().addNotification(teamUser_connected.getUsername() + " would like to have access to " + app.getName(), channel.getDb_id().toString() + "?app_id=" + app.getDBid(), app.getLogo(), sm.getTimestamp(), sm.getDB());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), shareableApp.getOrigin()));
            sm.setSuccess(shareableApp.getShareableJson());
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
