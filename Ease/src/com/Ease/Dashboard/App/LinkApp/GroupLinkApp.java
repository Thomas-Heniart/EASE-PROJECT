package com.Ease.Dashboard.App.LinkApp;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.AppPermissions;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
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
			String db_id2 = rs.getString(Data.ID.toString());
			LinkAppInformation linkAppInformations = LinkAppInformation.loadLinkAppInformation(db_id2, db);
			GroupLinkApp groupLinkApp = new GroupLinkApp(db_id, groupProfile, group, permissions, informations, linkAppInformations, common, single_id, db_id2);
			GroupManager.getGroupManager(context).add(groupLinkApp);
			return groupLinkApp;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
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

}
