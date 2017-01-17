package com.Ease.Dashboard.App.WebsiteApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class WebsiteApp extends App {
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
	
	public static WebsiteApp loadWebsiteApp(String appDBid, Profile profile, int position, String insertDate, AppInformation appInfos, GroupApp groupApp, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT * from websiteApps WHERE app_id=" + appDBid + ";");
			//System.out.println(appDBid);
			try {
				if (rs.next()) {
					String websiteAppDBid = rs.getString(Data.ID.ordinal());
					Website website = ((Catalog)sm.getContextAttr("catalog")).getWebsiteWithDBid(rs.getString(Data.WEBSITE_ID.ordinal()));
					/*GroupWebsiteApp groupWebsiteApp = null;
					String groupWebsiteId = rs.getString(Data.GROUP_WEBSITE_ID.ordinal());
					if (groupWebsiteId != null)
						groupWebsiteApp = (GroupWebsiteApp) GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupWebsiteId);*/
					IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
					switch (rs.getString(Data.TYPE.ordinal())) {
					case "websiteApp" :
						return new WebsiteApp(appDBid, profile, position, appInfos, groupApp, insertDate, idGenerator.getNextId(), website, websiteAppDBid);
					case "logwithApp" :
						return LogwithApp.loadLogwithApp(appDBid, profile, position, appInfos, groupApp, insertDate, website, websiteAppDBid, sm);
					case "classicApp" :
						return ClassicApp.loadClassicApp(appDBid, profile, position, appInfos, groupApp, insertDate, website, websiteAppDBid, sm);
					}
				} 
				throw new GeneralException(ServletManager.Code.InternError, "Website app not complete in db.");
			} catch (SQLException e) {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
		} catch (GeneralException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static String createWebsiteApp(Profile profile, int position, String name, String type, Website site, Map<String, Object>elevator, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm);
		String websiteAppDBid = db.set("INSERT INTO websiteApps VALUES(NULL, " + site.getDb_id() + ", " + appDBid + ", NULL, '" + type + "');").toString();
		elevator.put("appDBid", appDBid);
		db.commitTransaction(transaction);
		return websiteAppDBid;
	}
	
	public static WebsiteApp createEmptyApp(Profile profile, int position, String name, Website site, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Map<String, Object> elevator = new HashMap<String, Object>();
		String appDBid = App.createApp(profile, position, name, "websiteApp", elevator, sm);
		String websiteAppDBid = db.set("INSERT INTO websiteApps VALUES(NULL, " + site.getDb_id() + ", " + appDBid + ", NULL, 'websiteApp');").toString();
		db.commitTransaction(transaction);
		return new WebsiteApp(appDBid, profile, position, (AppInformation)elevator.get("appInfos"), null, (String)elevator.get("registrationDate"), ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid);
	}
	
	public static void Empty(String appId, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		try {
			ResultSet rs = db.get("SELECT * FROM websiteApps WHERE app_id=" + appId + ";");
			rs.next();
			switch (rs.getString(Data.TYPE.ordinal())) {
				case ("classicApp"):
					//remove classic app for unconnected user;
				break;
				case ("logwithApp"):
					//remove logwith app for unconnected user;
				break;
			}
			db.set("UPDATE websiteApps SET type='websiteApp' WHERE app_id=" + appId + ";");
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected Website 		website;
	protected String		websiteAppDBid;
	protected GroupWebsiteApp groupWebsiteApp;
	
	public WebsiteApp(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid) {
		super(db_id, profile, position, infos, groupApp, insertDate, single_id);
		this.website = site;
		this.websiteAppDBid = websiteAppDBid;
		this.groupWebsiteApp = (GroupWebsiteApp) groupApp;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM websiteApps WHERE id=" + websiteAppDBid + ";");
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
	
	public JSONArray getJSON(ServletManager sm) throws GeneralException{
		JSONArray infos = new JSONArray();
		JSONObject websiteInfos = new JSONObject();
		websiteInfos.put("website", website.getJSON(sm));
		infos.add(websiteInfos);
		return infos;
	}
	
	/* For sancho le robot */
	public boolean isEmpty() {
		return true;
	}
}