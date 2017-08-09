package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

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
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            Integer pending_teamUser_id = sm.getIntParam("team_user_id", true);
            Integer app_id = sm.getIntParam("app_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            TeamUser teamUser = sm.getTeamUserForTeamId(team_id);
            if (!teamUser.isTeamAdmin() && shareableApp.getTeamUser_owner() != teamUser)
                throw new HttpServletException(HttpStatus.Forbidden, "Not allowed");
            shareableApp.removePendingTeamUser(team.getTeamUserWithId(pending_teamUser_id), sm.getDB());
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
