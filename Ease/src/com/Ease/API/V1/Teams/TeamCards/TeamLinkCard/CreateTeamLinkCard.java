package com.Ease.API.V1.Teams.TeamCards.TeamLinkCard;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.AppFactory;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamLinkCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/CreateTeamLinkCard")
public class CreateTeamLinkCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!channel.getTeamUsers().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.Forbidden, "You must be part of the room.");
            String url = sm.getStringParam("url", true, false);
            if (url.length() >= 2000 || url.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            String img_url = sm.getStringParam("img_url", true, false);
            if (img_url.length() >= 2000 || img_url.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter img_url");
            String name = sm.getStringParam("name", true, false);
            if (name.length() >= 255 || name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            String description = sm.getStringParam("description", true, true);
            if (description != null && description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            JSONArray receivers = sm.getArrayParam("receivers", false, false);
            TeamCard teamCard = new TeamLinkCard(name, team, channel, description, url, img_url);
            sm.saveOrUpdate(teamCard);
            for (Object receiver : receivers) {
                Integer teamUser_id = Math.toIntExact((Long) receiver);
                TeamUser teamUser_receiver = team.getTeamUserWithId(teamUser_id);
                App app;
                if (teamUser_receiver.isVerified())
                    app = AppFactory.getInstance().createLinkApp(teamCard.getName(), url, img_url, teamUser_receiver.getOrCreateProfile(sm.getHibernateQuery()));
                else
                    app = AppFactory.getInstance().createLinkApp(teamCard.getName(), url, img_url);
                TeamCardReceiver teamCardReceiver = new TeamLinkCardReceiver(app, teamCard, teamUser_receiver);
                sm.saveOrUpdate(teamCardReceiver);
                if (teamUser_receiver.isVerified() && !teamUser_receiver.equals(teamUser_connected))
                    NotificationFactory.getInstance().createAppSentNotification(teamUser_receiver.getUser(), teamUser_connected, teamCardReceiver, sm.getUserWebSocketManager(teamUser_receiver.getUser().getDb_id()), sm.getHibernateQuery());
                teamCard.addTeamCardReceiver(teamCardReceiver);
            }
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CREATED, teamCard.getJson()));
            channel.addTeamCard(teamCard);
            team.addTeamCard(teamCard);
            sm.setSuccess(teamCard.getJson());
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
