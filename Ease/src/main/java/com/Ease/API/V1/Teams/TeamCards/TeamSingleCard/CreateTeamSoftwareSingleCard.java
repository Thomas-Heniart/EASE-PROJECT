package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Software;
import com.Ease.Catalog.SoftwareFactory;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamSingleSoftwareCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/CreateTeamSoftwareSingleCard")
public class CreateTeamSoftwareSingleCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            String description = sm.getStringParam("description", true, false);
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter description");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String folder = name.replaceAll("\\W", "_");
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            if (!channel.getTeamUsers().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.Forbidden, "You must be part of the room.");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
            Software software = catalog.getSoftwareWithFolderOrName(name, folder, connection_information, hibernateQuery);
            if (software == null) {
                String logo_url = sm.getStringParam("logo_url", false, true);
                if (logo_url != null && logo_url.length() > 2000)
                    throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter logo_url");
                software = SoftwareFactory.getInstance().createSoftwareAndLogo(name, folder, logo_url, connection_information, hibernateQuery);
            }
            JSONObject account_information = sm.getJsonParam("account_information", false, true);
            sm.decipher(account_information);
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter password_reminder_interval");
            TeamSingleSoftwareCard teamSingleSoftwareCard = new TeamSingleSoftwareCard(name, team, channel, description, software);
            Account account = null;
            String teamKey = sm.getTeamKey(team);
            TeamUser teamUser_filler = null;
            if (account_information == null || account_information.keySet().isEmpty()) {
                Integer teamUser_filler_id = sm.getIntParam("teamUser_filler_id", true, true);
                if (teamUser_filler_id != null && teamUser_filler_id != -1) {
                    teamUser_filler = team.getTeamUserWithId(teamUser_filler_id);
                    teamSingleSoftwareCard.setTeamUser_filler(teamUser_filler);
                }
            } else {
                account_information = software.getAllCredentialsFromJson(account_information);
                account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, password_reminder_interval, hibernateQuery);
                teamSingleSoftwareCard.setAccount(account);
            }
            sm.saveOrUpdate(teamSingleSoftwareCard);
            JSONObject receivers = sm.getJsonParam("receivers", false, false);
            for (Object object : receivers.keySet()) {
                String key = String.valueOf(object);
                JSONObject value = receivers.getJSONObject(key);
                Integer teamUser_id = Integer.valueOf(key);
                Boolean allowed_to_see_password = value.getBoolean("allowed_to_see_password");
                TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
                if (!channel.getTeamUsers().contains(teamUser))
                    throw new HttpServletException(HttpStatus.BadRequest, "All receivers must belong to the channel");
                App app;
                if (account != null)
                    app = AppFactory.getInstance().createSoftwareApp(name, software, teamKey, account_information, password_reminder_interval, hibernateQuery);
                else
                    app = AppFactory.getInstance().createSoftwareApp(name, software);
                TeamCardReceiver teamCardReceiver = new TeamSingleCardReceiver(app, teamSingleSoftwareCard, teamUser, allowed_to_see_password);
                if (teamUser.isVerified()) {
                    Profile profile = teamUser.getOrCreateProfile(sm.getUserWebSocketManager(teamUser.getUser().getDb_id()), sm.getHibernateQuery());
                    app.setProfile(profile);
                    app.setPosition(profile.getSize());
                }
                sm.saveOrUpdate(teamCardReceiver);
                if (teamUser_filler != null && teamUser.equals(teamUser_filler))
                    NotificationFactory.getInstance().createMustFillAppNotification(teamUser_filler, teamUser_connected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
                if (!teamUser.equals(teamUser_connected))
                    NotificationFactory.getInstance().createAppSentNotification(teamUser, teamUser_connected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
                teamSingleSoftwareCard.addTeamCardReceiver(teamCardReceiver);
            }
            channel.addTeamCard(teamSingleSoftwareCard);
            team.addTeamCard(teamSingleSoftwareCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CREATED, teamSingleSoftwareCard.getWebSocketJson()));
            sm.setSuccess(teamSingleSoftwareCard.getJson());
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
