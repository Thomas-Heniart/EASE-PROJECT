package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Servlets.PostServletManager;

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
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Integer app_id = sm.getIntParam("app_id", true);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            shareableApp.addPendingTeamUser(teamUser, sm.getDB());
            shareableApp.getTeamUser_owner().addNotification(shareableApp.getTeamUser_owner().getUsername() + " would like to have access to " + ((App) shareableApp).getName(), sm.getTimestamp());
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
