package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.ServletManager2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 31/05/2017.
 */
@WebServlet("/api/v1/teams/EditTeamUserUsername")
public class ServletEditTeamUserUsername extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager2 sm = new ServletManager2(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            String username = sm.getStringParam("username", true);
            if (username == null || username.equals(""))
                throw new HttpServletException(ServletManager2.HttpStatus.BadRequest, "Empty username.");
            for (TeamUser teamUser1 : team.getTeamUsers()) {
                if (teamUser1 == teamUser)
                    continue;
                if (teamUser1.getUsername().equals(username))
                    throw new HttpServletException(ServletManager2.HttpStatus.BadRequest, "Username already taken.");
            }
            teamUser.editUsername(username);
            sm.setSuccess("TeamUser username edited, new username.");
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
