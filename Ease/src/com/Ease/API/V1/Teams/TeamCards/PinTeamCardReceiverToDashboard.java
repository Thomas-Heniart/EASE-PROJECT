package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamManager;
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

@WebServlet("/api/v1/teams/PinTeamCardReceiverToDashboard")
public class PinTeamCardReceiverToDashboard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeTeamUserOfTeam(team_id);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            Integer team_card_receiver_id = sm.getIntParam("team_card_receiver_id", true, false);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (teamCard.isTeamLinkCard())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(team_card_receiver_id);
            if (!teamCardReceiver.getTeamUser().equals(sm.getTeamUserForTeam(team)))
                throw new HttpServletException(HttpStatus.Forbidden, "You must be the receiver to pin");
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            Profile profile = null;
            if (profile_id != -1)
                profile = sm.getUser().getDashboardManager().getProfile(profile_id);
            Profile old_profile = null;
            if (teamCardReceiver.getApp().getProfile() != null)
                old_profile = sm.getUser().getDashboardManager().getProfile(teamCardReceiver.getApp().getProfile().getDb_id());
            if (old_profile != null && !old_profile.equals(profile)) {
                old_profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getHibernateQuery());
                teamCardReceiver.getApp().setPosition(profile == null ? null : profile.getAppMap().size());
            } else if (old_profile == null)
                teamCardReceiver.getApp().setPosition(profile == null ? null : profile.getAppMap().size());
            teamCardReceiver.getApp().setProfile(profile);
            teamCardReceiver.getApp().getAppInformation().setName(name);
            sm.saveOrUpdate(teamCardReceiver.getApp());
            if (profile != null)
                profile.addApp(teamCardReceiver.getApp());
            sm.getUser().getDashboardManager().addApp(teamCardReceiver.getApp());
            sm.setSuccess(teamCardReceiver.getApp().getJson());
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
