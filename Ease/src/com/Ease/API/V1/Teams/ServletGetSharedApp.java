package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 29/05/2017.
 */
@WebServlet("/api/v1/teams/GetSharedApp")
public class ServletGetSharedApp extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String team_id = sm.getServletParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            /* @TODO For the moment we use single_id but it will be replaced by db_id in the future */
            String app_id = sm.getServletParam("app_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            ShareableApp shareableApp = team.getShareableAppWithId(Integer.parseInt(app_id));
            App app = (App) shareableApp;
            if (app.isClassicApp())
                ((ClassicApp)app).getAccount().decipherWithTeamKeyIfNeeded(teamUser.getDeciphered_teamKey());
            sm.setResponse(ServletManager.Code.Success, shareableApp.getShareableJson().toString());
            sm.setLogResponse("GetSharedApp done");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
