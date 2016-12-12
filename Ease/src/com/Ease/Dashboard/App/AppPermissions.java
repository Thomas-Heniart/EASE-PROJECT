package com.Ease.Dashboard.App;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class AppPermissions{
	enum Data {
		NOTHING,
		ID,
		GROUP_ID,
		PERMS
	}
	public static enum Perm {
		RENAME(1),
		EDIT(2),
		SHOWINFO(4),
		DELETE(8),
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
	
	public static AppPermissions loadAppPermissions(String id, DataBaseConnection db) throws GeneralException {
		try {
			ResultSet rs = db.get("SELECT * FROM appPermissions WHERE id=" + id + ";");
			rs.next();
			int perms = Integer.parseInt(rs.getString(Data.PERMS.ordinal()));
			String group_id = rs.getString(Data.GROUP_ID.ordinal());
			String db_id = rs.getString(Data.ID.ordinal());
			return new AppPermissions(db_id, group_id, perms);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	public static AppPermissions loadPersonnalAppPermissions(ServletManager sm) throws GeneralException {
		return new AppPermissions(null, null, Perm.ALL.getValue());
	}
	
	public static AppPermissions loadCommomAppPermissions(ServletManager sm) throws GeneralException {
		return new AppPermissions(null, null, 0);
	}
	
	public static AppPermissions CreateAppPermissions(int perms, String group_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO appPermissions VALUES(NULL, " + group_id + ", " + perms + ");").toString();
		return new AppPermissions(db_id, group_id, perms);
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String	db_id;
	protected String	group_id;
	protected int		perms;
	
	public AppPermissions(String db_id, String group_id, int perms) {
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
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (this.db_id != null)
			db.set("REMOVE FROM appPermissions WHERE id=" + this.db_id + ";");
	}
	
	public boolean havePermission(int perm) {
		if ((perms >> perm) % 2 == 1){
			return true;
		}
		return false;
	}
}