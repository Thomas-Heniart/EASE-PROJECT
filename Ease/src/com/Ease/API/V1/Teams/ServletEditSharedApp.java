package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.SharedApp;
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
@WebServlet("/api/v1/teams/EditSharedApp")
public class ServletEditSharedApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            Integer sharedApp_id = sm.getIntParam("app_id", true, false);
            SharedApp sharedApp = team.getAppManager().getSharedApp(sharedApp_id);
            //Integer team_user_id = sm.getIntParam("team_user_id", true);
            if (!teamUser_connected.isTeamAdmin() && !(sm.getTeamUserForTeam(team) == sharedApp.getTeamUser_tenant()))
                throw new HttpServletException(HttpStatus.Forbidden, "You are not allowed to do this.");
            JSONObject params = new JSONObject();
            params.put("account_information", sm.getParam("account_information", false, true));
            params.put("can_see_information", sm.getParam("can_see_information", true, true));
            params.put("url", sm.getStringParam("url", true, true));
            sharedApp.modifyShared(sm.getDB(), params);
            JSONObject target = sharedApp.getHolder().getOrigin();
            target.put("team_id", team_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, sharedApp.getHolder().getShareableJson(), target));
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
