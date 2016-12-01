package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

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
	
	public static List<Group> loadGroups(DataBaseConnection db)throws GeneralException {
		return loadGroup(db, null);
	}
	
	public static List<Group> loadGroup(DataBaseConnection db, Group parent) throws GeneralException {
		try {
			List<Group> groups = new LinkedList<Group>();
			ResultSet rs = db.get("SELECT * FROM groups WHERE parent " + ((parent == null) ? " IS NULL" : ("="+parent.getDBid())) + ";");
			String db_id;
			String name;
			List<GroupProfile> groupProfiles;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				System.out.println(db_id);
				name = rs.getString(Data.NAME.ordinal());
				Group child = new Group(db_id, name, parent);
				groupProfiles = GroupProfile.loadGroupProfiles(child, db);
				child.setChildrens(loadGroup(db, child));
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
	protected List<User> users;
	
	public Group(String db_id, String name, Group parent) {
		this.db_id = db_id;
		this.name = name;
		this.children = null;
		this.parent = parent;
		this.users = new LinkedList<User>();
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
	public Group getInfra() {
		if (parent == null) {
			return this;
		} else {
			return this.parent.getInfra();
		}
	}
	
	/*
	 * 
	 * Utils 
	 * 
	 */
	
	public static Map<String, Group> getGroupMap(List<Group> groupTrees) {
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
	}

	public void loadContent(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		if (this.parent != null)
			parent.loadContent(user, sm);
		int mostEmptyColumn;
		for (GroupProfile gProfile : this.groupProfiles) {
			mostEmptyColumn = user.getMostEmptyProfileColumn();
			user.getProfilesColumn().get(mostEmptyColumn).add(Profile.createProfileWithGroup(user, mostEmptyColumn, user.getProfilesColumn().get(mostEmptyColumn).size(), gProfile, sm));
		}
		db.commitTransaction(transaction);
	}
	
	public String toString() {
		return (this.db_id + " : " + this.name);
	}

	public void connectUser(User user) {
		this.users.add(user);
	}
	
	public void addUser(User newUser, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("INSERT INTO groupsAndUsersMap values (null, " + this.db_id + ", " + newUser.db_id + ");");
		this.connectUser(newUser);
		
	}
	public void deconnectUser(User user) {
		this.users.remove(user);
	}
	
	public void removeUser(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM groupsAndUsersMap WHERE group_id=" + this.db_id + " AND user_id=" + user.getDBid() + ";");
		this.deconnectUser(user);
	}
}
