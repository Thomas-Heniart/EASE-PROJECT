package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.ShareableApp;
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 09/06/2017.
 */
@WebServlet("/api/v1/teams/DeleteChannel")
public class ServletDeleteChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Channel channel = team.getChannelWithId(channel_id);
            if (!teamUser.isTeamOwner() && channel.getRoom_manager() != teamUser)
                throw new HttpServletException(HttpStatus.Forbidden, "Only room manager and owner can delete a room.");
            if (channel.getName().equals("openspace"))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot modify this channel.");
            List<ShareableApp> shareableAppsToRemove = new LinkedList<>();
            for (ShareableApp shareableApp : team.getAppManager().getShareableApps().values()) {
                if (shareableApp.getChannel() == channel) {
                    shareableAppsToRemove.add(shareableApp);
                }
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            for (ShareableApp shareableApp : shareableAppsToRemove)
                team.getAppManager().removeShareableApp(shareableApp, db);
            db.commitTransaction(transaction);
            channel.delete();
            team.removeChannel(channel);
            sm.deleteObject(channel);
            sm.saveOrUpdate(team);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_ROOM, WebSocketMessageAction.REMOVED, channel_id, channel.getOrigin()));
            sm.setSuccess("Channel delete");
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
