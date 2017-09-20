package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.App.SharedApp;
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
import org.json.simple.JSONArray;
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
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            JSONArray receivers = (JSONArray) sm.getParam("receivers", false, false);
            String app_name = sm.getStringParam("name", true, false);
            String url = sm.getStringParam("url", true, false);
            String img_url = sm.getStringParam("img_url", false, false);
            String description = sm.getStringParam("description", false, true);
            if (app_name == null || app_name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty app name");
            if (!Regex.isValidLink(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url.");
            if (!img_url.equals("") && !Regex.isValidLink(img_url))
                throw new HttpServletException(HttpStatus.BadRequest, "Url entered for icon is invalid.");
            if (description == null)
                description = "";
            Channel channel = team.getChannelWithId(channel_id);
                if (!channel.getTeamUsers().contains(teamUser_owner) && !teamUser_owner.isTeamAdmin())
                    throw new HttpServletException(HttpStatus.Forbidden, "You don't have access to this channel.");
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            LinkApp linkApp = LinkApp.createShareableLinkApp(app_name, url, img_url, sm);
            linkApp.becomeShareable(sm.getDB(), team, channel, description);
            for (Object receiver : receivers) {
                Integer receiver_id = Math.toIntExact((Long) receiver);
                TeamUser teamUser_tenant = team.getTeamUserWithId(receiver_id);
                SharedApp sharedApp = linkApp.share(teamUser_tenant, team, new JSONObject(), sm);
                linkApp.addSharedApp(sharedApp);
                if (teamUser_tenant != teamUser_owner) {
                    String notif_url = channel.getDb_id() + "?app_id=" + linkApp.getDBid();
                    teamUser_tenant.addNotification(teamUser_owner.getUsername() + " sent you " + linkApp.getName() + " in #" + channel.getName(), notif_url, linkApp.getLogo(), sm.getTimestamp(), db);
                }
            }
            db.commitTransaction(transaction);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.ADDED, linkApp.getShareableJson(), linkApp.getOrigin()));
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
