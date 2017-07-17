package com.Ease.API.V1.Teams;

import com.Ease.Notification.TeamUserNotification;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/GetTeamUserNotifications")
public class ServletGetTeamUserNotifications extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Integer teamUser_id = sm.getIntParam("team_user_id", true);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            TeamUser teamUserConnected = sm.getTeamUserForTeam(team);
            if (!teamUserConnected.isTeamAdmin() && teamUser != teamUserConnected)
                throw new HttpServletException(HttpStatus.Forbidden, "Get out !");
            JSONArray notifications = new JSONArray();
            for (TeamUserNotification notification : teamUser.getTeamUserNotifications())
                notifications.add(notification.getJson());
            sm.setSuccess(notifications);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
