package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteFactory;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
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

@WebServlet("/api/v1/teams/EditTeamAnySingleCard")
public class EditTeamAnySingleCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamSingleCard teamSingleCard = (TeamSingleCard) sm.getHibernateQuery().get(TeamSingleCard.class, team_card_id);
            if (teamSingleCard == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            Team team = teamSingleCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            if (!teamUser.isTeamAdmin() && (teamSingleCard.getTeamUser_filler() == null || !teamUser.equals(teamSingleCard.getTeamUser_filler())))
                throw new HttpServletException(HttpStatus.Forbidden);
            String description = sm.getStringParam("description", true, true);
            if (description != null && description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            teamSingleCard.setDescription(description);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            teamSingleCard.setName(name);
            JSONObject account_information = sm.getJsonParam("account_information", false, true);
            if (account_information == null)
                account_information = new JSONObject();
            sm.decipher(account_information);
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0 || !team.isValidFreemium())
                password_reminder_interval = 0;
            teamSingleCard.setPassword_reminder_interval(password_reminder_interval);
            String teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            teamSingleCard.decipher(teamKey);
            String url = sm.getStringParam("url", false, false);
            if (!Regex.isSimpleUrl(url) || url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
            Website website = teamSingleCard.getWebsite();
            if (!url.equals(teamSingleCard.getWebsite().getLogin_url()) || connection_information.length() != website.getWebsiteInformationList().size()) {
                Catalog catalog = (Catalog) sm.getContextAttr("catalog");
                website = catalog.getWebsiteWithUrl(url, connection_information, sm.getHibernateQuery());
                if (website == null) {
                    String img_url = sm.getStringParam("img_url", false, true);
                    website = WebsiteFactory.getInstance().createWebsiteAndLogo(sm.getUser().getEmail(), url, name, img_url, connection_information, sm.getHibernateQuery());
                }
                if (website.getWebsiteAttributes().isIntegrated()) {
                    for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values()) {
                        AnyApp anyApp = (AnyApp) teamCardReceiver.getApp();
                        Account account = AccountFactory.getInstance().createAccountFromAccount(anyApp.getAccount(), sm.getHibernateQuery());
                        App tmp_app = new ClassicApp(new AppInformation(anyApp.getAppInformation().getName()), website, account);
                        tmp_app.setProfile(anyApp.getProfile());
                        tmp_app.setPosition(anyApp.getPosition());
                        teamCardReceiver.setApp(tmp_app);
                        sm.saveOrUpdate(teamCardReceiver);
                        sm.deleteObject(anyApp);
                        sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CHANGED, teamCardReceiver.getWebSocketJson()));
                    }
                }
                teamSingleCard.setWebsite(website);
            }
            account_information = teamSingleCard.getWebsite().getPresentCredentialsFromJson(account_information);
            if (account_information.length() != 0) {
                if (teamSingleCard.getAccount() == null) {
                    Account account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamSingleCard.getPassword_reminder_interval(), sm.getHibernateQuery());
                    teamSingleCard.setAccount(account);
                    for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values())
                        teamCardReceiver.getApp().setAccount(AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamSingleCard.getPassword_reminder_interval(), sm.getHibernateQuery()));
                    NotificationFactory.getInstance().createAppFilledNotification(teamUser, teamSingleCard, sm.getUserIdMap(), sm.getHibernateQuery());
                    teamSingleCard.setTeamUser_filler(null);
                } else {
                    teamSingleCard.getAccount().edit(account_information, teamSingleCard.getPassword_reminder_interval(), sm.getHibernateQuery());
                    for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values()) {
                        teamCardReceiver.getApp().getAccount().edit(account_information, sm.getHibernateQuery());
                    }
                }
            }
            sm.saveOrUpdate(teamSingleCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamSingleCard.getWebSocketJson()));
            sm.setSuccess(teamSingleCard.getJson());
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
