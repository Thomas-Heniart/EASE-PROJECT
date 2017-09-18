package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
 * Created by thomas on 08/05/2017.
 */
@WebServlet("/api/v1/teams/CreateShareableSingleApp")
public class ServletCreateShareableSingleApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            Integer website_id = sm.getIntParam("website_id", true, false);
            JSONArray account_information = (JSONArray) sm.getParam("account_information", false, false);
            JSONArray receivers = (JSONArray) sm.getParam("receivers", false, false);
            Integer channel_id = sm.getIntParam("channel_id", true, true);
            Integer team_user_id = sm.getIntParam("team_user_id", true, true);
            if (channel_id == null && team_user_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot create this app here");
            String app_name = sm.getStringParam("name", true, false);
            String description = sm.getStringParam("description", true, true);
            Integer reminderInterval = Integer.parseInt(sm.getStringParam("reminder_interval", true, true));
            if (app_name == null || app_name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty app name");
            if (description == null)
                description = "";
            if (account_information == null || account_information.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "Account information are null.");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id);
            Channel channel = null;
            if (channel_id != null) {
                channel = team.getChannelWithId(channel_id);
                teamUser_owner = channel.getRoom_manager();
                if (!channel.getTeamUsers().contains(teamUser_owner) && !teamUser_owner.isTeamAdmin())
                    throw new HttpServletException(HttpStatus.Forbidden, "You don't have access to this channel.");
            }
            List<JSONObject> accountInformationList = new LinkedList<>();
            for (Object accountInformationObj : account_information) {
                JSONObject accountInformation = (JSONObject) accountInformationObj;
                accountInformationList.add(accountInformation);
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            ClassicApp classicApp = ClassicApp.createShareableClassicApp(app_name, website, accountInformationList, teamUser_owner, reminderInterval, sm);
            classicApp.becomeShareable(db, team, teamUser_owner, team_user_id, channel, description);
            for (Object receiver : receivers) {
                JSONObject receiver_json = (JSONObject) receiver;
                Integer receiver_id = (Integer) receiver_json.get("team_user_id");
                TeamUser teamUser_tenant = team.getTeamUserWithId(receiver_id);
                SharedApp sharedApp = classicApp.share(teamUser_owner, teamUser_tenant, channel, team, receiver_json, sm);
                if (teamUser_tenant == sm.getTeamUserForTeam(team))
                    sharedApp.accept(db);
                else {
                    String url = ((channel == null) ? ("@" + teamUser_tenant.getDb_id()) : channel.getDb_id().toString()) + "?app_id=" + classicApp.getDBid();
                    teamUser_tenant.addNotification(teamUser_owner.getUsername() + " sent you " + classicApp.getName() + " in " + (channel == null ? "your Personal Space" : ("#" + channel.getName())), url, classicApp.getLogo(), sm.getTimestamp(), db);
                }
                classicApp.addSharedApp(sharedApp);
                team.getAppManager().addSharedApp(sharedApp);
            }
            db.commitTransaction(transaction);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.ADDED, classicApp.getShareableJson(), classicApp.getOrigin()));
            sm.setSuccess(classicApp.getShareableJson());
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
