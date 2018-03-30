package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
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

@WebServlet("/api/v1/teams/EditTeamCardChannel")
public class EditTeamCardChannel extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamId = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(teamId);
            sm.needToBeTeamUserOfTeam(team);
            Integer teamCardId = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCardId);
            TeamUser teamUser = sm.getTeamUser(team);
            if (!teamUser.isTeamAdmin() && !teamUser.equals(teamCard.getTeamUser_sender()))
                throw new HttpServletException(HttpStatus.Forbidden, "You are not allowed to move this card");
            Integer channelId = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channelId);
            Channel oldChannel = teamCard.getChannel();
            if (!channel.containsAllTeamUsers(teamCard.getTeamUsers()))
                throw new HttpServletException(HttpStatus.BadRequest, "All team users must be part of new channel");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                TeamUser teamUserReceiver = teamCardReceiver.getTeamUser();
                App app = teamCardReceiver.getApp();
                Profile oldProfile = app.getProfile();
                if (oldProfile == null)
                    continue;
                //Remove from old profile
                oldProfile.removeAppAndUpdatePositions(app, hibernateQuery);
                //Get new profile and add app
                Profile newProfile = teamUserReceiver.getOrCreateProfile(channel, hibernateQuery);
                newProfile.addAppAndUpdatePositions(app, 0, hibernateQuery);
                sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CHANGED, teamCardReceiver.getWebSocketJson()));
            }
            teamCard.setChannel(channel);
            sm.saveOrUpdate(teamCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamCard.getWebSocketJson()));
            sm.setSuccess(teamCard.getJson());
        } catch (Exception e) {
            sm.setError(e);
        } finally {
            sm.sendResponse();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
