package com.Ease.dashboard;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.context.Site;
import com.Ease.dashboard.App.AppData;
import com.Ease.dashboard.LinkApp.LinkAppData;
import com.Ease.dashboard.LogWithApp.LogWithAppData;
import com.Ease.dashboard.Profile.Data;
import com.Ease.session.Account;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class WebsiteApp extends App {
	
	public enum WebsiteAppData {
		NOTHING,
		WEBSITE_ID,
		APP_ID
	}
	
	public enum WebsiteLogWithData {
		NAME,
		PROFILE_ID,
		POSITION,
		PERMISSION_ID,
		TYPE,
		WORK,
		WEBSITE_ID
	}
	
	public enum LoadData {
		NAME,
		PROFILE_ID,
		WEBSITE_ID,
		POSITION,
		PERMISSION_ID,
		WORK
	}
	
	@SuppressWarnings("unchecked")
	public static WebsiteApp loadApp(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String name;
		Profile profile;
		int position;
		Permissions permissions;
		Site site;
		Boolean work;
		ResultSet rs = db.get("SELECT name, profile_id, position, permission_id, type, work, website_id FROM apps JOIN websiteApps ON apps.id = app_id WHERE apps.id = " + db_id + ";");
		try {
			if (rs.next()) {
				name = rs.getString(WebsiteLogWithData.NAME.ordinal());
				profile = Profile.loadProfile(rs.getString(WebsiteLogWithData.PROFILE_ID.ordinal()), sm);
				position = Integer.parseInt(rs.getString(WebsiteLogWithData.POSITION.ordinal()));
				permissions = AppPermissions.loadAppPermissions(rs.getString(WebsiteLogWithData.PERMISSION_ID.ordinal()), sm);
				site = Site.loadSite(rs.getString(WebsiteLogWithData.WEBSITE_ID.ordinal()), sm);
				work = rs.getString(WebsiteLogWithData.WORK.ordinal()).equals("1") ? true : false;
				String type = rs.getString(WebsiteLogWithData.TYPE.ordinal());
				Constructor<App> c = (Constructor<App>) Class.forName("com.Ease.dashboard." + type).getConstructor(String.class, ServletManager.class);
				return (WebsiteApp) c.newInstance(db_id, sm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		db.commitTransaction(transaction);
	}
	
	public static WebsiteApp createEmptyApp(String name, Profile profile, Site site, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Permissions permissions = AppPermissions.loadDefaultAppPermissions(sm);
		int position = profile.getNextPosition();
		int transaction = db.startTransaction();
		int app_id = db.set("INSERT INTO apps values (null, '" + name + "', " + profile.getDb_id() + ", " + position + " , " + permissions.getDBid() + ", 'WebsiteApp', 1);");
		db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ");");
		db.commitTransaction(transaction);
		return new WebsiteApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), true, site);
	}
	
	protected Site site;
	
	public WebsiteApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, boolean working, Site site) {
		this.name = name;
		this.profile = profile;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
		this.site = site;
		this.working = working;
	}
	
	public WebsiteApp() {
		
	}
	
	public WebsiteApp(String app_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT id, name, profile_id, website_id, position, permission_id, work FROM apps JOIN websiteApps ON apps.id = websiteApps.app_id WHERE apps.id = " + db_id + ";");
		if (rs.next()) {
			this.name = rs.getString(LoadData.NAME.ordinal());
			this.profile = Profile.loadProfile(rs.getString(LoadData.PROFILE_ID.ordinal()), sm);
			this.site = Site.loadSite(rs.getString(LoadData.WEBSITE_ID.ordinal()), sm);
			this.position = rs.getInt(LoadData.POSITION.ordinal());
			this.permissions = AppPermissions.loadAppPermissions(rs.getString(LoadData.PERMISSION_ID.ordinal()), sm);
			this.working = rs.getBoolean(LoadData.WORK.ordinal());
			this.single_id = sm.getNextSingleId();
			this.db_id = app_id;
		}
	}
	
	public Site getSite() {
		return this.site;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM websiteApps WHERE id = " + this.getDb_id() + ";");
		super.removeFromDb(sm);
		db.commitTransaction(transaction);
	}
}
