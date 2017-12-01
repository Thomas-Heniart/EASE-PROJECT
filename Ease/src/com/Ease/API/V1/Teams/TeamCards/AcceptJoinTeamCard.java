package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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

@WebServlet("/api/v1/teams/AcceptJoinTeamCard")
public class AcceptJoinTeamCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer teamCard_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCard_id);
            Integer request_id = sm.getIntParam("request_id", true, false);
            JoinTeamCardRequest joinTeamCardRequest = teamCard.getJoinTeamCardRequest(request_id);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamCardReceiver teamCardReceiver = joinTeamCardRequest.accept((String) sm.getTeamProperties(team_id).get("teamKey"), hibernateQuery);
            TeamUser teamUser_receiver = teamCardReceiver.getTeamUser();
            if (teamUser_receiver.isVerified()) {
                Profile profile = teamUser_receiver.getOrCreateProfile(sm.getUserWebSocketManager(teamUser_receiver.getUser().getDb_id()), hibernateQuery);
                App app = teamCardReceiver.getApp();
                app.setProfile(profile);
                app.setPosition(profile.getSize());
                sm.saveOrUpdate(app);
                NotificationFactory.getInstance().createAcceptJoinRequestNotification(teamUser_receiver, sm.getTeamUser(team), teamCard, sm.getUserWebSocketManager(teamUser_receiver.getUser().getDb_id()), hibernateQuery);
            }
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_REQUEST, WebSocketMessageAction.REMOVED, request_id));
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CREATED, teamCardReceiver.getJson()));
            sm.setSuccess(teamCardReceiver.getJson());
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
