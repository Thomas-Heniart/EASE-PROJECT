package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.Context.Group.Group.Data;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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
	
	public static List<Infrastructure> loadInfrastructures(DataBaseConnection db) throws GeneralException {
		try {
			List<Infrastructure> infras = new LinkedList<Infrastructure>();
			ResultSet rs = db.get("SELECT * FROM infrastructures;");
			Infrastructure infra;
			String db_id;
			String name;
			List<Group> groups;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				name = rs.getString(Data.NAME.ordinal());
				infra = new Infrastructure(db_id, name);
				groups = Group.loadGroups(db, infra);
				infra.setGroups(groups);
			}
			return infras;
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
	
	public Infrastructure(String db_id, String name) {
		this.db_id = db_id;
		this.name = name;
		this.groups = null;
	}
	
	public void removeFromDB(ServletManager sm) {
		DataBaseConnection db = sm.getDB();
		
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */
	
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(ServletManager sm ) throws GeneralException {
		
	}
	
	public String getDBid() {
		return db_id;
	}
}
