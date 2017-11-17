package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.JoinTeamSingleCardRequest;
import com.Ease.Team.TeamCard.TeamCard;
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

@WebServlet("/api/v1/teams/JoinTeamSingleCard")
public class JoinTeamSingleCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamSingleCard())
                throw new HttpServletException(HttpStatus.Forbidden, "This is not a TeamSingleCard");
            if (teamCard.getTeamCardReceiver(teamUser) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already are a receiver of this card");
            if (teamCard.getJoinTeamCardRequest(teamUser) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already ask  to join this card");
            JoinTeamCardRequest joinTeamCardRequest = new JoinTeamSingleCardRequest(teamCard, teamUser);
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
