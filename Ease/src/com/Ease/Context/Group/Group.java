package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Group {
	enum Data {
		NOTHING,
		ID,
		NAME,
		PARENT
	}
	
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static Group createGroup(String name, Group parent, Infrastructure infra, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO groups values (null, '" + name + "', " + parent.getDBid() + ");");
		return new Group(String.valueOf(db_id), name, parent, infra);
	}
	
	public static List<Group> loadGroups(DataBaseConnection db, Infrastructure infra)throws GeneralException {
		return loadGroup(db, null, infra);
	}
	
	public static List<Group> loadGroup(DataBaseConnection db, Group parent, Infrastructure infra) throws GeneralException {
		try {
			List<Group> groups = new LinkedList<Group>();
			ResultSet rs = db.get("SELECT * FROM groups WHERE parent " + ((parent == null) ? " IS NULL" : ("="+parent.getDBid())) + " AND infra_id=" + infra.getDBid() + ";");
			String db_id;
			String name;
			List<GroupProfile> groupProfiles;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				System.out.println(db_id);
				name = rs.getString(Data.NAME.ordinal());
				Group child = new Group(db_id, name, parent, infra);
				groupProfiles = GroupProfile.loadGroupProfiles(child, db);
				child.setChildrens(loadGroup(db, child, infra));
				child.setGroupProfiles(groupProfiles);
				groups.add(child);
			}
			return groups;
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
	protected String name;
	protected List<Group> children;
	protected Group		parent;
	protected List<GroupProfile> groupProfiles;
	protected Infrastructure infra;
	
	public Group(String db_id, String name, Group parent, Infrastructure infra) {
		this.db_id = db_id;
		this.name = name;
		this.children = null;
		this.parent = parent;
		this.infra = infra;
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return this.db_id;
	}
	public void setChildrens(List<Group> children) {
		this.children = children;
	}
	private void setGroupProfiles(List<GroupProfile> groupProfiles) {
		this.groupProfiles = groupProfiles;
	}
	public List<Group> getChildren() {
		return this.children;
	}
	public List<GroupProfile> getGroupProfiles() {
		return this.groupProfiles;
	}
	
	public String getName() {
		return name;
	}
	public Infrastructure getInfra() {
		return infra;
	}
	
	/*
	 * 
	 * Utils 
	 * 
	 */
	
	/*public static Map<String, Group> getGroupMap(List<Group> groupTrees) {
		Map<String, Group> groupsMap = new HashMap<String, Group>();
		for (Group group : groupTrees) {
			groupsMap.put(group.getDBid(), group);
			groupsMap.putAll(getGroupMap(group.getChildren()));
		}
		return groupsMap;
	}
	
	public static Map<String, GroupProfile> getGroupProfileMap(List<Group> groupTrees) {
		Map<String, GroupProfile> groupProfileMap = new HashMap<String, GroupProfile>();
		for (Group group : groupTrees) {
			for (GroupProfile groupProfile : group.getGroupProfiles()) {
				groupProfileMap.put(groupProfile.getDBid(), groupProfile);
			}
			groupProfileMap.putAll(getGroupProfileMap(group.getChildren()));
		}
		return groupProfileMap;
	}*/

	public void loadContentForConnectedUser(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		if (this.parent != null)
			parent.loadContentForConnectedUser(user, sm);
		int mostEmptyColumn;
		for (GroupProfile gProfile : this.groupProfiles) {
			mostEmptyColumn = user.getMostEmptyProfileColumn();
			user.getProfilesColumn().get(mostEmptyColumn).add(Profile.createProfileWithGroup(user, mostEmptyColumn, user.getProfilesColumn().get(mostEmptyColumn).size(), gProfile, sm));
		}
		db.commitTransaction(transaction);
	}
	
	public void loadContentForUnconnectedUser(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		if (this.parent != null)
			this.parent.loadContentForUnconnectedUser(db_id, sm);
		db.commitTransaction(transaction);
	}
	
	public void removeContentForConnectedUser(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (List<Profile> column : user.getProfilesColumn()) {
			for (Profile profile : column) {
				GroupProfile groupProfile = profile.getGroupProfile();
				if (groupProfile != null && groupProfile.getGroup() == this) {
					profile.removeFromDB(sm);
					column.remove(profile);
				}
			}
		}
		user.updateProfilesIndex(sm);
		db.commitTransaction(transaction);
	}
	
	public void removeContentForUnconnectedUser(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
	}
	
	public String toString() {
		return (this.db_id + " : " + this.name);
	}
	
	@SuppressWarnings("unchecked")
	public void addUser(String email, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		User user;
		String userDBid;
		int transaction = db.startTransaction();
		if ((user = ((Map<String, User>)sm.getContextAttr("users")).get(email)) == null) {
			userDBid = User.findDBid(email, sm);
			this.loadContentForUnconnectedUser(userDBid, sm);
		} else {
			userDBid = user.getDBid();
			this.loadContentForConnectedUser(user, sm);
		}
		db.set("INSERT INTO groupsAndUsersMap values (null, " + this.db_id + ", " + userDBid + ");");
		db.commitTransaction(transaction);
	}
	
	@SuppressWarnings("unchecked")
	public void removeUser(String email, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		User user;
		String userDBid;
		int transaction = db.startTransaction();
		if ((user = ((Map<String, User>)sm.getContextAttr("users")).get(email)) == null) {
			userDBid = User.findDBid(email, sm);
			this.removeContentForUnconnectedUser(userDBid, sm);
		} else {
			userDBid = user.getDBid();
			this.removeContentForConnectedUser(user, sm);
		}
		db.set("DELETE FROM groupsAndUsersMap WHERE group_id=" + this.db_id + " AND user_id=" + userDBid + ";");
		db.commitTransaction(transaction);
	}
}
