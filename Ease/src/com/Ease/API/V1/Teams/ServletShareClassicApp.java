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
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 08/05/2017.
 */
@WebServlet("/api/v1/teams/ShareClassicApp")
public class ServletShareClassicApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            sm.needToBeTeamUser();
            String website_id = sm.getServletParam("website_id", true);
            String team_id = sm.getServletParam("team_id", true);
            String channel_id = sm.getServletParam("channel_id", true);
            String accountAndTeamUserIds_string = sm.getServletParam("accountAndTeamUserIds", true);
            String app_name = sm.getServletParam("app_name", true);
            String description = sm.getServletParam("description", false);
            if (app_name == null || app_name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty app name");
            if (website_id == null || website_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Website is null");
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null");
            if (accountAndTeamUserIds_string == null || accountAndTeamUserIds_string.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "accountAndTeamUserIds_string array is null");
            if (description == null || description.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Description is null");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithSingleId(Integer.parseInt(website_id));
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            Channel channel = null;
            if (channel_id != null)
                channel = team.getChannelWithId(Integer.parseInt(channel_id));
            JSONParser parser = new JSONParser();
            JSONArray accountAndTeamUserIds = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(accountAndTeamUserIds_string));
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            for (Object accountAndTeamUserIdObj :  accountAndTeamUserIds) {
                JSONObject accountAndTeamUserId = (JSONObject) accountAndTeamUserIdObj;
                JSONArray teamUser_tenant_ids = (JSONArray) accountAndTeamUserId.get("teamUser_ids");
                JSONArray accountInformationArray = (JSONArray) accountAndTeamUserId.get("accountInformation");
                Map<String, String> accountInformationMap = new HashMap<>();
                for (Object accountInformationObj : accountInformationArray) {
                    JSONObject accountInformation = (JSONObject)accountInformationObj;
                    String info_name = (String)accountInformation.get("info_name");
                    String info_value = (String)accountInformation.get("info_value");
                    accountInformationMap.put(info_name, info_value);
                }
                ClassicApp shareableApp = ClassicApp.createClassicApp(null, null, app_name, website, accountInformationMap, sm, null);
                teamUser_owner.addShareableApp(shareableApp);
                team.addShareableApp(shareableApp);
                DatabaseRequest databaseRequest = db.prepareRequest("INSERT INTO shareableApps values (?, ?, ?, ?, ?);");
                databaseRequest.setInt(shareableApp.getDBid());
                databaseRequest.setInt(team.getDb_id());
                databaseRequest.setInt(teamUser_owner.getDb_id());
                if (channel == null)
                    databaseRequest.setNull();
                else
                    databaseRequest.setInt(channel_id);
                databaseRequest.setString(description);
                databaseRequest.set();
                JSONObject informationObj = new JSONObject();
                informationObj.put("accountInformation", accountInformationArray);
                for (Object teamUser_tenant_id : teamUser_tenant_ids) {
                    TeamUser teamUser_tenant = team.getTeamUserWithId(Integer.parseInt((String)teamUser_tenant_id));
                    SharedApp sharedApp = shareableApp.share(teamUser_owner, teamUser_tenant, channel, team, informationObj, sm);
                    teamUser_tenant.addSharedApp(sharedApp);
                    if (channel != null)
                        channel.addSharedApp(sharedApp);
                }
            }
            db.commitTransaction(transaction);
            sm.setResponse(ServletManager.Code.Success, "ClassicApp shared");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
