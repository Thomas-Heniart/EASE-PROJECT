package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/RemoveTeamCardReceiver")
public class RemoveTeamCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeAdminOfTeam(team_id);
            Integer teamCard_id = sm.getIntParam("teamCard_id", true, false);
            Integer teamCard_receiver_id = sm.getIntParam("teamCardReceiver_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCard_id);
            TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(teamCard_receiver_id);
            
            teamCard.removeTeamCardReceiver(teamCard_receiver_id);
            sm.setSuccess("Receiver removed");
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
