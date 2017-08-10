package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
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

/**
 * Created by thomas on 08/05/2017.
 */
@WebServlet("/api/v1/teams/CreateShareableMultiApp")
public class ServletCreateShareableMultiApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true);
            Integer team_user_id = sm.getIntParam("team_user_id", true);
            if (channel_id == null && team_user_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot create this app here");
            String app_name = sm.getStringParam("name", true);
            Integer website_id = sm.getIntParam("website_id", true);
            Integer reminderValue = Integer.parseInt(sm.getStringParam("reminder_interval", true));
            String description = sm.getStringParam("description", false);
            if (app_name == null || app_name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty app name");
            if (reminderValue == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Reminder interval is null.");
            if (description == null)
                description = "";
            Channel channel = null;
            if (channel_id != null)
                channel = team.getChannelWithId(channel_id);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            WebsiteApp websiteApp = WebsiteApp.createShareableMultiApp(app_name, website, reminderValue, sm);
            websiteApp.becomeShareable(sm.getDB(), team, teamUser_owner, team_user_id, channel, description);
            db.commitTransaction(transaction);
            JSONObject target = websiteApp.getOrigin();
            target.put("team_id", team_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.ADDED, websiteApp.getShareableJson(), target));
            sm.setSuccess(websiteApp.getShareableJson());
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
