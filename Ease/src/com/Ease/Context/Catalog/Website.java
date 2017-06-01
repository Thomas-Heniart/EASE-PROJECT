package com.Ease.Context.Catalog;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Context.Variables;
import com.Ease.Context.Group.Group;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.RSA;

public class Website {

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

    private static String last_db_id = "0";

    public static List<Website> loadNewWebsites(Map<String, Sso> ssoDbIdMap, ServletManager sm) throws GeneralException {
        List<Website> newWebsites = new LinkedList<Website>();
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM websites WHERE id > ?;");
        request.setInt(last_db_id);
        DatabaseResult rs = request.get();
        while (rs.next()) {
            String db_id = rs.getString(WebsiteData.ID.ordinal());
            if (Integer.parseInt(db_id) > Integer.parseInt(last_db_id))
                last_db_id = db_id;
            List<WebsiteInformation> website_informations = WebsiteInformation.loadInformations(db_id, db);
            String loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
            String name = rs.getString(WebsiteData.NAME.ordinal());
            String folder = rs.getString(WebsiteData.FOLDER.ordinal());
            Sso sso = ssoDbIdMap.get(rs.getString(WebsiteData.SSO.ordinal()));
            boolean noLogin = rs.getBoolean(WebsiteData.NO_LOGIN.ordinal());
            String website_homepage = rs.getString(WebsiteData.WEBSITE_HOMEPAGE.ordinal());
            int ratio = rs.getInt(WebsiteData.RATIO.ordinal());
            int position = rs.getInt(WebsiteData.POSITION.ordinal());
            String websiteAttributesId = rs.getString(WebsiteData.WEBSITE_ATTRIBUTES_ID.ordinal());
            WebsiteAttributes websiteAttributes = null;
            if (websiteAttributesId != null)
                websiteAttributes = WebsiteAttributes.loadWebsiteAttributes(websiteAttributesId, db);
            int single_id = ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId();
            Website site = new Website(db_id, single_id, name, loginUrl, folder, sso, noLogin, website_homepage, ratio, position, website_informations, websiteAttributes);
            newWebsites.add(site);
            if (sso != null)
                sso.addWebsite(site);
            site.loadGroupIds(db);
            site.loadTeamIds(db);
        }
        return newWebsites;
    }

    public static Website getWebsite(int single_id, ServletManager sm) throws GeneralException {
        @SuppressWarnings("unchecked")
        Map<Integer, Website> websitesMap = (Map<Integer, Website>) sm.getContextAttr("websites");
        Website site = websitesMap.get(single_id);
        if (site == null)
            throw new GeneralException(ServletManager.Code.InternError, "This website dosen't exist!");
        return site;
    }

