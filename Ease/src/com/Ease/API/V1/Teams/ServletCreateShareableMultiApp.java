package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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
@WebServlet("/api/v1/teams/CreateShareableMultiApp")
public class ServletCreateShareableMultiApp extends HttpServlet {
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
            String website_id = sm.getServletParam("website_id", true);
            String reminderValue = sm.getServletParam("reminderInterval", true);
            String description = sm.getServletParam("description", false);
            if (app_name == null || app_name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty app name");
            if (website_id == null || website_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty link.");
            if (reminderValue == null || reminderValue.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Reminder interval is null.");
            if (description == null || description.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Description is null");
            Channel channel = null;
            if (channel_id != null && !channel_id.equals(""))
                channel = team.getChannelWithId(Integer.parseInt(channel_id));
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithSingleId(Integer.parseInt(website_id));
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            WebsiteApp websiteApp = WebsiteApp.createShareableMultiApp(app_name, website, Integer.parseInt(reminderValue), sm);
            websiteApp.becomeShareable(sm.getDB(), team, teamUser_owner, channel, description);
            db.commitTransaction(transaction);
            sm.setResponse(ServletManager.Code.Success, "ShareableMultiApp created and single_id is " + websiteApp.getSingleId());
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
