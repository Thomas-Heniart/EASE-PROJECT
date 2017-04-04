package com.Ease.Dashboard.App.WebsiteApp.LogwithApp;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.AppPermissions;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

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
	
	public static LogwithApp loadLogwithApp(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, String websiteAppDBid, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT * from logWithApps WHERE website_app_id= ?;");
		request.setInt(websiteAppDBid);
		DatabaseResult rs = request.get();
		if (rs.next()) {
			String logwith = rs.getString(Data.LOGWITH_APP_ID.ordinal());
			String logwithDBid = rs.getString(Data.ID.ordinal());
			IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
			return new LogwithApp(db_id, profile, position, infos, groupApp, insertDate, idGenerator.getNextId(), site, websiteAppDBid, logwith, logwithDBid);
		} 
		throw new GeneralException(ServletManager.Code.InternError, "Logwith app not complete in db.");
	}
	
	public static LogwithApp createLogwithApp(Profile profile, int position, String name, Website site, WebsiteApp logwith, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Map<String, Object> elevator = new HashMap<String, Object>();
		String websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "logwithApp", site, elevator, sm);
		DatabaseRequest request = db.prepareRequest("INSERT INTO logWithApps VALUES(NULL, ?, ?, NULL);");
		request.setInt(websiteAppDBid);
		request.setInt(logwith.getWebsiteAppDBid());
		String logwithDBid = request.set().toString();
		db.commitTransaction(transaction);
		LogwithApp app = new LogwithApp((String)elevator.get("appDBid"), profile, position, (AppInformation)elevator.get("appInfos"), null, (String)elevator.get("registrationDate"), ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid, logwith.getDBid(), logwithDBid);
		app.rempLogwith(logwith);
		return app;
	}
	
	public static LogwithApp createFromWebsiteApp(WebsiteApp websiteApp, String name, WebsiteApp logwith, ServletManager sm, User user) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String websiteAppDBid = websiteApp.getWebsiteAppDBid();
		DatabaseRequest request = db.prepareRequest("UPDATE websiteApps SET type='logwithApp' WHERE id = ?;");
		request.setInt(websiteAppDBid);
		request.set();
		request = db.prepareRequest("INSERT INTO logWithApps VALUES(NULL, ?, ?, NULL);");
		request.setInt(websiteAppDBid);
		request.setInt(logwith.getWebsiteAppDBid());
		String logwithDBid = request.set().toString();
		LogwithApp newLogwithApp = new LogwithApp(websiteApp.getDBid(), user.getDashboardManager().getProfileFromApp(websiteApp.getSingleId()), websiteApp.getPosition(), websiteApp.getAppInformation(),null, websiteApp.getInsertDate(), websiteApp.getSingleId(), websiteApp.getSite(), websiteAppDBid, logwith.getDBid(), logwithDBid);
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
	
	protected String logwithAppDBid;
	protected String logwithDBid;
	protected WebsiteApp logwith;
	
	public LogwithApp(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, String logwith, String logwithDBid) {
		super(db_id, profile, position, infos, groupApp, insertDate, single_id, site, websiteAppDBid);
		this.logwithDBid = logwith;
		this.logwithAppDBid = logwithDBid;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		DatabaseRequest request = db.prepareRequest("DELETE FROM logWithApps WHERE id = ?;");
		request.setInt(logwithAppDBid);
		request.set();
		super.removeFromDB(sm);
		this.website.decrementRatio(db);
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
	
	public String getLogwithDBid() {
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

	public void fillJson(JSONObject json){
		super.fillJson(json);
		json.put("logWithAppId", this.logwith.getSingleId());
		json.put("type", "logWithApp");
		json.put("website_name", this.website.getName());
	}
	
	public JSONArray getJSON(ServletManager sm) throws GeneralException{
		JSONArray infos = logwith.getJSON(sm);
		JSONObject websiteInfos = (JSONObject) super.getJSON(sm).get(0);
		websiteInfos.put("logWith", logwith.getSite().getName());
		infos.add(websiteInfos);
		return infos;
	}


	public void edit(String name, App logwith, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		this.setName(name, sm);
		if (this.groupApp == null || (!this.groupApp.isCommon() && this.groupApp.getPerms().havePermission(AppPermissions.Perm.EDIT.ordinal()))) {
			if (logwith.getType().equals("ClassicApp") || logwith.getType().equals("LogwithApp")) {
				this.setLogwith((WebsiteApp)logwith, sm);
			}
		}
		db.commitTransaction(transaction);
	}
	
	/* For sancho le robot */
	public boolean isEmpty() {
		return false;
	}
}
