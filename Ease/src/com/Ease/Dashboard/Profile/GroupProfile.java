package com.Ease.Dashboard.Profile;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.*;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		DatabaseRequest request = db.prepareRequest("SELECT * FROM groupProfiles WHERE group_id= ?;");
		request.setInt(group.getDBid());
		DatabaseResult rs = request.get();
		String db_id;
		ProfilePermissions perms;
		ProfileInformation infos;
		Boolean common;
		int single_id;
		GroupProfile groupProfile;
		while (rs.next()) {
			db_id = rs.getString(Data.ID.ordinal());
			perms = ProfilePermissions.loadProfilePermissions(rs.getString(Data.PERM_ID.ordinal()), db);
			infos = ProfileInformation.loadProfileInformation(rs.getString(Data.INFO_ID.ordinal()), db);
			common = rs.getBoolean(Data.COMMON.ordinal());
			single_id = idGenerator.getNextId();
			groupProfile = new GroupProfile(db_id, group, perms, infos, common, single_id);
			GroupManager.getGroupManager(context).add(groupProfile);
			profiles.add(groupProfile);
		}
		return profiles;
	}
	
	public static GroupProfile createGroupProfile(Group group, int permissions, String name, String color, Boolean common, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ProfilePermissions perms = ProfilePermissions.CreateProfilePermissions(permissions, group.getDBid(), sm);
		ProfileInformation infos = ProfileInformation.createProfileInformation(name, color, sm.getDB());
		DatabaseRequest request = db.prepareRequest("INSERT INTO groupProfiles VALUES(NULL, ?, ?, ?, ?);");
		request.setInt(group.getDBid());
		request.setInt(perms.getDBid());
		request.setInt(infos.getDBid());
		request.setBoolean(common);
		String db_id = request.set().toString();
		IdGenerator idGen = (IdGenerator) sm.getContextAttr("idGenerator");
		int single_id = idGen.getNextId();
		GroupProfile groupProfile = new GroupProfile(db_id, group, perms, infos, common, single_id);
		GroupManager.getGroupManager(sm).add(groupProfile);
		request = db.prepareRequest("SELECT email, user_id FROM groupsAndUsersMap JOIN users ON user_id = users.id WHERE group_id = ?;");
		request.setInt(group.getDBid());
		DatabaseResult rs = request.get();
		@SuppressWarnings("unchecked")
		Map<String, User> users = (Map<String, User>) sm.getContextAttr("users");
		String user_email;
		User user;
		while (rs.next()) {
			user_email = rs.getString(1);
			if ((user = users.get(user_email)) != null)
				groupProfile.loadContentForConnectedUser(user, sm);
			else
				groupProfile.loadContentForUnconnectedUser(rs.getString(2), sm);
		}
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
		this.single_id = single_id;
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return this.db_id;
	}
	public int getSingleId() {
		return single_id;
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

	public void removeFromDb(ServletManager sm) throws GeneralException, HttpServletException {
		DataBaseConnection db = sm.getDB();
		@SuppressWarnings("unchecked")
		Map<String, User> users = (Map<String, User>)sm.getContextAttr("users");
		DatabaseRequest request = db.prepareRequest("SELECT email, user_id, profile_id FROM profiles JOIN users ON user_id = users.id WHERE group_profile_id = ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		while (rs.next()) {
			String email = rs.getString(1);
			String user_id = rs.getString(2);
			if (users.containsKey(email))
				this.removeContentForConnectedUser(users.get(email), sm);
			else
				this.removeContentForUnconnectedUser(user_id, sm);
		}
		request = db.prepareRequest("DELETE FROM groupProfiles WHERE id = ?;");
		request.setInt(this.db_id);
		request.set();
	}
	
	public Integer loadContentForUnconnectedUser(String db_id, ServletManager sm) throws GeneralException {
		int columnIdx = User.getMostEmptyProfileColumnForUnconnected(db_id, sm);
		int posIdx = User.getColumnNextPositionForUnconnected(db_id, columnIdx, sm);
		return Profile.createProfileWithGroupForUnconnected(db_id, columnIdx, posIdx, this, sm);
	}
	
	public Profile loadContentForConnectedUser(User user, ServletManager sm) throws GeneralException {
		int mostEmptyColumn = user.getDashboardManager().getMostEmptyProfileColumn();
		Profile profile = Profile.createProfileWithGroup(user, mostEmptyColumn, user.getDashboardManager().getProfiles().get(mostEmptyColumn).size(), this, sm);
		user.getDashboardManager().getProfiles().get(mostEmptyColumn).add(profile);
		user.getDashboardManager().addProfileToMaps(profile);
		return profile;

	}
	
	public void removeContentForConnectedUser(User user, ServletManager sm) throws GeneralException, HttpServletException {
		Iterator<List<Profile>> it = user.getDashboardManager().getProfiles().iterator();
		while (it.hasNext()) {
			Iterator<Profile> it2 = it.next().iterator();
			while (it2.hasNext()) {
				Profile tmpProfile = it2.next();
				GroupProfile tmpGroupProfile = tmpProfile.getGroupProfile();
				if (tmpGroupProfile.getDBid() == this.db_id) {
					tmpProfile.removeFromDB(sm.getDB());
					return;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "Group profile not found");
	}
	
	public void removeContentForUnconnectedUser(String user_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT id, profile_info_id FROM profiles WHERE user_id = ?;");
		request.setInt(user_id);
		DatabaseResult rs = request.get();
		while (rs.next())
			Profile.removeProfileForUnconnected(rs.getInt(1), rs.getString(2), sm);
	}

	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		json.put("perms", this.perm.getJson());
		json.put("common", this.common);
		json.put("infos", infos.getJson());
		json.put("groupName", this.group.getName());
		json.put("groupId", this.group.getSingleId());
		json.put("infraName", this.group.getInfra().getName());
		json.put("infraId", this.group.getInfra().getSingleId());
		json.put("singleId", this.single_id);
		return json;
	}
}
