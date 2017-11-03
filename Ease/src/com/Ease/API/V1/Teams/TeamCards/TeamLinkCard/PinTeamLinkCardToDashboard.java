package com.Ease.API.V1.Teams.TeamCards.TeamLinkCard;

import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamLinkCardReceiver;
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

@WebServlet("/api/v1/teams/PinTeamLinkCardToDashboard")
public class PinTeamLinkCardToDashboard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Integer teamCard_id = sm.getIntParam("teamCard_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCard_id);
            if (!teamCard.isTeamLinkCard())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamLinkCard teamLinkCard = (TeamLinkCard) teamCard;
            if (!teamCard.getChannel().getTeamUsers().contains(teamUser))
                throw new HttpServletException(HttpStatus.BadRequest, "You are not part of this channel");
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(teamUser);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = sm.getUser().getDashboardManager().getProfile(profile_id);
            if (teamCardReceiver == null) {
                AppInformation appInformation = new AppInformation(name);
                LinkAppInformation linkAppInformation = new LinkAppInformation(teamLinkCard.getUrl(), teamLinkCard.getImg_url());
                App app = new LinkApp(appInformation, linkAppInformation);
                teamCardReceiver = new TeamLinkCardReceiver(app, teamCard, teamUser);
                sm.saveOrUpdate(teamCardReceiver);
                teamCard.addTeamCardReceiver(teamCardReceiver);
            }
            Profile old_profile = teamCardReceiver.getApp().getProfile();
            if (old_profile != null && old_profile != profile) {
                old_profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getHibernateQuery());
                teamCardReceiver.getApp().setPosition(profile.getAppMap().size());
            } else if (old_profile == null)
                teamCardReceiver.getApp().setPosition(profile.getAppMap().size());
            teamCardReceiver.getApp().setProfile(profile);
            teamCardReceiver.getApp().getAppInformation().setName(name);
            sm.saveOrUpdate(teamCardReceiver.getApp());
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
