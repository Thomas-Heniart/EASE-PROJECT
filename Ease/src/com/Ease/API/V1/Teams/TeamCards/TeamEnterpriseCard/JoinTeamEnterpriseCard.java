package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.*;
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

@WebServlet("/api/v1/teams/JoinTeamEnterpriseCard")
public class JoinTeamEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden, "This is not a TeamSingleCard");
            if (teamCard.getTeamCardReceiver(teamUser) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already are a receiver of this card");
            if (teamCard.getJoinTeamCardRequest(teamUser) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already ask  to join this card");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            Account account = AccountFactory.getInstance().createAccountFromJson(account_information, teamUser.getDeciphered_teamKey(), ((TeamEnterpriseCard)teamCard).getPassword_reminder_interval());
            JoinTeamCardRequest joinTeamCardRequest = new JoinTeamEnterpriseCardRequest(teamCard, teamUser, account);
            sm.saveOrUpdate(joinTeamCardRequest);
            teamCard.addJoinTeamCardRequest(joinTeamCardRequest);
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
