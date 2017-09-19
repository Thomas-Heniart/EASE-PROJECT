package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/ShareSingleApp")
public class ServletShareSingleApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            Integer app_id = sm.getIntParam("app_id", true, false);
            Integer team_user_id = sm.getIntParam("team_user_id", true, false);
            Boolean can_see_information = sm.getBooleanParam("can_see_information", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            App app = (App) shareableApp;
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid app_id");
            JSONObject params = new JSONObject();
            params.put("can_see_information", can_see_information);
            TeamUser teamUser_tenant = team.getTeamUserWithId(team_user_id);
            Channel channel = shareableApp.getChannel();
            if (!channel.getTeamUsers().contains(teamUser_tenant))
                throw new HttpServletException(HttpStatus.Forbidden, "You can only share this app with people in this room.");
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            SharedApp sharedApp = shareableApp.share(teamUser_tenant, team, params, sm);
            if (teamUser_tenant == teamUser_connected)
                sharedApp.accept(db);
            else {
                String url = channel.getDb_id() + "?app_id=" + app.getDBid();
                teamUser_tenant.addNotification(teamUser_connected.getUsername() + " sent you " + app.getName() + " in #" + channel.getName(), url, app.getLogo(), sm.getTimestamp(), db);
            }
            db.commitTransaction(transaction);
            shareableApp.addSharedApp(sharedApp);
            team.getAppManager().addSharedApp(sharedApp);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.ADDED, app.getShareableJson(), app.getOrigin()));
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
