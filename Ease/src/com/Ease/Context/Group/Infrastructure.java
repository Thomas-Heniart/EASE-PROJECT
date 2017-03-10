package com.Ease.Context.Group;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

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
	
	public static void loadInfrastructures(DataBaseConnection db, ServletContext context) throws GeneralException {
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
			infra = new Infrastructure(db_id, name, img_path, single_id);
			GroupManager.getGroupManager(context).add(infra);
			groups = Group.loadGroups(db, infra, context);
			infra.setGroups(groups);
		}
	}
	
	public static Infrastructure createInfrastructure(String name, String img_path, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO infrastructures values(NULL, '" + name + "', '" + img_path + "');").toString();
		IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
		Infrastructure infra = new Infrastructure(db_id, name, img_path, idGenerator.getNextId());
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
	protected int 		single_id;
	
	public Infrastructure(String db_id, String name, String img_path, int single_id) {
		this.db_id = db_id;
		this.name = name;
		this.groups = null;
		this.single_id = single_id;
		this.img_path = img_path;
		this.groups = new LinkedList<Group> ();
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
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
	
	public void setName(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("UPDATE infrastructures set name='" + name + "' WHERE id=" + this.db_id + ";");
		this.name = name;
		db.commitTransaction(transaction);
	}
	
	public void setImgPath(String img_path, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("UPDATE infrastructures set img_path='" + img_path + "' WHERE id=" + this.db_id + ";");
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
	
	public boolean isAdmin(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM infrastructuresAdminsMap WHERE infrastructure_id= ? AND user_id= ?;");
		request.setInt(this.db_id);
		request.setInt(user.getDBid());
		DatabaseResult rs = request.get();
		if (rs.next()) {
			return true;
		} else {
			throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permissions to do that.");
		}
	}
	
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
}
