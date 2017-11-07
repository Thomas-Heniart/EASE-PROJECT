package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/ReactivateTeamUser")
public class ServletReactivateTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            if (!teamUser.isDisabled())
                throw new HttpServletException(HttpStatus.BadRequest, "This user isn't disabled");
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            String team_key = teamUser_connected.getDeciphered_teamKey();
            if (teamUser.getDashboard_user() != null) {
                teamUser.setTeamKey(teamUser.getDashboard_user().encrypt(team_key));
                teamUser.decipher_teamKey();
                teamUser.setDisabled(false);
            } else {
                HibernateQuery hibernateQuery = sm.getHibernateQuery();
                hibernateQuery.querySQLString("SELECT publicKey FROM userKeys JOIN users ON users.key_id = userKeys.id WHERE users.id = ?");
                hibernateQuery.setParameter(1, teamUser.getUser_id());
                String publicKey = (String) hibernateQuery.getSingleResult();
                teamUser.setTeamKey(RSA.Encrypt(team_key, publicKey));
            }
            teamUser.addNotification("@" + teamUser_connected.getUsername() + " validated again your access to " + team.getName(), team.getDb_id().toString(), "/resources/notifications/flag.png", sm.getTimestamp(), sm.getDB());
            sm.saveOrUpdate(teamUser);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getJson(), teamUser.getOrigin()));
            sm.setSuccess(teamUser.getJson());
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
