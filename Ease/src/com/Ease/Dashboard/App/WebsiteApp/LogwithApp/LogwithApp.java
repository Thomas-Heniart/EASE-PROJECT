package com.Ease.Dashboard.App.WebsiteApp.LogwithApp;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.*;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public class LogwithApp extends WebsiteApp {
    public enum Data {
        NOTHING,
        ID,
        WEBSITE_APP_ID,
        LOGWITH_APP_ID
    }

	/*
     *
	 * Loader And Creator
	 * 
	 */

    public static LogwithApp loadLogwithApp(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, Integer websiteAppDBid, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * from logWithApps WHERE website_app_id= ?;");
        request.setInt(websiteAppDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            Integer logwith = rs.getInt(Data.LOGWITH_APP_ID.ordinal());
            Integer logwithDBid = rs.getInt(Data.ID.ordinal());
            return new LogwithApp(db_id, profile, position, infos, groupApp, insertDate, site, websiteAppDBid, logwith, logwithDBid);
        }
        throw new GeneralException(ServletManager.Code.InternError, "Logwith app not complete in db.");
    }

    public static LogwithApp createLogwithApp(Profile profile, int position, String name, Website site, WebsiteApp logwith, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "logwithApp", site, elevator, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO logWithApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppDBid);
        request.setInt(logwith.getWebsiteAppDBid());
        Integer logwithDBid = request.set();
        db.commitTransaction(transaction);
        LogwithApp app = new LogwithApp((Integer) elevator.get("appDBid"), profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), site, websiteAppDBid, logwith.getDBid(), logwithDBid);
        app.rempLogwith(logwith);
        return app;
    }

    public static LogwithApp createFromWebsiteApp(WebsiteApp websiteApp, String name, WebsiteApp logwith, ServletManager sm, User user) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Integer websiteAppDBid = websiteApp.getWebsiteAppDBid();
        DatabaseRequest request = db.prepareRequest("UPDATE websiteApps SET type='logwithApp' WHERE id = ?;");
        request.setInt(websiteAppDBid);
        request.set();
        request = db.prepareRequest("INSERT INTO logWithApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppDBid);
        request.setInt(logwith.getWebsiteAppDBid());
        Integer logwithDBid = request.set();
        LogwithApp newLogwithApp = new LogwithApp(websiteApp.getDBid(), user.getDashboardManager().getProfileFromApp(websiteApp.getDBid()), websiteApp.getPosition(), websiteApp.getAppInformation(), null, websiteApp.getInsertDate(), websiteApp.getSite(), websiteAppDBid, logwith.getDBid(), logwithDBid);
        newLogwithApp.rempLogwith(logwith);
        user.getDashboardManager().replaceApp(newLogwithApp);
        db.commitTransaction(transaction);
        return newLogwithApp;
    }
	
	/*
	 * 
	 * Constructor
	 * 
	 */

    protected Integer logwithAppDBid;
    protected Integer logwithDBid;
    protected WebsiteApp logwith;

    public LogwithApp(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, Integer websiteAppDBid, Integer logwith, Integer logwithDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, site, websiteAppDBid);
        this.logwithDBid = logwith;
        this.logwithAppDBid = logwithDBid;
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM logWithApps WHERE id = ?;");
        request.setInt(logwithAppDBid);
        request.set();
        super.removeFromDB(db);
        //this.website.decrementRatio(db);
        db.commitTransaction(transaction);
    }
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */

    public WebsiteApp getLogwith() {
        return logwith;
    }

    public Integer getLogwithDBid() {
        return logwithDBid;
    }

    public void rempLogwith(WebsiteApp logwith) {
        this.logwith = logwith;
    }

    public void setLogwith(WebsiteApp logwith, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE logWithApps SET logwith_website_app_id= ? WHERE id = ?;");
        request.setInt(logwith.getDBid());
        request.setInt(db_id);
        request.set();
        this.logwith = logwith;
        this.logwithDBid = logwith.getDBid();
    }

    public void fillJson(JSONObject json) {
        super.fillJson(json);
        json.put("logWithAppId", this.logwith.getDBid());
        json.put("type", "logWithApp");
        json.put("website_name", this.website.getName());
    }

    public JSONArray getJSON(ServletManager sm) throws GeneralException {
        JSONArray infos = logwith.getJSON(sm);
        JSONObject websiteInfos = (JSONObject) super.getJSON(sm).get(0);
        websiteInfos.put("logWith", logwith.getSite().getName());
        websiteInfos.put("app_name", this.informations.getName());
        websiteInfos.put("website_name", this.website.getName());
        websiteInfos.put("type", "logWithApp");
        infos.add(websiteInfos);
        return infos;
    }

    public JSONObject getJSON() {
        JSONObject res = super.getJSON();
        res.put("logWithAppId", this.logwith.getDBid());
        return res;
    }

    public void edit(String name, App logwith, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        this.setName(name, sm.getDB());
        if (this.groupApp == null || (!this.groupApp.isCommon() && this.groupApp.getPerms().havePermission(AppPermissions.Perm.EDIT.ordinal()))) {
            if (logwith.getType().equals("ClassicApp") || logwith.getType().equals("LogwithApp")) {
                this.setLogwith((WebsiteApp) logwith, sm);
            }
        }
        db.commitTransaction(transaction);
    }

    /* For sancho le robot */
    public boolean isEmpty() {
        return false;
    }

    @Override
    public SharedApp share(TeamUser teamUser_tenant, Team team, JSONObject params, PostServletManager sm) throws GeneralException {
        throw new GeneralException(ServletManager.Code.ClientError, "You shouldn't be there");
    }
}
