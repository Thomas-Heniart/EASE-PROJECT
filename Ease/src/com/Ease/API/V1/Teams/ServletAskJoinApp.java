package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 05/05/2017.
 */
@WebServlet("/api/v1/teams/AskJoinApp")
public class ServletAskJoinApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Integer app_id = sm.getIntParam("app_id", true, false);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Channel channel = shareableApp.getChannel();
            if (channel != null && !(channel.getTeamUsers().contains(teamUser)))
                throw new HttpServletException(HttpStatus.Forbidden, "You don't have access to this channel");
            shareableApp.addPendingTeamUser(teamUser, sm.getDB());
            String url = ((channel == null) ? ("@" + teamUser.getDb_id()) : channel.getDb_id().toString()) + "?app_id=" + ((App) shareableApp).getDBid();
            shareableApp.getTeamUser_owner().addNotification(teamUser.getUsername() + " would like to have access to " + ((App) shareableApp).getName(), url, ((App) shareableApp).getLogo(), sm.getTimestamp(), sm.getDB());
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
