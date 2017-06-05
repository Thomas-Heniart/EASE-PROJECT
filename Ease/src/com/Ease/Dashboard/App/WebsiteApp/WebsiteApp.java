package com.Ease.Dashboard.App.WebsiteApp;

import java.util.HashMap;
import java.util.Map;

import com.Ease.Dashboard.App.*;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Servlets.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Mail.SendGridMail;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletContext;

public class WebsiteApp extends App implements SharedApp, ShareableApp {

    public enum Data {
        NOTHING,
        ID,
        WEBSITE_ID,
        APP_ID,
        GROUP_WEBSITE_ID,
        TYPE,
        REMINDER_INTERVAL_VALUE,
        REMINDER_INTERVAL_TYPE
    }

	/*
     *
	 * Loader And Creator
	 * 
	 */

    public static WebsiteApp loadWebsiteApp(String appDBid, Profile profile, Integer position, String insertDate, AppInformation appInfos, GroupApp groupApp, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * from websiteApps WHERE app_id= ?;");
        request.setInt(appDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            String websiteAppDBid = rs.getString(Data.ID.ordinal());
            Website website = ((Catalog) context.getAttribute("catalog")).getWebsiteWithDBid(rs.getString(Data.WEBSITE_ID.ordinal()));
            /* GroupWebsiteApp groupWebsiteApp = null;
            String groupWebsiteId = rs.getString(Data.GROUP_WEBSITE_ID.ordinal());
			if (groupWebsiteId != null)
				groupWebsiteApp = (GroupWebsiteApp) GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupWebsiteId); */
            IdGenerator idGenerator = (IdGenerator) context.getAttribute("idGenerator");
            switch (rs.getString(Data.TYPE.ordinal())) {
                case "websiteApp":
                    return new WebsiteApp(appDBid, profile, position, appInfos, groupApp, insertDate, idGenerator.getNextId(), website, websiteAppDBid);
                case "logwithApp":
                    return LogwithApp.loadLogwithApp(appDBid, profile, position, appInfos, groupApp, insertDate, website, websiteAppDBid, context, db);
                case "classicApp":
                    return ClassicApp.loadClassicApp(appDBid, profile, position, appInfos, groupApp, insertDate, website, websiteAppDBid, context, db);
            }
        }
        throw new GeneralException(ServletManager.Code.InternError, "Website app not complete in db.");
    }

