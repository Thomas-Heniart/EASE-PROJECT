package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.LogWithApp;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/GetConnection")
public class ServletGetConnection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer appId = sm.getIntParam("app_id", true, false);
            User user = sm.getUser();
            App app = user.getApp(appId, sm.getHibernateQuery());
            String symmetricKey = null;
            String teamKey = null;
            TeamCardReceiver teamCardReceiver = app.getTeamCardReceiver();
            if (teamCardReceiver != null) {
                Team team = teamCardReceiver.getTeamCard().getTeam();
                sm.initializeTeamWithContext(team);
                sm.needToBeTeamUserOfTeam(team);
                teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            } else {
                symmetricKey = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
                if (app.isLogWithApp()) {
                    WebsiteApp websiteApp = ((LogWithApp) app).getLoginWith_app();
                    if (websiteApp != null) {
                        if (websiteApp.getTeamCardReceiver() != null) {
                            Team team = websiteApp.getTeamCardReceiver().getTeamCard().getTeam();
                            if (!sm.getTeamUser(team).isDisabled())
                                teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                        }
                    }
                }
            }
            app.decipher(symmetricKey, teamKey);
            if (app.getTeamCardReceiver() != null && app.getTeamCardReceiver().getTeamUser().isDisabled())
                throw new HttpServletException(HttpStatus.Forbidden);
            String publicKey = (String) sm.getContextAttr("publicKey");
            sm.setSuccess(app.getConnectionJson(publicKey));
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
