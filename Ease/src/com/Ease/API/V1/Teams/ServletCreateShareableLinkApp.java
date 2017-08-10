package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
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
@WebServlet("/api/v1/teams/CreateShareableLinkApp")
public class ServletCreateShareableLinkApp extends HttpServlet {
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
            String url = sm.getStringParam("url", true);
            String description = sm.getStringParam("description", false);
            if (app_name == null || app_name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty app name");
            if (url == null || url.equals("") || !Regex.isValidLink(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url.");
            if (description == null)
                description = "";
            Channel channel = null;
            if (channel_id != null)
                channel = team.getChannelWithId(channel_id);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            LinkApp linkApp = LinkApp.createShareableLinkApp(app_name, url, sm);
            linkApp.becomeShareable(sm.getDB(), team, teamUser_owner, team_user_id, channel, description);
            db.commitTransaction(transaction);
            JSONObject target = linkApp.getOrigin();
            target.put("team_id", team_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.ADDED, linkApp.getShareableJson(), target));
            sm.setSuccess(linkApp.getShareableJson());
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
