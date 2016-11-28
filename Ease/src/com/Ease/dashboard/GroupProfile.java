package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class GroupProfile {
	public static enum Data {
		NOTHING,
		ID,
		GROUP_ID,
		PERM_ID,
		INFO_ID,
		COMMON
	}
	
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static List<GroupProfile> loadGroupProfiles(Group group, DataBaseConnection db) throws GeneralException {
		List<GroupProfile> profiles = new LinkedList<GroupProfile>();
		try {
			ResultSet rs = db.get("SELECT * FROM groupProfile WHERE group_id=" + group.getDBid() + ";");
			String db_id;
			ProfilePermissions perms;
			ProfileInformation infos;
			Boolean common;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				perms = ProfilePermissions.loadProfilePermissions(rs.getString(Data.GROUP_ID.ordinal()), db);
				infos = ProfileInformation.loadProfileInformation(rs.getString(Data.INFO_ID.ordinal()), db);
				common = rs.getBoolean(Data.COMMON.ordinal());
				profiles.add(new GroupProfile(db_id, group, perms, infos, common));
			}
			return profiles;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static GroupProfile createGroupProfile(Group group, int permissions, String name, String color, Boolean common, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ProfilePermissions perms = ProfilePermissions.CreateProfilePermissions(permissions, group.getDBid(), sm);
		ProfileInformation infos = ProfileInformation.createProfileInformation(name, color, sm);
		String db_id = db.set("INSERT INTO groupProfile VALUES(NULL, " + group.getDBid() + ", " + perms.getDBid() + ", " + infos.getDBid() + ", " + ((common == true) ? 1 : 0) + ");").toString();
		GroupProfile groupProfile = new GroupProfile(db_id, group, perms, infos, common);
		db.commitTransaction(transaction);
		return groupProfile;
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String 	db_id;
	protected Group		group;
	protected ProfilePermissions perm;
	protected ProfileInformation infos;
	protected Boolean 	common;
	
	public GroupProfile(String db_id, Group group, ProfilePermissions perms, ProfileInformation infos, Boolean common) {
		this.db_id = db_id;
		this.group = group;
		this.perm = perms;
		this.infos = infos;
		this.common = common;
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return this.db_id;
	}
	
	public Group getGroup() {
		return this.group;
	}
	
	public ProfilePermissions getPerms() {
		return this.perm;
	}
	public void setPerms(int permissions, ServletManager sm) throws GeneralException{
		this.perm.setPerms(permissions, sm);
	}
	
	public String getName() {
		return this.infos.getName();
	}
	public void setName(String name, ServletManager sm) throws GeneralException {
		this.infos.setName(name, sm);
	}
	public String getColor() {
		return this.infos.getColor();
	}
	public void setColor(String color, ServletManager sm) throws GeneralException {
		this.infos.setColor(color, sm);
	}
	public Boolean isCommon() {
		return common;
	}
	
}
