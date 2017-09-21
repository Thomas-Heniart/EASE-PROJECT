package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
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
import java.util.Map;

/**
 * Created by thomas on 08/05/2017.
 */
@WebServlet("/api/v1/teams/CreateSingleApp")
public class ServletCreateSingleApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            Integer website_id = sm.getIntParam("website_id", true, false);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            JSONArray receivers = sm.getArrayParam("receivers", false, false);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            String description = sm.getStringParam("description", true, true);
            Integer password_change_interval = sm.getIntParam("password_change_interval", true, false);
            if (description == null)
                description = "";
            if (account_information.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "Account information are null.");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id);
            Channel channel = team.getChannelWithId(channel_id);
            if (!channel.getTeamUsers().contains(teamUser_connected) && !teamUser_connected.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden, "You don't have access to this channel.");
            String key = (String) sm.getContextAttr("privateKey");
            for (Object entry : account_information.entrySet()) {
                Map.Entry<String, String> type_value = (Map.Entry<String, String>) entry;
                account_information.put(type_value.getKey(), RSA.Decrypt(type_value.getValue(), key));
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            ClassicApp classicApp = ClassicApp.createShareableClassicApp(website.getName(), website, account_information, teamUser_connected, password_change_interval, sm);
            classicApp.becomeShareable(db, team, channel, description);
            for (Object receiver : receivers) {
                JSONObject receiver_json = (JSONObject) receiver;
                Integer receiver_id = Math.toIntExact((Long) receiver_json.get("team_user_id"));
                TeamUser teamUser_tenant = team.getTeamUserWithId(receiver_id);
                SharedApp sharedApp = classicApp.share(teamUser_tenant, team, receiver_json, sm);
                if (teamUser_tenant == sm.getTeamUserForTeam(team))
                    sharedApp.accept(db);
                else {
                    String url = channel.getDb_id() + "?app_id=" + classicApp.getDBid();
                    teamUser_tenant.addNotification(teamUser_connected.getUsername() + " sent you " + classicApp.getName() + " in #" + channel.getName(), url, classicApp.getLogo(), sm.getTimestamp(), db);
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
