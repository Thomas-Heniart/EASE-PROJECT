package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
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

@WebServlet("/api/v1/teams/DeleteTeamCard")
public class DeleteTeamCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            Channel channel = teamCard.getChannel();
            channel.removeTeamCard(teamCard);
            team.removeTeamCard(teamCard);
            for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                Profile profile = teamCardReceiver.getApp().getProfile();
                if (profile != null)
                    profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getHibernateQuery());
            }
            sm.deleteObject(teamCard);
            sm.setSuccess("Team card deleted");
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
