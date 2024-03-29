package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/teams/CreateTeamSingleCard")
public class CreateTeamSingleCard extends HttpServlet {
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
            Integer website_id = sm.getIntParam("website_id", true, false);
            JSONObject account_information_obj = sm.getJsonParam("account_information", false, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id, sm.getHibernateQuery());
            Integer reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (reminder_interval < 0 || !team.isValidFreemium())
                reminder_interval = 0;
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            String description = sm.getStringParam("description", true, true);
            if (description != null && description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            Integer teamUser_filler_id = sm.getIntParam("team_user_filler_id", true, true);
            TeamUser teamUser_filler = null;
            Boolean generateMagicLink = sm.getBooleanParam("generate_magic_link", true, true);
            if (generateMagicLink == null || !team.isValidFreemium())
                generateMagicLink = false;
            Map<String, String> account_information = new HashMap<>();
            if (account_information_obj.length() != 0) {
                sm.decipher(account_information_obj);
                account_information = website.getInformationNeeded(account_information_obj);
            }
            if (teamUser_filler_id != null && !teamUser_filler_id.equals(-1))
                teamUser_filler = team.getTeamUserWithId(teamUser_filler_id);
            else if (account_information.isEmpty() && !generateMagicLink)
                throw new HttpServletException(HttpStatus.BadRequest, "You must fill the or choose someone to fill it");
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            Account account = null;
            if (account_information != null && !account_information.isEmpty())
                account = AccountFactory.getInstance().createAccountFromMap(account_information, teamKey, reminder_interval, sm.getHibernateQuery());
            TeamCard teamCard = new TeamSingleCard(name, team, channel, description, website, reminder_interval, account, teamUser_filler);
            teamCard.setTeamUser_sender(teamUser_connected);
            if (generateMagicLink && account == null) {
                sm.saveOrUpdate(teamCard);
                ((TeamSingleCard)teamCard).generateMagicLink();
                account = AccountFactory.getInstance().createAccountFromMap(new HashMap<>(), teamKey, reminder_interval, sm.getHibernateQuery());
                ((TeamSingleCard)teamCard).setAccount(account);
            }
            JSONObject receivers = sm.getJsonParam("receivers", false, false);
            sm.saveOrUpdate(teamCard);
            for (Object object : receivers.keySet()) {
                String key = String.valueOf(object);
                Integer teamUser_id = Integer.valueOf(key);
                Boolean allowed_to_see_password = true; //value.getBoolean("allowed_to_see_password");
                TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
                if (!channel.getTeamUsers().contains(teamUser))
                    throw new HttpServletException(HttpStatus.BadRequest, "All receivers must belong to the channel");
                App app;
                if (account != null)
                    app = AppFactory.getInstance().createClassicApp(name, website, teamKey, account_information, reminder_interval, sm.getHibernateQuery());
                else
                    app = AppFactory.getInstance().createClassicApp(name, website);
                sm.saveOrUpdate(app);
                TeamCardReceiver teamCardReceiver = new TeamSingleCardReceiver(app, teamCard, teamUser, allowed_to_see_password);
                if (teamUser.isVerified()) {
                    Profile profile = teamUser.getOrCreateProfile(channel, sm.getHibernateQuery());
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
