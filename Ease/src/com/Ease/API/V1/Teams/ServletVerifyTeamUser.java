package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
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

@WebServlet("/api/v1/teams/VerifyTeamUser")
public class ServletVerifyTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeAdminOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT publicKey FROM userKeys JOIN users ON users.key_id = userKeys.id WHERE users.id = ?");
            hibernateQuery.setParameter(1, teamUser.getUser_id());
            String publicKey = (String) hibernateQuery.getSingleResult();
            teamUser.setTeamKey(RSA.Encrypt(teamUser_connected.getDeciphered_teamKey(), publicKey));
            if (teamUser.getDashboard_user() != null)
                teamUser.finalizeRegistration();
            sm.saveOrUpdate(teamUser);
            teamUser.addNotification(teamUser_connected.getUsername() + " validated your access to " + team.getName(), "", "/resources/notifications/flag.png", sm.getTimestamp(), sm.getDB());
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
