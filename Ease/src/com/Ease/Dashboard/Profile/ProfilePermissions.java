package com.Ease.Dashboard.Profile;

import org.json.simple.JSONObject;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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
		DatabaseRequest request = db.prepareRequest("SELECT * FROM profilePermissions WHERE id= ?;");
		request.setInt(id);
		DatabaseResult rs = request.get();
		rs.next();
		int perms = Integer.parseInt(rs.getString(Data.PERMS.ordinal()));
		String group_id = rs.getString(Data.GROUP_ID.ordinal());
		String db_id = rs.getString(Data.ID.ordinal());
		return new ProfilePermissions(db_id, group_id, perms);
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
	public Object getJson() {
		JSONObject json = new JSONObject();
		json.put("rename", this.havePermission(Perm.RENAME.ordinal()));
		json.put("color", this.havePermission(Perm.COLOR.ordinal()));
		json.put("delete", this.havePermission(Perm.DELETE.ordinal()));
		json.put("moveOutSide", this.havePermission(Perm.MOVE_APP_OUTSIDE.ordinal()));
		json.put("addApp", this.havePermission(Perm.ADDAPP.ordinal()));
		return json;
	}
}