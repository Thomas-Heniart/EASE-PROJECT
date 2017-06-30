package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
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

/**
 * Created by thomas on 19/05/2017.
 */
@WebServlet("/api/v1/teams/PinAppToDashboard")
public class ServletPinAppToDashboard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Integer app_id = sm.getIntParam("shared_app_id", true);
            SharedApp sharedApp = team.getAppManager().getSharedApp(app_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            if (teamUser != sharedApp.getTeamUser_tenant())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot pin this app to your dashboard.");
            User user = sm.getUser();
            Integer profile_id = sm.getIntParam("profile_id", true);
            if (profile_id == -1) {
                if (sharedApp.getPinned_app() != null)
                    user.getDashboardManager().removeAppWithSingleId(sharedApp.getPinned_app().getSingleId(), sm.getDB());
            } else {
                Profile profile = user.getDashboardManager().getProfile(profile_id);
                App pinned_app = sharedApp.getPinned_app();
                if (pinned_app == null) {
                    String name = sm.getStringParam("app_name", true);
                    if (name == null || name.equals(""))
                        throw new HttpServletException(HttpStatus.BadRequest, "App name cannot be null");
                    pinned_app = sharedApp.createPinned_app(name, profile, teamUser.getDeciphered_teamKey(), sm.getDB());
                    pinned_app.setSingleId(sm.getNextSingle_id());
                    profile.addApp(pinned_app);
                } else if (profile_id != pinned_app.getProfile().getSingleId()) {
                    user.getDashboardManager().moveApp(pinned_app.getSingleId(), profile_id, profile.getApps().size(), sm.getDB());
                    pinned_app.setProfile(profile, sm.getDB());
                }
            }
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
