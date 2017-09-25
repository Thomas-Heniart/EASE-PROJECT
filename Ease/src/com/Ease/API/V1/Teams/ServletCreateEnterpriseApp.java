package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.EnterpriseApp.EnterpriseAppAttributes;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
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
@WebServlet("/api/v1/teams/CreateEnterpriseApp")
public class ServletCreateEnterpriseApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Integer website_id = sm.getIntParam("website_id", true, false);
            Integer password_change_interval = sm.getIntParam("password_change_interval", true, false);
            String description = sm.getStringParam("description", false, false);
            JSONArray receivers = sm.getArrayParam("receivers", false, false);
            Boolean fill_in_switch = sm.getBooleanParam("fill_in_switch", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            if (!channel.getTeamUsers().contains(teamUser_owner) && !teamUser_owner.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden, "You don't have access to this channel.");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id);
            JSONArray jsonArray = new JSONArray();
            String privateKey = (String) sm.getContextAttr("privateKey");
            for (Object receiver : receivers) {
                JSONObject receiver_obj = (JSONObject) receiver;
                Integer team_user_id = Math.toIntExact((Long) receiver_obj.get("team_user_id"));
                receiver_obj.put("team_user_id", team_user_id);
                JSONObject account_information;
                try {
                    account_information = (JSONObject) receiver_obj.get("account_information");
                } catch (ClassCastException e) {
                    throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter type for account_information, excepted JSON.");
                }
                for (Object entry : account_information.entrySet()) {
                    Map.Entry<String, String> key_value = (Map.Entry<String, String>) entry;
                    account_information.put(key_value.getKey(), RSA.Decrypt(key_value.getValue(), privateKey));
                }
                jsonArray.add(receiver_obj);
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            WebsiteApp websiteApp = WebsiteApp.createShareableMultiApp(website.getName(), website, password_change_interval, fill_in_switch, sm);
            EnterpriseAppAttributes enterpriseAppAttributes = EnterpriseAppAttributes.createEnterpriseAppAttributes(websiteApp.getDBid(), fill_in_switch, db);
            websiteApp.setEnterpriseAppAttributes(enterpriseAppAttributes);
            websiteApp.becomeShareable(sm.getDB(), team, channel, description);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            for (Object receiver : jsonArray) {
                JSONObject receiver_json = (JSONObject) receiver;
                Integer team_user_id = (Integer) receiver_json.get("team_user_id");
                TeamUser teamUser_tenant = team.getTeamUserWithId(team_user_id);
                SharedApp sharedApp = websiteApp.share(teamUser_tenant, team, receiver_json, sm);
                if (teamUser_tenant == teamUser_connected)
                    sharedApp.accept(db);
                else {
                    String url = channel.getDb_id() + "?app_id=" + websiteApp.getDBid();
                    teamUser_tenant.addNotification(teamUser_connected.getUsername() + " sent you " + websiteApp.getName() + " in #" + channel.getName(), url, websiteApp.getLogo(), sm.getTimestamp(), db);
                }
                websiteApp.addSharedApp(sharedApp);
                team.getAppManager().addSharedApp(sharedApp);
            }
            db.commitTransaction(transaction);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.ADDED, websiteApp.getShareableJson(), websiteApp.getOrigin()));
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
