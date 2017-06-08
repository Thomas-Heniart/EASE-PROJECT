package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 08/05/2017.
 */
@WebServlet("/api/v1/teams/CreateShareableSingleApp")
public class ServletCreateShareableSingleApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser_owner = sm.getTeamUserForTeam(team);
            Integer website_id = sm.getIntParam("website_id", true);
            JSONArray account_information = (JSONArray) sm.getParam("account_information", false);
            Integer channel_id = sm.getIntParam("channel_id", true);
            String app_name = sm.getStringParam("name", true);
            String description = sm.getStringParam("description", true);
            Integer reminderInterval = Integer.parseInt(sm.getStringParam("reminder_interval", true));
            if (app_name == null || app_name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty app name");
            if (description == null)
                description = "";
            if (account_information == null || account_information.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "Account information are null.");
            if (reminderInterval == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Reminder cannot be null");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithSingleId(website_id);
            Channel channel = null;
            if (channel_id != null)
                channel = team.getChannelWithId(channel_id);
            // Map<String, String> accountInformationMap = new HashMap<>();
            List<JSONObject> accountInformationList = new LinkedList<>();
            for (Object accountInformationObj : account_information) {
                JSONObject accountInformation = (JSONObject) accountInformationObj;
                /* String info_name = (String) accountInformation.get("info_name");
                String info_value = (String) accountInformation.get("info_value");
                //accountInformationMap.put(info_name, info_value); */
                accountInformationList.add(accountInformation);
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            ClassicApp classicApp = ClassicApp.createShareableClassicApp(app_name, website, accountInformationList, teamUser_owner, reminderInterval, sm);
            classicApp.becomeShareable(sm.getDB(), team, teamUser_owner, channel, description);
            db.commitTransaction(transaction);
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
