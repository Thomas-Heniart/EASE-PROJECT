package com.Ease.Dashboard.App;

import org.json.simple.JSONObject;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
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
        SHOW_PASSWORD(16),

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
		DatabaseRequest request = db.prepareRequest("SELECT * FROM appPermissions WHERE id= ?;");
		request.setInt(id);
		DatabaseResult rs = request.get();
		rs.next();
		int perms = Integer.parseInt(rs.getString(Data.PERMS.ordinal()));
		String group_id = rs.getString(Data.GROUP_ID.ordinal());
		String db_id = rs.getString(Data.ID.ordinal());
		return new AppPermissions(db_id, group_id, perms);
	}

	public static AppPermissions loadPersonnalAppPermissions(ServletManager sm) throws GeneralException {
		return new AppPermissions(null, null, Perm.ALL.getValue());
	}
	
	public static AppPermissions loadCommomAppPermissions(ServletManager sm) throws GeneralException {
		return new AppPermissions(null, null, 0);
	}
	
	public static AppPermissions CreateAppPermissions(int perms, String group_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("INSERT INTO appPermissions VALUES(NULL, ?, ?);");
		request.setInt(group_id);
		request.setInt(perms);
		String db_id = request.set().toString();
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
		DatabaseRequest request = db.prepareRequest("DELETE FROM appPermissions WHERE id = ?;");
		request.setInt(db_id);
		request.set();
	}
	
	public boolean havePermission(int perm) {
		return (perms & perm) == perm;
	}

	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		json.put("rename", this.havePermission(Perm.RENAME.ordinal()));
		json.put("delete", this.havePermission(Perm.DELETE.ordinal()));
		json.put("showInfo", this.havePermission(Perm.SHOWINFO.ordinal()));
		json.put("edit", this.havePermission(Perm.EDIT.ordinal()));
		return json;
	}
}