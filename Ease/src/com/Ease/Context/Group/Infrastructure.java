package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;
import com.sun.xml.internal.ws.api.server.DocumentAddressResolver;

public class Infrastructure {
	
	public enum Data {
		NOTHING,
		ID,
		NAME
	}
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static void loadInfrastructures(DataBaseConnection db, ServletContext context) throws GeneralException {
		try {
			IdGenerator idGenerator = (IdGenerator)context.getAttribute("idGenerator");
			ResultSet rs = db.get("SELECT * FROM infrastructures;");
			Infrastructure infra;
			String db_id;
			String name;
			List<Group> groups;
			int single_id;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				name = rs.getString(Data.NAME.ordinal());
				single_id = idGenerator.getNextId();
				infra = new Infrastructure(db_id, name, single_id);
				GroupManager.getGroupManager(context).add(infra);
				groups = Group.loadGroups(db, infra, context);
				infra.setGroups(groups);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String	db_id;
	protected String 	name;
	protected List<Group>		groups;
	protected int 		single_id;
	
	public Infrastructure(String db_id, String name, int single_id) {
		this.db_id = db_id;
		this.name = name;
		this.groups = null;
		this.single_id = single_id;
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
	public void setName(ServletManager sm) throws GeneralException {
		
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
		try {
			DataBaseConnection db = sm.getDB();
			ResultSet rs = db.get("SELECT * FROM infrastructuresAdminsMap WHERE infrastructure_id=" + this.db_id + " AND user_id=" + user.getDBid() + ";");
			if (rs.next()) {
				return true;
			} else {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permissions to do that.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
