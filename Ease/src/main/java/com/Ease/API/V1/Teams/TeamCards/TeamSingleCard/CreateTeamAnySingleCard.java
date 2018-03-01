package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Catalog.*;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.Crypto.RSA;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/teams/CreateTeamAnySingleCard")
public class CreateTeamAnySingleCard extends HttpServlet {
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
            JSONObject account_information_obj = sm.getJsonParam("account_information", false, false);
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
            Integer reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (reminder_interval < 0 || !team.isValidFreemium())
                reminder_interval = 0;
            Integer teamUser_filler_id = sm.getIntParam("team_user_filler_id", true, true);
            TeamUser teamUser_filler = null;
            Boolean generateMagicLink = sm.getBooleanParam("generate_magic_link", true, true);
            if (generateMagicLink == null)
                generateMagicLink = false;
            Map<String, String> account_information = new HashMap<>();
            if (account_information_obj.length() != 0) {
                sm.decipher(account_information_obj);
                account_information = website.getInformationNeeded(account_information_obj);
                Boolean credentials_provided = sm.getBooleanParam("credentials_provided", true, false);
                if (credentials_provided) {
                    HibernateQuery hibernateQuery = sm.getHibernateQuery();
                    hibernateQuery.queryString("SELECT key FROM ServerPublicKey key");
                    ServerPublicKey serverPublicKey = (ServerPublicKey) hibernateQuery.getSingleResult();
                    WebsiteCredentials websiteCredentials = new WebsiteCredentials(RSA.Encrypt(account_information.get("login"), serverPublicKey.getPublicKey()), RSA.Encrypt(account_information.get("password"), serverPublicKey.getPublicKey()), website, serverPublicKey);
                    sm.saveOrUpdate(websiteCredentials);
                    website.addWebsiteCredentials(websiteCredentials);
                }
            }
            if (teamUser_filler_id != null && teamUser_filler_id != -1)
                teamUser_filler = team.getTeamUserWithId(teamUser_filler_id);
            else if (account_information.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "You must fill the card or choose someone to fill it");
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            Account account = null;
            if (account_information != null && !account_information.isEmpty())
                account = AccountFactory.getInstance().createAccountFromMap(account_information, teamKey, reminder_interval, sm.getHibernateQuery());
            TeamCard teamCard = new TeamSingleCard(name, team, channel, description, website, reminder_interval, account, teamUser_filler);
            JSONObject receivers = sm.getJsonParam("receivers", false, false);
            sm.saveOrUpdate(teamCard);
            for (Object object : receivers.keySet()) {
                String key = String.valueOf(object);
                JSONObject value = receivers.getJSONObject(key);
                Integer teamUser_id = Integer.valueOf(key);
                Boolean allowed_to_see_password = true; //value.getBoolean("allowed_to_see_password");
                TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
                if (!channel.getTeamUsers().contains(teamUser))
                    throw new HttpServletException(HttpStatus.BadRequest, "All receivers must belong to the channel");
                App app;
                if (account != null) {
                    if (website.getWebsiteAttributes().isIntegrated())
                        app = AppFactory.getInstance().createClassicApp(name, website, teamKey, account_information, reminder_interval, sm.getHibernateQuery());
                    else
                        app = AppFactory.getInstance().createAnyApp(name, website, teamKey, account_information, reminder_interval, sm.getHibernateQuery());
                } else {
                    if (website.getWebsiteAttributes().isIntegrated())
                        app = AppFactory.getInstance().createClassicApp(name, website);
                    else
                        app = AppFactory.getInstance().createAnyApp(name, website);
                }
                sm.saveOrUpdate(app);
                TeamCardReceiver teamCardReceiver = new TeamSingleCardReceiver(app, teamCard, teamUser, allowed_to_see_password);
                if (teamUser.isVerified()) {
                    Profile profile = teamUser.getOrCreateProfile(sm.getHibernateQuery());
                    app.setProfile(profile);
                    app.setPosition(profile.getSize());
                    profile.addApp(app);
                }
                sm.saveOrUpdate(teamCardReceiver);
                if (teamUser_filler != null && teamUser.equals(teamUser_filler))
                    NotificationFactory.getInstance().createMustFillAppNotification(teamUser_filler, teamUser_connected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
                if (!teamUser.equals(teamUser_connected))
                    NotificationFactory.getInstance().createAppSentNotification(teamUser, teamUser_connected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
                teamCard.addTeamCardReceiver(teamCardReceiver);
                teamUser.addTeamCardReceiver(teamCardReceiver);
            }
            channel.addTeamCard(teamCard);
            team.addTeamCard(teamCard);
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
