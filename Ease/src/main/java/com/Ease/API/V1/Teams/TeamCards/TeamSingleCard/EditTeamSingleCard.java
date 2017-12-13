package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
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

@WebServlet("/api/v1/teams/EditTeamSingleCard")
public class EditTeamSingleCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = (TeamCard) sm.getHibernateQuery().get(TeamSingleCard.class, team_card_id);
            if (teamCard == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!teamCard.isTeamSingleCard())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
            Team team = teamSingleCard.getTeam();
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            if (!teamUser.isTeamAdmin() && (teamSingleCard.getTeamUser_filler() == null || !teamUser.equals(teamSingleCard.getTeamUser_filler())))
                throw new HttpServletException(HttpStatus.Forbidden);
            String description = sm.getStringParam("description", true, true);
            if (description == null)
                description = "";
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            teamSingleCard.setDescription(description);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            teamCard.setName(name);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            sm.decipher(account_information);
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter password_reminder_interval");
            teamSingleCard.setPassword_reminder_interval(password_reminder_interval);
            String teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            teamSingleCard.decipher(teamKey);
            if (teamSingleCard.getAccount() == null) {
                Account account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamSingleCard.getPassword_reminder_interval());
                teamSingleCard.setAccount(account);
                for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values()) {
                    ClassicApp classicApp = (ClassicApp) teamCardReceiver.getApp();
                    classicApp.setAccount(AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamSingleCard.getPassword_reminder_interval()));
                }
                NotificationFactory.getInstance().createAppFilledNotification(teamUser, teamCard, sm.getUserIdMap(), sm.getHibernateQuery());
                teamSingleCard.setTeamUser_filler(null);

            } else {
                teamSingleCard.getAccount().edit(account_information, teamSingleCard.getPassword_reminder_interval(), sm.getHibernateQuery());
                for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values()) {
                    ClassicApp classicApp = (ClassicApp) teamCardReceiver.getApp();
                    classicApp.getAccount().edit(account_information, sm.getHibernateQuery());
                }
            }
            sm.saveOrUpdate(teamSingleCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamSingleCard.getWebSocketJson()));
            sm.setSuccess(teamSingleCard.getJson());
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
