package com.Ease.API.V1.Update;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteInformation;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.SsoApp;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Update.Update;
import com.Ease.Update.UpdateFactory;
import com.Ease.User.NotificationFactory;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/api/v1/updates")
public class ServletUpdate extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id");
            hibernateQuery.setParameter("user_id", sm.getUser().getDb_id());
            List<Update> updates = hibernateQuery.list();
            String private_key = sm.getUserPrivateKey();
            JSONArray res = new JSONArray();
            for (Update update : updates) {
                update.decipher(private_key);
                res.put(update.getJson());
            }
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String url = sm.getStringParam("url", true, false);
            if (url.equals("") || url.length() > 2000 || !Regex.isSimpleUrl(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Set<String> informationNameSet = new HashSet<>();
            for (Object object : account_information.keySet()) {
                String key = (String) object;
                String value = account_information.optString(key);
                if (value.length() > 117)
                    throw new HttpServletException(HttpStatus.BadRequest, "Cannot encrypt this kind of password");
                informationNameSet.add(key);
            }
            Website website = catalog.getPublicWebsiteWithUrl(url, informationNameSet, hibernateQuery);
            if (website != null) {
                /* Hack for websites with more than 2 fields */
                populateAccountInformation(website, url, account_information);
                /* Find update(s) with this website */
                if (!website.getWebsiteAttributes().isIntegrated()) {
                    hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id AND u.url LIKE :url");
                    hibernateQuery.setParameter("url", website.getLogin_url());
                } else {
                    if (website.getSso() == null) {
                        hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id AND u.website.db_id = :website_id");
                        hibernateQuery.setParameter("website_id", website.getDb_id());
                    } else {
                        hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id AND u.website.sso.db_id = :sso_id");
                        hibernateQuery.setParameter("sso_id", website.getSso().getDb_id());
                    }

                }
                hibernateQuery.setParameter("user_id", user.getDb_id());
            } else {
                /* Find update(s) with same URL */
                hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id AND u.url LIKE :url");
                hibernateQuery.setParameter("user_id", user.getDb_id());
                hibernateQuery.setParameter("url", url);
            }
            List<Update> updates = hibernateQuery.list();
            /* Decipher updates and check if credentials are the same except for password */
            String privateKey = sm.getUserPrivateKey();
            JSONArray res = new JSONArray();
            for (Update update : updates)
                update.decipher(privateKey);
            updates = updates.stream().filter(update -> update.accountMatch(account_information)).collect(Collectors.toList());
            Iterator<Update> updateIterator = updates.iterator();
            while (updateIterator.hasNext()) {
                Update update = updateIterator.next();
                update.edit(account_information, user.getUserKeys().getPublicKey());
                App app = update.getApp();
                if (app == null)
                    continue;
                String teamKey = null;
                String keyUser = sm.getKeyUser();
                if (app.getTeamCardReceiver() != null)
                    teamKey = sm.getTeamKey(app.getTeamCardReceiver().getTeamCard().getTeam());
                app.decipher(keyUser, teamKey);
                if (app.getAccount() != null && app.getAccount().sameAs(account_information)) {
                    hibernateQuery.deleteObject(update);
                    updateIterator.remove();
                }
            }
            populateResponse(res, user, account_information, website, url, hibernateQuery, sm, updates);
            sm.setSuccess(res);
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void populateAccountInformation(Website website, String url, JSONObject account_information) throws HttpServletException {
        try {
            URL aUrl = new URL(url);
            String[] url_parsed = aUrl.getHost().split("\\.");
            String subdomain = "";
            if (url_parsed.length < 2)
                throw new HttpServletException(HttpStatus.BadRequest, "This is not a valid URL");
            else {
                for (int i = 0; i < url_parsed.length - 2; i++)
                    subdomain += url_parsed[i];
                if (subdomain.equals("www"))
                    subdomain = "";
            }
            for (WebsiteInformation websiteInformation : website.getWebsiteInformationList()) {
                if (websiteInformation.getInformation_name().equals("login") || websiteInformation.getInformation_name().equals("password"))
                    continue;
                account_information.put(websiteInformation.getInformation_name(), subdomain);
            }
        } catch (MalformedURLException e) {
            throw new HttpServletException(HttpStatus.BadRequest, "This is not a valid URL");
        }
    }

    private void populateResponse(JSONArray res, User user, JSONObject account_information, Website website, String url, HibernateQuery hibernateQuery, PostServletManager sm, List<Update> updates) throws HttpServletException {
        if (website != null) {
            if (website.getSso() != null) {
                hibernateQuery.queryString("SELECT w FROM WebsiteApp w WHERE w.website.sso.db_id = :sso_id AND w.profile.user.db_id = :user_id");
                hibernateQuery.setParameter("sso_id", website.getSso().getDb_id());
            } else {
                hibernateQuery.queryString("SELECT w FROM WebsiteApp w WHERE w.website.db_id = :website_id AND w.profile.user.db_id = :user_id");
                hibernateQuery.setParameter("website_id", website.getDb_id());
            }
            hibernateQuery.setParameter("user_id", user.getDb_id());
            List<WebsiteApp> websiteApps = hibernateQuery.list();
            Iterator<WebsiteApp> iterator = websiteApps.iterator();
            while (iterator.hasNext()) {
                WebsiteApp websiteApp = iterator.next();
                String teamKey = null;
                if (websiteApp.getTeamCardReceiver() != null)
                    teamKey = sm.getTeamKey(websiteApp.getTeamCardReceiver().getTeamCard().getTeam());
                websiteApp.decipher(sm.getKeyUser(), teamKey);
            }
            if (websiteApps.isEmpty() && updates.isEmpty()) {
                Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, website);
                hibernateQuery.saveOrUpdateObject(tmp);
                res.put(tmp.getJson());
                NotificationFactory.getInstance().createNewUpdateNotification(tmp, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery);
                return;
            } else if (website.getSso() != null) {
                iterator = websiteApps.iterator();
                while (iterator.hasNext()) {
                    WebsiteApp websiteApp = iterator.next();
                    if (!websiteApp.isSsoApp())
                        continue;
                    SsoApp ssoApp = (SsoApp) websiteApp;
                    if (websiteApps.stream().filter(websiteApp1 -> websiteApp1.isSsoApp() && !websiteApp1.equals(websiteApp) && ssoApp.getSsoGroup().equals(((SsoApp) websiteApp1).getSsoGroup())).count() > 0)
                        iterator.remove();
                }
            }
            boolean create_update = true;
            for (WebsiteApp websiteApp : websiteApps) {
                String teamKey = null;
                String keyUser = sm.getKeyUser();
                if (updates.stream().filter(update -> update.getApp() != null && update.getApp().equals(websiteApp)).count() > 0)
                    continue;
                if (websiteApp.getTeamCardReceiver() != null)
                    teamKey = sm.getTeamKey(websiteApp.getTeamCardReceiver().getTeamCard().getTeam());
                websiteApp.decipher(keyUser, teamKey);
                if (websiteApp.getTeamCardReceiver() != null && websiteApp.isEmpty()) {
                    TeamCard teamCard = websiteApp.getTeamCardReceiver().getTeamCard();
                    if (teamCard.isTeamSingleCard()) {
                        TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
                        if (teamSingleCard.getTeamUser_filler() != null && teamSingleCard.getTeamUser_filler().equals(sm.getTeamUser(teamCard.getTeam()))) {
                            Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, websiteApp);
                            hibernateQuery.saveOrUpdateObject(tmp);
                            updates.add(tmp);
                            res.put(tmp.getJson());
                            NotificationFactory.getInstance().createNewUpdateNotification(tmp, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery);
                            continue;
                        }
                    } else {
                        Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, websiteApp);
                        hibernateQuery.saveOrUpdateObject(tmp);
                        updates.add(tmp);
                        res.put(tmp.getJson());
                        NotificationFactory.getInstance().createNewUpdateNotification(tmp, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery);
                        continue;
                    }
                }
                if (websiteApp.getAccount() != null && websiteApp.getAccount().sameAs(account_information)) {
                    create_update = false;
                    continue;
                }
                if (websiteApp.getAccount() != null && websiteApp.getAccount().matchExceptPassword(account_information)) {
                    Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, websiteApp);
                    hibernateQuery.saveOrUpdateObject(tmp);
                    updates.add(tmp);
                    NotificationFactory.getInstance().createNewUpdateNotification(tmp, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery);
                    res.put(tmp.getJson());
                }
            }
            if (create_update)
                create_update = updates.stream().filter(update -> update.passwordMatch(account_information)).count() == 0;
            if (create_update && !url.startsWith("https://accounts.google.com")) {
                Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, website);
                hibernateQuery.saveOrUpdateObject(tmp);
                NotificationFactory.getInstance().createNewUpdateNotification(tmp, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery);
                res.put(tmp.getJson());
            }
        } else if (!url.startsWith("https://accounts.google.com")) {
            if (updates.stream().filter(update -> update.getUrl() != null && update.getUrl().equals(url)).count() > 0)
                return;
            Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, url);
            hibernateQuery.saveOrUpdateObject(tmp);
            NotificationFactory.getInstance().createNewUpdateNotification(tmp, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery);
            res.put(tmp.getJson());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Long id = sm.getLongParam("id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Update update = (Update) hibernateQuery.get(Update.class, id);
            if (update == null || !update.getUser().equals(sm.getUser()))
                throw new HttpServletException(HttpStatus.BadRequest, "This update does not exist");
            if (update.getTeamUser() != null) {
                TeamCard teamCard = update.getTeamCard();
                Team team = teamCard.getTeam();
                sm.needToBeAdminOfTeam(team);
                Hibernate.initialize(teamCard);
                if (teamCard instanceof HibernateProxy)
                    teamCard = (TeamCard) ((HibernateProxy) teamCard).getHibernateLazyInitializer().getImplementation();
                TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
                teamSingleCard.decipher(sm.getTeamKey(team));
                if (!teamSingleCard.getAccount().sameAs(update.getAccountInformation()))
                    for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values())
                        teamCardReceiver.getApp().getAccount().edit(teamSingleCard.getAccount().getAccountInformationJson(), sm.getHibernateQuery());
            }
            sm.deleteObject(update);
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}
