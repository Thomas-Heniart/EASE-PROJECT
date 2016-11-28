package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Group {
	enum Data {
		NOTHING,
		ID,
		NAME,
		PARENT,
		INFRA
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
			ResultSet rs = db.get("SELECT * FROM groups WHERE parent=" + (parent == null ? "NULL" : parent.getDBid()) + ";");
			String db_id;
			String name;
			List<GroupProfile> groupProfiles;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
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
	
	public Group(String db_id, String name, Group parent) {
		this.db_id = db_id;
		this.name = name;
		this.children = null;
		this.parent = parent;
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
}
