package com.Ease.Dashboard.App;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.LinkApp.GroupLinkApp;
import com.Ease.Dashboard.App.WebsiteApp.GroupWebsiteApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.*;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroupApp {
	public enum Data {
		NOTHING,
		ID,
		GROUP_PROFILE_ID,
		GROUP_ID,
		PERMISSION_ID,
		TYPE,
		APP_INFO_ID,
		COMMON
	}
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static List<GroupApp> loadGroupApps(Group group, DataBaseConnection db, ServletContext context) throws GeneralException, HttpServletException {
		IdGenerator idGenerator = (IdGenerator)context.getAttribute("idGenerator");
		List<GroupApp> groupApps = new LinkedList<GroupApp>();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM groupApps WHERE group_id= ?;");
		request.setInt(group.getDBid());
		DatabaseResult rs = request.get();
		String db_id;
		GroupProfile groupProfile;
		AppPermissions perms;
		AppInformation appInfo;
		boolean common;
		int single_id;
		while (rs.next()) {
			db_id = rs.getString(Data.ID.ordinal());
			groupProfile = GroupManager.getGroupManager(context).getGroupProfileFromDBid(rs.getString(Data.GROUP_PROFILE_ID.ordinal()));
			perms = AppPermissions.loadAppPermissions(rs.getString(Data.PERMISSION_ID.ordinal()), db);
			appInfo = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), db);
			common = rs.getBoolean(Data.COMMON.ordinal());
			single_id = idGenerator.getNextId();
			switch (rs.getString(Data.TYPE.ordinal())) {
				case "groupLinkApp":
					groupApps.add(GroupLinkApp.loadGroupLinkApp(db_id, group, groupProfile, perms, appInfo, common, single_id, db, context));
				break;
				case "groupWebsiteApp":
					groupApps.add(GroupWebsiteApp.loadGroupWebsiteApp(db_id, group, groupProfile, perms, appInfo, common, single_id, db, context));
				break;
				default:
					throw new GeneralException(ServletManager.Code.InternError, "This GroupApp type dosen't exist.");
			}
		}
		return groupApps;
	}
	
	public static String createGroupApp(GroupProfile groupProfile, Group group, int perms, String name, boolean common, String type, Map<String, Object> elevator, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		AppPermissions permissions = AppPermissions.CreateAppPermissions(perms, group.getDBid(), sm);
		AppInformation infos = AppInformation.createAppInformation(name, db);
		DatabaseRequest request = db.prepareRequest("INSERT INTO groupApps VALUES(NULL, ?, ?, ?, ?, ?, ?);");
		request.setInt(groupProfile.getDBid());
		request.setInt(group.getDBid());
		request.setInt(permissions.getDBid());
		request.setString(type);
		request.setInt(infos.getDb_id());
		request.setBoolean(common);
		String db_id = request.set().toString();
		elevator.put("perms", permissions);
		elevator.put("appInfos", infos);
		db.commitTransaction(transaction);
		return db_id;
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
	protected AppInformation app_informations;
	protected boolean common;
	protected int single_id;
	
	public GroupApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, AppInformation app_informations, boolean common, int single_id) {
		this.db_id = db_id;
		this.groupProfile = groupProfile;
		this.group = group;
		this.permissions = permissions;
		this.app_informations = app_informations;
		this.common = common;
		this.single_id = single_id;
	}
	
	public String getDBid() {
		return this.db_id;
	}
	public int getSingleId() {
		return single_id;
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
	
	public AppPermissions getPerms() {
		return this.permissions;
	}

	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		
		db.commitTransaction(transaction);
		
	}

	public void loadContentForConnectedUser(User user, ServletManager sm) throws GeneralException {
		System.out.println("ICI?");
		
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

	public void fillJson(JSONObject json) {
		json.put("perms", this.permissions.getJson());
		json.put("common", this.common);
		json.put("groupName", this.group.getName());
		json.put("groupId", this.group.getSingleId());
		json.put("infraName", this.group.getInfra().getName());
		json.put("infraId", this.group.getInfra().getSingleId());
	}

	public JSONObject getJson() {
		JSONObject res = new JSONObject();
		res.put("infos", this.app_informations.getJson());
		return res;
	}
}
