package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
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

@WebServlet("/api/v1/teams/DeleteJoinAppRequest")
public class ServletDeleteJoinAppRequest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            Integer pending_teamUser_id = sm.getIntParam("team_user_id", true, false);
            Integer app_id = sm.getIntParam("app_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            shareableApp.removePendingTeamUser(team.getTeamUserWithId(pending_teamUser_id), sm.getDB());
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
