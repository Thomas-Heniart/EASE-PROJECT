package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.User.Notification;
import com.Ease.User.NotificationFactory;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketManager;
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
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT publicKey FROM userKeys JOIN users ON users.key_id = userKeys.id WHERE users.id = ?");
            hibernateQuery.setParameter(1, teamUser.getUser().getDb_id());
            String publicKey = (String) hibernateQuery.getSingleResult();
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            teamUser.setTeamKey(RSA.Encrypt(teamKey, publicKey));
            String keyUser = (String) sm.getUserProperties(teamUser.getUser().getDb_id()).get("keyUser");
            if (keyUser != null)
                teamUser.finalizeRegistration(keyUser, teamUser.getUser().getUserKeys().getDecipheredPrivateKey(keyUser), sm.getHibernateQuery());
            sm.saveOrUpdate(teamUser);
            Notification notification = NotificationFactory.getInstance().createNotification(teamUser.getUser(), teamUser_connected.getUsername() + " validated your access to " + team.getName(), "/resources/notifications/flag.png", team.getDb_id().toString());
            sm.saveOrUpdate(notification);
            WebSocketManager webSocketManager = sm.getUserWebSocketManager(teamUser.getUser().getDb_id());
            webSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getWebSocketJson()));
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
