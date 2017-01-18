package com.Ease.Dashboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class DashboardManager {
	protected User user;
	protected List<App> apps;
	protected List<List<Profile>> profiles;
	protected Map<String, App> appsDBMap;
	protected Map<Integer, App> appsIDMap;
	protected Map<String, Profile> profileDBMap;
	protected Map<Integer, Profile> profileIDMap;
	
	public DashboardManager(User user) {
		this.user = user;
		this.apps = new LinkedList<App>();
		this.appsDBMap = new HashMap<String, App>();
		this.appsIDMap = new HashMap<Integer, App>();
		this.profileDBMap = new HashMap<String, Profile>();
		this.profileIDMap = new HashMap<Integer, Profile>();
	}
	
	public void loadProfiles(ServletManager sm) throws GeneralException {
		this.profiles = Profile.loadProfiles(user, sm);
	}
	
	public void addApp(App app) {
		this.apps.add(app);
		this.appsDBMap.put(app.getDBid(), app);
		this.appsIDMap.put(app.getSingleId(), app);
	}
	
	/*
	 * Profiles getters and setters
	 */
	
	public List<Profile> getProfilesList() {
		List<Profile> profiles = new LinkedList<Profile>();
		for (int i = 1; i < this.profiles.size(); i++) {
			List<Profile> column = this.profiles.get(i);
			for (Profile profile : column) {
				if (profile != null)
					profiles.add(profile);
			}
		}
		return profiles;
	}
	
	public Profile getProfileFromApp(int single_id) throws GeneralException {
		for (List<Profile> column : this.profiles) {
			for (Profile profile : column) {
				for (App app : profile.getApps()) {
					if (app.getSingleId() == single_id)
						return profile;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This app's single_id dosen't exist.");
	}
	
	/*
	 * Apps getter and setters
	 */
	
	public App getAppWithID(Integer single_id) throws GeneralException {
		App app = this.appsIDMap.get(single_id);
		if (app == null)
			throw new GeneralException(ServletManager.Code.ClientError, "No such single_id for apps");
		return app;
	}
	
}
