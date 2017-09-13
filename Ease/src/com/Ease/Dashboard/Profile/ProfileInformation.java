package com.Ease.Dashboard.Profile;

import com.Ease.Utils.*;
import org.json.simple.JSONObject;

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
	
	public static ProfileInformation createProfileInformation(String name, String color, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("INSERT INTO profileInfo values (null, ?, ?);");
		request.setString(name);
		request.setString(color);
		int db_id = request.set();
		return new ProfileInformation(String.valueOf(db_id), name, color);
	}
	
	public static String createProfileInformationForUnconnected(String name, String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("INSERT INTO profileInfo values (null, ?, ?);");
		request.setString(name);
		request.setString(color);
		String db_id = request.set().toString();
		return db_id;
	}
	
	public static ProfileInformation loadProfileInformation(String db_id, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM profileInfo WHERE id= ?;");
		request.setInt(db_id);
		DatabaseResult rs = request.get();
		if (rs.next())
				return new ProfileInformation(db_id, rs.getString(InfoData.NAME.ordinal()), rs.getString(InfoData.COLOR.ordinal()));
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

	public void removeFromDB(DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("DELETE FROM profileInfo WHERE id = ?;");
		request.setInt(db_id);
		request.set();
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
		DatabaseRequest request = db.prepareRequest("UPDATE profileInfo SET name = ? WHERE id = ?;");
		request.setString(name);
		request.setInt(db_id);
		request.set();
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("UPDATE profileInfo SET color = ? WHERE id = ?;");
		request.setString(color);
		request.setInt(db_id);
		request.set();
		this.color = color;
	}

	public JSONObject getJson() {
		JSONObject res = new JSONObject();
		res.put("name", this.name);
		res.put("color", this.color);
		return res;
	}
}
