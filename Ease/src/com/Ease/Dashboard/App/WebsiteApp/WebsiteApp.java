package com.Ease.Dashboard.App.WebsiteApp;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.*;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

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

    public static WebsiteApp loadWebsiteApp(Integer appDBid, Profile profile, Integer position, String insertDate, AppInformation appInfos, GroupApp groupApp, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * from websiteApps WHERE app_id= ?;");
        request.setInt(appDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            Integer websiteAppDBid = rs.getInt(Data.ID.ordinal());
            Integer reminderInterval = rs.getInt("reminderIntervalValue");
            String reminderType = rs.getString("reminderIntervalType");
            Website website = ((Catalog) context.getAttribute("catalog")).getWebsiteWithId(rs.getInt(Data.WEBSITE_ID.ordinal()));
            /* GroupWebsiteApp groupWebsiteApp = null;
            String groupWebsiteId = rs.getString(Data.GROUP_WEBSITE_ID.ordinal());
			if (groupWebsiteId != null)
				groupWebsiteApp = (GroupWebsiteApp) GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupWebsiteId); */
            IdGenerator idGenerator = (IdGenerator) context.getAttribute("idGenerator");
            switch (rs.getString(Data.TYPE.ordinal())) {
                case "websiteApp":
                    return new WebsiteApp(appDBid, profile, position, appInfos, groupApp, insertDate, website, reminderInterval, reminderType, websiteAppDBid);
                case "logwithApp":
                    return LogwithApp.loadLogwithApp(appDBid, profile, position, appInfos, groupApp, insertDate, website, websiteAppDBid, context, db);
                case "classicApp":
                    return ClassicApp.loadClassicApp(appDBid, profile, position, appInfos, groupApp, insertDate, website, websiteAppDBid, context, db);
            }
        }
        throw new GeneralException(ServletManager.Code.InternError, "Website app not complete in db.");
    }

    public static Integer createWebsiteApp(Profile profile, Integer position, String name, String type, Website site, Map<String, Object> elevator, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        Integer appDBid = App.createApp(profile, position, name, "websiteApp", elevator, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, ?, null, null);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        request.setString(type);
        Integer websiteAppDBid = request.set();
        site.incrementRatio(db);
        if (site.getRatio() == 100) {
            SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            mail.sendAwesomeUserEmail(site, db);
        }
        elevator.put("appDBid", appDBid);
        db.commitTransaction(transaction);
        return websiteAppDBid;
    }

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer appDBid = App.createApp(profile, position, name, "websiteApp", elevator, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', null, null);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        Integer websiteAppDBid = request.set();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), site, websiteAppDBid);
    }

    public static WebsiteApp createShareableMultiApp(String name, Website website, Integer reminderValue, PostServletManager sm) throws GeneralException, HttpServletException {
        return createEmptyApp(null, null, name, website, reminderValue, "MONTH", sm);
    }

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, Integer reminderIntervalValue, String reminderIntervalType, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', ?, ?);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        request.setInt(reminderIntervalValue);
        request.setString(reminderIntervalType);
        Integer websiteAppDBid = request.set();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), site, reminderIntervalValue, reminderIntervalType, websiteAppDBid);
    }

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, Integer reminderIntervalValue, String reminderIntervalType, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp', ?, ?);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        request.setInt(reminderIntervalValue);
        request.setString(reminderIntervalType);
        Integer websiteAppDBid = request.set();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), site, reminderIntervalValue, reminderIntervalType, websiteAppDBid);
    }

    public static Integer createSharedWebsiteApp(WebsiteApp websiteApp, Map<String, Object> elevator, Integer team_id, Integer channel_id, Integer team_user_tenant_id, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Integer appDBid = App.createSharedApp(null, null, websiteApp.getName(), "websiteApp", elevator, team_id, channel_id == null ? null : channel_id, team_user_tenant_id, websiteApp, false, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'classicApp', null, null);");
        request.setInt(websiteApp.getSite().getDb_id());
        request.setInt(appDBid);
        Integer websiteAppDBid = request.set();
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
    protected Integer websiteAppDBid;
    protected GroupWebsiteApp groupWebsiteApp;
    protected Integer reminderIntervalValue = null;
    protected String reminderIntervalType = null;


    public WebsiteApp(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, Integer reminderIntervalValue, String reminderIntervalType, Integer websiteAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate);
        this.website = site;
        this.reminderIntervalType = reminderIntervalType;
        this.reminderIntervalValue = reminderIntervalValue;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
    }

    public WebsiteApp(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, Integer websiteAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate);
        this.website = site;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
    }

    public WebsiteApp(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, Integer websiteAppDBid, ShareableApp holder, Integer reminderIntervalValue, String reminderIntervalType) {
        super(db_id, profile, position, infos, groupApp, insertDate, holder);
        this.website = site;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
        this.reminderIntervalType = reminderIntervalType;
        this.reminderIntervalValue = reminderIntervalValue;
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM websiteApps WHERE id = ?;");
        request.setInt(websiteAppDBid);
        request.set();
        super.removeFromDB(db);
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

    public Integer getWebsiteAppDBid() {
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
        json.put("websiteId", website.getDb_id());
        json.put("ssoId", (website.getSso() == null) ? -1 : website.getSso().getDbid());
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
        jsonObject.put("website_id", this.website.getDb_id());
        jsonObject.put("type", this.getType());
        jsonObject.put("isEmpty", this.isEmpty());
        return jsonObject;
    }

    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("logo", this.website.getFolder() + "logo.png");
        return res;
    }

    /* For sancho le robot */
    public boolean isEmpty() {
        return true;
    }

    @Override
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        Boolean canSeeInformation = (Boolean) params.get("canSeeInformation");
        elevator.put("canSeeInformation", canSeeInformation);
        JSONObject account_information = (JSONObject) params.get("account_information");
        App sharedApp = null;
        DatabaseRequest request = null;
        Integer single_id = ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId();
        Integer websiteAppId = null;
        if (account_information == null)
            throw new HttpServletException(HttpStatus.BadRequest);
        websiteAppId = WebsiteApp.createSharedWebsiteApp(this, elevator, team.getDb_id(), channel == null ? null : channel.getDb_id(), teamUser_tenant.getDb_id(), sm);
        String deciphered_teamKey = sm.getTeamUserForTeam(team).getDeciphered_teamKey();
        Boolean adminHasAccess = (Boolean) params.get("adminHasAccess");
        Account account = Account.createSharedAccountFromJson(account_information, deciphered_teamKey, adminHasAccess, this.getReminderIntervalValue(), sm.getDB());
        request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppId);
        request.setInt(account.getDBid());
        Integer classicDBid = request.set();
        sharedApp = new ClassicApp((Integer) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), this.getSite(), websiteAppId, account, classicDBid, this);
        sharedApp.setAdminHasAccess((Boolean) params.get("adminHasAccess"), sm.getDB());
        sharedApp.setTeamUser_tenant(teamUser_tenant);
        sharedApp.setReceived(false);
        sharedApp.setCanSeeInformation(canSeeInformation);
        db.commitTransaction(transaction);
        return sharedApp;
    }

    @Override
    public JSONObject getShareableJson() throws HttpServletException {
        JSONObject res = super.getShareableJson();
        res.put("type", "multi");
        res.put("website", this.website.getInformationJson());
        res.put("password_change_interval", this.getReminderIntervalValue());
        JSONArray jsonArray = (JSONArray) res.get("receivers");
        for (Object object : jsonArray) {
            JSONObject sharedAppObject = (JSONObject) object;
            Integer shared_app_id = (Integer) sharedAppObject.get("shared_app_id");
            App sharedApp = (App) this.getSharedAppWithId(shared_app_id);
            if (sharedApp.isClassicApp() && this.isEmpty()) {
                ClassicApp classicApp = (ClassicApp) sharedApp;
                sharedAppObject.put("account_information", classicApp.getAccount().getInformationJsonWithoutPassword());
                sharedAppObject.put("last_modification", classicApp.getAccount().printLastUpdatedDate());
                sharedAppObject.put("password_must_be_updated", classicApp.getAccount().mustUpdatePassword());
            }
            object = sharedAppObject;
        }
        res.put("receivers", jsonArray);
        return res;
    }

    @Override
    public JSONObject getNeededParams(PostServletManager sm) {
        JSONObject jsonObject = super.getNeededParams(sm);
        if (this.isEmpty()) {
            JSONObject account_information = (JSONObject) sm.getParam("account_information", false);
            if (account_information != null)
                jsonObject.put("account_information", account_information);
        }
        return jsonObject;
    }

    @Override
    public void modifyShareable(DataBaseConnection db, JSONObject editJson, SharedApp sharedApp) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            super.modifyShareable(db, editJson, sharedApp);
            if (this.isEmpty()) {
                Integer reminderInterval = (Integer) editJson.get("reminderInterval");
                if (reminderInterval != null) {
                    DatabaseRequest request = db.prepareRequest("UPDATE websiteApps SET reminderIntervalValue = ? WHERE id = ?;");
                    request.setInt(reminderInterval);
                    request.setInt(this.websiteAppDBid);
                    request.set();
                    this.reminderIntervalValue = reminderInterval;
                    for (SharedApp sharedApp1 : this.getSharedApps()) {
                        ((ClassicApp) sharedApp1).getAccount().setReminderInterval(reminderInterval, db);
                    }
                }
            }
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public JSONObject getJsonWithoutId() {
        JSONObject jsonObject = super.getJsonWithoutId();
        jsonObject.put("website_id", this.website.getDb_id());
        return jsonObject;
    }
}