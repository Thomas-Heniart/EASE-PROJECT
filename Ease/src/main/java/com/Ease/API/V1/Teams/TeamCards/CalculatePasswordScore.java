package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
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

@WebServlet("/api/v1/teams/calculate-password-score")
public class CalculatePasswordScore extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamId = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(teamId);
            TeamUser teamUserConnected = sm.getTeamUser(team);
            Integer teamCardId = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCardId);
            if (!teamUserConnected.equals(teamCard.getTeamUser_sender()) && !teamUserConnected.isTeamAdmin())
                throw new HttpServletException(HttpStatus.BadRequest, "Not allowed");
            if (teamCard.isTeamLinkCard())
                throw new HttpServletException(HttpStatus.BadRequest, "Not allowed");
            String teamKey = sm.getTeamKey(team);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            if (teamCard.isTeamSingleCard()) {
                teamCard.decipher(teamKey);
                teamCard.calculatePasswordScore();
                hibernateQuery.saveOrUpdateObject(teamCard);
                sm.setSuccess(teamCard.getJson());
                sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamCard.getWebSocketJson()));
            } else {
                Integer teamCardReceiverId = sm.getIntParam("team_card_receiver_id", true, true);
                if (teamCardReceiverId == null) {
                    teamCard.decipher(teamKey);
                    for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                        ((TeamEnterpriseCardReceiver) teamCardReceiver).calculatePasswordScore();
                        hibernateQuery.saveOrUpdateObject(teamCardReceiver);
                    }
                    sm.setSuccess(teamCard.getJson());
                    sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamCard.getWebSocketJson()));
                } else {
                    TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) teamCard.getTeamCardReceiver(teamCardReceiverId);
                    teamEnterpriseCardReceiver.decipher(teamKey);
                    teamEnterpriseCardReceiver.calculatePasswordScore();
                    hibernateQuery.saveOrUpdateObject(teamEnterpriseCardReceiver);
                    sm.setSuccess(teamEnterpriseCardReceiver.getCardJson());
                    sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CHANGED, teamEnterpriseCardReceiver.getWebSocketJson()));
                }
            }
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
