package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/GetSingleAppPassword")
public class ServletGetSingleAppPassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            Integer app_id = sm.getIntParam("app_id", true);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            App app = (App) shareableApp;
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!shareableApp.getTeamUser_tenants().contains(teamUser_connected) && !teamUser_connected.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot copy password.");
            ClassicApp classicApp = (ClassicApp) app;
            String password;
            if (teamUser_connected.isTeamAdmin()) {
                password = classicApp.getAccount().getInformationNamed("password");
            } else if (shareableApp.getTeamUser_tenants().contains(teamUser_connected)) {
                SharedApp sharedApp = shareableApp.getSharedAppForTeamUser(teamUser_connected);
                if (!sharedApp.canSeeInformation())
                    throw new HttpServletException(HttpStatus.Forbidden);
                password = classicApp.getAccount().getInformationNamed("password");
            } else
                throw new HttpServletException(HttpStatus.Forbidden);
            if (password == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No password for this app");
            JSONObject res = new JSONObject();
            String key = (String) sm.getContextAttr("publicKey");
            res.put("password", RSA.Encrypt(password, key));
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
