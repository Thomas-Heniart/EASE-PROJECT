package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 30/05/2017.
 */
@WebServlet("/api/v1/teams/ShareApp")
public class ServletShareApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeamId(team_id);
            Integer teamUser_tenant_id = sm.getIntParam("team_user_id", true);
            TeamUser teamUser_tenant = team.getTeamUserWithId(teamUser_tenant_id);
            Integer app_id = sm.getIntParam("app_id", true);
            ShareableApp shareableApp = team.getShareableAppWithId(app_id);
            if (!(shareableApp.getTeamUser_owner() == teamUser_owner) && !teamUser_owner.isTeamAdmin())
                throw new GeneralException(ServletManager.Code.ClientError, "You cannot access this app");
            Channel channel = shareableApp.getChannel();
            JSONObject params = shareableApp.getNeededParams(sm);
            params.put("canSeeInformation", (Boolean) sm.getParam("can_see_information", true));
            SharedApp sharedApp = shareableApp.share(teamUser_owner, teamUser_tenant, channel, team, params, sm);
            shareableApp.addSharedApp(sharedApp);
            teamUser_tenant.addSharedApp(sharedApp);
            sm.setSuccess(sharedApp.getSharedJSON());
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
