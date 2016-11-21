package com.Ease.dashboard;

import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class LogWithApp extends WebsiteApp {
	public static LogWithApp createLogWithApp(String name, Profile profile, Site site, WebsiteApp logWithApp, ServletManager sm) throws GeneralException {
		
	}
	
	protected WebsiteApp logWithApp;
	
	public LogWithApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, Site site, WebsiteApp logWithApp) {
		super(name, profile, permissions, single_id, db_id, site);
		this.logWithApp = logWithApp;
	}
	
	public WebsiteApp getLogWithApp() {
		return this.logWithApp;
	}
	
	public void setLogWithApp(WebsiteApp logWithApp, ServletManager sm) throws GeneralException {
		//Todo do db
		this.logWithApp = logWithApp;
	}
}
