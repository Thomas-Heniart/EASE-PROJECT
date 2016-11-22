package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.dashboard.ClassicApp.ClassicAppData;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class LogWithApp extends WebsiteApp {
	
	public enum LogWithAppData {
		NOTHING,
		ID,
		WEBSITE_APP_ID,
		LOGWITH_APP_ID,
		WEBSITE_ID
	}
	
	/*public LogWithApp loadContent(String name, Profile profile, Permissions permissions, int position, String db_id, boolean working, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ResultSet rs = db.get("SELECT logWithApp.id, website_app_id, logWith_app_id, website_id FROM logWithApps JOIN websiteApps ON website_app_id = websiteApps.id WHERE websiteApps.app_id = " + db_id + ";");
		try {
			if (rs.next()) {
				Website site = Website.loadWebsite(rs.getString(ClassicAppData.WEBSITE_ID.ordinal()), sm);
				WebsiteApp logWithApp = WebsiteApp.loadApp(rs.getString(LogWithAppData.LOGWITH_APP_ID.ordinal()), sm);
				return new LogWithApp(name, profile, permissions, position, sm.getNextSingleId(), db_id, working, site, logWithApp);
			}
			db.commitTransaction(transaction);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}*/
	
	public static LogWithApp createLogWithApp(String name, Profile profile, Website site, WebsiteApp logWithApp, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Permissions permissions = AppPermissions.loadDefaultAppPermissions(sm);
		int position = profile.getNextPosition();
		Integer app_id = db.set("INSERT INTO apps values (null, '" + name + "' , " + profile.getDb_id() + ", " + position + ", " + permissions.getDBid() + ", 'LinkApp', 1);");
		Integer website_app_id = db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ");");
		db.set("INSERT INTO logWithApps values (null, " + website_app_id + ", " + logWithApp.getDb_id() + ");");
		db.commitTransaction(transaction);
		return new LogWithApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), true, site, logWithApp);
	}
	
	protected WebsiteApp logWithApp;
	
	public LogWithApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, boolean working, Website site, WebsiteApp logWithApp) {
		super(name, profile, permissions, position, single_id, db_id, working, site);
		this.logWithApp = logWithApp;
	}
	
	public LogWithApp(String db_id, ServletManager sm) throws GeneralException {
		super(db_id, sm);
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT logWith_app_id FROM logWithApps JOIN websiteApps ON website_app_id = websiteApps.id WHERE websiteApps.app_id = " + db_id + ";");
		try {
			this.logWithApp = WebsiteApp.loadApp(rs.getString(1), sm);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public WebsiteApp getLogWithApp() {
		return this.logWithApp;
	}
	
	public void setLogWithApp(WebsiteApp logWithApp, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE logWithApps SET website_app_id = " + logWithApp.getDb_id() + " WHERE id = " + this.getDb_id() + ";");
		this.logWithApp = logWithApp;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM logWithApps WHERE id = " + this.getDb_id() + ";");
		super.removeFromDb(sm);
		db.commitTransaction(transaction);
	}
}
