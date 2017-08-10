package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
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

/**
 * Created by thomas on 06/06/2017.
 */
@WebServlet("/api/v1/teams/EditShareableApp")
public class ServletEditShareableApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            Integer shareableApp_id = sm.getIntParam("app_id", true);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(shareableApp_id);
            if ((shareableApp.getTeamUser_owner() != teamUser_connected) && !teamUser_connected.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden, "You are not allowed to do this.");
            JSONObject params = new JSONObject();
            params.put("name", sm.getStringParam("name", true));
            params.put("description", sm.getStringParam("description", true));
            params.put("account_information", sm.getParam("account_information", false));
            params.put("url", sm.getStringParam("url", true));
            String reminderIntervalParam = sm.getStringParam("password_change_interval", true);
            Integer reminderInterval = null;
            if (reminderIntervalParam != null && !reminderIntervalParam.equals(""))
                reminderInterval = Integer.parseInt(reminderIntervalParam);
            params.put("reminderInterval", reminderInterval);
            shareableApp.modifyShareable(sm.getDB(), params, null);
            JSONObject target = shareableApp.getOrigin();
            target.put("team_id", team_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), target));
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
