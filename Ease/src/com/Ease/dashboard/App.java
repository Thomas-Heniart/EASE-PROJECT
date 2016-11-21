package com.Ease.dashboard;

import org.json.simple.JSONObject;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public abstract class App {
	enum Data {
		NOTHING,
		ID,
		ACCOUNT_ID,
		WEBSITE_ID,
		PROFILE_ID,
		POSITION,
		NAME,
		CUSTOM
	}
	
	public static void loadApps(Profile profile, ServletManager sm) throws GeneralException {
		
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