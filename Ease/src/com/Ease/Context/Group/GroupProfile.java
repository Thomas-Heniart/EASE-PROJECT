package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.json.simple.JSONObject;

import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.Profile.ProfileInformation;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

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
	
	public static List<GroupProfile> loadGroupProfiles(Group group, DataBaseConnection db, ServletContext context) throws GeneralException {
		List<GroupProfile> profiles = new LinkedList<GroupProfile>();
		IdGenerator idGenerator = (IdGenerator)context.getAttribute("idGenerator");
		try {
			@SuppressWarnings("unchecked")
			Map<Integer, GroupProfile> groupProfiles = (Map<Integer, GroupProfile>) context.getAttribute("groupProfiles");
			ResultSet rs = db.get("SELECT * FROM groupProfiles WHERE group_id=" + group.getDBid() + ";");
			String db_id;
			ProfilePermissions perms;
			ProfileInformation infos;
			Boolean common;
			int single_id;
			GroupProfile groupProfile;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				perms = ProfilePermissions.loadProfilePermissions(rs.getString(Data.GROUP_ID.ordinal()), db);
				infos = ProfileInformation.loadProfileInformation(rs.getString(Data.INFO_ID.ordinal()), db);
				common = rs.getBoolean(Data.COMMON.ordinal());
				single_id = idGenerator.getNextId();
				groupProfile = new GroupProfile(db_id, group, perms, infos, common, single_id);
				profiles.add(groupProfile);
				groupProfiles.put(single_id, groupProfile);
			}
			return profiles;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static GroupProfile createGroupProfile(Group group, int permissions, String name, String color, Boolean common, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		@SuppressWarnings("unchecked")
		Map<Integer, GroupProfile> groupProfiles = (Map<Integer, GroupProfile>) sm.getContextAttr("groupProfiles");
		ProfilePermissions perms = ProfilePermissions.CreateProfilePermissions(permissions, group.getDBid(), sm);
		ProfileInformation infos = ProfileInformation.createProfileInformation(name, color, sm);
		String db_id = db.set("INSERT INTO groupProfile VALUES(NULL, " + group.getDBid() + ", " + perms.getDBid() + ", " + infos.getDBid() + ", " + ((common == true) ? 1 : 0) + ");").toString();
		IdGenerator idGen = (IdGenerator) sm.getContextAttr("idGenerator");
		int single_id = idGen.getNextId();
		GroupProfile groupProfile = new GroupProfile(db_id, group, perms, infos, common, single_id);
		groupProfiles.put(single_id, groupProfile);
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
	protected int		single_id;
	
	public GroupProfile(String db_id, Group group, ProfilePermissions perms, ProfileInformation infos, Boolean common, int single_id) {
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
	public ProfileInformation getInfo() {
		return this.infos;
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
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public static GroupProfile getGroupProfile(String db_id, ServletManager sm) throws GeneralException {
		@SuppressWarnings("unchecked")
		Map<String, GroupProfile> groupProfileMap = (Map<String, GroupProfile>)sm.getContextAttr("groupProfiles");
		GroupProfile groupProfile = groupProfileMap.get(db_id);
		if (groupProfile == null)
			throw new GeneralException(ServletManager.Code.InternError, "This groupProfile dosen't exist!");
		return groupProfile;
	}

	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		@SuppressWarnings("unchecked")
		Map<String, User> users = (Map<String, User>)sm.getContextAttr("users");
		ResultSet rs = db.get("SELECT email, user_id, profile_id FROM profiles JOIN users ON user_id = users.id WHERE group_profile_id = " + this.db_id + ";");
		try {
			while (rs.next()) {
				String email = rs.getString(1);
				String user_id = rs.getString(2);
				if (users.containsKey(email))
					this.removeContentForConnectedUser(users.get(email), sm);
				else
					this.removeContentForUnconnectedUser(user_id, sm);
			}
			db.set("DELETE FROM groupProfiles WHERE id=" + this.db_id + ";");
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
	
	public void removeContentForConnectedUser(User user, ServletManager sm) throws GeneralException {
		Iterator<List<Profile>> it = user.getProfileColumns().iterator();
		while (it.hasNext()) {
			Iterator<Profile> it2 = it.next().iterator();
			while (it2.hasNext()) {
				Profile tmpProfile = it2.next();
				GroupProfile tmpGroupProfile = tmpProfile.getGroupProfile();
				if (tmpGroupProfile.getDBid() == this.db_id) {
					tmpProfile.removeFromDB(sm);
					return;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "Group profile not found");
	}
	
	public void removeContentForUnconnectedUser(String user_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT id, profile_info_id FROM profiles WHERE user_id = " + user_id + ";");
		try {
			while (rs.next())
				Profile.removeProfileForUnconnected(rs.getString(1), rs.getString(2), sm);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public JSONObject getJSON() {
		JSONObject res = new JSONObject();
		
		return res;
	}
	
	public String getJSONString() {
		return this.getJSON().toString();
	}
}