    public static String createWebsiteApp(Profile profile, Integer position, String name, String type, Website site, Map<String, Object> elevator, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, ?, null, null);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        request.setString(type);
        String websiteAppDBid = request.set().toString();
        site.incrementRatio(db);
        if (site.getRatio() == 100) {
            SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            mail.sendAwesomeUserEmail(site, db);
        }
        elevator.put("appDBid", appDBid);
        db.commitTransaction(transaction);
        return websiteAppDBid;
    }

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', null, null);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        String websiteAppDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid);
    }

    public static WebsiteApp createShareableMultiApp(String name, Website website, Integer reminderValue, PostServletManager sm) throws GeneralException, HttpServletException {
        return createEmptyApp(null, null, name, website, reminderValue, "MONTH", sm);
    }

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, Integer reminderIntervalValue, String reminderIntervalType, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', ?, ?);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        request.setInt(reminderIntervalValue);
        request.setString(reminderIntervalType);
        String websiteAppDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, reminderIntervalValue, reminderIntervalType, websiteAppDBid);
    }

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, Integer reminderIntervalValue, String reminderIntervalType, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', ?, ?);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        request.setInt(reminderIntervalValue);
        request.setString(reminderIntervalType);
        String websiteAppDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, reminderIntervalValue, reminderIntervalType, websiteAppDBid);
    }

    public static String createSharedWebsiteApp(WebsiteApp websiteApp, Map<String, Object> elevator, Integer team_id, Integer channel_id, Integer team_user_tenant_id, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        String appDBid = App.createSharedApp(null, null, websiteApp.getName(), "websiteApp", elevator, false, true, team_id, channel_id == null ? null : channel_id, team_user_tenant_id, websiteApp, false, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', null, null);");
        request.setInt(websiteApp.getSite().getDb_id());
        request.setInt(appDBid);
        String websiteAppDBid = request.set().toString();
        db.commitTransaction(transaction);
        elevator.put("appDBid", appDBid);
        return websiteAppDBid;
    }

    public static void Empty(String appId, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM websiteApps WHERE app_id= ?;");
        request.setInt(appId);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This app does not exist");
        String website_app_id = rs.getString(Data.ID.ordinal());
        int transaction = db.startTransaction();
        switch (rs.getString(Data.TYPE.ordinal())) {
            case ("classicApp"):
                request = db.prepareRequest("DELETE FROM accountsInformations WHERE account_id IN (SELECT account_id FROM classicApps WHERE website_app_id = ?);");
                request.setInt(website_app_id);
                request.set();
                request = db.prepareRequest("SELECT account_id FROM classicApps WHERE website_app_id = ?;");
                request.setInt(website_app_id);
                DatabaseResult rs2 = request.get();
                rs2.next();
                int account_id = rs2.getInt(1);
                request = db.prepareRequest("DELETE FROM classicApps WHERE website_app_id = ?;");
                request.setInt(website_app_id);
                request.set();
                request = db.prepareRequest("DELETE FROM accounts WHERE id = ?;");
                request.setInt(account_id);
                request.set();
                break;
            case ("logwithApp"):
                request = db.prepareRequest("DELETE FROM logWithApps WHERE website_app_id = ?;");
                request.setInt(website_app_id);
                request.set();
                break;
            default:
                break;
        }
        request = db.prepareRequest("UPDATE websiteApps SET type='websiteApp' WHERE app_id = ?;");
        request.setInt(appId);
        request.set();
        db.commitTransaction(transaction);
    }

	/*
     *
	 * Constructor
	 * 
	 */

    protected Website website;
    protected String websiteAppDBid;
    protected GroupWebsiteApp groupWebsiteApp;
    protected Integer reminderIntervalValue = null;
    protected String reminderIntervalType = null;


    public WebsiteApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, Integer reminderIntervalValue, String reminderIntervalType, String websiteAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id);
        this.website = site;
        this.reminderIntervalType = reminderIntervalType;
        this.reminderIntervalValue = reminderIntervalValue;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
    }

    public WebsiteApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id);
        this.website = site;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
    }

    public WebsiteApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, ShareableApp holder, Integer reminderIntervalValue, String reminderIntervalType) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, holder);
        this.website = site;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
        this.reminderIntervalType = reminderIntervalType;
        this.reminderIntervalValue = reminderIntervalValue;
    }

    public void removeFromDB(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM websiteApps WHERE id = ?;");
        request.setInt(websiteAppDBid);
        request.set();
        super.removeFromDB(sm);
        db.commitTransaction(transaction);
    }

	/*
     *
	 * Getter And Setter
	 * 
	 */

    public Website getSite() {
        return this.website;
    }

    public String getWebsiteAppDBid() {
        return this.websiteAppDBid;
    }

    public Integer getReminderIntervalValue() {
        return reminderIntervalValue;
    }

    public String getReminderIntervalType() {
        return reminderIntervalType;
    }

	/*
     *
	 * Utils
	 * 
	 */

    public void fillJson(JSONObject json) {
        super.fillJson(json);
        json.put("websiteId", website.getSingleId());
        json.put("ssoId", (website.getSso() == null) ? -1 : website.getSso().getSingleId());
        json.put("imgSrc", this.website.getFolder() + "logo.png");
        json.put("type", "emptyApp");
    }

    public JSONArray getJSON(ServletManager sm) throws GeneralException {
        JSONArray infos = new JSONArray();
        JSONObject websiteInfos = new JSONObject();
        websiteInfos.put("website", website.getJSON(sm));
        infos.add(websiteInfos);
        return infos;
    }

    public JSONObject getJSON() {
        JSONObject jsonObject = super.getJSON();
        jsonObject.put("website_id", this.website.getSingleId());
        jsonObject.put("type", this.getType());
        jsonObject.put("isEmpty", this.isEmpty());
        return jsonObject;
    }

    /* For sancho le robot */
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void modifyShared(ServletManager sm, JSONObject editJson) throws GeneralException {
        throw new GeneralException(ServletManager.Code.ClientError, "Go fuck yourself");
    }

    @Override
    public void deleteShared(ServletManager sm) throws GeneralException {
        this.removeFromDB(sm);
    }

    @Override
    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp) throws GeneralException {
        throw new GeneralException(ServletManager.Code.ClientError, "Go fuck yourself");
    }

    @Override
    public void deleteShareable(ServletManager sm, SharedApp sharedApp) throws GeneralException {
        for (SharedApp sharedApp1 : this.sharedApps)
            sharedApp1.deleteShared(sm);
        this.removeFromDB(sm);
    }

    @Override
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        JSONArray account_information_array = (JSONArray) params.get("account_information");
        App sharedApp = null;
        DatabaseRequest request = null;
        Integer single_id = ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId();
        String websiteAppId = null;
        if (account_information_array == null || account_information_array.isEmpty()) {
            String appDBid = App.createSharedApp(null, null, this.getName(), "websiteApp", elevator, false, true, team.getDb_id(), (channel == null) ? null : channel.getDb_id(), teamUser_tenant.getDb_id(), this, true, sm);
            request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', ?, ?);");
            request.setInt(this.getSite().getDb_id());
            request.setInt(appDBid);
            if (reminderIntervalValue == null) {
                request.setNull();
                request.setNull();
            } else {
                request.setInt(reminderIntervalValue);
                request.setString(reminderIntervalType);
            }
            websiteAppId = request.set().toString();
            sharedApp = new WebsiteApp(appDBid, null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), single_id, this.getSite(), websiteAppId, this, reminderIntervalValue, reminderIntervalType);
        } else {
            websiteAppId = WebsiteApp.createSharedWebsiteApp(this, elevator, team.getDb_id(), channel == null ? null : channel.getDb_id(), teamUser_tenant.getDb_id(), sm);
            String deciphered_teamKey = sm.getTeamUserForTeam(team).getDeciphered_teamKey();
            Boolean adminHasAccess = (Boolean) params.get("adminHasAccess");
            Account account = Account.createSharedAccountFromJson(account_information_array, deciphered_teamKey, adminHasAccess, sm.getDB());
            request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
            request.setInt(websiteAppId);
            request.setInt(account.getDBid());
            String classicDBid = request.set().toString();
            sharedApp = new ClassicApp((String) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), single_id, this.getSite(), websiteAppId, account, classicDBid, this);
            if (adminHasAccess)
                sharedApp.setAdminHasAccess(adminHasAccess, sm.getDB());
            sharedApp.setReceived(false);
        }
        db.commitTransaction(transaction);
        sharedApp.setTeamUser_tenant(teamUser_tenant);
        return sharedApp;
    }

    @Override
    public JSONObject getShareableJson() throws GeneralException {
        JSONObject res = super.getShareableJson();
        res.put("type", "multi");
        res.put("website", this.website.getInformationJson());
        res.put("password_change_interval", this.getReminderIntervalValue());
        JSONArray jsonArray = (JSONArray) res.get("receivers");
        for (Object object : jsonArray) {
            JSONObject sharedAppObject = (JSONObject) object;
            Integer shared_app_id = (Integer) sharedAppObject.get("shared_app_id");
            App sharedApp = (App)this.getSharedAppWithId(shared_app_id);
            if (sharedApp.isClassicApp()) {
                ClassicApp classicApp = (ClassicApp)sharedApp;
                sharedAppObject.put("account_information", classicApp.getAccount().getInformationJsonWithoutPassword());
                sharedAppObject.put("last_modification", classicApp.getAccount().getLastUpdatedDate());
            }


        }
        return res;
    }

    public JSONObject getNeededParams(ServletManager sm) throws GeneralException {
        try {
            JSONObject jsonObject = new JSONObject();
            String account_information = sm.getServletParam("account_information", false);
            String adminHasAccess = sm.getServletParam("adminHasAccess", true);
            jsonObject.put("adminHasAccess", Boolean.parseBoolean(adminHasAccess));
            JSONParser parser = new JSONParser();
            JSONArray account_information_array = (JSONArray) parser.parse(account_information);
            jsonObject.put("account_information", account_information_array);
            return jsonObject;
        } catch (ParseException e) {
            throw new GeneralException(ServletManager.Code.ClientError, e);
        }
    }

    public JSONObject getJsonWithoutId() {
        JSONObject jsonObject = super.getJsonWithoutId();
        jsonObject.put("website_id", this.website.getSingleId());
        return jsonObject;
    }
}