package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Catalog.*;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCard.TeamSingleSoftwareCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/fill")
public class FillTeamSingleCardServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer card_id = sm.getIntParam("card_id", true, false);
            String uuid = sm.getStringParam("uuid", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamCard teamCard = this.getTeamCard(card_id, uuid, hibernateQuery);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            sm.decipher(account_information);
            if (teamCard.isTeamWebsiteCard()) {
                TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
                Website website = teamSingleCard.getWebsite();
                if (!website.getWebsiteAttributes().isIntegrated()) {
                    String url = sm.getStringParam("url", true, false);
                    JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
                    if (!Regex.isSimpleUrl(url) || url.length() > 2000)
                        throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
                    if (!url.equals(website.getLogin_url()) || connection_information.length() != website.getWebsiteInformationList().size()) {
                        Catalog catalog = (Catalog) sm.getContextAttr("catalog");
                        website = catalog.getWebsiteWithUrl(url, connection_information, sm.getHibernateQuery());
                        if (website == null) {
                            String img_url = sm.getStringParam("img_url", false, true);
                            website = WebsiteFactory.getInstance().createWebsiteAndLogo("nobody@nobody.co", url, teamCard.getName(), img_url, connection_information, sm.getHibernateQuery());
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
                            }
                        }
                        teamSingleCard.setWebsite(website);
                        for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values())
                            ((WebsiteApp) teamCardReceiver.getApp()).setWebsite(website);
                    }
                }
                teamSingleCard.getAccount().edit(account_information, teamSingleCard.getPassword_reminder_interval(), sm.getHibernateQuery());
                for (TeamCardReceiver teamCardReceiver : teamSingleCard.getTeamCardReceiverMap().values())
                    teamCardReceiver.getApp().getAccount().edit(account_information, sm.getHibernateQuery());
                teamSingleCard.setMagicLink(null);
                teamSingleCard.setMagicLinkExpirationDate(null);
            } else {
                TeamSingleSoftwareCard teamSingleSoftwareCard = (TeamSingleSoftwareCard) teamCard;
                JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
                Software software = teamSingleSoftwareCard.getSoftware();
                if (software.isDifferentConnectionInformation(connection_information)) {
                    software = SoftwareFactory.getInstance().createSoftwareAndLogo(software.getName(), software.getFolder(), software.getLogo_url(), connection_information, hibernateQuery);
                    teamSingleSoftwareCard.setSoftware(software);
                    for (TeamCardReceiver teamCardReceiver : teamSingleSoftwareCard.getTeamCardReceiverMap().values())
                        ((SoftwareApp) teamCardReceiver.getApp()).setSoftware(software);
                }
                teamSingleSoftwareCard.getAccount().edit(account_information, teamSingleSoftwareCard.getPassword_reminder_interval(), hibernateQuery);
                for (TeamCardReceiver teamCardReceiver : teamSingleSoftwareCard.getTeamCardReceiverMap().values())
                    teamCardReceiver.getApp().getAccount().edit(account_information, sm.getHibernateQuery());
                teamSingleSoftwareCard.setMagicLink(null);
                teamSingleSoftwareCard.setMagicLinkExpirationDate(null);
            }
            teamCard.getAccount().calculatePasswordScore();
            sm.saveOrUpdate(teamCard);
            sm.setTeam(teamCard.getTeam());
            NotificationFactory.getInstance().createAppFilledNotification(null, teamCard, sm.getUserIdMap(), sm.getHibernateQuery());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamCard.getWebSocketJson()));
            sm.setSuccess("Successfully fill the app");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer card_id = sm.getIntParam("card_id", true, false);
            String uuid = sm.getParam("uuid", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamCard teamCard = this.getTeamCard(card_id, uuid, hibernateQuery);
            TeamUser teamUser = teamCard.getTeamUser_sender();
            JSONObject res = teamCard.getJson();
            if (teamUser != null) {
                JSONObject extra = new JSONObject();
                extra
                        .put("email", teamUser.getEmail())
                        .put("username", teamUser.getUsername())
                        .put("team_name", teamUser.getTeam().getName());
                res.put("extra_information", extra);
            }
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private TeamCard getTeamCard(Integer card_id, String uuid, HibernateQuery hibernateQuery) throws HttpServletException {
        hibernateQuery.queryString("SELECT t FROM TeamSingleCard t WHERE t.db_id = :id AND t.magicLink LIKE :magicLink");
        hibernateQuery.setParameter("id", card_id);
        hibernateQuery.setParameter("magicLink", "%?card_id=" + card_id + "&uuid=" + uuid);
        TeamCard teamCard = (TeamCard) hibernateQuery.getSingleResult();
        if (teamCard == null) {
            hibernateQuery.queryString("SELECT t FROM TeamSingleSoftwareCard t WHERE t.db_id = :id AND t.magicLink LIKE :magicLink");
            hibernateQuery.setParameter("id", card_id);
            hibernateQuery.setParameter("magicLink", "%?card_id=" + card_id + "&uuid=" + uuid);
            teamCard = (TeamCard) hibernateQuery.getSingleResult();
        }
        if (teamCard == null)
            throw new HttpServletException(HttpStatus.BadRequest, "Link expired");
        Long now = new Date().getTime();
        if (teamCard.isTeamWebsiteCard() && ((TeamSingleCard) teamCard).getMagicLinkExpirationDate().getTime() <= now)
            throw new HttpServletException(HttpStatus.BadRequest, "Link expired");
        if (teamCard.isTeamSoftwareCard() && ((TeamSingleSoftwareCard) teamCard).getMagicLinkExpirationDate().getTime() <= now)
            throw new HttpServletException(HttpStatus.BadRequest, "Link expired");
        return teamCard;
    }
}
