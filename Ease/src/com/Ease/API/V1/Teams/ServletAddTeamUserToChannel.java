package com.Ease.API.V1.Teams;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.Notification;
import com.Ease.User.NotificationFactory;
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

/**
 * Created by thomas on 06/06/2017.
 */
@WebServlet("/api/v1/teams/AddTeamUserToChannel")
public class ServletAddTeamUserToChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            channel.addTeamUser(teamUser);
            sm.saveOrUpdate(channel);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (teamUser != teamUser_connected) {
                Notification notification = NotificationFactory.getInstance().createNotification(teamUser.getUser(), teamUser_connected.getUsername() + " added you in #" + channel.getName(), "/resources/notifications/channel.png", channel);
                sm.saveOrUpdate(notification);
                WebSocketManager webSocketManager = sm.getUserWebSocketManager(teamUser.getUser().getDb_id());
                webSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
            }
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_ROOM, WebSocketMessageAction.CHANGED, channel.getJson(), channel.getOrigin()));
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getJson(), teamUser.getOrigin()));
            sm.setSuccess(channel.getJson());
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
