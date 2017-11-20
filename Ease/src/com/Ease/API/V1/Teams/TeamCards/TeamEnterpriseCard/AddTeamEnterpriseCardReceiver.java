package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
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

@WebServlet("/api/v1/teams/AddTeamEnterpriseCardReceiver")
public class AddTeamEnterpriseCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            JSONObject account_information = sm.getJsonParam("account_information", true, true);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden, "This is not a team enterprise card");
            TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) teamCard;
            TeamUser teamUser_receiver = team.getTeamUserWithId(teamUser_id);
            if (teamCard.containsTeamUser(teamUser_receiver))
                throw new HttpServletException(HttpStatus.BadRequest, "This user is already a receiver of this card");
            Account account = null;
            if (account_information != null && !account_information.isEmpty()) {
                String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
                account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamEnterpriseCard.getPassword_reminder_interval());
            }
            AppInformation appInformation = new AppInformation(teamEnterpriseCard.getName());
            App app = new ClassicApp(appInformation, teamEnterpriseCard.getWebsite(), account);
            TeamCardReceiver teamCardReceiver = new TeamEnterpriseCardReceiver(app, teamCard, teamUser_receiver);
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
