package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Software;
import com.Ease.Catalog.SoftwareFactory;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamEnterpriseSoftwareCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
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

@WebServlet("/api/v1/teams/CreateTeamSoftwareEnterpriseCard")
public class CreateTeamSoftwareEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!channel.getTeamUsers().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.Forbidden, "You must be part of the room.");
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            String description = sm.getStringParam("description", true, false);
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter description");
            String folder = name.replaceAll("\\W", "_");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
            Software software = catalog.getSoftwareWithFolderOrName(name, folder, connection_information, hibernateQuery);
            if (software == null) {
                String logo_url = sm.getStringParam("logo_url", false, true);
                if (logo_url != null && logo_url.length() > 2000)
                    throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter logo_url");
                software = SoftwareFactory.getInstance().createSoftwareAndLogo(name, folder, logo_url, connection_information, hibernateQuery);
            }
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0 || !team.isValidFreemium())
                password_reminder_interval = 0;
            TeamEnterpriseSoftwareCard teamEnterpriseSoftwareCard = new TeamEnterpriseSoftwareCard(name, team, channel, description, software, password_reminder_interval);
            teamEnterpriseSoftwareCard.setTeamUser_sender(teamUser_connected);
            sm.saveOrUpdate(teamEnterpriseSoftwareCard);
            JSONObject receivers = sm.getJsonParam("receivers", false, false);
            for (Object object : receivers.keySet()) {
                String key = (String) object;
                JSONObject value = receivers.getJSONObject(key);
                Integer teamUser_id = Integer.valueOf(key);
                JSONObject account_information = value.getJSONObject("account_information");
                TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
                if (!channel.getTeamUsers().contains(teamUser))
                    throw new HttpServletException(HttpStatus.BadRequest, "All receivers must belong to the channel");
                Account account = null;
                if (account_information != null && account_information.length() != 0) {
                    sm.decipher(account_information);
                    String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
                    JSONObject accountInformationObj = software.getPresentCredentialsFromJson(account_information);
                    if (accountInformationObj.length() != 0)
                        account = AccountFactory.getInstance().createAccountFromJson(accountInformationObj, teamKey, password_reminder_interval, sm.getHibernateQuery());
                }
                App app;
                System.out.println(account == null);
                if (account == null)
                    app = AppFactory.getInstance().createSoftwareApp(name, software);
                else
                    app = AppFactory.getInstance().createSoftwareApp(name, software, account);
                TeamCardReceiver teamCardReceiver = new TeamEnterpriseCardReceiver(app, teamEnterpriseSoftwareCard, teamUser);
                if (teamUser.isVerified()) {
                    Profile profile = teamUser.getOrCreateProfile(teamEnterpriseSoftwareCard.getChannel(), sm.getHibernateQuery());
                    app.setProfile(profile);
                    app.setPosition(profile.getSize());
                    sm.saveOrUpdate(app);
                    profile.addApp(app);
                }
                if (account != null)
                    ((TeamEnterpriseCardReceiver)teamCardReceiver).calculatePasswordScore();
                sm.saveOrUpdate(teamCardReceiver);
                if (!teamUser.equals(teamUser_connected))
                    NotificationFactory.getInstance().createAppSentNotification(teamUser, teamUser_connected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
                teamEnterpriseSoftwareCard.addTeamCardReceiver(teamCardReceiver);
                teamUser.addTeamCardReceiver(teamCardReceiver);
            }
            channel.addTeamCard(teamEnterpriseSoftwareCard);
            team.addTeamCard(teamEnterpriseSoftwareCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CREATED, teamEnterpriseSoftwareCard.getWebSocketJson()));
            sm.setSuccess(teamEnterpriseSoftwareCard.getJson());
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
