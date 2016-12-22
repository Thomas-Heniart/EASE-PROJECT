package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.AppPermissions;
import com.Ease.Dashboard.App.WebsiteApp.GroupWebsiteApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
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
				Account account = Account.loadAccount(rs.getString(Data.ACCOUNT_ID.ordinal()), db);
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
	
	public static GroupClassicApp createGroupClassicApp(GroupProfile groupProfile, Group group, int perms, String name, boolean common, Website site, String password, Map<String, String> accInfos, ServletManager sm) throws GeneralException {
		Map<String, Object> elevator = new HashMap<String, Object>();
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String websiteAppId = GroupWebsiteApp.createGroupWebsiteApp(groupProfile, group, perms, name, common, site, "groupClassicApp", elevator, sm);
		AppPermissions permissions = (AppPermissions) elevator.get("perms");
		AppInformation appInfos = (AppInformation) elevator.get("appInfos");
		String appId = (String) elevator.get("appId");
		Account account = Account.createGroupAccount(password, false, accInfos, group.getInfra(), sm);
		String db_id = db.set("INSERT INTO groupClassicApps VALUES(NULL, " + websiteAppId + ", " + account.getDBid() + ");").toString();
		int single_id = ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId();
		GroupClassicApp groupClassicApp = new GroupClassicApp(appId, groupProfile, group, permissions, appInfos, common, single_id, site, websiteAppId, account, db_id);
		GroupManager.getGroupManager(sm).add(groupClassicApp);
		db.commitTransaction(transaction);
		return groupClassicApp;
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
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public void removeFromDb(ServletManager sm) {
		// TODO Auto-generated method stub
		
	}

	public void loadContentForConnectedUser(User user, ServletManager sm) {
		// TODO Auto-generated method stub
		
	}

	public void loadContentForUnconnectedUser(String db_id2, ServletManager sm) {
		// TODO Auto-generated method stub
		
	}

	public void removeContentForConnectedUser(User user, ServletManager sm) {
		// TODO Auto-generated method stub
		
	}

	public void removeContentForUnconnectedUser(String db_id2, ServletManager sm) {
		// TODO Auto-generated method stub
		
	}
}
