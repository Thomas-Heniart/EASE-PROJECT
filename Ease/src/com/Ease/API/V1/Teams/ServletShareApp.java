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

/**
 * Created by thomas on 30/05/2017.
 */
@WebServlet("/api/v1/teams/ShareApp")
public class ServletShareApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeamId(team_id);
            Integer teamUser_tenant_id = sm.getIntParam("team_user_id", true);
            TeamUser teamUser_tenant = team.getTeamUserWithId(teamUser_tenant_id);
            Integer app_id = sm.getIntParam("app_id", true);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            if (!(shareableApp.getTeamUser_owner() == teamUser_owner) && !teamUser_owner.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden, "Not allowed");
            if (shareableApp.getTeamUser_tenants().contains(teamUser_tenant))
                throw new HttpServletException(HttpStatus.BadRequest, "You already shared this app to this user");
            Channel channel = shareableApp.getChannel();
            if (channel != null && !(channel.getTeamUsers().contains(teamUser_tenant)))
                throw new HttpServletException(HttpStatus.Forbidden, "This user doesn't have access to this channel.");
            JSONObject params = shareableApp.getNeededParams(sm);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            SharedApp sharedApp = shareableApp.share(teamUser_owner, teamUser_tenant, channel, team, params, sm);
            if (teamUser_tenant == sm.getTeamUserForTeam(team))
                sharedApp.accept(db);
            if (shareableApp.getPendingTeamUsers().contains(teamUser_tenant)) {
                shareableApp.removePendingTeamUser(teamUser_tenant, db);
                String url = (channel == null) ? ("@" + teamUser_owner.getDb_id()) : channel.getDb_id().toString();
                teamUser_tenant.addNotification(teamUser_owner.getUsername() + " approved your access to " + ((App) shareableApp).getName() + ((channel == null) ? "" : " in " + channel.getName()), url, ((App) shareableApp).getLogo(), sm.getTimestamp(), sm.getDB());
                sharedApp.accept(db);
            } else if (teamUser_owner != teamUser_tenant) {
                String url = (channel == null) ? ("@" + teamUser_tenant.getDb_id()) : channel.getDb_id().toString();
                teamUser_tenant.addNotification(teamUser_owner.getUsername() + " sent you " + ((App) shareableApp).getName() + " in " + (shareableApp.getChannel() == null ? "your Personal Space" : shareableApp.getChannel().getName()), url, ((App) shareableApp).getLogo(), sm.getTimestamp(), sm.getDB());
            }
            db.commitTransaction(transaction);
            shareableApp.addSharedApp(sharedApp);
            team.getAppManager().addSharedApp(sharedApp);
            JSONObject target = shareableApp.getOrigin();
            target.put("team_id", team_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), target));
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
