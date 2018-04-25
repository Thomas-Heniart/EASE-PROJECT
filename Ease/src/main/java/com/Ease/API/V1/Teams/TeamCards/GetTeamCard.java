package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/api/v1/teams/GetTeamCard")
public class GetTeamCard extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = (TeamCard) sm.getHibernateQuery().get(TeamCard.class, team_card_id);
            Team team = teamCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeConnected();
            TeamUser teamUser = sm.getTeamUser(team);
            if (teamUser == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!teamUser.isDisabled() && !teamUser.departureExpired()) {
                String teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                teamCard.decipher(teamKey);
            }
            sm.setSuccess(teamCard.getJson());
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
