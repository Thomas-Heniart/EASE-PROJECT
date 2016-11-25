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
		NAME,
		COLOR,
		PERMS,
		COLUMN_IDX,
		POSITION_IDX
	}
	public static void loadProfiles(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM profiles where user_id=" + user.getDBid() + ";");
		String db_id;
		String name;
		String color;
		ProfilePermissions perms;
		int columnIdx;
		int positionIdx;
		int single_id;
		Profile profile;
		try {
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				name = rs.getString(Data.NAME.ordinal());
				color = rs.getString(Data.COLOR.ordinal());
				perms = ProfilePermissions.loadProfilePermissions(rs.getString(Data.PERMS.ordinal()), sm);
				columnIdx = rs.getInt(Data.COLUMN_IDX.ordinal());
				positionIdx = rs.getInt(Data.POSITION_IDX.ordinal());
				single_id = sm.getNextSingleId();
				profile = new Profile(db_id, user, name, color, perms, columnIdx, positionIdx, single_id);
				user.getProfilesColumn().get(columnIdx).add(profile);
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

	
	public static Profile createProfile(String name, String color, User user, ServletManager sm) throws GeneralException {
		int columnIdx = Profile.getMostLittleProfileColumn(user);
		int positionIdx = user.getProfilesColumn().get(columnIdx).size();
		ProfilePermissions perm = ProfilePermissions.loadDefaultProfilePermissions(sm);
		int single_id = sm.getNextSingleId();
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO profiles VALUES(NULL, " + user.getDBid() + ", '" + name + "', '" + color + "', " + perm.getDBid() + ", " + columnIdx + ", " + positionIdx + ");").toString();
		return new Profile(db_id, user, name, color, perm, columnIdx, positionIdx, single_id);
	}
	
	protected String	db_id;
	protected User		user;
	protected String	name;
	protected String	color;
	protected ProfilePermissions permissions;
	protected int		columnIdx;
	protected int		positionIdx;
	protected List<App> apps;
	protected int 		single_id;

	
	public Profile(String db_id, User user, String name, String color, ProfilePermissions perms, int columnIdx, int positionIdx, int single_id) {
		this.db_id = db_id;
		this.user = user;
		this.name = name;
		this.color = color;
		this.permissions = perms;
		this.columnIdx = columnIdx;
		this.positionIdx = positionIdx;
		this.apps = new LinkedList<App>();
		this.single_id = single_id;
	}
	
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
	
	public String getName() {
		return name;
	}
	public void setName(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (this.permissions.havePermission(ProfilePermissions.Perm.RENAME.ordinal())) {
			db.set("UPDATE FROM profiles SET name='" + name + "' WHERE id=" + this.db_id + ";");
		} else {
			throw new GeneralException(ServletManager.Code.ClientWarning, "You don't have the permission to change this profile's name.");
		}
		this.name = name;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (this.permissions.havePermission(ProfilePermissions.Perm.COLOR.ordinal())) {
			db.set("UPDATE FROM profiles SET color='" + color + "' WHERE id=" + this.db_id + ";");
		} else {
			throw new GeneralException(ServletManager.Code.ClientWarning, "You don't have the permission to change this profile's color.");
		}
		this.color = color;
	}
	
	public int getColumnIdx() {
		return columnIdx;
	}
	public void setColumnIdx(int idx, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (this.permissions.havePermission(ProfilePermissions.Perm.MOVE.ordinal())) {
			db.set("UPDATE FROM profiles SET columnIdx=" + idx + " WHERE id=" + this.db_id + ";");
		} else {
			throw new GeneralException(ServletManager.Code.ClientWarning, "You don't have the permission to move this profile.");
		}
		this.columnIdx = idx;
	}
	
	public int getPositionIdx() {
		return this.positionIdx;
	}
	public void setPositionIdx(int idx, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (this.permissions.havePermission(ProfilePermissions.Perm.MOVE.ordinal())) {
			db.set("UPDATE FROM profiles SET positionIdx=" + idx + " WHERE id=" + this.db_id + ";");
		} else {
			throw new GeneralException(ServletManager.Code.ClientWarning, "You don't have the permission to move this profile.");
		}
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
	
	public int getSize() {
		if (apps.size() <= 3) {
			return 2;
		}
		return (apps.size() + 2) / 3;
	}
	
	public int getNextPosition() {
		return apps.size();
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
