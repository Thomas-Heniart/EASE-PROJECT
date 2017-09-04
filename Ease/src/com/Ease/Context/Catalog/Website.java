package com.Ease.Context.Catalog;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Variables;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletContext;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Website {

    public void setName(String name, DataBaseConnection db) throws HttpServletException {
        if (this.name.equals(name))
            return;
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websites SET website_name = ? WHERE id = ?;");
            request.setString(name);
            request.setInt(this.db_id);
            request.set();
            this.name = name;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setLandingUrl(String landing_url, DataBaseConnection db) throws HttpServletException {
        if (this.website_homepage.equals(landing_url))
            return;
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websites SET website_homepage = ? WHERE id = ?;");
            request.setString(landing_url);
            request.setInt(this.db_id);
            request.set();
            this.website_homepage = landing_url;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setLoginUrl(String login_url, DataBaseConnection db) throws HttpServletException {
        if (this.loginUrl.equals(login_url))
            return;
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websites SET login_url = ? WHERE id = ?;");
            request.setString(login_url);
            request.setInt(this.db_id);
            request.set();
            this.loginUrl = login_url;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setFolder(String folder, DataBaseConnection db) throws HttpServletException {
        if (this.folder.equals(folder))
            return;
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websites SET folder = ? WHERE id = ?;");
            request.setString(folder);
            request.setInt(this.db_id);
            request.set();
            this.folder = folder;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setIntegrated(Boolean integrated, DataBaseConnection db) throws HttpServletException {
        this.websiteAttributes.setIntegrated(integrated, db);
    }

    public void removeFromDb(DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            DatabaseRequest request = db.prepareRequest("DELETE FROM websitesAndGroupsMap WHERE website_id = ?;");
            request.setInt(this.db_id);
            request.set();
            request = db.prepareRequest("DELETE FROM websitesLogWithMap WHERE website_id = ? OR website_logwith_id = ?;");
            request.setInt(this.db_id);
            request.setInt(this.db_id);
            request.set();
            request = db.prepareRequest("DELETE FROM websitesInformations WHERE website_id = ?;");
            request.setInt(this.db_id);
            request.set();
            request = db.prepareRequest("DELETE FROM websites WHERE id = ?;");
            request.setInt(this.db_id);
            request.set();
            this.websiteAttributes.removeFromDb(db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public enum WebsiteData {
        NOTHING,
        ID,
        LOGIN_URL,
        NAME,
        FOLDER,
        SSO,
        NO_LOGIN,
        WEBSITE_HOMEPAGE,
        RATIO,
        POSITION,
        WEBSITE_ATTRIBUTES_ID
    }

    public static Website createWebsite(String url, String name, String homePage, String folder, boolean haveLoginButton, boolean noLogin, boolean is_public, String[] haveLoginWith, String[] infoNames, String[] infoTypes, String[] placeholders, String[] placeholderIcons, Catalog catalog, Integer ssoId, String team_id, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM websites WHERE folder = ? AND website_name = ?;");
        request.setString(folder);
        request.setString(name);
        DatabaseResult rs = request.get();
        if (rs.next())
            throw new GeneralException(ServletManager.Code.UserMiss, "This website already exists");
        int transaction = db.startTransaction();
        WebsiteAttributes attributes = WebsiteAttributes.createWebsiteAttributes(is_public, db);
        request = db.prepareRequest("INSERT INTO websites VALUES (null, ?, ?, ?, ?, ?, ?, 0, 1, ?);");
        request.setString(url);
        request.setString(name);
        request.setString(folder);
        if (ssoId == null || ssoId.equals(""))
            request.setNull();
        else
            request.setInt(ssoId);
        request.setBoolean(noLogin);
        request.setString(homePage);
        request.setInt(attributes.getDbId());
        Integer db_id = request.set();

        List<WebsiteInformation> infos = new LinkedList<>();
        if (!noLogin) {
            for (int i = 0; i < infoNames.length; i++) {
                infos.add(WebsiteInformation.createInformation(db_id, infoNames[i], infoTypes[i], String.valueOf(i), placeholders[i], placeholderIcons[i], db));
            }
        }

        if (haveLoginButton) {
            request = db.prepareRequest("INSERT INTO loginWithWebsites VALUES (null, ?);");
            request.setInt(db_id);
            request.set();
        }

        List<Website> loginWithWebsites = new LinkedList<Website>();
        if (haveLoginWith != null) {
            for (int i = 0; i < haveLoginWith.length; i++) {
                DatabaseRequest request2 = db.prepareRequest("SELECT id FROM loginWithWebsites WHERE website_id = ?;");
                request2.setInt(haveLoginWith[i]);
                DatabaseResult rs2 = request2.get();
                if (rs2.next()) {
                    request = db.prepareRequest("INSERT INTO websitesLogWithMap VALUES (null, ?, ?);");
                    request.setInt(db_id);
                    request.setInt(rs2.getString(1));
                    request.set();
                }
                loginWithWebsites.add(catalog.getWebsiteWithId(Integer.parseInt(haveLoginWith[i])));
            }
        }
        db.commitTransaction(transaction);
        Website newWebsite = new Website(db_id, name, url, folder, null, noLogin, homePage, 0, 1, infos, attributes, loginWithWebsites);
        WebsitesVisitedManager websitesVisitedManager = (WebsitesVisitedManager) context.getAttribute("websitesVisitedManager");
        int visits = websitesVisitedManager.websiteDone(newWebsite.getHostname(), db);
        attributes.setVisits(visits, db);
        if (ssoId != null && !ssoId.equals("")) {
            Sso sso = catalog.getSsoWithDbId(ssoId);
            sso.addWebsite(newWebsite);
            newWebsite.setSso(sso);
        }
        if (team_id != null && !team_id.equals("")) {
            request = db.prepareRequest("INSERT INTO teamAndWebsiteMap values (null, ?, ?);");
            request.setInt(team_id);
            request.setInt(newWebsite.getDb_id());
            newWebsite.addTeamId(team_id);
        }
        return newWebsite;
    }

    public static Website createWebsite(String url, String website_name, WebsiteAttributes websiteAttributes, ServletContext context, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            DatabaseRequest request = db.prepareRequest("INSERT INTO websites VALUES (null, ?, ?, ?, ?, ?, ?, 0, 1, ?);");
            request.setString(url);
            request.setString(website_name);
            request.setString("undefined");
            request.setNull();
            request.setBoolean(false);
            request.setString(url);
            request.setInt(websiteAttributes.getDbId());
            Integer db_id = request.set();

            List<WebsiteInformation> infos = new LinkedList<>();
            infos.add(WebsiteInformation.createInformation(db_id, "login", "text", "0", "Login", "fa-user-o", db));
            infos.add(WebsiteInformation.createInformation(db_id, "password", "password", "1", "Password", "fa-lock", db));
            db.commitTransaction(transaction);
            Website newWebsite = new Website(db_id, website_name, url, "undefined", null, false, url, 0, 1, infos, websiteAttributes, null);
            WebsitesVisitedManager websitesVisitedManager = (WebsitesVisitedManager) context.getAttribute("websitesVisitedManager");
            int visits = websitesVisitedManager.websiteDone(newWebsite.getHostname(), db);
            websiteAttributes.setVisits(visits, db);
            return newWebsite;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static List<Website> loadWebsites(DataBaseConnection db, Map<Integer, Sso> ssoIdMap, ServletContext context) throws GeneralException {
        List<Website> websites = new LinkedList<Website>();
        DatabaseResult rs = db.prepareRequest("SELECT websites.* FROM websites LEFT JOIN websiteAttributes ON (website_attributes_id = websiteAttributes.id) ORDER BY new, ratio").get();
        while (rs.next()) {
            Integer db_id = rs.getInt(WebsiteData.ID.ordinal());
            List<WebsiteInformation> website_informations = WebsiteInformation.loadInformations(db_id, db);
            String loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
            String name = rs.getString(WebsiteData.NAME.ordinal());
            String folder = rs.getString(WebsiteData.FOLDER.ordinal());
            Sso sso = ssoIdMap.get(rs.getInt(WebsiteData.SSO.ordinal()));
            boolean noLogin = rs.getBoolean(WebsiteData.NO_LOGIN.ordinal());
            String website_homepage = rs.getString(WebsiteData.WEBSITE_HOMEPAGE.ordinal());
            int ratio = rs.getInt(WebsiteData.RATIO.ordinal());
            int position = rs.getInt(WebsiteData.POSITION.ordinal());
            String websiteAttributesId = rs.getString(WebsiteData.WEBSITE_ATTRIBUTES_ID.ordinal());
            WebsiteAttributes websiteAttributes = null;
            if (websiteAttributesId != null)
                websiteAttributes = WebsiteAttributes.loadWebsiteAttributes(websiteAttributesId, db);
            Website site = new Website(db_id, name, loginUrl, folder, sso, noLogin, website_homepage, ratio, position, website_informations, websiteAttributes);
            websites.add(site);
            if (sso != null)
                sso.addWebsite(site);
            site.loadGroupIds(db);
            site.loadTeamIds(db);
        }
        return websites;
    }

    ///// Check if website exist when scrapp

    public static JSONArray existsInDb(String websiteHost, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        Catalog catalog = (Catalog) sm.getContextAttr("catalog");
        DatabaseResult rs = db.prepareRequest("select websites.* from websites JOIN websiteAttributes ON websites.website_attributes_id = websiteAttributes.id where noLogin=0 AND noScrap = 0;").get();
        JSONArray result = new JSONArray();
        while (rs.next()) {
            String loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
            websiteHost = websiteHost.toLowerCase();
            loginUrl = loginUrl.toLowerCase();
            if (loginUrl.contains(websiteHost)) {
                result.add(String.valueOf((catalog.getWebsiteWithId(rs.getInt(WebsiteData.ID.ordinal())).getDb_id())));
            }
        }
        return result;
    }

    public static String existsInDbFacebook(String appName, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        Catalog catalog = (Catalog) sm.getContextAttr("catalog");
        DatabaseResult rs = db.prepareRequest("select * from websites where id in (select website_id from websitesLogWithMap where website_logwith_id in (select id from loginWithWebsites where website_id in (select id from websites where website_name='Facebook')));").get();
        while (rs.next()) {
            String name = rs.getString(WebsiteData.NAME.ordinal());
            appName = appName.toLowerCase();
            name = name.toLowerCase();
            if (appName.contains(name)) {
                return String.valueOf((catalog.getWebsiteWithId(rs.getInt(WebsiteData.ID.ordinal())).getDb_id()));
            }
        }
        return null;
    }

    public static String existsInDbLinkedin(String appName, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        Catalog catalog = (Catalog) sm.getContextAttr("catalog");
        DatabaseResult rs = db.prepareRequest("select * from websites where id in (select website_id from websitesLogWithMap where website_logwith_id in (select id from loginWithWebsites where website_id in (select id from websites where website_name='Linkedin')));").get();
        while (rs.next()) {
            String name = rs.getString(WebsiteData.NAME.ordinal());
            appName = appName.toLowerCase();
            name = name.toLowerCase();
            if (appName.contains(name)) {
                return String.valueOf(rs.getInt(WebsiteData.ID.ordinal()));
            }
        }
        return null;
    }

    //// -------

    protected Integer db_id;
    protected String name;
    protected String loginUrl;
    protected String folder;
    protected int position;
    protected Sso sso;
    protected boolean noLogin;
    protected String website_homepage;
    protected int ratio;
    protected WebsiteAttributes websiteAttributes;
    protected List<WebsiteInformation> website_informations;
    protected List<Website> loginWithWebsites;
    protected List<String> groupIds;
    protected List<String> teamIds = new LinkedList<>();

    public Website(Integer db_id, String name, String loginUrl, String folder, Sso sso, boolean noLogin, String website_homepage, int ratio, int position, List<WebsiteInformation> website_informations, WebsiteAttributes websiteAttributes) {
        this.db_id = db_id;
        this.loginUrl = loginUrl;
        this.folder = folder;
        this.sso = sso;
        this.noLogin = noLogin;
        this.website_homepage = website_homepage;
        this.ratio = ratio;
        this.website_informations = website_informations;
        this.name = name;
        this.position = position;
        this.loginWithWebsites = new LinkedList<Website>();
        this.websiteAttributes = websiteAttributes;
        this.groupIds = new LinkedList<String>();
    }

    public Website(Integer db_id, String name, String loginUrl, String folder, Sso sso, boolean noLogin, String website_homepage, int ratio, int position, List<WebsiteInformation> website_informations, WebsiteAttributes websiteAttributes, List<Website> loginWithWebsites) {
        this.db_id = db_id;
        this.loginUrl = loginUrl;
        this.folder = folder;
        this.sso = sso;
        this.noLogin = noLogin;
        this.website_homepage = website_homepage;
        this.ratio = ratio;
        this.website_informations = website_informations;
        this.name = name;
        this.position = position;
        this.loginWithWebsites = loginWithWebsites;
        this.websiteAttributes = websiteAttributes;
        this.groupIds = new LinkedList<String>();
    }

    public void loadGroupIds(DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT group_id FROM websitesAndGroupsMap JOIN groups ON (websitesAndGroupsMap.group_id = groups.id) WHERE website_id = ?;");
        request.setInt(this.db_id);
        DatabaseResult rs = request.get();
        while (rs.next()) {
            String parent_id = rs.getString(1);
            this.groupIds.add(parent_id);
            this.loadSubGroupIds(parent_id, db);
        }
    }

    public void loadSubGroupIds(String parent_id, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT id FROM groups WHERE parent = ?;");
        request.setInt(parent_id);
        DatabaseResult rs = request.get();
        while (rs.next()) {
            String newParent_id = rs.getString(1);
            this.groupIds.add(newParent_id);
            this.loadSubGroupIds(newParent_id, db);
        }
    }

    public void loadTeamIds(DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT team_id FROM teamAndWebsiteMap WHERE website_id = ?;");
        request.setInt(this.getDb_id());
        DatabaseResult rs = request.get();
        while (rs.next())
            this.teamIds.add(rs.getString(1));
    }

    public Integer getDb_id() {
        return this.db_id;
    }

    public List<WebsiteInformation> getInfos() {
        return website_informations;
    }

    public Map<String, String> getNeededInfos(ServletManager sm) throws GeneralException {
        Map<String, String> infos = new HashMap<String, String>();
        for (WebsiteInformation info : website_informations) {
            String info_name = info.getInformationName();
            String value = sm.getServletParam(info_name, false);
            if (value == null || value.isEmpty()) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong info: " + info_name + ".");
            }
            if (info_name.equals("password")) {
                //Mettre un param keyDate dans le post si besoin de decrypter en RSA. Correspond à la private key RSA,
                String keyDate = sm.getServletParam("keyDate", true);
                if (keyDate != null && !keyDate.equals("")) {
                    value = RSA.Decrypt(value, Integer.parseInt(keyDate));
                }
                //value = sm.getUser().encrypt(value);
            }
            infos.put(info_name, value);
        }
        return infos;
    }

    public Map<String, String> getNeededInfosForEdition(ServletManager sm) throws GeneralException {
        Map<String, String> infos = new HashMap<String, String>();
        for (WebsiteInformation info : website_informations) {
            String info_name = info.getInformationName();
            String value = sm.getServletParam(info_name, false);
            if (value == null || value.equals("")) {
                if (info_name.equals("password"))
                    continue;
                else
                    throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong info: " + info_name + ".");
            }
            if (info_name.equals("password")) {
                //Mettre un param keyDate dans le post si besoin de decrypter en RSA. Correspond à la private key RSA,
                String keyDate = sm.getServletParam("keyDate", true);
                if (keyDate != null && !keyDate.equals(""))
                    value = RSA.Decrypt(value, Integer.parseInt(keyDate));
            }
            infos.put(info_name, value);
        }
        return infos;
    }

    public void loadLoginWithWebsites(DataBaseConnection db, Catalog catalog) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT loginWithWebsites.website_id FROM loginWithWebsites JOIN websitesLogWithMap ON loginWithWebsites.id = website_logwith_id WHERE websitesLogWithMap.website_id = ?;");
        request.setInt(this.db_id);
        DatabaseResult rs = request.get();
        while (rs.next()) {
            this.loginWithWebsites.add(catalog.getWebsiteWithId(rs.getInt(1)));
        }
    }

    public Sso getSso() {
        return this.sso;
    }

    public void setSso(Sso sso) {
        this.sso = sso;
    }

    public int getSsoId() {
        if (this.sso == null)
            return -1;
        else
            return this.sso.getDbid();
    }

    public String getName() {
        return this.name;
    }

    public String getFolder() {
        return Variables.WEBSITES_PATH + this.folder + "/";
    }

    public String getDbFolder() {
        return this.folder;
    }

    public String getAbsolutePath() {
        return Variables.PROJECT_PATH + this.getFolder();
    }

    public String getUrl() {
        return this.loginUrl;
    }

    public String getHomePageUrl() {
        return this.website_homepage;
    }

    public String getLogo() {
        return this.getFolder() + "logo.png";
    }

    public List<WebsiteInformation> getInformations() {
        return this.website_informations;
    }

    public boolean isInPublicCatalog() {
        return this.websiteAttributes != null && this.groupIds.isEmpty();
    }

    public boolean noLogin() {
        return this.noLogin;
    }

    public boolean isPublic() {
        if (this.websiteAttributes == null)
            return true;
        return this.websiteAttributes.isPublic();
    }

    public void setPublic(boolean is_public, DataBaseConnection db) throws HttpServletException {
        if (is_public)
            this.websiteAttributes.bePublic(db);
        else
            this.websiteAttributes.bePrivate(db);
    }

    public boolean isIntegrated() {
        return this.websiteAttributes.isIntegrated();
    }

    public boolean isNew() {
        if (this.websiteAttributes == null)
            return false;
        return this.websiteAttributes.isNew();
    }

    public void setNew(boolean b, DataBaseConnection db) throws GeneralException {
        if (this.websiteAttributes == null)
            return;
        this.websiteAttributes.setNew(b, db);
    }

    public JSONObject getJSON(ServletManager sm) throws GeneralException {
        JSONParser parser = new JSONParser();
        try {
            JSONObject a = (JSONObject) parser.parse(new FileReader(this.getAbsolutePath() + "connect.json"));
            a.put("loginUrl", loginUrl);
            a.put("website_name", this.name);
            a.put("siteSrc", this.getFolder());
            a.put("img", Variables.URL_PATH + this.getLogo());
            return a;
        } catch (IOException | ParseException e) {
            throw new GeneralException(ServletManager.Code.InternError, e);
        }
    }

    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("website_name", this.name);
        jsonObject.put("img", this.getLogo());
        jsonObject.put("loginUrl", this.loginUrl);
        jsonObject.put("websiteUrl", this.website_homepage);
        JSONArray websiteInformation = new JSONArray();
        for (WebsiteInformation websiteInformation1 : this.website_informations)
            websiteInformation.add(websiteInformation1.getJson());
        jsonObject.put("websiteInformation", websiteInformation);
        jsonObject.put("id", this.getDb_id());
        return jsonObject;
    }

    public boolean loginUrlMatch(String url) {
        if (this.loginUrl.startsWith("http")) {
            String[] loginUrlSplitted = this.loginUrl.split("\\/*\\/");
            return url.contains(loginUrlSplitted[1]);
        }
        if (url.length() > this.loginUrl.length())
            return url.contains(this.loginUrl);
        return this.loginUrl.contains(url);

    }

    public boolean homepageUrlMatch(String url) {
        if (this.website_homepage.startsWith("http")) {
            String[] homepageUrlSplitted = this.website_homepage.split("\\/*\\/");
            return url.contains(homepageUrlSplitted[1]);
        }
        if (url.length() > this.website_homepage.length())
            return url.contains(this.website_homepage);
        return this.website_homepage.contains(url);
    }

    @SuppressWarnings("unchecked")
    public JSONObject getJsonForCatalog(User user) {
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("id", this.getDb_id());
        res.put("logo", this.getLogo());
        JSONArray logWithWebsites = new JSONArray();
        for (Website logWithWebsite : this.loginWithWebsites)
            logWithWebsites.add(logWithWebsite.getDb_id());
        res.put("loginWith", logWithWebsites);
        if (this.sso != null)
            res.put("ssoId", this.sso.getDbid());
        else
            res.put("ssoId", -1);
        JSONArray team_ids = new JSONArray();
        for (String team_id : this.teamIds)
            team_ids.add(team_id);
        res.put("team_ids", team_ids);
        res.put("url", this.website_homepage);
        JSONArray inputs = new JSONArray();
        for (WebsiteInformation websiteInformation : this.website_informations) {
            inputs.add(websiteInformation.getJson());
        }
        res.put("inputs", inputs);
        res.put("isNew", this.isNew());
        res.put("position", this.position);
        res.put("count", user.getWebsiteCount(this));
        return res;
    }

    public void incrementRatio(DataBaseConnection db) throws GeneralException {
        this.ratio++;
        DatabaseRequest request = db.prepareRequest("UPDATE websites SET ratio = ratio + 1 WHERE id = ?;");
        request.setInt(db_id);
        request.set();
    }

    public void decrementRatio(DataBaseConnection db) throws GeneralException {
        if (this.ratio > 0) {
            this.ratio--;
            DatabaseRequest request = db.prepareRequest("UPDATE websites SET ratio = ratio - 1 WHERE id = ?;");
            request.setInt(db_id);
            request.set();
        }
    }

    public int getRatio() {
        return this.ratio;
    }

    public int getVisits() {
        return this.websiteAttributes.getVisits();
    }

    public boolean isInPublicCatalogForUser(User user) {
        if (!this.isIntegrated())
            return false;
        if (user.isAdmin())
            return true;
        for (Group group : user.getGroups()) {
            if (this.groupIds.contains(group.getDBid()))
                return true;
        }
        for (TeamUser teamUser : user.getTeamUsers()) {
            if (this.teamIds.contains(teamUser.getTeam().getDb_id()))
                return true;
        }
        return (this.groupIds.isEmpty() && this.teamIds.isEmpty() && this.isPublic());
    }

    public boolean isInCatalogForTeam(String team_id) {
        return this.teamIds.isEmpty() || this.teamIds.contains(team_id);
    }

    public String getHostname() {
        String[] urlParsed = this.website_homepage.split("\\.");
        String host;
        if (urlParsed.length == 3)
            host = urlParsed[1];
        else {
            host = urlParsed[0];
            if (host.startsWith("http")) {
                host = host.split("//")[1];
            }
        }
        return host;
    }

    public void turnOff(ServletManager sm) throws GeneralException {
        this.websiteAttributes.turnOff(sm);
    }

    public void turnOn(ServletManager sm) throws GeneralException {
        this.websiteAttributes.turnOn(sm);
    }

    public int compareToWithVisits(Website w2) {
        if (this.getVisits() < w2.getVisits())
            return 1;
        else if (this.getVisits() == w2.getVisits())
            return 0;
        else
            return -1;
    }

    public void increaseVisits(int count, ServletManager sm) throws GeneralException {
        this.websiteAttributes.increaseVisits(count, sm);
    }

    public void blacklist(ServletManager sm) throws GeneralException {
        this.websiteAttributes.blacklist(sm);
    }

    public void whitelist(ServletManager sm) throws GeneralException {
        this.websiteAttributes.whitelist(sm);
    }

    public Boolean isBlacklisted() {
        return this.websiteAttributes.isBlacklisted();
    }

    public List<String> getGroupIds() {
        return this.groupIds;
    }

    public JSONObject getSearchJson() {
        JSONObject res = this.getSimpleJson();
        res.put("id", this.getDb_id());
        return res;
    }

    public JSONObject getSimpleJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("website_name", this.name);
        jsonObject.put("logo", this.getLogo());
        jsonObject.put("pinneable", this.isIntegrated());
        return jsonObject;
    }

    private void addTeamId(String team_id) {
        this.teamIds.add(team_id);
    }

    public JSONObject getInformationJson() {
        JSONObject res = this.getSimpleJson();
        JSONObject information = new JSONObject();
        for (WebsiteInformation websiteInformation : this.website_informations)
            information.put(websiteInformation.getInformationName(), websiteInformation.getInformationJson());
        res.put("information", information);
        return res;
    }
}
