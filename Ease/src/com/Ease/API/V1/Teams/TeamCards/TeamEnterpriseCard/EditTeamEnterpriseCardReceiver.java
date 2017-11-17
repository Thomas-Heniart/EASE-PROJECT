package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
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

@WebServlet("/api/v1/teams/EditTeamEnterpriseCardReceiver")
public class EditTeamEnterpriseCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeam(team_id, sm.getHibernateQuery());
            sm.needToBeTeamUserOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) teamCard;
            Integer team_card_receiver_id = sm.getIntParam("team_card_receiver_id", true, false);
            TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(team_card_receiver_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_connected.equals(teamCardReceiver.getTeamUser()) && !teamUser_connected.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            /* String private_key = (String) sm.getContextAttr("privateKey");
            for (Object object : account_information.entrySet()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) object;
                account_information.put(entry.getKey(), RSA.Decrypt(entry.getValue(), private_key));
            } */
            TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) teamCardReceiver;
            ClassicApp classicApp = (ClassicApp) teamEnterpriseCardReceiver.getApp();
            if (classicApp.getAccount() == null) {
                String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
                Account account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamEnterpriseCard.getPassword_reminder_interval());
                classicApp.setAccount(account);
            } else
                classicApp.getAccount().edit(account_information);
            sm.saveOrUpdate(teamCard);
            sm.setSuccess(teamCardReceiver.getCardJson());
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
