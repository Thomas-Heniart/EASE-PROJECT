package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class ProfilePermissions extends Permissions {
	enum Data {
		NOTHING,
		ID,
		GROUP_ID,
		PERMS
	}
	enum Perm {
		RENAME,
		COLOR,
		MOVE,
		DELETE,
		ADDAPP
	}
	protected static String DEFAULT_PERM_ID = "0";
	
	public static ProfilePermissions loadProfilePermissions(String id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
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
	public static ProfilePermissions loadDefaultProfilePermissions(ServletManager sm) throws GeneralException {
		return loadProfilePermissions(DEFAULT_PERM_ID, sm);
	}
	
	public static ProfilePermissions CreateProfilePermissions(int perms, String group_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO profilePermissions VALUES(NULL, " + group_id + ", " + perms + ");").toString();
		return new ProfilePermissions(db_id, group_id, perms);
	}
	
	public ProfilePermissions(String db_id, String group_id, int perms) {
		super(db_id, group_id, perms);
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("REMOVE FROM profilePermissions WHERE id=" + this.db_id + ";");
	}
}
