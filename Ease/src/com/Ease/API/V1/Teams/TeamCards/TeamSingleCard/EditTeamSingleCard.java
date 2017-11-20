package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/EditTeamSingleCard")
public class EditTeamSingleCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamSingleCard())
                throw new HttpServletException(HttpStatus.Forbidden);
            String description = sm.getStringParam("description", true, true);
            if (description == null)
                description = "";
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            teamCard.setDescription(description);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            /* String private_key = (String) sm.getContextAttr("privateKey");
            for (Object object : account_information.entrySet()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) object;
                account_information.put(entry.getKey(), RSA.Decrypt(entry.getValue(), private_key));
            } */
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter password_reminder_interval");
            TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
            teamSingleCard.setPassword_reminder_interval(password_reminder_interval);
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            if (teamSingleCard.getAccount() == null) {
                Account account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamSingleCard.getPassword_reminder_interval());
                teamSingleCard.setAccount(account);
                for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values()) {
                    ClassicApp classicApp = (ClassicApp) teamCardReceiver.getApp();
                    classicApp.setAccount(AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamSingleCard.getPassword_reminder_interval()));
                }
            } else {
                teamSingleCard.getAccount().edit(account_information);
                for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values()) {
                    ClassicApp classicApp = (ClassicApp) teamCardReceiver.getApp();
                    classicApp.getAccount().edit(account_information);
                }
            }
            sm.saveOrUpdate(teamCard);
            sm.setSuccess(teamCard.getJson());
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
