package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/api/v1/teams/RemoveUserFromChannel")
public class ServletRemoveTeamUserFromChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Channel channel = (Channel) hibernateQuery.get(Channel.class, channel_id);
            Team team = channel.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeAdminOfTeam(team);
            if (channel.isDefault())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot remove user from default channel.");
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser_to_remove = channel.getTeamUser(teamUser_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!channel.getTeamUsers().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.Forbidden, "You must be part of the room.");
            if (channel.getRoom_manager().equals(teamUser_to_remove))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot remove the room manager.");
            Set<TeamSingleCard> teamSingleCardSet = teamUser_to_remove.getTeamSingleCardToFillSet().stream().filter(teamSingleCard -> teamSingleCard.getChannel().equals(channel)).collect(Collectors.toSet());
            if (!teamSingleCardSet.isEmpty()) {
                StringBuilder message = new StringBuilder(teamUser_to_remove.getUsername()).append(" cannot be removed from this channel as long as this person is responsible to fill credentials for ");
                teamSingleCardSet.forEach(teamSingleCard -> message.append(teamSingleCard.getName()).append(", "));
                message.replace(message.length() - 2, message.length(), ".");
                throw new HttpServletException(HttpStatus.BadRequest, message.toString());
            }
            for (TeamCard teamCard : channel.getTeamCardMap().values()) {
                TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(teamUser_to_remove);
                if (teamCardReceiver == null)
                    continue;
                if (teamCardReceiver.getTeamUser().equals(teamUser_to_remove)) {
                    App app = teamCardReceiver.getApp();
                    Profile profile = app.getProfile();
                    if (profile != null)
                        profile.removeAppAndUpdatePositions(app, hibernateQuery);
                    teamCard.removeTeamCardReceiver(teamCardReceiver);
                    sm.deleteObject(teamCardReceiver);
                }
            }
            Set<JoinTeamCardRequest> joinTeamCardRequestSet = new HashSet<>();
            for (JoinTeamCardRequest joinTeamCardRequest : teamUser_to_remove.getJoinTeamCardRequestSet()) {
                TeamCard teamCard = joinTeamCardRequest.getTeamCard();
                if (teamCard.getChannel().equals(channel)) {
                    teamCard.removeJoinTeamCardRequest(joinTeamCardRequest);
                    sm.deleteObject(joinTeamCardRequest);
                    joinTeamCardRequestSet.add(joinTeamCardRequest);
                }
            }
            teamUser_to_remove.getJoinTeamCardRequestSet().removeAll(joinTeamCardRequestSet);
            channel.removeTeamUser(teamUser_to_remove);
            sm.saveOrUpdate(channel);
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("team_id", team_id);
            ws_obj.put("room_id", channel_id);
            ws_obj.put("team_user_id", teamUser_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_ROOM_MEMBER, WebSocketMessageAction.REMOVED, ws_obj));
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
