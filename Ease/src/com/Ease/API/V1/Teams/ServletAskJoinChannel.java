package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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
 * Created by thomas on 05/05/2017.
 */
@WebServlet("/api/v1/teams/AskJoinChannel")
public class ServletAskJoinChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            Channel channel = team.getChannelWithId(channel_id);
            channel.addPendingTeamUser(teamUser);
            sm.saveOrUpdate(channel);
            NotificationFactory.getInstance().createAskJoinChannelNotification(teamUser, channel, sm.getUserWebSocketManager(channel.getRoom_manager().getUser().getDb_id()), sm.getHibernateQuery());
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
            TeamUser room_manager = channel.getRoom_manager();
            mailJetBuilder.addTo(room_manager.getEmail(), room_manager.getUsername());
            mailJetBuilder.setTemplateId(210939);
            mailJetBuilder.addVariable("room_name", channel.getName());
            mailJetBuilder.addVariable("team_name", team.getName());
            mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
            mailJetBuilder.addVariable("last_name", teamUser.getLastName());
            mailJetBuilder.addVariable("teamUser", teamUser.getUsername());
            mailJetBuilder.addVariable("link", Variables.URL_PATH + "#/teams" + team.getDb_id() + "/" + channel.getDb_id() + "/flexPanel");
            mailJetBuilder.sendEmail();
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("team_id", team_id);
            ws_obj.put("team_user_id", teamUser.getDb_id());
            ws_obj.put("room_id", channel.getDb_id());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_ROOM_REQUEST, WebSocketMessageAction.CREATED, ws_obj));
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