    public static Website createWebsite(String url, String name, String homePage, String folder, boolean haveLoginButton, boolean noLogin, boolean noScrap, String[] haveLoginWith, String[] infoNames, String[] infoTypes, String[] placeholders, String[] placeholderIcons, Catalog catalog, String ssoId, String team_id, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM websites WHERE folder = ? AND website_name = ?;");
        request.setString(folder);
        request.setString(name);
        DatabaseResult rs = request.get();
        if (rs.next())
            throw new GeneralException(ServletManager.Code.UserMiss, "This website already exists");
        int transaction = db.startTransaction();
        WebsiteAttributes attributes = WebsiteAttributes.createWebsiteAttributes(noScrap, db);
        request = db.prepareRequest("INSERT INTO websites VALUES (null, ?, ?, ?, ?, ?, ?, 0, 1, ?);");
        request.setString(url);
        request.setString(name);
        request.setString(folder);
        System.out.println(ssoId == null || ssoId.equals(""));
        if (ssoId == null || ssoId.equals(""))
            request.setNull();
        else
            request.setInt(ssoId);
        request.setBoolean(noLogin);
        request.setString(homePage);
        request.setInt(attributes.getDbId());
        String db_id = request.set().toString();
        last_db_id = db_id;

        List<WebsiteInformation> infos = new LinkedList<WebsiteInformation>();
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
                loginWithWebsites.add(catalog.getWebsiteWithDBid(haveLoginWith[i]));
            }
        }
        IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
        db.commitTransaction(transaction);
        Website newWebsite = new Website(db_id, idGenerator.getNextId(), name, url, folder, null, noLogin, homePage, 0, 1, infos, attributes, loginWithWebsites);
        WebsitesVisitedManager websitesVisitedManager = (WebsitesVisitedManager) sm.getContextAttr("websitesVisitedManager");
        int visits = websitesVisitedManager.websiteDone(newWebsite.getHostname(), sm);
        attributes.setVisits(visits, sm);
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

    public static List<Website> loadWebsites(DataBaseConnection db, Map<String, Sso> ssoDbIdMap, ServletContext context) throws GeneralException {
        List<Website> websites = new LinkedList<Website>();
        DatabaseResult rs = db.prepareRequest("SELECT websites.* FROM websites LEFT JOIN websiteAttributes ON (website_attributes_id = websiteAttributes.id) ORDER BY new, ratio").get();
        while (rs.next()) {
            String db_id = rs.getString(WebsiteData.ID.ordinal());
            if (Integer.parseInt(db_id) > Integer.parseInt(last_db_id))
                last_db_id = db_id;
            List<WebsiteInformation> website_informations = WebsiteInformation.loadInformations(db_id, db);
            String loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
            String name = rs.getString(WebsiteData.NAME.ordinal());
            String folder = rs.getString(WebsiteData.FOLDER.ordinal());
            Sso sso = ssoDbIdMap.get(rs.getString(WebsiteData.SSO.ordinal()));
            boolean noLogin = rs.getBoolean(WebsiteData.NO_LOGIN.ordinal());
            String website_homepage = rs.getString(WebsiteData.WEBSITE_HOMEPAGE.ordinal());
            int ratio = rs.getInt(WebsiteData.RATIO.ordinal());
            int position = rs.getInt(WebsiteData.POSITION.ordinal());
            String websiteAttributesId = rs.getString(WebsiteData.WEBSITE_ATTRIBUTES_ID.ordinal());
            WebsiteAttributes websiteAttributes = null;
            if (websiteAttributesId != null)
                websiteAttributes = WebsiteAttributes.loadWebsiteAttributes(websiteAttributesId, db);
            int single_id = ((IdGenerator) context.getAttribute("idGenerator")).getNextId();
            Website site = new Website(db_id, single_id, name, loginUrl, folder, sso, noLogin, website_homepage, ratio, position, website_informations, websiteAttributes);
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
                result.add(String.valueOf((catalog.getWebsiteWithDBid(rs.getString(WebsiteData.ID.ordinal())).getSingleId())));
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
                return String.valueOf((catalog.getWebsiteWithDBid(rs.getString(WebsiteData.ID.ordinal())).getSingleId()));
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
                return String.valueOf((catalog.getWebsiteWithDBid(rs.getString(WebsiteData.ID.ordinal())).getSingleId()));
            }
        }
        return null;
    }

    //// -------

    protected String db_id;
    protected int single_id;
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

    public Website(String db_id, int single_id, String name, String loginUrl, String folder, Sso sso, boolean noLogin, String website_homepage, int ratio, int position, List<WebsiteInformation> website_informations, WebsiteAttributes websiteAttributes) {
        this.db_id = db_id;
        this.single_id = single_id;
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

    public Website(String db_id, int single_id, String name, String loginUrl, String folder, Sso sso, boolean noLogin, String website_homepage, int ratio, int position, List<WebsiteInformation> website_informations, WebsiteAttributes websiteAttributes, List<Website> loginWithWebsites) {
        this.db_id = db_id;
        this.single_id = single_id;
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

    public String getDb_id() {
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
        System.out.println(infos.size());
        return infos;
    }

    public void loadLoginWithWebsites(DataBaseConnection db, Catalog catalog) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT loginWithWebsites.website_id FROM loginWithWebsites JOIN websitesLogWithMap ON loginWithWebsites.id = website_logwith_id WHERE websitesLogWithMap.website_id = ?;");
        request.setInt(this.db_id);
        DatabaseResult rs = request.get();
        while (rs.next()) {
            this.loginWithWebsites.add(catalog.getWebsiteWithDBid(rs.getString(1)));
        }
    }

    public int getSingleId() {
        return this.single_id;
    }

    public void setSingleId(int singleId) {
        this.single_id = singleId;
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
            return this.sso.getSingleId();
    }

    public String getName() {
        return this.name;
    }

    public String getFolder() {
        return Variables.WEBSITES_PATH + this.folder + "/";
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

    public String getLoginWith() {
        String res = "";
        Iterator<Website> it = this.loginWithWebsites.iterator();
        while (it.hasNext()) {
            res += it.next().getSingleId();
            if (it.hasNext())
                res += ",";
        }
        return res;
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

    public boolean work() {
        if (this.websiteAttributes == null)
            return true;
        return this.websiteAttributes.isWorking();
    }

    public boolean isNew() {
        if (this.websiteAttributes == null)
            return false;
        return this.websiteAttributes.isNew();
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
        jsonObject.put("single_id", this.getSingleId());
        jsonObject.put("db_id", this.getDb_id());
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
        res.put("singleId", this.single_id);
        res.put("logo", this.getLogo());
        JSONArray logWithWebsites = new JSONArray();
        for (Website logWithWebsite : this.loginWithWebsites)
            logWithWebsites.add(logWithWebsite.getSingleId());
        res.put("loginWith", logWithWebsites);
        if (this.sso != null)
            res.put("ssoId", this.sso.getSingleId());
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

    public void refresh(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM websites WHERE id = ?;");
        request.setInt(this.db_id);
        DatabaseResult rs = request.get();
        rs.next();
        this.loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
        this.name = rs.getString(WebsiteData.NAME.ordinal());
        this.folder = rs.getString(WebsiteData.FOLDER.ordinal());
        this.noLogin = rs.getBoolean(WebsiteData.NO_LOGIN.ordinal());
        this.website_homepage = rs.getString(WebsiteData.WEBSITE_HOMEPAGE.ordinal());
        this.ratio = rs.getInt(WebsiteData.RATIO.ordinal());
        this.position = rs.getInt(WebsiteData.POSITION.ordinal());
        for (WebsiteInformation info : this.website_informations)
            info.refresh(sm);
        this.websiteAttributes.refresh(sm);
    }

    public boolean isInPublicCatalogForUser(User user) {
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
        return (this.groupIds.isEmpty() && this.teamIds.isEmpty());
    }

    public boolean isInCatalogForTeam(String team_id) {
        return this.teamIds.isEmpty() || this.teamIds.contains(team_id);
    }

    public String getHostname() {
        return this.website_homepage.split("\\.")[1];
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
        res.put("id", this.single_id);
        return res;
    }

    public JSONObject getSimpleJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("website_name", this.name);
        jsonObject.put("logo", this.getLogo());
        return jsonObject;
    }

    private void addTeamId(String team_id) {
        this.teamIds.add(team_id);
    }

    public JSONObject getInformationJson() {
        JSONObject res = this.getSimpleJson();
        JSONArray information = new JSONArray();
        for (WebsiteInformation websiteInformation : this.website_informations)
            information.add(websiteInformation.getJson());
        res.put("information", information);
        return res;
    }
}
