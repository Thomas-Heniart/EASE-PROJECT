package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.JoinTeamEnterpriseCardRequest;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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
import java.util.Map;

@WebServlet("/api/v1/teams/JoinTeamEnterpriseCard")
public class JoinTeamEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden, "This is not a TeamSingleCard");
            if (teamCard.getTeamCardReceiver(teamUser) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already are a receiver of this card");
            if (teamCard.getJoinTeamCardRequest(teamUser) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already ask  to join this card");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            Map<String, String> accountInformation = ((TeamEnterpriseCard) teamCard).getWebsite().getInformationNeeded(account_information);
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            Account account = AccountFactory.getInstance().createAccountFromMap(accountInformation, teamKey, ((TeamEnterpriseCard) teamCard).getPassword_reminder_interval());
            JoinTeamCardRequest joinTeamCardRequest = new JoinTeamEnterpriseCardRequest(teamCard, teamUser, account);
            sm.saveOrUpdate(joinTeamCardRequest);
            teamCard.addJoinTeamCardRequest(joinTeamCardRequest);
            TeamUser room_manager = teamCard.getChannel().getRoom_manager();
            if (!teamUser.equals(room_manager))
                NotificationFactory.getInstance().createJoinTeamCardNotification(teamUser, teamCard, sm.getUserWebSocketManager(room_manager.getUser().getDb_id()), sm.getHibernateQuery());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_REQUEST, WebSocketMessageAction.CREATED, joinTeamCardRequest.getWebSocketJson()));
            sm.setSuccess(joinTeamCardRequest.getJson());
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
