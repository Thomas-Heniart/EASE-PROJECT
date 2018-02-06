package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
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

@WebServlet("/api/v1/teams/EditTeamSingleCardReceiver")
public class EditTeamSingleCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_receiver_id = sm.getIntParam("team_card_receiver_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamSingleCardReceiver teamSingleCardReceiver = (TeamSingleCardReceiver) hibernateQuery.get(TeamSingleCardReceiver.class, team_card_receiver_id);
            if (teamSingleCardReceiver == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such receiver");
            TeamCard teamCard = teamSingleCardReceiver.getTeamCard();
            Team team = teamCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeAdminOfTeam(team);
            Boolean allowed_to_see_password = true; //sm.getBooleanParam("allowed_to_see_password", true, false);
            teamSingleCardReceiver.setAllowed_to_see_password(allowed_to_see_password);
            sm.saveOrUpdate(teamSingleCardReceiver);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CHANGED, teamSingleCardReceiver.getWebSocketJson()));
            sm.setSuccess(teamSingleCardReceiver.getCardJson());
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
