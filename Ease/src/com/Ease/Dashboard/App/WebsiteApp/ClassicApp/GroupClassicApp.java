package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.AppPermissions;
import com.Ease.Dashboard.App.Website;
import com.Ease.Dashboard.App.WebsiteApp.GroupWebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.GroupLogwithApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.GroupLogwithApp.Data;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class GroupClassicApp extends GroupWebsiteApp{
	public enum Data {
		NOTHING,
		ID,
		GROUP_WEBSITE_APP_ID,
		ACCOUNT_ID
	}
	
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static GroupClassicApp loadGroupClassicApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions perms, AppInformation info, boolean common, int single_id, Website site, String db_id2, DataBaseConnection db, ServletContext context) throws GeneralException {
		try {
			ResultSet rs = db.get("SELECT * FROM groupLogwithApps WHERE group_website_app_id=" + db_id + ";");
			if (rs.next()) {
				String db_id3 = rs.getString(Data.ID.ordinal());
				Account account = Account.loadAccount(rs.getString(Data.ACCOUNT_ID), user, sm);
				GroupClassicApp groupClassicApp = new GroupClassicApp(db_id, groupProfile, group, perms, info, common, single_id, site, db_id2, account, db_id3);
				GroupManager.getGroupManager(context).add(groupClassicApp);
				return groupClassicApp;
			} else {
				throw new GeneralException(ServletManager.Code.InternError, "This GroupWebsiteApp dosen't exist.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String db_id3;
	protected Account account;
	
	public GroupClassicApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, AppInformation app_informations, boolean common, int single_id, Website website, String db_id2, Account account, String db_id3) {
		super(db_id, groupProfile, group, permissions, app_informations, common, single_id, website, db_id2);
		this.db_id3 = db_id3;
		this.account = account;
	}
}
