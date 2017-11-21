package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
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

@WebServlet("/api/v1/teams/RemoveTeamCardReceiver")
public class RemoveTeamCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            Integer team_card_receiver_id = sm.getIntParam("team_card_receiver_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (teamCard.isTeamLinkCard())
                throw new HttpServletException(HttpStatus.Forbidden, "You must call PinTeamLinkCard");
            TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(team_card_receiver_id);
            teamCard.removeTeamCardReceiver(teamCardReceiver);
            Profile profile = teamCardReceiver.getApp().getProfile();
            if (profile != null)
                profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getHibernateQuery());
            sm.saveOrUpdate(teamCard);
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
