package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
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
 * Created by thomas on 31/05/2017.
 */
@WebServlet("/api/v1/teams/EditTeamUserUsername")
public class ServletEditTeamUserUsername extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeam(team_id, sm.getHibernateQuery());
            TeamUser teamUser = sm.getTeamUser(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUserToModify = team.getTeamUserWithId(teamUser_id);
            if (!(teamUser.isSuperior(teamUserToModify) || teamUser == teamUserToModify))
                throw new HttpServletException(HttpStatus.Forbidden, "You don't have access.");
            String username = sm.getStringParam("username", true, false);
            checkUsernameIntegrity(username);
            for (TeamUser teamUser1 : team.getTeamUsers().values()) {
                if (teamUser1 == teamUserToModify)
                    continue;
                if (teamUser1.getUsername().equals(username))
                    throw new HttpServletException(HttpStatus.BadRequest, "Username is already taken.");
            }
            teamUserToModify.editUsername(username);
            sm.saveOrUpdate(teamUserToModify);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUserToModify.getJson(), teamUserToModify.getOrigin()));
            sm.setSuccess("TeamUser username edited, new username.");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private String checkUsernameIntegrity(String username) throws HttpServletException {
        if (username == null || username.equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "Usernames can't be empty!");
        if (username.length() < 3 || username.length() >= 22)
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must be greater than 2 characters and fewer than 22 characters.");
        if (!username.equals(username.toLowerCase()) || !Regex.isValidUsername(username))
            throw new HttpServletException(HttpStatus.BadRequest, "Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores");
        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
