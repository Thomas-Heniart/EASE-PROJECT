package com.Ease.API.V1.Teams;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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
@WebServlet("/api/v1/teams/EditChannelName")
public class ServletEditChannelName extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            if (channel.isDefault())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot modify this channel.");
            String name = sm.getStringParam("name", true, false);
            if (name == null || name.equals("") || !Regex.isValidRoomName(name))
                throw new HttpServletException(HttpStatus.BadRequest, "Room names can't contain spaces, periods or most punctuation and must be shorter than 22 characters.");
            for (Channel channel1 : team.getChannels().values()) {
                if (channel1 == channel)
                    continue;
                if (channel1.getName().equals(name))
                    throw new HttpServletException(HttpStatus.BadRequest, "<<" + name + ">> is already used for another room");
            }
            String old_name = channel.getName();
            channel.editName(name);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!old_name.equals(channel.getName())) {
                for (TeamUser teamUser : channel.getTeamUsers()) {
                    if (teamUser.equals(teamUser_connected))
                        continue;
                    NotificationFactory.getInstance().createEditRoomNameNotification(teamUser, channel, old_name, sm.getUserIdMap(), sm.getHibernateQuery());
                }
            }
            sm.saveOrUpdate(channel);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_ROOM, WebSocketMessageAction.CHANGED, channel.getJson(), channel.getOrigin()));
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
