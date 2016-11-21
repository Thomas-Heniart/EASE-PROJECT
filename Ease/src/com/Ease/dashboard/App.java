package com.Ease.dashboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import com.Ease.dashboard.Profile.Data;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public abstract class App {
	enum Data {
		NOTHING,
		ID,
		NAME,
		PROFILE_ID,
		POSITION,
		PERMISSION_ID,
		TYPE,
		WORK
	}
	
	protected static abstract App loadContent(String name, Profile profile, Permissions permissions, int position, String db_id, boolean working, ServletManager sm) throws GeneralException;
	
	public static void loadApps(Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String name;
		String db_id;
		String type;
		Permissions permissions;
		boolean working;
		int position;
		ResultSet rs = db.get("SELECT * FROM apps WHERE profile_id=" + profile.getDb_id() + ";");
		try {
			while (rs.next()) {
				db_id = rs.getString(AppData.ID.ordinal());
				name = rs.getString(AppData.NAME.ordinal());
				permissions = AppPermissions.loadAppPermissions(rs.getString(Data.PERMS.ordinal()), sm);
				working = rs.getString(AppData.WORK.ordinal()).equals("1") ? true : false;
				position = Integer.parseInt(rs.getString(AppData.POSITION.ordinal()));
				type = rs.getString(AppData.TYPE.ordinal());
				Method method = Class.forName("com.Ease.dashboard" + type).getMethod("loadContent", String.class, Profile.class, Permissions.class, Integer.class, String.class, Boolean.class, ServletManager.class);
				App loadApp =  (App) method.invoke(null, name, profile, permissions, position, db_id, working, sm);
				profile.getApps().add(loadApp);
			}
		} catch (SQLException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		db.commitTransaction(transaction);
	}
	
	protected String name;
	protected String db_id;
	protected int single_id;
	protected Profile profile;
	protected Permissions permissions;
	protected boolean working;
	protected int position;
	public String getName() {
		return name;
	}
	public String getDb_id() {
		return this.db_id;
	}
	public void setName(String name, ServletManager sm) throws GeneralException {
		//Update name in db
		this.name = name;
	}
	public int getSingle_id() {
		return single_id;
	}
	public void setSingle_id(int single_id) {
		this.single_id = single_id;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE apps SET profile_id = " + profile.getDb_id() + " WHERE id = " + this.getDb_id() + ";");
		this.profile = profile;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE apps SET position = " + position + " WHERE id = " + this.getDb_id() + ";");
		this.position = position;
	}
	
	public boolean isWorking() {
		return this.working;
	}
	
	public JSONObject getInformations() {
		JSONObject res = new JSONObject();
		// get JSON for connection
		return res;
	}
	
	public void remove(ServletManager sm) throws GeneralException {
		this.removeFromDb(sm);
		this.profile.getApps().remove(this);
		this.profile.updateAppsIndex(sm);
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM apps WHERE id = " + this.getDb_id() + ";");
	}
}