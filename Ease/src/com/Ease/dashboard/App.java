package com.Ease.dashboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public abstract class App {
	enum AppData {
		NOTHING,
		ID,
		NAME,
		PROFILE_ID,
		POSITION,
		PERMISSION_ID,
		TYPE,
		WORK
	}
	
	@SuppressWarnings("unchecked")
	public static void loadApps(Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ResultSet rs = db.get("SELECT * FROM apps WHERE profile_id=" + profile.getDb_id() + ";");
		try {
			while (rs.next()) {
				String db_id = rs.getString(AppData.ID.ordinal());
				String type = rs.getString(AppData.TYPE.ordinal());
				Constructor<App> c = (Constructor<App>) Class.forName("com.Ease.dashboard." + type).getConstructor(String.class, Profile.class, ServletManager.class);
				App tmpApp = (App) c.newInstance(db_id, profile, sm);
				profile.getApps().add(tmpApp);
			}
		} catch (SQLException | SecurityException | IllegalArgumentException | NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
	
	public void updateApp(App updatedApp) {
		this.profile.replaceApp(this, updatedApp);
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