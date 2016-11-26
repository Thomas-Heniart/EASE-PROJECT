package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	public static WebsiteApp createEmptyApp(String name, Profile profile, Website site, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Permissions permissions = AppPermissions.loadPersonnalAppPermissions(sm);
		int position = profile.getNextPosition();
		int transaction = db.startTransaction();
		int app_id = db.set("INSERT INTO apps values (null, '" + name + "', " + profile.getDb_id() + ", " + position + " , " + permissions.getDBid() + ", 'WebsiteApp', 1);");
		db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ");");
		db.commitTransaction(transaction);
		return new WebsiteApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), true, site);
	}
	
	protected Website site;
	
	public WebsiteApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, boolean working, Website site) {
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
	
	public WebsiteApp(String app_id, Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT id, name, profile_id, website_id, position, permission_id, work FROM apps JOIN websiteApps ON apps.id = websiteApps.app_id WHERE apps.id = " + db_id + ";");
		try {
			if (rs.next()) {
				this.name = rs.getString(LoadData.NAME.ordinal());
				this.profile = profile;
				this.site = Website.loadWebsite(rs.getString(LoadData.WEBSITE_ID.ordinal()), sm);
				this.position = rs.getInt(LoadData.POSITION.ordinal());
				this.permissions = AppPermissions.loadAppPermissions(rs.getString(LoadData.PERMISSION_ID.ordinal()), sm);
				this.working = rs.getBoolean(LoadData.WORK.ordinal());
				this.single_id = sm.getNextSingleId();
				this.db_id = app_id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Website getSite() {
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
