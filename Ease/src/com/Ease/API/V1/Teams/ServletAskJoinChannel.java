package com.Ease.API.V1.Teams;

import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
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
 * Created by thomas on 05/05/2017.
 */
@WebServlet("/api/v1/teams/AskJoinChannel")
public class ServletAskJoinChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            Integer channel_id = sm.getIntParam("channel_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Channel channel = team.getChannelWithId(channel_id);
            channel.addPendingTeamUser(teamUser, sm.getDB());
            team.getTeamUserWithId(channel.getCreator_id()).addNotification(teamUser.getUsername() + " would like to join " + channel.getName(), "", "", sm.getTimestamp(), sm.getDB());
            SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            mail.sendJoinChannelEmail(team.getName(), channel.getName(), team.getAdministratorsUsernameAndEmail(), teamUser.getUsername(), teamUser.getEmail());
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
