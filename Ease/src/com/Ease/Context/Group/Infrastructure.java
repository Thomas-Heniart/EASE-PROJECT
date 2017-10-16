package com.Ease.Context.Group;

import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.LinkedList;
import java.util.List;

public class Infrastructure {
	
	public enum Data {
		NOTHING,
		ID,
		NAME,
		IMG
	}
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static void loadInfrastructures(DataBaseConnection db, ServletContext context) throws GeneralException, HttpServletException {
		IdGenerator idGenerator = (IdGenerator)context.getAttribute("idGenerator");
		DatabaseResult rs = db.prepareRequest("SELECT * FROM infrastructures;").get();
		Infrastructure infra;
		String db_id;
		String name;
		String img_path;
		List<Group> groups;
		int single_id;
		while (rs.next()) {
			db_id = rs.getString(Data.ID.ordinal());
			name = rs.getString(Data.NAME.ordinal());
			img_path = rs.getString(Data.IMG.ordinal());
			single_id = idGenerator.getNextId();
			DatabaseRequest request = db.prepareRequest("SELECT user_id FROM infrastructuresAdminsMap WHERE infrastructure_id = ?;");
			request.setInt(db_id);
			DatabaseResult rs2 = request.get();
			List<String> adminIds = new LinkedList<String>();
			while (rs2.next())
				adminIds.add(rs2.getString(1));
			infra = new Infrastructure(db_id, name, img_path, adminIds, single_id);
			GroupManager.getGroupManager(context).add(infra);
			groups = Group.loadGroups(db, infra, context);
			infra.setGroups(groups);
		}
	}
	
	public static Infrastructure createInfrastructure(String name, String img_path, ServletManager sm) throws GeneralException {
		return createInfrastructure(name, img_path, new LinkedList<String>(), sm);
	}
	
	public static Infrastructure createInfrastructure(String name, String img_path, List<String> adminIds, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		DatabaseRequest request = db.prepareRequest("INSERT INTO infrastructures values(NULL, ?, ?);");
		request.setString(name);
		request.setString(img_path);
		String db_id = request.set().toString();
		for (String id : adminIds) {
			request = db.prepareRequest("INSERT INTO infrastructuresAdminsMap values(?, ?, ?)");
			request.setNull();
			request.setInt(id);
			request.setInt(db_id);
			request.set();
		}
		db.commitTransaction(transaction);
		IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
		Infrastructure infra = new Infrastructure(db_id, name, img_path, adminIds, idGenerator.getNextId());
		GroupManager.getGroupManager(sm).add(infra);
		return infra;
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String	db_id;
	protected String 	name;
	protected String	img_path;
	protected List<Group>		groups;
	protected List<String> adminIds;
	protected int 		single_id;
	
	public Infrastructure(String db_id, String name, String img_path, List<String> adminIds, int single_id) {
		this.db_id = db_id;
		this.name = name;
		this.groups = null;
		this.single_id = single_id;
		this.img_path = img_path;
		this.groups = new LinkedList<Group> ();
		this.adminIds = adminIds;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException, HttpServletException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (Group group : groups) {
			group.removeFromDb(sm);
		}
		GroupManager.getGroupManager(sm).remove(this);
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */
	
	public int getSingleId() {
		return single_id;
	}
	
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLogoPath() {
		return "/resources/infras/" + this.img_path;
	}
	
	public List<String> getAdminIds() {
		return this.adminIds;
	}
	
	public void addAdmin(String id, ServletManager sm) throws GeneralException {
		if (this.adminIds.contains(id))
			throw new GeneralException(ServletManager.Code.ClientWarning, "This user is already an admin");
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("INSERT INTO infrastructuresAdminsMap values(?, ?, ?);");
		request.setNull();
		request.setInt(id);
		request.setInt(db_id);
		request.set();
		this.adminIds.add(id);
	}
	
	public void addAdmins(List<String> adminIds, ServletManager sm) throws GeneralException {
		for (String id : adminIds)
			this.addAdmin(id, sm);
	}
	
	public void setName(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		DatabaseRequest request = db.prepareRequest("UPDATE infrastructures set name= ? WHERE id= ?;");
		request.setString(name);
		request.setInt(db_id);
		request.set();
		this.name = name;
		db.commitTransaction(transaction);
	}
	
	public void setImgPath(String img_path, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		DatabaseRequest request = db.prepareRequest("UPDATE infrastructures set img_path= ? WHERE id= ?;");
		request.setString(img_path);
		request.setInt(db_id);
		request.set();
		this.img_path = img_path;
		db.commitTransaction(transaction);
	}
	
	public String getDBid() {
		return db_id;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		json.put("name", this.name);
		json.put("imgPath", this.img_path);
		JSONArray array = new JSONArray();
		for (Group group : this.groups) {
			array.add(group.getJson());
		}
		json.put("groups", array);
		return json;
	}

	public void addGroup(Group group) {
		this.groups.add(group);
	}

	public boolean isAdmin(String user_id) {
		return this.adminIds.contains(user_id);
	}
}
