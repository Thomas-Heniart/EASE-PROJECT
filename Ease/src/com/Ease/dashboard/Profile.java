package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Profile {
	enum Data {
		NOTHING,
		ID,
		USER_ID,
		COLUMN_IDX,
		POSITION_IDX,
		GROUP_PROFILE_ID,
		PROFILE_INFO_ID,
	}
	
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static void loadProfiles(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM profiles where user_id=" + user.getDBid() + ";");
		Profile profile;
		try {
			while (rs.next()) {
				profile = loadProfile(user, rs, sm);
				user.getProfilesColumn().get(rs.getInt(Data.COLUMN_IDX.ordinal())).add(profile);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		for (int i = 0; i < user.getProfilesColumn().size(); ++i) {
			user.getProfilesColumn().get(i).sort(new Comparator<Profile>() {
				@Override
				public int compare(Profile a, Profile b) {
					return a.getPositionIdx() - b.getPositionIdx();
				}
			});
		}
	}

	public static Profile loadProfile(User user, ResultSet rs, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id;
		ProfilePermissions perms;
		ProfileInformation informations;
		int columnIdx;
		int positionIdx;
		int single_id;
		try {
			db_id = rs.getString(Data.ID.ordinal());
			columnIdx = rs.getInt(Data.COLUMN_IDX.ordinal());
			positionIdx = rs.getInt(Data.POSITION_IDX.ordinal());
			single_id = sm.getNextSingleId();
			String group_profile_id = rs.getString(Data.GROUP_PROFILE_ID.ordinal());
			if (!(group_profile_id == null || group_profile_id.equals("null"))) {
				ResultSet rs2 = db.get("SELECT permission_id, common FROM groupProfiles WHERE id=" + group_profile_id + ";");
				rs2.next();
				if (rs2.getBoolean(2)) {
					perms = ProfilePermissions.loadCommomProfilePermissions(sm);
				} else {
					perms = ProfilePermissions.loadProfilePermissions(rs2.getString(1), sm);
				}
			}
			else
				perms = ProfilePermissions.loadPersonnalProfilePermissions(sm);
			informations = ProfileInformation.loadProfileInformation(rs.getString(Data.PROFILE_INFO_ID.ordinal()), sm);
			return new Profile(db_id, user, perms, columnIdx, positionIdx, single_id, informations);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static Profile createProfile(String name, String color, String group_profile_id, User user, ServletManager sm) throws GeneralException {
		int columnIdx = Profile.getMostLittleProfileColumn(user);
		int positionIdx = user.getProfilesColumn().get(columnIdx).size();
		ProfilePermissions perms; 
		int single_id = sm.getNextSingleId();
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		if (group_profile_id == null || group_profile_id.equals("null"))
			perms = ProfilePermissions.loadPersonnalProfilePermissions(sm);
		else {
			ResultSet rs2 = db.get("SELECT permission_id, common FROM groupProfiles WHERE id=" + group_profile_id + ";");
			try {
				rs2.next();
				if (rs2.getBoolean(2)) {
					perms = ProfilePermissions.loadCommomProfilePermissions(sm);
				} else {
					perms = ProfilePermissions.loadProfilePermissions(rs2.getString(1), sm);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
		}
		ProfileInformation informations = ProfileInformation.createProfileInformation(name, color, sm);
		String db_id = db.set("INSERT INTO profiles VALUES(NULL, " + user.getDBid() + ", '" + name + "', '" + color + ", " + columnIdx + ", " + positionIdx + ");").toString();
		db.commitTransaction(transaction);
		return new Profile(db_id, user, perms, columnIdx, positionIdx, single_id, informations);
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String	db_id;
	protected User		user;
	protected ProfilePermissions permissions;
	protected int		columnIdx;
	protected int		positionIdx;
	protected List<App> apps;
	protected int 		single_id;
	ProfileInformation informations;

	
	public Profile(String db_id, User user, ProfilePermissions perms, int columnIdx, int positionIdx, int single_id, ProfileInformation informations) {
		this.db_id = db_id;
		this.user = user;
		this.permissions = perms;
		this.columnIdx = columnIdx;
		this.positionIdx = positionIdx;
		this.apps = new LinkedList<App>();
		this.single_id = single_id;
		this.informations = informations;
	}
	
	public void remove(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		//delete all apps
		this.removeFromDB(sm);
		this.user.getProfilesColumn().get(this.columnIdx).remove(this);
		this.user.updateProfilesIndex(sm);
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDb_id() {
		return this.db_id;
	}
	
	public User getUser() {
		return user;
	}
	
	public int getColumnIdx() {
		return columnIdx;
	}
	public void setColumnIdx(int idx, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE FROM profiles SET columnIdx=" + idx + " WHERE id=" + this.db_id + ";");
		this.columnIdx = idx;
	}
	
	public int getPositionIdx() {
		return this.positionIdx;
	}
	public void setPositionIdx(int idx, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE FROM profiles SET positionIdx=" + idx + " WHERE id=" + this.db_id + ";");
		this.positionIdx = idx;
	}
	
	public List<App> getApps() {
		return apps;
	}
	
	public App getApp(int single_id) throws GeneralException {
		for (int i = 0; i < apps.size(); ++i) {
			if (apps.get(i).getSingle_id() == single_id)
				return apps.get(i);
		}
		throw new GeneralException(ServletManager.Code.ClientError, "App's single id dosen't exist.");
	}
	
	public int getSingleId() {
		return this.single_id;
	}
	
	/*
	 * Utils functions
	 * 
	 */
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Iterator<App> it = this.apps.iterator();
		while(it.hasNext()) {
			it.next().removeFromDb(sm);
		}
		db.set("DELETE FROM profiles WHERE id=" + this.db_id + ";");
		db.commitTransaction(transaction);
	}
	
	public int getSize() {
		if (apps.size() <= 3) {
			return 2;
		}
		return (apps.size() + 2) / 3;
	}
	
	public int getNextPosition() {
		return apps.size();
	}
	
	public ProfilePermissions getPermissions() {
		return this.permissions;
	}
	
	public static int getMostLittleProfileColumn(User user) {
		int mostLittleProfileColumnIdx = -1;
		int size;
		int ret = -1;
		for (int i = 0; i < user.getProfilesColumn().size(); ++i) {
			size = 0;
			for (int j = 0; j < user.getProfilesColumn().get(i).size(); ++j) {
				size += user.getProfilesColumn().get(i).get(j).getSize();
			}
			if (ret == -1 || ret > size) {
				ret = size;
				mostLittleProfileColumnIdx = i;
			}
		}
		return mostLittleProfileColumnIdx;
	}
	
	public void move(int columnIdx, int positionIdx, ServletManager sm) throws GeneralException {
		this.user.getProfilesColumn().get(this.columnIdx).remove(this);
		this.user.getProfilesColumn().get(columnIdx).add(positionIdx, this);
		this.user.updateProfilesIndex(sm);
	}
	
	public void replaceApp(App appToReplace, App newApp) {
		int position = this.apps.indexOf(appToReplace);
		this.apps.set(position, newApp);
	}
	
	public void updateAppsIndex(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (int i = 0; i < apps.size(); ++i) {
			if (apps.get(i).getPosition() != i) {
				apps.get(i).setPosition(i, sm);
			}
		}
		db.commitTransaction(transaction);
	}
}
