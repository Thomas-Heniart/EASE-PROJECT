package com.Ease.API.V1.Teams;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 09/06/2017.
 */
@WebServlet("/api/v1/teams/DeleteChannel")
public class ServletDeleteChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            Channel channel = team.getChannelWithId(channel_id);
            if (!teamUser.isTeamOwner() && channel.getRoom_manager() != teamUser)
                throw new HttpServletException(HttpStatus.Forbidden, "Only room manager and owner can delete a room.");
            if (channel.getName().equals("openspace"))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot modify this channel.");
            channel.delete();
            team.removeChannel(channel);
            sm.deleteObject(channel);
            sm.saveOrUpdate(team);
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("team_id", team_id);
            ws_obj.put("room_id", channel_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_ROOM, WebSocketMessageAction.REMOVED, ws_obj));
            sm.setSuccess("Channel delete");
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
