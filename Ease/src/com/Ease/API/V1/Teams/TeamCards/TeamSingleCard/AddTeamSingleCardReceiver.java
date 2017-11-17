package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/AddTeamSingleCardReceiver")
public class AddTeamSingleCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            Boolean allowed_to_see_password = sm.getBooleanParam("allowed_to_see_password", true, false);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamSingleCard())
                throw new HttpServletException(HttpStatus.Forbidden, "This is not a team single card");
            TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
            TeamUser teamUser_receiver = team.getTeamUserWithId(teamUser_id);
            if (teamCard.containsTeamUser(teamUser_receiver))
                throw new HttpServletException(HttpStatus.BadRequest, "This user is already a receiver of this card");
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            if (teamSingleCard.getAccount() != null)
                teamSingleCard.getAccount().decipher(teamKey);
            Account account = AccountFactory.getInstance().createAccountFromAccount(teamSingleCard.getAccount(), teamKey);
            AppInformation appInformation = new AppInformation(teamSingleCard.getName());
            App app = new ClassicApp(appInformation, teamSingleCard.getWebsite(), account);
            TeamCardReceiver teamCardReceiver = new TeamSingleCardReceiver(app, teamCard, teamUser_receiver, allowed_to_see_password);
            sm.saveOrUpdate(teamCardReceiver);
            teamCard.addTeamCardReceiver(teamCardReceiver);
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
