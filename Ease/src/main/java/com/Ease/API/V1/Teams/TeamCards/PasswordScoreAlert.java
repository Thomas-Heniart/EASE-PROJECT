package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/api/v1/teams/password-score-alert")
public class PasswordScoreAlert extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamId = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(teamId);
            sm.needToBeAdminOfTeam(team);
            Integer teamCardId = sm.getIntParam("team_card_id", true, true);
            TeamUser teamUserConnected = sm.getTeamUser(team);
            String teamKey = sm.getTeamKey(team);
            if (teamCardId != null) {
                TeamCard teamCard = team.getTeamCard(teamCardId);
                if (teamCard.isTeamSingleCard()) {
                    notificationForSingleCard(teamCard, teamUserConnected, sm);
                    teamCard.decipher(teamKey);
                    sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamCard.getWebSocketJson()));
                    sm.setSuccess(teamCard.getJson());
                } else if (teamCard.isTeamEnterpriseCard()) {
                    Integer teamCardReceiverId = sm.getIntParam("team_card_receiver_id", true, true);
                    if (teamCardReceiverId != null) {
                        TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) teamCard.getTeamCardReceiver(teamCardReceiverId);
                        notificationForEnterpriseCardReceiver(teamEnterpriseCardReceiver, teamUserConnected, sm);
                        teamEnterpriseCardReceiver.decipher(teamKey);
                        sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CHANGED, teamEnterpriseCardReceiver.getWebSocketJson()));
                        sm.setSuccess(teamEnterpriseCardReceiver.getCardJson());
                    } else {
                        notificationForEnterpriseCard(teamCard, teamUserConnected, sm);
                        teamCard.decipher(teamKey);
                        sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamCard.getWebSocketJson()));
                        sm.setSuccess(teamCard.getJson());
                    }
                } else
                    throw new HttpServletException(HttpStatus.BadRequest, "Not allowed");
            } else {
                for (TeamCard teamCard : team.getTeamCardSet()) {
                    if (teamCard.isTeamSingleCard())
                        notificationForSingleCard(teamCard, teamUserConnected, sm);
                    else if (teamCard.isTeamEnterpriseCard())
                        notificationForEnterpriseCard(teamCard, teamUserConnected, sm);
                }
                team.setLastPasswordScoreAlertDate(new Date());
                sm.saveOrUpdate(team);
                JSONObject res = new JSONObject();
                res.put("last_password_score_alert_date", team.getLastPasswordScoreAlertDate().getTime());
                sm.setSuccess(res);
            }
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void notificationForEnterpriseCard(TeamCard teamCard, TeamUser teamUserConnected, PostServletManager sm) {
        if (!teamCard.isTeamEnterpriseCard())
            return;
        for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values())
            notificationForEnterpriseCardReceiver((TeamEnterpriseCardReceiver) teamCardReceiver, teamUserConnected, sm);
        teamCard.setLastPasswordScoreAlertDate(new Date());
        sm.saveOrUpdate(teamCard);
    }

    private void notificationForSingleCard(TeamCard teamCard, TeamUser teamUserConnected, PostServletManager sm) {
        if (!teamCard.isTeamSingleCard())
            return;
        if (teamCard.getPasswordScore() == null || teamCard.getPasswordScore().equals(4))
            return;
        TeamUser teamUserReceiver = teamCard.getChannel().getRoom_manager();
        if (teamUserConnected.equals(teamUserReceiver))
            return;
        teamCard.setLastPasswordScoreAlertDate(new Date());
        sm.saveOrUpdate(teamCard);
        if (teamUserReceiver.isRegistered())
            NotificationFactory.getInstance().createPasswordScoreTooWeakNotification(teamUserConnected, teamUserReceiver, teamCard, sm.getUserWebSocketManager(teamUserReceiver.getUser().getDb_id()), sm.getHibernateQuery());
    }

    private void notificationForEnterpriseCardReceiver(TeamEnterpriseCardReceiver teamEnterpriseCardReceiver, TeamUser teamUserConnected, PostServletManager sm) {
        if (teamEnterpriseCardReceiver.getPasswordScore() == null || teamEnterpriseCardReceiver.getPasswordScore().equals(4))
            return;
        TeamUser teamUserReceiver = teamEnterpriseCardReceiver.getTeamUser();
        if (teamUserConnected.equals(teamUserReceiver))
            return;
        teamEnterpriseCardReceiver.setLastPasswordScoreAlertDate(new Date());
        sm.saveOrUpdate(teamEnterpriseCardReceiver);
        if (teamUserReceiver.isRegistered())
            NotificationFactory.getInstance().createPasswordScoreTooWeakNotification(teamUserConnected, teamUserReceiver, teamEnterpriseCardReceiver.getTeamCard(), sm.getUserWebSocketManager(teamUserReceiver.getUser().getDb_id()), sm.getHibernateQuery());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
