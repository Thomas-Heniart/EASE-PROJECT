package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.LogWithApp;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/EditAppName")
public class ServletEditAppName extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            String name = sm.getStringParam("name", true, false);
            App app = sm.getUser().getApp(app_id, sm.getHibernateQuery());
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            app.getAppInformation().setName(name);
            sm.saveOrUpdate(app.getAppInformation());
            String symmetric_key = null;
            String team_key = null;
            if (app.getTeamCardReceiver() != null)
                team_key = (String) sm.getTeamProperties(app.getTeamCardReceiver().getTeamCard().getTeam().getDb_id()).get("teamKey");
            else {
                symmetric_key = (String) sm.getUserProperties(sm.getUser().getDb_id()).get("keyUser");
                if (app.isLogWithApp()) {
                    WebsiteApp websiteApp = ((LogWithApp) app).getLoginWith_app();
                    if (websiteApp != null) {
                        if (websiteApp.getTeamCardReceiver() != null) {
                            Team team = websiteApp.getTeamCardReceiver().getTeamCard().getTeam();
                            if (!sm.getTeamUser(team).isDisabled())
                                team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                        }
                    }
                }
            }
            app.decipher(symmetric_key, team_key);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.DASHBOARD_APP, WebSocketMessageAction.CHANGED, app.getWebSocketJson()));
            sm.setSuccess(app.getJson());
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
