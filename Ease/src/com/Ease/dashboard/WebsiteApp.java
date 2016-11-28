package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class WebsiteApp extends App<AppInformation> {

	public enum WebsiteAppData {
		NOTHING, WEBSITE_ID, APP_ID
	}

	public enum WebsiteLogWithData {
		NAME, PROFILE_ID, POSITION, PERMISSION_ID, TYPE, WORK, WEBSITE_ID
	}

	public enum LoadData {
		NAME, PROFILE_ID, WEBSITE_ID, POSITION, PERMISSION_ID, WORK, GROUP_WEBSITE_APP_ID, APP_INFO_ID
	}

	public static WebsiteApp createEmptyApp(String name, Profile profile, Website site, ServletManager sm)
			throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Permissions permissions = AppPermissions.loadPersonnalAppPermissions(sm);
		int position = profile.getNextPosition();
		int transaction = db.startTransaction();
		int app_id = db.set("INSERT INTO apps values (null, '" + name + "', " + profile.getDb_id() + ", " + position
				+ " , " + permissions.getDBid() + ", 'WebsiteApp', 1);");
		db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ");");
		db.commitTransaction(transaction);
		AppInformation informations = AppInformation.createAppInformation(name, sm);
		return new WebsiteApp(profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), true, site,
				informations);
	}

	protected Website site;

	public WebsiteApp(Profile profile, Permissions permissions, int position, int single_id, String db_id,
			boolean working, Website site, AppInformation informations) {
		this.profile = profile;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
		this.site = site;
		this.working = working;
		this.informations = informations;
	}

	public WebsiteApp() {

	}

	public WebsiteApp(String app_id, Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db
				.get("SELECT website_id, position, permission_id, work, group_website_app_id, app_info_id FROM apps JOIN websiteApps ON apps.id = websiteApps.app_id WHERE apps.id = "
						+ app_id + ";");
		try {
			rs.next();
			this.profile = profile;
			this.site = Website.loadWebsite(rs.getString(LoadData.WEBSITE_ID.ordinal()), sm);
			this.position = rs.getInt(LoadData.POSITION.ordinal());
			this.working = rs.getBoolean(LoadData.WORK.ordinal());
			this.single_id = sm.getNextSingleId();
			this.db_id = app_id;
			String group_website_app_id = rs.getString(LoadData.GROUP_WEBSITE_APP_ID.ordinal());
			if (group_website_app_id == null || group_website_app_id.equals("null"))
				this.permissions = AppPermissions.loadPersonnalAppPermissions(sm);
			else {
				ResultSet rs2 = db.get("SELECT permission_id, common FROM groupApps JOIN groupWebsiteApps ON groupsApps.id = groupWebsiteApps.group_app_id WHERE groupWebsiteApps.id=" + group_website_app_id + ";");
				try {
					rs2.next();
					if (rs2.getBoolean(2)) {
						this.permissions = AppPermissions.loadCommomAppPermissions(sm);
					} else {
						this.permissions = AppPermissions.loadAppPermissions(rs2.getString(1), sm);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new GeneralException(ServletManager.Code.InternError, e);
				}
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
