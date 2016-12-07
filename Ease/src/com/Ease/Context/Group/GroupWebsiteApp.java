package com.Ease.Context.Group;

import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.Website;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class GroupWebsiteApp extends GroupApp {

	public enum Data {
		NOTHING, ID, GROUP_APP_ID, WEBSITE_ID
	}

	protected Website site;

	public GroupWebsiteApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions,
			String type, AppInformation app_informations, boolean common, Website site) {
		super(db_id, groupProfile, group, permissions, type, app_informations, common);
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
