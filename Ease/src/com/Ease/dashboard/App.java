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
		PROFILE_ID,
		POSITION,
		INSERT_DATE,
		TRASH_DATE,
		TYPE,
		WORK,
		APP_INFO_ID,
		GROUP_APP_ID
	}
	
	@SuppressWarnings("unchecked")
	public static void loadApps(Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ResultSet rs = db.get("SELECT * FROM apps WHERE profile_id=" + profile.getDb_id() + ";");
		try {
			while (rs.next()) {
				String type = rs.getString(AppData.TYPE.ordinal());
				Constructor<App> c = (Constructor<App>) Class.forName("com.Ease.dashboard." + type).getConstructor(ResultSet.class, Profile.class, ServletManager.class);
				App tmpApp = (App) c.newInstance(rs, profile, sm);
				profile.getApps().add(tmpApp);
			}
		} catch (SQLException | SecurityException | IllegalArgumentException | NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		db.commitTransaction(transaction);
	}
	
	protected String db_id;
	protected int single_id;
	protected Profile profile;
	protected Permissions permissions;
	protected boolean working;
	protected int position;
	protected AppInformation informations;
	
	public String getDb_id() {
		return this.db_id;
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
		if (this.profile.getPermissions().havePermission(ProfilePermissions.Perm.MOVE_APP_OUTSIDE.ordinal())) {
			db.set("UPDATE apps SET profile_id = " + profile.getDb_id() + " WHERE id = " + this.getDb_id() + ";");
			this.profile = profile;
		}
		else
			throw new GeneralException(ServletManager.Code.ClientWarning, "Can't move apps outside this profile");
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
		if (this.permissions.havePermission(AppPermissions.Perm.DELETE.ordinal())) {
			this.removeFromDb(sm);
			this.profile.getApps().remove(this);
			this.profile.updateAppsIndex(sm);
		} else
			throw new GeneralException(ServletManager.Code.ClientWarning, "Can't remove this app");
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM apps WHERE id = " + this.getDb_id() + ";");
	}
}