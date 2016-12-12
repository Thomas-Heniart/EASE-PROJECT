package com.Ease.Dashboard.App.WebsiteApp;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.AppPermissions;
import com.Ease.Dashboard.App.Website;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsiteApp<groupAppClass extends GroupWebsiteApp> extends App<GroupWebsiteApp> {

	public enum Data {
		NOTHING,
		ID,
		WEBSITE_ID,
		APPP_ID,
		GROUP_WEBSITE_APP_ID,
		TYPE
	}
	
	public enum WebsiteAppData {
		NOTHING, WEBSITE_ID, APP_ID
	}

	public enum WebsiteLogWithData {
		NOTHING, NAME, PROFILE_ID, POSITION, PERMISSION_ID, TYPE, WORK, WEBSITE_ID
	}

	public enum LoadData {
		NOTHING, NAME, PROFILE_ID, WEBSITE_ID, POSITION, PERMISSION_ID, WORK, GROUP_WEBSITE_APP_ID, APP_INFO_ID
	}

	public static WebsiteApp<?> createEmptyApp(String name, Profile profile, GroupWebsiteApp groupWebsiteApp, Website site, ServletManager sm)
			throws GeneralException {
		int position = profile.getApps().size();
		AppInformation app_information = AppInformation.createAppInformation(name, sm);
		String app_id = WebsiteApp.insertNewWebsiteAppInDb(profile, position, "WebsiteApp", app_information, null, site, sm);
		return new WebsiteApp<GroupWebsiteApp>(profile, position, sm.getNextSingleId(), app_id, true, site,
				app_information, groupWebsiteApp);
	}

	public static String insertNewWebsiteAppInDb(Profile profile, int position, String type, AppInformation app_information, GroupWebsiteApp groupWebsiteApp, Website site, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String app_id = App.insertNewAppInDb(profile, position, type, app_information, (groupWebsiteApp == null) ? null : groupWebsiteApp.getGroupApp(), sm);
		db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ", " + ((groupWebsiteApp == null) ? "null" : groupWebsiteApp.getDb_id()) + ");");
		db.commitTransaction(transaction);
		return app_id;
	}
	
	protected Website site;
	protected GroupWebsiteApp groupWebsiteApp;

	public WebsiteApp(Profile profile, int position, int single_id, String db_id, boolean working, Website site, AppInformation informations, GroupWebsiteApp groupWebsiteApp) {
		this.profile = profile;
		this.position = position;
		this.single_id = single_id;
		this.db_id = db_id;
		this.site = site;
		this.working = working;
		this.informations = informations;
		this.groupWebsiteApp = groupWebsiteApp;
		this.groupApp = groupWebsiteApp;
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
