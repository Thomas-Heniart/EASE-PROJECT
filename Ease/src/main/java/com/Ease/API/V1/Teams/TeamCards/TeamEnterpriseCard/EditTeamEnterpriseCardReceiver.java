package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.App;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamCard.TeamSoftwareCard;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamUser;
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

@WebServlet("/api/v1/teams/EditTeamEnterpriseCardReceiver")
public class EditTeamEnterpriseCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_receiver_id = sm.getIntParam("team_card_receiver_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) hibernateQuery.get(TeamEnterpriseCardReceiver.class, team_card_receiver_id);
            if (teamEnterpriseCardReceiver == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such receiver");
            TeamCard teamCard = teamEnterpriseCardReceiver.getTeamCard();
            Team team = teamCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_connected.isTeamAdmin() && !teamUser_connected.equals(teamCard.getTeamUser_sender()) && !teamUser_connected.equals(teamEnterpriseCardReceiver.getTeamUser()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot edit this card");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            sm.decipher(account_information);
            if (teamCard.isTeamWebsiteCard()) {
                TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) teamCard;
                account_information = teamEnterpriseCard.getWebsite().getPresentCredentialsFromJson(account_information);
            } else {
                TeamSoftwareCard teamSoftwareCard = (TeamSoftwareCard) teamCard;
                account_information = teamSoftwareCard.getSoftware().getPresentCredentialsFromJson(account_information);
            }
            App app = teamEnterpriseCardReceiver.getApp();
            String teamKey = sm.getTeamKey(team);
            app.decipher(null, teamKey);
            Account account = app.getAccount();
            if (account == null) {
                account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamCard.getPassword_reminder_interval(), sm.getHibernateQuery());
                app.setAccount(account);
            } else
                account.edit(account_information, sm.getHibernateQuery());
            if (account != null)
                teamEnterpriseCardReceiver.calculatePasswordScore();
            sm.saveOrUpdate(teamCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CHANGED, teamEnterpriseCardReceiver.getWebSocketJson()));
            sm.setSuccess(teamEnterpriseCardReceiver.getCardJson());
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
