package com.Ease.Dashboard.App.WebsiteApp.LogwithApp;

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
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class GroupLogwithApp extends GroupWebsiteApp {
	public enum Data {
		NOTHING,
		ID,
		GROUP_WEBSITE_APP_ID,
		LOGWITH_GROUP_WEBSITE_APP_ID
	}
	
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static GroupLogwithApp loadGroupLogwithApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions perms, AppInformation info, boolean common, int single_id, Website site, String db_id2, DataBaseConnection db, ServletContext context) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM groupLogWithApps WHERE group_website_app_id= ?;");
		request.setInt(db_id);
		DatabaseResult rs = request.get();
		if (rs.next()) {
			String db_id3 = rs.getString(Data.ID.ordinal());
			GroupWebsiteApp logwith = (GroupWebsiteApp)GroupManager.getGroupManager(context).getGroupAppFromDBid(rs.getString(Data.LOGWITH_GROUP_WEBSITE_APP_ID.ordinal()));
			GroupLogwithApp groupLogwithApp = new GroupLogwithApp(db_id, groupProfile, group, perms, info, common, single_id, site, db_id2, logwith, db_id3);
			GroupManager.getGroupManager(context).add(groupLogwithApp);
			return groupLogwithApp;
		} else {
			throw new GeneralException(ServletManager.Code.InternError, "This GroupWebsiteApp doesn't exist.");
		}
	}
	
	public static GroupLogwithApp createGroupLogwithApp(GroupProfile groupProfile, Group group, int perms, String name, boolean common, Website site, GroupWebsiteApp logwith, ServletManager sm) throws GeneralException {
		Map<String, Object> elevator = new HashMap<String, Object>();
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String websiteAppId = GroupWebsiteApp.createGroupWebsiteApp(groupProfile, group, perms, name, common, site, "groupLogwithApp", elevator, sm);
		AppPermissions permissions = (AppPermissions) elevator.get("perms");
		AppInformation appInfos = (AppInformation) elevator.get("appInfos");
		String appId = (String) elevator.get("appId");
		String db_id = db.set("INSERT INTO groupLogWithApps VALUES(NULL, " + websiteAppId + ", " + logwith.getDBid() + ");").toString();
		int single_id = ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId();
		GroupLogwithApp groupLogwithApp = new GroupLogwithApp(appId, groupProfile, group, permissions, appInfos, common, single_id, site, websiteAppId, logwith, db_id);
		GroupManager.getGroupManager(sm).add(groupLogwithApp);
		db.commitTransaction(transaction);
		return groupLogwithApp;
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected GroupWebsiteApp logwith_group_website_app;
	protected String db_id3;
	
	public GroupLogwithApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, AppInformation app_informations, boolean common, int single_id, Website website, String db_id2, GroupWebsiteApp logwith, String db_id3) {
		super(db_id, groupProfile, group, permissions, app_informations, common, single_id, website, db_id2);
		this.logwith_group_website_app = logwith;
		this.db_id3 = db_id3;
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
