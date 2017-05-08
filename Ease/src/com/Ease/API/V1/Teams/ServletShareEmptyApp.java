package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InterruptedIOException;

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
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithSingleId(Integer.parseInt(website_id));
            WebsiteApp emptyApp = WebsiteApp.createEmptyApp(null, null, app_name, website, sm);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            if (teamUser_ids_string != null) {
                JSONParser parser = new JSONParser();
                JSONArray teamUser_ids = (JSONArray) parser.parse(teamUser_ids_string);
                for (Object teamUser_id : teamUser_ids)
                    emptyApp.share(teamUser.getDb_id(), Integer.parseInt((String)teamUser_id), sm);
            }
            if (channel_id != null) {
                Channel channel = team.getChannelWithId(Integer.parseInt(channel_id));
                emptyApp.share(channel, sm);
            }
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
