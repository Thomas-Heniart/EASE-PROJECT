package com.Ease.Dashboard.App;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class App {
	enum Data {
		NOTHING,
		ID,
		PROFILE_ID,
		POSITION,
		INSERT_DATE,
		TYPE,
		WORK,
		APP_INFO_ID,
		GROUP_APP_ID
	}
	
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static List<App> loadApps(Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		List<App> apps = new LinkedList<App>();
		try {
			ResultSet rs = db.get("SELECT * FROM apps WHERE profile_id=" + profile.getDBid() + ";");
			String db_id;
			int position;
			String insertDate;
			AppInformation infos;
			GroupApp groupApp;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				position = rs.getInt(Data.POSITION.ordinal());
				insertDate = rs.getString(Data.INSERT_DATE.ordinal());
				infos = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), db);
				groupApp = GroupManager.getGroupManager(sm).getGroupAppFromDBid(rs.getString(Data.GROUP_APP_ID.ordinal()));
				switch (rs.getString(Data.TYPE.ordinal())) {
					case "linkApp":
						apps.add(LinkApp.loadLinkApp(db_id, profile, position, insertDate, infos, groupApp, sm));
					break;
					case "websiteApp":
						apps.add(WebsiteApp.loadWebsiteApp(db_id, profile, position, insertDate, infos, groupApp, sm));
					break;
					default:
						throw new GeneralException(ServletManager.Code.InternError, "This app type dosen't exist.");
				}
			}
			return apps;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static String createApp(Profile profile, int position, String name, String type, Map<String, Object>elevator, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		AppInformation infos = AppInformation.createAppInformation(name, sm);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String registrationDate = dateFormat.format(date);
		String appDBid = db.set("INSERT INTO apps VALUES(NULL, " + profile.getDBid() + ", " + position + ", " + registrationDate + ", " + type + ", " + infos.getDb_id() + ", NULL);").toString();
		elevator.put("appInfos", infos);
		elevator.put("insertDate", registrationDate);
		db.commitTransaction(transaction);
		return appDBid;
		
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String db_id;
	protected Profile profile;
	protected int position;
	protected AppInformation informations;
	protected GroupApp groupApp;
	protected String insertDate;
	protected int single_id;
	
	public App(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id) {
		this.db_id = db_id;
		this.profile = profile;
		this.position = position;
		this.informations = infos;
		this.groupApp = groupApp;
		this.insertDate = insertDate;
		this.single_id = single_id;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		if (this.groupApp != null && (this.groupApp.isCommon() == true || !this.groupApp.getPermissions().havePermission(AppPermissions.Perm.DELETE.ordinal())))
			throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permission to remove this app.");
		if (this.groupApp == null || this.groupApp.isCommon() == false)
			informations.removeFromDb(sm);
		db.set("DELETE FROM apps WHERE id=" + db_id + ";");
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */

	public String getDBid() {
		return db_id;
	}
	
	public int getSingle_id() {
		return single_id;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public int getPosition() {
		return position;
	}
	
	public String getType() {
		return this.getClass().getName();
	}
	
	public void setPosition(int pos, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE apps SET position=" + pos + " WHERE id=" + this.db_id + ";");
		this.position = pos;
	}
	
	public void setProfile(Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE apps SET profileId=" + profile.getDBid() + " WHERE id=" + this.db_id + ";");
		this.profile = profile;
	}
}