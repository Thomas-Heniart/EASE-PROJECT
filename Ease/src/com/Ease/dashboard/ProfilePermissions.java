package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class ProfilePermissions{
	enum Data {
		NOTHING,
		ID,
		GROUP_ID,
		PERMS
	}
	public static enum Perm {
		RENAME(1),
		COLOR(2),
		DELETE(4),
		MOVE_APP_OUTSIDE(8),
		ADDAPP(16),
		ALL(1048575);
		
		private int value;    

		private Perm(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static ProfilePermissions loadProfilePermissions(String id, DataBaseConnection db) throws GeneralException {
		try {
			ResultSet rs = db.get("SELECT * FROM profilePermissions WHERE id=" + id + ";");
			rs.next();
			int perms = Integer.parseInt(rs.getString(Data.PERMS.ordinal()));
			String group_id = rs.getString(Data.GROUP_ID.ordinal());
			String db_id = rs.getString(Data.ID.ordinal());
			return new ProfilePermissions(db_id, group_id, perms);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	public static ProfilePermissions loadPersonnalProfilePermissions(ServletManager sm) throws GeneralException {
		return new ProfilePermissions(null, null, Perm.ALL.getValue());
	}
	
	public static ProfilePermissions loadCommomProfilePermissions(ServletManager sm) throws GeneralException {
		return new ProfilePermissions(null, null, Perm.MOVE_APP_OUTSIDE.getValue() + Perm.ADDAPP.getValue());
	}
	
	public static ProfilePermissions CreateProfilePermissions(int perms, String group_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO profilePermissions VALUES(NULL, " + group_id + ", " + perms + ");").toString();
		return new ProfilePermissions(db_id, group_id, perms);
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String	db_id;
	protected String	group_id;
	protected int		perms;
	
	public ProfilePermissions(String db_id, String group_id, int perms) {
		this.db_id = db_id;
		this.group_id = group_id;
		this.perms = perms;
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return db_id;
	}
	public String getGroupId() {
		return group_id;
	}
	
	public void setPerms(int permissions, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE profilePermissions SET permission=" + permissions + " WHERE id=" + this.db_id + ";");
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (this.db_id != null)
			db.set("REMOVE FROM profilePermissions WHERE id=" + this.db_id + ";");
	}
	
	public boolean havePermission(int perm) {
		if ((perms >> perm) % 2 == 1){
			return true;
		}
		return false;
	}
}