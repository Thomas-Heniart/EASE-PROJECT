package com.Ease.dashboard;

import org.json.simple.JSONObject;

public abstract class App {
	enum AppData {
		NOTHING,
		ID,
		ACCOUNT_ID,
		WEBSITE_ID,
		PROFILE_ID,
		POSITION,
		NAME,
		CUSTOM
	}
	public enum AppPerm {
		RENAME,
		MODIFY,
		MOVE,
		CHANGEPROFILE,
		SHOWINFO,
		DELETE
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
		//Set profile for this app in db
		this.profile = profile;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position, ServletManager sm) throws GeneralException {
		//Set position for this app in db
		this.position = position;
	}
	
	public boolean isWorking() {
		return this.working;
	}
	
	public JSONObject getInformations() {
		JSONObject res = new JSONObject();
		
		return res;
	}
}