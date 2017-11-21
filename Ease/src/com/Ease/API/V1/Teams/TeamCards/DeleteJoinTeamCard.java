package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/DeleteJoinTeamCard")
public class DeleteJoinTeamCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer teamCard_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCard_id);
            Integer request_id = sm.getIntParam("request_id", true, false);
            JoinTeamCardRequest joinTeamCardRequest = teamCard.getJoinTeamCardRequest(request_id);
            teamCard.removeJoinTeamCardRequest(joinTeamCardRequest);
            sm.deleteObject(joinTeamCardRequest);
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
