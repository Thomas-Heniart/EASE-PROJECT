package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
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
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Integer app_id = sm.getIntParam("shared_app_id", true, false);
            SharedApp sharedApp = team.getAppManager().getSharedApp(app_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            if (teamUser != sharedApp.getTeamUser_tenant())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot pin this app to your dashboard.");
            User user = sm.getUser();
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            App app = (App) sharedApp.getHolder();
            if (app.isLinkApp())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot pin link app like this.");
            WebsiteApp websiteApp = (WebsiteApp) app;
            /* if (!websiteApp.getSite().getWebsiteAttributes().isIntegrated())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot until this app becomes integrated."); */
            if (profile_id == -1)
                sharedApp.unpin(db);
            else {
                String name = sm.getStringParam("app_name", true, false);
                if (name == null || name.equals(""))
                    throw new HttpServletException(HttpStatus.BadRequest, "You cannot leave name empty.");
                Profile profile = user.getDashboardManager().getProfile(profile_id);
                app = (App) sharedApp;
                app.setName(name, db);
                sharedApp.pinToDashboard(profile, db);
            }
            db.commitTransaction(transaction);
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
