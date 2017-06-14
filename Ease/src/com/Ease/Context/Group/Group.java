package com.Ease.Context.Group;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.User.User;

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
		return createGroup(name, parent, infra, new LinkedList<String>(), sm);
	}
	
	public static Group createGroup(String name, Group parent, Infrastructure infra, List<String> adminIds, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String infra_id = (infra == null) ? "null" : infra.getDBid();
		IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
		DatabaseRequest request = db.prepareRequest("INSERT INTO groups values (null, ?, ?, ?);");
		request.setString(name);
		if (parent == null)
			request.setNull();
		else
			request.setInt(parent.getDBid());
		request.setInt(infra_id);
		int db_id = request.set();
		for (String id : adminIds) {
			request = db.prepareRequest("INSERT INTO groupAdminsMap values(?, ?, ?);");
			request.setNull();
			request.setInt(db_id);
			request.setInt(id);
			request.set();
		}
		db.commitTransaction(transaction);
		Group group = new Group(String.valueOf(db_id), name, parent, infra, adminIds, idGenerator.getNextId());
		if (parent != null)
			parent.getChildren().add(group);
		GroupManager.getGroupManager(sm).add(group);
		return group;
	}
	
	public static List<Group> loadGroups(DataBaseConnection db, Infrastructure infra, ServletContext context)throws GeneralException {
		return loadGroup(db, null, infra, context);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Group> loadGroup(DataBaseConnection db, Group parent, Infrastructure infra, ServletContext context) throws GeneralException {
		IdGenerator idGenerator = (IdGenerator)context.getAttribute("idGenerator");
		Catalog catalog = (Catalog) context.getAttribute("catalog");
		List<Group> groups = new LinkedList<Group>();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM groups WHERE parent " + ((parent == null) ? " IS NULL" : ("="+parent.getDBid())) + " AND infrastructure_id= ?;");
		request.setInt(infra.getDBid());
		DatabaseResult rs = request.get();
		String db_id;
		String name;
		int single_id;
		List<GroupProfile> groupProfiles;
		List<GroupApp> groupApps;
		while (rs.next()) {
			db_id = rs.getString(Data.ID.ordinal());
			name = rs.getString(Data.NAME.ordinal());
			single_id = idGenerator.getNextId();
			List<String> adminIds = new LinkedList<String> ();
			request = db.prepareRequest("SELECT user_id FROM groupAdminsMap WHERE group_id = ?;");
			request.setInt(db_id);
			DatabaseResult rs2 = request.get();
			while (rs2.next())
				adminIds.add(rs2.getString(1));
			Group child = new Group(db_id, name, parent, infra, adminIds,single_id);
			child.setChildrens(loadGroup(db, child, infra, context));
			groupProfiles = GroupProfile.loadGroupProfiles(child, db, context);
			child.setGroupProfiles(groupProfiles);	
			groupApps = GroupApp.loadGroupApps(child, db, context);
			child.setGroupApps(groupApps);
			groups.add(child);
			child.loadWebsites(db, catalog);
			GroupManager.getGroupManager(context).add(child);
		}
		return groups;
	}
	
	
	private void loadWebsites(DataBaseConnection db, Catalog catalog) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT website_id FROM websitesAndGroupsMap WHERE group_id = ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		while (rs.next()) {
			System.out.println("Catalog websites size : " + catalog.getWebsites().size());
			this.groupWebsites.add(catalog.getWebsiteWithDBid(rs.getString(1)));
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
	protected List<GroupApp> groupApps;
	protected Infrastructure infra;
	protected int	single_id;
	protected List<Website> groupWebsites;
	protected List<String> adminIds;
	
	public Group(String db_id, String name, Group parent, Infrastructure infra, List<String> adminIds,int single_id) {
		this.db_id = db_id;
		this.name = name;
		this.children = new LinkedList<Group>();
		this.parent = parent;
		this.infra = infra;
		this.single_id = single_id;
		this.groupProfiles = null;
		this.groupApps = null;
		this.groupWebsites = new LinkedList<Website>();
		this.adminIds = adminIds;
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
		return this.single_id;
	}
	
	public void setChildrens(List<Group> children) {
		this.children = children;
	}
	public void setGroupProfiles(List<GroupProfile> groupProfiles) {
		this.groupProfiles = groupProfiles;
	}
	public void setGroupApps(List<GroupApp> groupApps) {
		this.groupApps = groupApps;
	}
	public List<Group> getChildren() {
		return this.children;
	}
	public List<GroupProfile> getGroupProfiles() {
		return this.groupProfiles;
	}
	public List<GroupApp> getGroupApps() {
		return this.groupApps;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("UPDATE groups SET name = ? WHERE id = ?;");
		request.setString(name);
		request.setInt(db_id);
		request.set();
		this.name = name;
	}
	
	public Infrastructure getInfra() {
		return infra;
	}
	
	/*
	 * 
	 * Utils 
	 * 
	 */
	
	public void removeFromDb(ServletManager sm) throws GeneralException, HttpServletException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (Group group : children) {
			group.removeFromDb(sm);
		}
		Map<String, User> users = (Map<String, User>) sm.getContextAttr("users");
		DatabaseRequest request = db.prepareRequest("SELECT email, user_id FROM groupsAndUsersMap JOIN users ON user_id = users.id WHERE group_id= ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		while (rs.next()) {
			String email  = rs.getString(1);
			String user_id = rs.getString(2);
			User user;
			if ((user = users.get(email)) != null) {
				this.removeContentForConnectedUser(user, sm);
			} else {
				this.removeContentForUnconnectedUser(user_id, sm);
			}
		}
		request = db.prepareRequest("SELECT user_id FROM groupsAndUsersMap WHERE group_id = ?;");
		request.setInt(this.db_id);
		rs = request.get();
		DatabaseRequest request2;
		while (rs.next()) {
			if (this.parent != null) {
				request2 = db.prepareRequest("UPDATE groupsAndUsersMap SET group_id = ? WHERE group_id = ? AND user_id = ?;");
				request2.setInt(this.parent.db_id);
				request2.setInt(db_id);
				request2.setInt(rs.getString(1));
			} else {
				request2 = db.prepareRequest("DELETE FROM groupsAndUsersMap WHERE group_id = ? AND user_id = ?;");
				request2.setInt(db_id);
				request2.setInt(rs.getString(1));
			}
			request2.set();
		}
		for (GroupProfile groupProfile : this.groupProfiles) {
			groupProfile.removeFromDb(sm);
		}
		for (GroupApp groupApp : this.groupApps) {
			groupApp.removeFromDb(sm);
		}
		GroupManager.getGroupManager(sm).remove(this);
		request = db.prepareRequest("DELETE FROM groups WHERE id = ?;");
		request.setInt(db_id);
		request.set();
		db.commitTransaction(transaction);
	}
	
	private void loadContentForConnectedUser(User user, ServletManager sm) throws GeneralException {
		for (GroupProfile groupProfile: this.groupProfiles) {
			groupProfile.loadContentForConnectedUser(user, sm);
		}
		for (GroupApp groupApp: this.groupApps) {
			groupApp.loadContentForConnectedUser(user, sm);
		}
	}
	private void loadContentForUnconnectedUser(String db_id, ServletManager sm) throws GeneralException {
		for (GroupProfile groupProfile: this.groupProfiles) {
			groupProfile.loadContentForUnconnectedUser(db_id, sm);
		}
		for (GroupApp groupApp: this.groupApps) {
			groupApp.loadContentForUnconnectedUser(db_id, sm);
		}
	}
	private void removeContentForConnectedUser(User user, ServletManager sm) throws GeneralException, HttpServletException {
		for (GroupProfile groupProfile: this.groupProfiles) {
			groupProfile.removeContentForConnectedUser(user, sm);
		}
		for (GroupApp groupApp: this.groupApps) {
			groupApp.removeContentForConnectedUser(user, sm);
		}
	}
	private void removeContentForUnconnectedUser(String db_id, ServletManager sm) throws GeneralException {
		for (GroupProfile groupProfile: this.groupProfiles) {
			groupProfile.removeContentForUnconnectedUser(db_id, sm);
		}
		for (GroupApp groupApp: this.groupApps) {
			groupApp.removeContentForUnconnectedUser(db_id, sm);
		}
	}
	
	private void loadAllContentUnconnected(String db_id, ServletManager sm) throws GeneralException {
		if (this.parent != null)
			this.parent.loadAllContentUnconnected(db_id, sm);
		this.loadContentForUnconnectedUser(db_id, sm);
	}
	
	private void loadAllContentConnected(User user, ServletManager sm) throws GeneralException {
		if (this.parent != null)
			this.parent.loadAllContentConnected(user, sm);
		this.loadContentForConnectedUser(user, sm);
	}
	
	public void addUser(String email, String name, boolean testUser,ServletManager sm) throws GeneralException {
		@SuppressWarnings("unchecked")
		Map<String, User> users = (Map<String, User>) sm.getContextAttr("users");
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String userDBid = null;
		try {
			userDBid = User.findDBid(email, sm);
		}
		catch (GeneralException e) {
			if (e.getCode() == ServletManager.Code.ClientError) {
				if (testUser)
					Invitation.sendInvitation(email, name, this, sm);
				db.commitTransaction(transaction);
				return;
			} else {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
		}
		User user;
		if ((user = users.get(email)) != null) {
			this.loadAllContentConnected(user, sm);
		} else {
			this.loadAllContentUnconnected(userDBid, sm);
		}
		DatabaseRequest request = db.prepareRequest("INSERT INTO groupsAndUsersMap VALUES(NULL, ?, ?, ?);");
		request.setInt(db_id);
		request.setInt(userDBid);
		request.setBoolean(user.tutoDone());
		request.set();
		db.commitTransaction(transaction);
	}

	public void tutoStepDone(String user_id, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("UPDATE groupsAndUsersMap SET saw_group = 1 WHERE group_id = ? AND user_id = ?;");
		request.setInt(db_id);
		request.setInt(user_id);
		request.set();
	}
	
	public boolean containsWebsite(Website website) {
		if (this.parent == null)
			return this.groupWebsites.contains(website);
		return this.groupWebsites.contains(website) || this.parent.containsWebsite(website);
	}
	
	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		json.put("name", this.name);
		json.put("groupId", this.single_id);
		JSONArray array = new JSONArray();
		for (Group child : children) {
			array.add(child.getJson());
		}
		json.put("children", array);
		return json;
	}

	public Group getParent() {
		return this.parent;
	}
	
	public void addGroupProfile(GroupProfile groupProfile) {
		this.groupProfiles.add(groupProfile);
	}
	
	public void addGroupApp(GroupApp groupApp) {
		this.groupApps.add(groupApp);
	}

	public boolean isAdmin(String user_id) {
		return this.infra.isAdmin(user_id) || this.adminIds.contains(user_id);
	}
}
