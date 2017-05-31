package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
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
@WebServlet("/api/v1/teams/CreateShareableSingleApp")
public class ServletCreateShareableSingleApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            String team_id = sm.getServletParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            String website_id = sm.getServletParam("website_id", true);
            String account_information_string = sm.getServletParam("account_information", false);
            String channel_id = sm.getServletParam("channel_id", true);
            String app_name = sm.getServletParam("name", true);
            String description = sm.getServletParam("description", false);
            String reminderInterval = sm.getServletParam("reminderInterval", true);
            if (app_name == null || app_name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty app name");
            if (website_id == null || website_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Website is null");
            if (description == null || description.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Description is null");
            if (account_information_string == null || account_information_string.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Account information are null.");
            if (reminderInterval == null || reminderInterval.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Reminder cannot be null");
            JSONParser parser = new JSONParser();
            JSONArray account_information = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(account_information_string));
            if (account_information.isEmpty())
                throw new GeneralException(ServletManager.Code.ClientWarning, "Account information is empty.");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithSingleId(Integer.parseInt(website_id));
            Channel channel = null;
            if (channel_id != null && !channel_id.equals(""))
                channel = team.getChannelWithId(Integer.parseInt(channel_id));
            Map<String, String> accountInformationMap = new HashMap<>();
            for (Object accountInformationObj : account_information) {
                JSONObject accountInformation = (JSONObject)accountInformationObj;
                String info_name = (String)accountInformation.get("info_name");
                String info_value = (String)accountInformation.get("info_value");
                accountInformationMap.put(info_name, info_value);
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            ClassicApp classicApp = ClassicApp.createShareableClassicApp(app_name, website, accountInformationMap, teamUser_owner, Integer.parseInt(reminderInterval), sm);
            classicApp.becomeShareable(sm.getDB(), team, teamUser_owner, channel, description);
            db.commitTransaction(transaction);
            sm.setResponse(ServletManager.Code.Success, "ShareableSingleApp created and single_id is " + classicApp.getSingleId());
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
