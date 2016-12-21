package com.Ease.Dashboard.App.LinkApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.AppPermissions;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class GroupLinkApp extends GroupApp {
	public enum Data {
		NOTHING,
		ID,
		LINK_APP_INFO_ID,
		GROUP_APP_ID
	}
	
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static GroupLinkApp loadGroupLinkApp(String db_id, Group group, GroupProfile groupProfile, AppPermissions permissions, AppInformation informations, boolean common, int single_id, DataBaseConnection db, ServletContext context) throws GeneralException {
		ResultSet rs = db.get("SELECT link_app_info_id FROM groupLinkApps WHERE group_app_id = " + db_id + ";");
		try {
			rs.next();
			String db_id2 = rs.getString(1);
			LinkAppInformation linkAppInformations = LinkAppInformation.loadLinkAppInformation(db_id2, db);
			GroupLinkApp groupLinkApp = new GroupLinkApp(db_id, groupProfile, group, permissions, informations, linkAppInformations, common, single_id, db_id2);
			GroupManager.getGroupManager(context).add(groupLinkApp);
			return groupLinkApp;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static GroupLinkApp createGroupLinkApp(GroupProfile groupProfile, Group group, int perms, String name, boolean common, String url, String img_url, ServletManager sm) throws GeneralException {
		Map<String, Object> elevator = new HashMap<String, Object>();
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String appDBid = GroupApp.createGroupApp(groupProfile, group, perms, name, common, "groupLinkApp", elevator, sm);
		AppPermissions permissions = (AppPermissions) elevator.get("perms");
		AppInformation appInfos = (AppInformation) elevator.get("appInfos");
		LinkAppInformation infos = LinkAppInformation.createLinkAppInformation(url, img_url, sm);
		String db_id = db.set("INSERT INTO groupLinkApps VALUES(NULL, " + infos.getDb_id() + ", " + appDBid + ");").toString();
		int single_id = ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId();
		GroupLinkApp groupLinkApp = new GroupLinkApp(appDBid, groupProfile, group, permissions, appInfos, infos, common, single_id, db_id);
		GroupManager.getGroupManager(sm).add(groupLinkApp);
		db.commitTransaction(transaction);
		return groupLinkApp;
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected LinkAppInformation link_app_informations;
	protected String db_id2;
	
	public GroupLinkApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, AppInformation app_informations, LinkAppInformation link_app_informations, boolean common, int single_id, String db_id2) {
		super(db_id, groupProfile, group, permissions, app_informations, common, single_id);
		this.link_app_informations = link_app_informations;
		this.db_id2 = db_id2;
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */

	public LinkAppInformation getLinkAppInfo() {
		return this.link_app_informations;
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
