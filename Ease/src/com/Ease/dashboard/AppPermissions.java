package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class AppPermissions extends Permissions {
	enum Data {
		NOTHING,
		ID,
		GROUP_ID,
		PERMS
	}
	enum Perm {
		RENAME,
		MODIFY,
		MOVE,
		CHANGEPROFILE,
		SHOWINFO,
		DELETE
	}
	protected static String DEFAULT_PERM_ID = "0";
	
	public static AppPermissions loadAppPermissions(String id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
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
	public static AppPermissions loadDefaultAppPermissions(ServletManager sm) throws GeneralException {
		return loadAppPermissions(DEFAULT_PERM_ID, sm);
	}
	
	public static AppPermissions CreateAppPermissions(int perms, String group_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO appPermissions VALUES(NULL, " + group_id + ", " + perms + ");").toString();
		return new AppPermissions(db_id, group_id, perms);
	}
	
	public AppPermissions(String db_id, String group_id, int perms) {
		super(db_id, group_id, perms);
	}
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("REMOVE FROM appPermissions WHERE id=" + this.db_id + ";");
	}
}
