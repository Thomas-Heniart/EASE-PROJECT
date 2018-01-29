package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteFactory;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
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

@WebServlet("/api/v1/teams/CreateTeamAnyEnterpriseCard")
public class CreateTeamAnyEnterpriseCard extends HttpServlet {
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
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String url = sm.getStringParam("url", false, false);
            if (!Regex.isSimpleUrl(url) || url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            String description = sm.getStringParam("description", true, true);
            if (description != null && description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
            Website website = catalog.getWebsiteWithUrl(url, connection_information, sm.getHibernateQuery());
            if (website == null) {
                String img_url = sm.getStringParam("img_url", false, true);
                website = WebsiteFactory.getInstance().createWebsiteAndLogo(sm.getUser().getEmail(), url, name, img_url, connection_information, sm.getHibernateQuery());
            }
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0 || !team.isValidFreemium())
                password_reminder_interval = 0;
            TeamCard teamCard = new TeamEnterpriseCard(name, team, channel, description, website, password_reminder_interval);
            JSONObject receivers = sm.getJsonParam("receivers", false, false);
            sm.saveOrUpdate(teamCard);
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
                    account = AccountFactory.getInstance().createAccountFromMap(website.getInformationFromJson(account_information), teamKey, password_reminder_interval, sm.getHibernateQuery());
                }
                AppInformation appInformation = new AppInformation(name);
                App app;
                if (website.getWebsiteAttributes().isIntegrated())
                    app = new ClassicApp(appInformation, website, account);
                else
                    app = new AnyApp(appInformation, website, account);
                TeamCardReceiver teamCardReceiver = new TeamEnterpriseCardReceiver(app, teamCard, teamUser);
                if (teamUser.isVerified()) {
                    Profile profile = teamUser.getOrCreateProfile(sm.getUserWebSocketManager(teamUser.getUser().getDb_id()), sm.getHibernateQuery());
                    app.setProfile(profile);
                    app.setPosition(profile.getSize());
                }
                sm.saveOrUpdate(teamCardReceiver);
                if (!teamUser.equals(teamUser_connected))
                    NotificationFactory.getInstance().createAppSentNotification(teamUser, teamUser_connected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
                teamCard.addTeamCardReceiver(teamCardReceiver);
                teamUser.addTeamCardReceiver(teamCardReceiver);
            }
            team.addTeamCard(teamCard);
            channel.addTeamCard(teamCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CREATED, teamCard.getWebSocketJson()));
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
