package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.Notification;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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

@WebServlet("/api/v1/teams/ReactivateTeamUser")
public class ServletReactivateTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            if (!(teamUser_connected.isTeamAdmin() || teamUser_connected.isTeamOwner() || teamUser_connected.getDb_id().equals(teamUser.getAdmin_id())))
                throw new HttpServletException(HttpStatus.BadRequest, "You don't have this right");
            if (!teamUser.isDisabled())
                throw new HttpServletException(HttpStatus.BadRequest, "This user isn't disabled");
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            if (teamUser.getUser() == null || !teamUser.isRegistered())
                throw new HttpServletException(HttpStatus.BadRequest, "This user is not registered");
            String keyUser = (String) sm.getUserProperties(teamUser.getUser().getDb_id()).get("keyUser");
            if (keyUser == null) {
                String publicKey = teamUser.getUser().getUserKeys().getPublicKey();
                teamUser.setTeamKey(RSA.Encrypt(teamKey, publicKey));
            } else {
                teamUser.setTeamKey(AES.encrypt(teamKey, keyUser));
                teamUser.setState(2);
                teamUser.setDisabled(false);
            }
            Notification notification = NotificationFactory.getInstance().createNotification(teamUser.getUser(), teamUser_connected.getUsername() + " validated again your access to " + team.getName(), "/resources/notifications/flag.png", "#/teams" + team.getDb_id().toString());
            sm.saveOrUpdate(notification);
            WebSocketManager webSocketManager = sm.getUserWebSocketManager(teamUser.getUser().getDb_id());
            webSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
            sm.saveOrUpdate(teamUser);
            WebSocketManager webSocketManager1 = sm.getTeamWebSocketManager(team_id);
            webSocketManager1.sendObject(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getWebSocketJson()));
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
