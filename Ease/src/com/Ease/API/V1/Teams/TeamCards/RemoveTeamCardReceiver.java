package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
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

@WebServlet("/api/v1/teams/RemoveTeamCardReceiver")
public class RemoveTeamCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            Integer team_card_receiver_id = sm.getIntParam("team_card_receiver_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(team_card_receiver_id);
            TeamUser teamUser = teamCardReceiver.getTeamUser();
            TeamUser teamUser_admin = sm.getTeamUser(team);
            teamCard.removeTeamCardReceiver(teamCardReceiver);
            Profile profile = teamCardReceiver.getApp().getProfile();
            if (profile != null)
                profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getHibernateQuery());
            sm.saveOrUpdate(teamCard);
            if (teamUser.isVerified() && !teamUser.equals(sm.getTeamUser(team)))
                NotificationFactory.getInstance().createRemovedFromTeamCardNotification(teamUser, teamUser_admin, teamCard.getName(), teamCard.getLogo(), teamCard.getChannel(), sm.getUserWebSocketManager(teamUser.getUser().getDb_id()), sm.getHibernateQuery());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP_RECEIVER, WebSocketMessageAction.REMOVED, team_card_receiver_id));
            sm.setSuccess("Receiver removed");
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
