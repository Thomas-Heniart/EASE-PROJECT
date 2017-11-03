package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/teams/CreateTeamSingleCard")
public class CreateTeamSingleCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeTeamUserOfTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            if (!channel.getTeamUsers().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.Forbidden, "You must be part of the room.");
            Integer website_id = sm.getIntParam("website_id", true, false);
            JSONObject account_information_obj = sm.getJsonParam("account_information", false, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id);
            Integer reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Reminder interval cannot be under 0");
            Map<String, String> account_information = website.getInformationNeeded(account_information_obj);
            String team_key = teamUser_connected.getDeciphered_teamKey();
            Account account = AccountFactory.getInstance().createAccountFromMap(account_information, team_key, reminder_interval);
            TeamCard teamCard = new TeamSingleCard(team, channel, website, reminder_interval, account);
            JSONObject receivers = sm.getJsonParam("receivers", false, false);
            sm.saveOrUpdate(teamCard);
            for (Object object : receivers.entrySet()) {
                Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) object;
                Integer teamUser_id = Integer.valueOf(entry.getKey());
                Boolean allowed_to_see_password = (Boolean) entry.getValue().get("allowed_to_see_password");
                TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
                if (!channel.getTeamUsers().contains(teamUser))
                    throw new HttpServletException(HttpStatus.BadRequest, "All receivers must belong to the channel");
                Account account1 = AccountFactory.getInstance().createAccountFromMap(account_information, team_key, reminder_interval);
                AppInformation appInformation = new AppInformation(website.getName());
                App app = new ClassicApp(appInformation, website, account1);
                TeamCardReceiver teamCardReceiver = new TeamSingleCardReceiver(app, teamCard, teamUser, allowed_to_see_password);
                sm.saveOrUpdate(teamCardReceiver);
                teamCard.addTeamCardReceiver(teamCardReceiver);
            }
            channel.addTeamCard(teamCard);
            team.addTeamCard(teamCard);
            sm.setSuccess(teamCard.getJson());
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
