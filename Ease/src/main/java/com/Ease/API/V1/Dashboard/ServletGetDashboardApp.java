package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.LogWithApp;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 24/05/2017.
 */
@WebServlet("/api/v1/dashboard/GetDashboardApp")
public class ServletGetDashboardApp extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("id", true);
            App app = sm.getUser().getApp(app_id, sm.getHibernateQuery());
            String symmetric_key = null;
            String team_key = null;
            TeamCardReceiver teamCardReceiver = app.getTeamCardReceiver();
            if (teamCardReceiver != null) {
                Team team = teamCardReceiver.getTeamCard().getTeam();
                sm.needToBeTeamUserOfTeam(team);
                team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            } else {
                symmetric_key = (String) sm.getUserProperties(sm.getUser().getDb_id()).get("keyUser");
                if (app.isLogWithApp()) {
                    WebsiteApp websiteApp = ((LogWithApp) app).getLoginWith_app();
                    if (websiteApp != null) {
                        if (websiteApp.getTeamCardReceiver() != null) {
                            Team team = websiteApp.getTeamCardReceiver().getTeamCard().getTeam();
                            if (!sm.getTeamUser(team).isDisabled() && !sm.getTeamUser(team).departureExpired())
                                team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                        }
                    }
                }
            }
            app.decipher(symmetric_key, team_key);
            sm.setSuccess(app.getJson());
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
