package com.Ease.dashboard;

import java.util.Map;

import com.Ease.context.Site;
import com.Ease.session.Account;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class LogWithApp extends WebsiteApp {
	public static LogWithApp createLogWithApp(String name, Profile profile, Site site, WebsiteApp logWithApp, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Permissions permissions = AppPermissions.loadDefaultAppPermissions(sm);
		int position = profile.getNextPosition();
		Integer app_id = db.set("INSERT INTO apps values (null, '" + name + "' , " + profile.getDb_id() + ", " + position + ", " + permissions.getDBid() + ", 'LinkApp', 1);");
		Integer website_app_id = db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ");");
		db.set("INSERT INTO logWithApps values (null, " + website_app_id + ", " + logWithApp.getDb_id() + ");");
		db.commitTransaction(transaction);
		return new LogWithApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id),site, logWithApp);
	}
	
	protected WebsiteApp logWithApp;
	
	public LogWithApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, Site site, WebsiteApp logWithApp) {
		super(name, profile, permissions, position, single_id, db_id, site);
		this.logWithApp = logWithApp;
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
