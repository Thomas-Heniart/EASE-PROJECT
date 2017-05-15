package com.Ease.Dashboard.App.WebsiteApp;

import java.util.HashMap;
import java.util.Map;

import com.Ease.Dashboard.App.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

import javax.servlet.ServletContext;

public class WebsiteApp extends App implements SharedApp, ShareableApp {
    public enum Data {
        NOTHING,
        ID,
        WEBSITE_ID,
        APP_ID,
        GROUP_WEBSITE_ID,
        TYPE
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
			/*GroupWebsiteApp groupWebsiteApp = null;
			String groupWebsiteId = rs.getString(Data.GROUP_WEBSITE_ID.ordinal());
			if (groupWebsiteId != null)
				groupWebsiteApp = (GroupWebsiteApp) GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupWebsiteId);*/
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

    public static String createWebsiteApp(Profile profile, int position, String name, String type, Website site, Map<String, Object> elevator, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, ?);");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        request.setString(type);
        String websiteAppDBid = request.set().toString();
        site.incrementRatio(db);
        if (site.getRatio() == 100) {
            SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            mail.sendAwesomeUserEmail(site, sm);
        }
        elevator.put("appDBid", appDBid);
        db.commitTransaction(transaction);
        return websiteAppDBid;
    }

    public static WebsiteApp createEmptyApp(Profile profile, Integer position, String name, Website site, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp');");
        request.setInt(site.getDb_id());
        request.setInt(appDBid);
        String websiteAppDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("registrationDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid);
    }

    public static String createSharedWebsiteApp(WebsiteApp websiteApp, Map<String, Object> elevator, Integer team_id, Integer channel_id, Integer team_user_tenant_id, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        String appDBid = App.createSharedApp(null, null, websiteApp.getName(), "websiteApp", elevator, false, true, team_id, channel_id == null ? null : channel_id, team_user_tenant_id, websiteApp, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp');");
        request.setInt(websiteApp.getSite().getDb_id());
        request.setInt(appDBid);
        String websiteAppDBid = request.set().toString();
        db.commitTransaction(transaction);
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

    public WebsiteApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id);
        this.website = site;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
    }

    public WebsiteApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, boolean shareable, boolean shared) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, shareable, shared);
        this.website = site;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
    }

    public WebsiteApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, boolean shareable, boolean shared, ShareableApp holder) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, shareable, shared, holder);
        this.website = site;
        this.websiteAppDBid = websiteAppDBid;
        this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
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
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        String appDBid = App.createSharedApp(null, null, this.getName(), "websiteApp", elevator, false, true, team.getDb_id(), (channel == null) ? null : channel.getDb_id(), teamUser_tenant.getDb_id(), this, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteApps VALUES(NULL, ?, ?, NULL, 'websiteApp');");
        request.setInt(this.getSite().getDb_id());
        request.setInt(appDBid);
        String websiteAppDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new WebsiteApp(appDBid, null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("registrationDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), this.getSite(), websiteAppDBid, false, true, this);
    }
}