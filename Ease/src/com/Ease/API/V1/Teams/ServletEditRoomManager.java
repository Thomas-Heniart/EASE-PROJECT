package com.Ease.API.V1.Teams;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
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

@WebServlet("/api/v1/teams/EditRoomManager")
public class ServletEditRoomManager extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            TeamUser teamUser = sm.getTeamUserForTeamId(team_id);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Channel channel = team.getChannelWithId(channel_id);
            TeamUser new_room_manager = team.getTeamUserWithId(sm.getIntParam("team_user_id", true, false));
            if (channel.getRoom_manager() != teamUser && !teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You must be the room manager to transfer the room manager.");
            if (!new_room_manager.isTeamAdmin() || !new_room_manager.isVerified())
                throw new HttpServletException(HttpStatus.Forbidden, "New room manager must be verified and an admin of your team.");
            if (!channel.getTeamUsers().contains(new_room_manager))
                throw new HttpServletException(HttpStatus.Forbidden, "New room manager must be in the room.");
            channel.setRoom_manager(new_room_manager);
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
