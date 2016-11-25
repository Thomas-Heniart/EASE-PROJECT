package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class ProfileInformation {
	public enum InfoData {
		NOTHING,
		ID,
		NAME,
		COLOR
	}
	public static ProfileInformation createProfileInformation(String name, String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO profileInfo values (null, '" + name + "', '" + color + "');");
		return new ProfileInformation(String.valueOf(db_id), name, color);
	}
	
	public static ProfileInformation loadProfileInformation(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM profileInfo WHERE id=" + db_id + ";");
		try {
			if (rs.next())
				return new ProfileInformation(db_id, rs.getString(InfoData.NAME.ordinal()), rs.getString(InfoData.COLOR.ordinal()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}
	
	protected String db_id;
	protected String name;
	protected String color;
	
	public ProfileInformation(String db_id, String name, String color) {
		this.db_id = db_id;
		this.name = name;
		this.color = color;
	}
}
