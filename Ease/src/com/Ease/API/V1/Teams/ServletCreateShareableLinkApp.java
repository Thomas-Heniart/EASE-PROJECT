package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
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
@WebServlet("/api/v1/teams/CreateShareableLinkApp")
public class ServletCreateShareableLinkApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            String team_id = sm.getServletParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            String channel_id = sm.getServletParam("channel_id", true);
            String app_name = sm.getServletParam("name", true);
            String link = sm.getServletParam("link", true);
            String description = sm.getServletParam("description", false);
            if (app_name == null || app_name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty app name");
            if (link == null || link.equals("") || !Regex.isValidLink(link))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty link.");
            if (description == null || description.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Description is null");
            Channel channel = null;
            if (channel_id != null && !channel_id.equals(""))
                channel = team.getChannelWithId(Integer.parseInt(channel_id));
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            LinkApp linkApp = LinkApp.createShareableLinkApp(app_name, link, sm);
            linkApp.becomeShareable(sm.getDB(), team, teamUser_owner, channel, description);
            db.commitTransaction(transaction);
            sm.setResponse(ServletManager.Code.Success, "ShareableLinkApp created and single_id is " + linkApp.getSingleId());
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
