package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
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

/**
 * Created by thomas on 08/05/2017.
 */
@WebServlet("/api/v1/teams/ShareEmptyApp")
public class ServletShareEmptyApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeTeamUser();
            sm.needToBeTeamUser();
            String website_id = sm.getServletParam("website_id", true);
            String team_id = sm.getServletParam("team_id", true);
            String channel_id = sm.getServletParam("channel_id", true);
            String teamUser_ids_string = sm.getServletParam("teamUser_ids", true);
            String app_name = sm.getServletParam("app_name", true);
            if (app_name == null || app_name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty app name");
            if (website_id == null || website_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Website is null");
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null");
            if (teamUser_ids_string == null || teamUser_ids_string.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "TeamUser array is null");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithSingleId(Integer.parseInt(website_id));
            WebsiteApp emptyApp = WebsiteApp.createEmptyApp(null, null, app_name, website, sm);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            team.addShareableApp(emptyApp);
            teamUser_owner.addShareableApp(emptyApp);
            DataBaseConnection db = sm.getDB();
            Channel channel = null;
            if (channel_id != null)
                channel = team.getChannelWithId(Integer.parseInt(channel_id));
            int transaction = db.startTransaction();
            DatabaseRequest databaseRequest = db.prepareRequest("INSERT INTO shareableApps values (?, ?, ?, ?);");
            databaseRequest.setInt(emptyApp.getDBid());
            databaseRequest.setInt(team.getDb_id());
            databaseRequest.setInt(teamUser_owner.getDb_id());
            databaseRequest.set();
            if (channel == null)
                databaseRequest.setNull();
            else
                databaseRequest.setInt(channel.getDb_id());
            databaseRequest.set();
            JSONParser parser = new JSONParser();
            JSONArray teamUser_ids = (JSONArray) parser.parse(teamUser_ids_string);
            if (teamUser_ids_string != null) {
                for (Object teamUser_id : teamUser_ids) {
                    TeamUser teamUser_tenant = team.getTeamUserWithId(Integer.parseInt((String) teamUser_id));
                    SharedApp sharedApp = emptyApp.share(teamUser_owner, teamUser_tenant, channel, team, new JSONObject(), sm);
                    teamUser_tenant.addSharedApp(sharedApp);
                    if (channel != null)
                        channel.addSharedApp(sharedApp);
                }
            }
            if (channel_id != null && teamUser_ids.isEmpty()) {
                for(TeamUser teamUser_tenant : team.getTeamUsers()) {
                    if (teamUser_tenant == teamUser_owner)
                        continue;
                    SharedApp sharedApp = emptyApp.share(teamUser_owner, teamUser_tenant, channel, team, new JSONObject(), sm);
                    teamUser_tenant.addSharedApp(sharedApp);
                    channel.addSharedApp(sharedApp);
                }
            }
            db.commitTransaction(transaction);
            sm.setLogResponse("EmptyApp shared");
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
