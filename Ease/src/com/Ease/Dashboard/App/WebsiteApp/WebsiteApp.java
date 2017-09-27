package com.Ease.Dashboard.App.WebsiteApp;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Catalog.WebsiteInformation;
import com.Ease.Dashboard.App.*;
import com.Ease.Dashboard.App.EnterpriseApp.EnterpriseAppAttributes;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.AES;
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
        //site.incrementRatio(db);
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

    public static WebsiteApp createShareableMultiApp(String name, Website website, Integer password_change_interval, Boolean fill_in_switch, PostServletManager sm) throws GeneralException, HttpServletException {
        return createEmptyApp(null, null, name, website, password_change_interval, "MONTH", fill_in_switch, sm);
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

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, Integer reminderIntervalValue, String reminderIntervalType, Boolean fill_in_switch, PostServletManager sm) throws GeneralException, HttpServletException {
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

    public static Integer createSharedWebsiteApp(WebsiteApp websiteApp, Map<String, Object> elevator, Integer team_id, Integer team_user_tenant_id, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Integer appDBid = App.createSharedApp(null, null, websiteApp.getName(), "websiteApp", elevator, team_id, team_user_tenant_id, websiteApp, false, sm);
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
    protected EnterpriseAppAttributes enterpriseAppAttributes;


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

    public EnterpriseAppAttributes getEnterpriseAppAttributes() {
        return enterpriseAppAttributes;
    }

    public void setEnterpriseAppAttributes(EnterpriseAppAttributes enterpriseAppAttributes) {
        this.enterpriseAppAttributes = enterpriseAppAttributes;
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
    public String getLogo() {
        return this.website.getFolder() + "logo.png";
    }

    @Override
    public SharedApp share(TeamUser teamUser_tenant, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        Boolean canSeeInformation = (Boolean) params.get("canSeeInformation");
        elevator.put("canSeeInformation", canSeeInformation);
        JSONObject account_information = (JSONObject) params.get("account_information");
        App sharedApp;
        DatabaseRequest request;
        Integer websiteAppId;
        if (account_information.isEmpty()) {
            account_information = new JSONObject();
            for (WebsiteInformation websiteInformation : this.getSite().getInformations())
                account_information.put(websiteInformation.getInformationName(), "");
        }
        websiteAppId = WebsiteApp.createSharedWebsiteApp(this, elevator, team.getDb_id(), teamUser_tenant.getDb_id(), sm);
        String deciphered_teamKey = sm.getTeamUserForTeam(team).getDeciphered_teamKey();
        Boolean adminHasAccess = true;
        Account account = Account.createSharedAccountFromJson(account_information, deciphered_teamKey, adminHasAccess, this.getReminderIntervalValue(), db);
        request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppId);
        request.setInt(account.getDBid());
        Integer classicDBid = request.set();
        sharedApp = new ClassicApp((Integer) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), this.getSite(), websiteAppId, account, classicDBid, this);
        sharedApp.setAdminHasAccess(adminHasAccess, sm.getDB());
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
        if (this.isEmpty())
            res.put("fill_in_switch", this.getEnterpriseAppAttributes().getFill_in_switch());
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
    public JSONObject getNeededParams(PostServletManager sm) throws HttpServletException {
        JSONObject jsonObject = super.getNeededParams(sm);
        if (this.isEmpty()) {
            JSONObject account_information = (JSONObject) sm.getParam("account_information", false, true);
            if (account_information != null)
                jsonObject.put("account_information", account_information);
        }
        return jsonObject;
    }

    @Override
    public void modifyShareable(DataBaseConnection db, JSONObject editJson) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            super.modifyShareable(db, editJson);
            if (this.isEmpty()) {
                Integer password_change_interval = (Integer) editJson.get("password_change_interval");
                if (!password_change_interval.equals(this.getReminderIntervalValue())) {
                    DatabaseRequest request = db.prepareRequest("UPDATE websiteApps SET reminderIntervalValue = ? WHERE id = ?;");
                    request.setInt(password_change_interval);
                    request.setInt(this.websiteAppDBid);
                    request.set();
                    this.reminderIntervalValue = password_change_interval;
                    for (SharedApp sharedApp : this.getSharedApps())
                        ((ClassicApp) sharedApp).getAccount().setReminderInterval(password_change_interval, db);
                }
                Boolean fill_in_switch = (Boolean) editJson.get("fill_in_switch");
                if (fill_in_switch != this.getEnterpriseAppAttributes().getFill_in_switch())
                    this.getEnterpriseAppAttributes().setFill_in_switch(fill_in_switch, db);
            }
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public Integer addPendingTeamUser(TeamUser teamUser, JSONObject params, DataBaseConnection db) throws HttpServletException {
        try {

            JSONObject account_information = (JSONObject) params.get("account_information");
            String team_key = (String) params.get("team_key");
            int transaction = db.startTransaction();
            Integer request_id = super.addPendingTeamUser(teamUser, params, db);
            if (account_information == null || team_key == null) {
                db.commitTransaction(transaction);
                return request_id;
            }
            for (Object entry : account_information.entrySet()) {
                Map.Entry<String, String> key_value = (Map.Entry<String, String>) entry;
                DatabaseRequest request = db.prepareRequest("INSERT INTO enterpriseAppRequests values (null, ?, ?, ?);");
                request.setInt(request_id);
                request.setString(key_value.getKey());
                request.setString(AES.encrypt(key_value.getValue(), team_key));
                request.set();
            }
            db.commitTransaction(transaction);
            return request_id;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setWebsite(Website website, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websiteApps SET website_id = ? WHERE id = ?;");
            request.setInt(website.getDb_id());
            request.setInt(this.getDBid());
            request.set();
            this.website = website;
            for (SharedApp sharedApp : this.getSharedApps())
                ((WebsiteApp) sharedApp).setWebsite(website, db);
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