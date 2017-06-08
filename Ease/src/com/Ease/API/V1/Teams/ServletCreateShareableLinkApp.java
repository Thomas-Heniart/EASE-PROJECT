package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
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
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true);
            String app_name = sm.getStringParam("name", true);
            String url = sm.getStringParam("url", true);
            String description = sm.getStringParam("description", false);
            if (app_name == null || app_name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty app name");
            if (url == null || url.equals("") || !Regex.isValidLink(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url.");
            if (description == null)
                description = "";
            Channel channel = null;
            if (channel_id != null)
                channel = team.getChannelWithId(channel_id);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            LinkApp linkApp = LinkApp.createShareableLinkApp(app_name, url, sm);
            linkApp.becomeShareable(sm.getDB(), team, teamUser_owner, channel, description);
            db.commitTransaction(transaction);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("app_id", Integer.valueOf(linkApp.getDBid()));
            sm.setSuccess(jsonObject);
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
