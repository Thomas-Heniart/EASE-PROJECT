package com.Ease.API.Rest;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
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

@WebServlet("/GetAppInformation")
public class GetAppInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUserWithToken();
            Integer app_id = sm.getIntParam("app_id", true);
            Integer team_id = sm.getIntParam("team_id", true);
            String information_name = sm.getParam("information_name", true);
            App app;
            if (team_id == null)
                app = user.getDashboardManager().getAppWithId(app_id);
            else {
                TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
                Team team = teamManager.getTeamWithId(team_id);
                sm.needToBeTeamUserOfTeam(team_id);
                TeamUser teamUser = sm.getTeamUserForTeam(team);
                ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
                app = (App) shareableApp;
                SharedApp sharedApp = shareableApp.getSharedAppForTeamUser(teamUser);
                if (!sharedApp.canSeeInformation())
                    throw new HttpServletException(HttpStatus.Forbidden, "You cannot get login for this app");
                if (app.isClassicApp())
                    app = (App) shareableApp;
                else if (app.isEmpty())
                    app = (App) sharedApp;
                else
                    throw new HttpServletException(HttpStatus.Forbidden, "You cannot get login for this app");
            }
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot get password of this app");
            ClassicApp classicApp = (ClassicApp) app;
            JSONObject res = new JSONObject();
            res.put(information_name, classicApp.getAccount().getInformationNamed(information_name));
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
