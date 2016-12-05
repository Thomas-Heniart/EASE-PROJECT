package com.Ease.Dashboard.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class ProfileInformation {
	public enum InfoData {
		NOTHING,
		ID,
		NAME,
		COLOR
	}
	
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static ProfileInformation createProfileInformation(String name, String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO profileInfo values (null, '" + name + "', '" + color + "');");
		return new ProfileInformation(String.valueOf(db_id), name, color);
	}
	
	public static String createProfileInformationForUnconnected(String name, String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO profileInfo values (null, '" + name + "', '" + color + "');").toString();
		return db_id;
	}
	
	public static ProfileInformation loadProfileInformation(String db_id, DataBaseConnection db) throws GeneralException {
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
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String db_id;
	protected String name;
	protected String color;
	
	public ProfileInformation(String db_id, String name, String color) {
		this.db_id = db_id;
		this.name = name;
		this.color = color;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM profileInfo WHERE id=" + this.db_id + ";");
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return this.db_id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE profileInformations SET name='" + name + "' WHERE id=" + this.db_id + ";");
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE profileInformations SET color='" + color + "' WHERE id=" + this.db_id + ";");
	}
}
