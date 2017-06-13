package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Team;
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

/**
 * Created by thomas on 19/05/2017.
 */
@WebServlet("/api/v1/teams/AcceptSharedApp")
public class ServletAcceptSharedApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeTeamUser();
            Integer team_id = sm.getIntParam("team_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Integer app_id = sm.getIntParam("app_id", true);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            SharedApp sharedApp = team.getSharedApp(app_id);
            if (teamUser != sharedApp.getTeamUser_tenant())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot accept this app.");
            App app = (App) sharedApp;
            app.accept(sm.getDB());
            if (app.isClassicApp() && ((App) sharedApp.getHolder()).isClassicApp()) {
                ClassicApp classicApp = (ClassicApp) app;
                classicApp.getAccount().decipherWithTeamKeyIfNeeded(teamUser.getDeciphered_teamKey());
                classicApp.getAccount().cipherWithKeyUser(sm.getUser().getKeys().getKeyUser(), sm.getDB());
            }
            sm.setSuccess("App accepted");
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
