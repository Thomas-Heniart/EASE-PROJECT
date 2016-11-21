package com.Ease.dashboard;

import com.Ease.context.Site;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class WebsiteApp extends App {
	public static WebsiteApp createEmptyApp(String name, Profile profile, Site site, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Permissions permissions = AppPermissions.loadDefaultAppPermissions(sm);
		int position = profile.getNextPosition();
		int transaction = db.startTransaction();
		int app_id = db.set("INSERT INTO apps values (null, '" + name + "', " + profile.getDb_id() + ", " + position + " , " + permissions.getDBid() + ", 'WebsiteApp', 1);");
		db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ");");
		return new WebsiteApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), site);
	}
	
	protected Site site;
	
	public WebsiteApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, Site site) {
		this.name = name;
		this.profile = profile;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
		this.site = site;
	}
	
	public Site getSite() {
		return this.site;
	}
}
