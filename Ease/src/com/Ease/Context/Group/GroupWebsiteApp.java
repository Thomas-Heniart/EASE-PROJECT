package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.Website;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class GroupWebsiteApp extends GroupApp {

	public enum Data {
		NOTHING, ID, GROUP_APP_ID, WEBSITE_ID
	}

	public static GroupWebsiteApp loadGroupWebsiteApp(String db_id, Group group, GroupProfile groupProfile, AppPermissions permissions, AppInformation informations, boolean common, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT website_id FROM groupWebsiteApps WHERE group_app_id = " + db_id + "");
		try {
			rs.next();
			Website site = Website.getWebsite(rs.getString(1), sm);
			return new GroupWebsiteApp(db_id, groupProfile, group, permissions, informations, common, site);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	protected Website site;

	public GroupWebsiteApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, AppInformation app_informations, boolean common, Website site) {
		super(db_id, groupProfile, group, permissions, app_informations, common);
		this.site = site;
	}

	public GroupWebsiteApp(String db_id, ServletManager sm) throws GeneralException {
		super(db_id, sm);
		DataBaseConnection db = sm.getDB();
		db.get("SELECT website_id FROM groupWebsiteApps WHERE group_app_id = " + db_id  + ";");
		this.site = Website.loadWebsite(db_id, sm);
	}

	public Website getSite() {
		return this.site;
	}
}
