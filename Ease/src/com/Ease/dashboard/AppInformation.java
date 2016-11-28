package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class AppInformation {
	
	public enum Data {
		NOTHING,
		ID,
		NAME
	}
	
	public static AppInformation createAppInformation(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO appInformations values (null, '" + name + "');");
		return new AppInformation(String.valueOf(db_id), name);
	}
	
	public static AppInformation loadAppInformation(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM appInformations WHERE id = " + db_id + " ;");
		String name;
		try {
			name = rs.getString(Data.NAME.ordinal());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return new AppInformation(db_id, name);
	}
	
	protected String db_id;
	protected String name;
	
	public AppInformation(String db_id, String name) {
		this.db_id = db_id;
		this.name = name;
	}
	
	public String getDb_id() {
		return this.db_id;
	}
	
	public String getName() {
		return this.getName();
	}
	
	public void setName(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE appInformations SET name='" + name + "' WHERE id=" + this.db_id + ";");
		this.name = name;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM appInformations WHERE id=" + this.db_id + ";");
	}
}
