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
import org.json.simple.JSONObject;

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
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
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
            Profile profile = null;
            if (profile_id != -1)
                profile = sm.getUser().getDashboardManager().getProfile(profile_id);
            App app = null;
            if (teamCardReceiver == null && profile != null) {
                AppInformation appInformation = new AppInformation(name);
                LinkAppInformation linkAppInformation = new LinkAppInformation(teamLinkCard.getUrl(), teamLinkCard.getImg_url());
                app = new LinkApp(appInformation, linkAppInformation);
                app.setProfile(profile);
                app.setPosition(profile.getAppMap().size());
                teamCardReceiver = new TeamLinkCardReceiver(app, teamCard, teamUser);
                app.setTeamCardReceiver(teamCardReceiver);
                sm.saveOrUpdate(teamCardReceiver);
                profile.addApp(app);
                teamCard.addTeamCardReceiver(teamCardReceiver);
            } else if (teamCardReceiver != null) {
                Profile old_profile = sm.getUser().getDashboardManager().getProfile(teamCardReceiver.getApp().getProfile().getDb_id());
                app = teamCardReceiver.getApp();
                if (profile == null) {
                    old_profile.removeAppAndUpdatePositions(app, sm.getHibernateQuery());
                    sm.getUser().getDashboardManager().removeApp(app);
                    teamCard.removeTeamCardReceiver(teamCardReceiver.getDb_id());
                    app = null;
                } else if (!profile.equals(old_profile)) {
                    old_profile.removeAppAndUpdatePositions(app, sm.getHibernateQuery());
                    profile.addApp(app);
                    app.setProfile(profile);
                    app.setPosition(profile.getAppMap().size());
                }
                if (app != null)
                    app.getAppInformation().setName(name);
                sm.saveOrUpdate(teamCard);
            }
            if (app != null)
                sm.getUser().getDashboardManager().addApp(app);
            JSONObject res = new JSONObject();
            if (app == null)
                res.put("msg", "Unpin done");
            else
                res = app.getJson();
            sm.setSuccess(res);
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
