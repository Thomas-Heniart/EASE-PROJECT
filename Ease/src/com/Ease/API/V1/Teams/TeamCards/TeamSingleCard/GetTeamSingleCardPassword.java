package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountInformation;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/GetTeamSingleCardPassword")
public class GetTeamSingleCardPassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamCard_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamSingleCard teamSingleCard = (TeamSingleCard) hibernateQuery.get(TeamSingleCard.class, teamCard_id);
            if (teamSingleCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This card does not exist");
            Team team = teamSingleCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            TeamSingleCardReceiver teamCardReceiver = (TeamSingleCardReceiver) teamSingleCard.getTeamCardReceiver(teamUser);
            if (teamCardReceiver == null || !teamCardReceiver.isAllowed_to_see_password() && !teamUser.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden);
            Account account = teamSingleCard.getAccount();
            if (account == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            String teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            teamSingleCard.decipher(teamKey);
            AccountInformation password = account.getInformationNamed("password");
            if (password == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            JSONObject res = new JSONObject();
            res.put("password", sm.cipher(password.getDeciphered_information_value()));
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
