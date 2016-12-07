package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class GroupApp {
	public enum Data {
		NOTHING,
		ID,
		GROUP_PROFILE_ID,
		GROUP_ID,
		PERMISSION_ID,
		POSITION,
		TYPE,
		APP_INFO_ID,
		COMMON
	}
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static List<GroupApp> loadGroupApps(Group group, DataBaseConnection db, ServletContext context) throws GeneralException {
		try {
			List<GroupApp> groupApps = new LinkedList<GroupApp>();
			ResultSet rs = db.get("SELECT * FROM groupApps WHERE group_id=" + group.getDBid() + ";");
			String db_id;
			GroupProfile groupProfile;
			AppPermissions perms;
			String type;
			AppInformation appInfo;
			boolean common;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				groupProfile = group.getGroupProfile(rs.getString(Data.GROUP_PROFILE_ID.ordinal()));
				perms = AppPermissions.loadAppPermissions(rs.getString(Data.PERMISSION_ID.ordinal()), db);
				appInfo = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), db);
				common = rs.getBoolean(Data.COMMON.ordinal());
				switch (rs.getString(Data.TYPE.ordinal())) {
					case "groupLinkApp":
						groupApps.add(GroupLinkApp.loadGroupLinkApp(db_id, group, groupProfile, perms, appInfo, common, db, context));
					break;
					case "groupWebsiteApp":
						groupApps.add(GroupLinkApp.loadGroupWebsiteApp(db_id, group, groupProfile, perms, appInfo, common, db, context));
					break;
					default:
						throw new GeneralException(ServletManager.Code.InternError, "This GroupApp type dosen't exist.");
				}
			}
			return groupApps;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String db_id;
	protected GroupProfile groupProfile;
	protected Group group;
	protected AppPermissions permissions;
	protected String type;
	protected AppInformation app_informations;
	protected boolean common;
	
	public GroupApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, String type, AppInformation app_informations, boolean common) {
		this.db_id = db_id;
		this.groupProfile = groupProfile;
		this.group = group;
		this.permissions = permissions;
		this.type = type;
		this.app_informations = app_informations;
		this.common = common;
	}
	
	@SuppressWarnings("unchecked")
	public GroupApp(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		this.db_id = db_id;
		Map<Integer, GroupProfile> groupProfiles = (Map<Integer, GroupProfile>) sm.getContextAttr("groupProfiles");
		Map<Integer, Group> groups = (Map<Integer, Group>) sm.getContextAttr("groups");
		ResultSet rs = db.get("SELECT * FROM groupApps WHERE id=" + db_id + ";");
		try {
			rs.next();
			this.permissions = AppPermissions.loadAppPermissions(rs.getString(Data.PERMISSION_ID.ordinal()), sm);
			this.group = null;
			this.groupProfile = null;
			String group_id = rs.getString(Data.GROUP_ID.ordinal());
			String group_profile_id = rs.getString(Data.GROUP_PROFILE_ID.ordinal());
			for (Map.Entry<Integer, Group> entry : groups.entrySet()) {
				Group tmpGroup = entry.getValue();
				if (tmpGroup.getDBid().equals(group_id)) {
					this.group = tmpGroup;
					return;
				}
			}
			if (this.group == null)
				throw new GeneralException(ServletManager.Code.ClientError, "No group to load.");
			for (Map.Entry<Integer, GroupProfile> entry : groupProfiles.entrySet()) {
				GroupProfile tmpGroupProfile = entry.getValue();
				if (tmpGroupProfile.getDBid().equals(group_profile_id)) {
					this.groupProfile = tmpGroupProfile;
					return;
				}
			}
			if (this.groupProfile == null)
				throw new GeneralException(ServletManager.Code.ClientError, "No group profile to load.");
			this.app_informations = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), sm);
			this.type = rs.getString(Data.TYPE.ordinal());
			this.common = rs.getBoolean(Data.COMMON.ordinal());
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
	
	public String getDb_id() {
		return this.db_id;
	}
	
	public String getType() {
		return this.type;
	}
	
	public boolean isCommon() {
		return this.common;
	}
	
	public AppInformation getAppInfo() {
		return this.app_informations;
	}
	
	public GroupProfile getGroupProfile() {
		return this.groupProfile;
	}
	
	public Group getGroup() {
		return this.group;
	}
	
	public AppPermissions getPermissions() {
		return this.permissions;
	}
}
