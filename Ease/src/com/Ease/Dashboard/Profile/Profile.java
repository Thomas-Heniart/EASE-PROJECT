package com.Ease.Dashboard.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

public class Profile {
	public enum Data {
		NOTHING,
		ID,
		USER_ID,
		COLUMN_IDX,
		POSITION_IDX,
		GROUP_PROFILE_ID,
		PROFILE_INFO_ID,
	}
	public static int MAX_COLUMN = 5;
	/*
	 * 
	 * Loader and Creator
	 * 
	 */
	
	public static List<List<Profile>> createPersonnalProfiles(User user, ServletManager sm) throws GeneralException {
		List<List<Profile>> profilesColumn = new LinkedList<List<Profile>>();
		for (int i = 0; i < MAX_COLUMN; ++i) {
			profilesColumn.add(new LinkedList<Profile>());
		}
		profilesColumn.get(0).add(Profile.createPersonnalProfile(user, 0, 0, "Side", "#000000", sm));
		profilesColumn.get(1).add(Profile.createPersonnalProfile(user, 1, 0, "Me", "#373B60", sm));
		return profilesColumn;
	}
	
	public static List<List<Profile>> loadProfiles(User user, ServletManager sm) throws GeneralException {
		List<List<Profile>> profilesColumn = new LinkedList<List<Profile>>();
		for (int i = 0; i < MAX_COLUMN; ++i) {
			profilesColumn.add(new LinkedList<Profile>());
		}
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT * FROM profiles WHERE user_id=" + user.getDBid() + ";");
			String db_id;
			int	columnIdx;
			int	posIdx;
			GroupProfile groupProfile;
			ProfileInformation infos;
			int single_id;
			List<App> apps;
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				columnIdx = rs.getInt(Data.COLUMN_IDX.ordinal());
				posIdx = rs.getInt(Data.POSITION_IDX.ordinal());
				String groupProfileId = rs.getString(Data.GROUP_PROFILE_ID.ordinal());
				groupProfile = (groupProfileId == null) ? null : GroupManager.getGroupManager(sm).getGroupProfileFromDBid(groupProfileId);
				infos = ProfileInformation.loadProfileInformation(rs.getString(Data.PROFILE_INFO_ID.ordinal()), db);
				IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
				single_id = idGenerator.getNextId();
				Profile profile = new Profile(db_id, user, columnIdx, posIdx, groupProfile, infos, single_id);
				apps = App.loadApps(profile, sm);
				profile.setApps(apps);
				profilesColumn.get(columnIdx).add(profile);
			}
		} catch (SQLException e){
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		for (List<Profile> column : profilesColumn) {
			column.sort(new Comparator<Profile>() {
				@Override
				public int compare(Profile a, Profile b) {
					return a.getPositionIdx() - b.getPositionIdx();
				}
			});
		}
		return profilesColumn;
	}
	
	public static Profile createProfileWithGroup(User user, int columnIdx, int posIdx, GroupProfile groupProfile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ProfileInformation info;
		if (groupProfile.isCommon()) {
			info = groupProfile.getInfo();
		} else {
			info = ProfileInformation.createProfileInformation(groupProfile.getName(), groupProfile.getColor(), sm);
		}
		String db_id = db.set("INSERT INTO profiles VALUES(NULL, " + user.getDBid() + ", " + columnIdx + ", " + posIdx + ", " + groupProfile.getDBid() + ", " + info.getDBid() + ");").toString();
		IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
		int single_id = idGenerator.getNextId();
		Profile profile = new Profile(db_id, user, columnIdx, posIdx, groupProfile, info, single_id);
		db.commitTransaction(transaction);
		return profile;
	}
	
	public static String createProfileWithGroupForUnconnected(String db_id, int columnIdx, int posIdx, GroupProfile groupProfile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String info_id;
		if (groupProfile.isCommon()) {
			info_id = groupProfile.getInfo().getDBid();
		} else {
			info_id = ProfileInformation.createProfileInformationForUnconnected(groupProfile.getName(), groupProfile.getColor(), sm);
		}
		String id = db.set("INSERT INTO profiles VALUES(NULL, " + db_id + ", " + columnIdx + ", " + posIdx + ", " + groupProfile.getDBid() + ", " + info_id + ");").toString();
		db.commitTransaction(transaction);
		return id;
	}
	
	public static Profile createPersonnalProfile(User user, int columnIdx, int posIdx, String name, String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ProfileInformation info = ProfileInformation.createProfileInformation(name, color, sm);
		String db_id = db.set("INSERT INTO profiles VALUES(NULL, " + user.getDBid() + ", " + columnIdx + ", " + posIdx + ", NULL, " + info.getDBid() + ");").toString();
		IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
		int single_id = idGenerator.getNextId();
		Profile profile = new Profile(db_id, user, columnIdx, posIdx, null, info, single_id);
		db.commitTransaction(transaction);
		return profile;
	}
	
	public static String createPersonnalProfileForUnconnected(String db_id, int columnIdx, int posIdx, String name, String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String info_id = ProfileInformation.createProfileInformationForUnconnected(name, color, sm);
		String id = db.set("INSERT INTO profiles VALUES(NULL, " + db_id + ", " + columnIdx + ", " + posIdx + ", NULL, " + info_id + ");").toString();
		db.commitTransaction(transaction);
		return id;
	}
	
	public static void removeProfileForUnconnected(String db_id, String info_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM profiles WHERE id=" + db_id + "");
		if (info_id != null) {
			db.set("DELETE FROM profilesInformations WHERE id=" + info_id + ";");
		}
		db.commitTransaction(transaction);
	}
	
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String 	db_id;
	protected User		user;
	protected int		columnIdx;
	protected int		posIdx;
	protected GroupProfile groupProfile;
	protected ProfileInformation infos;
	protected int		single_id;
	protected List<App> apps;
	
	public Profile(String db_id, User user, int columnIdx, int posIdx, GroupProfile groupProfile, ProfileInformation infos, int single_id) {
		this.db_id = db_id;
		this.user = user;
		this.columnIdx = columnIdx;
		this.posIdx = posIdx;
		this.groupProfile = groupProfile;
		this.infos = infos;
		this.single_id = single_id;
		this.apps = new LinkedList<App>();
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		if (this.groupProfile != null && (this.groupProfile.isCommon() || !this.groupProfile.getPerms().havePermission(ProfilePermissions.Perm.DELETE.ordinal()))) {
			throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permission to remove this profile.");
		}
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (App app : apps) {
			app.removeFromDB(sm);
		}
		db.set("DELETE FROM profiles WHERE id=" + this.db_id + ";");
		if (this.groupProfile == null || this.groupProfile.isCommon() == false)
			this.infos.removeFromDB(sm);
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return this.db_id;
	}
	public User getUser() {
		return this.user;
	}
	public int getColumnIdx() {
		return this.columnIdx;
	}
	public void setColumnIdx(int idx, ServletManager sm) throws GeneralException{
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE profiles SET column_idx=" + idx + " WHERE id=" + this.db_id + ";");
		this.columnIdx = idx;
	}
	
	public int getPositionIdx() {
		return this.posIdx;
	}
	public void setPositionIdx(int idx, ServletManager sm) throws GeneralException{
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE profiles SET position_idx=" + idx + " WHERE id=" + this.db_id + ";");
		this.posIdx = idx;
	}
	
	public GroupProfile getGroupProfile() {
		return this.groupProfile;
	}
	public String getName() {
		return this.infos.getName();
	}
	public void setName(String name, ServletManager sm) throws GeneralException {
		if (this.groupProfile == null || (!this.groupProfile.isCommon() && this.groupProfile.getPerms().havePermission(ProfilePermissions.Perm.RENAME.ordinal())))
			this.infos.setName(name, sm);
		else
			throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permissions to change the profile's name.");
	}
	public String getColor() {
		return this.infos.color;
	}
	public void setColor(String color, ServletManager sm) throws GeneralException {
		if (this.groupProfile == null || (!this.groupProfile.isCommon() && this.groupProfile.getPerms().havePermission(ProfilePermissions.Perm.COLOR.ordinal())))
			this.infos.setColor(color, sm);
		else
			throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permissions to change the profile's color.");
	}
	
	public int getSingleId() {
		return single_id;
	}
	public List<App> getApps() {
		return apps;
	}
	
	public void setApps(List<App> apps) {
		this.apps = apps;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public int getSize() {
		int size;
		if (apps.size() < 4)
			size = 2;
		else {
			size = (apps.size() + 2) / 3;
		}
		return size;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSON() {
		JSONObject res = new JSONObject();
		res.put("singleId", this.single_id);
		res.put("color", this.infos.getColor());
		res.put("name", this.infos.getName());
		res.put("column", this.columnIdx);
		res.put("index", this.posIdx);
		if (this.groupProfile != null) {
			res.put("groupProfile", this.groupProfile.getJson());
		}
		JSONArray array = new JSONArray();
		for (App app : this.apps) {
			JSONObject jsonApp = new JSONObject();
			app.fillJson(jsonApp);
			array.add(jsonApp);
		}
		res.put("apps", array);
		return res;
	}
	
	public static int getSizeForUnconnected(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT COUNT(*) FROM apps WHERE profile_id=" + db_id + ";");
			if (rs.next()) {
				int ret = rs.getInt(1);
				if (ret < 4)
					return 2;
				return (ret + 2) / 3;
			} else {
				throw new GeneralException(ServletManager.Code.InternError, "Bizare.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void updateAppsIndex(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (int i = 0; i < apps.size(); ++i) {
			if (apps.get(i).getPosition() != i) {
				apps.get(i).setPosition(i, sm);
			}
			if (apps.get(i).getProfile() != this) {
				apps.get(i).setProfile(this, sm);
			}
		}
		db.commitTransaction(transaction);
	}
	
	public boolean havePermission(ProfilePermissions.Perm perm) {
		if (this.groupProfile == null || (!this.groupProfile.isCommon() && this.groupProfile.getPerms().havePermission(perm.ordinal())))
			return true;
		return false;
	}
	
	public ClassicApp addClassicApp(String name, Website site, String password, Map<String, String> infos, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		int position = this.apps.size();
		ClassicApp app = ClassicApp.createClassicApp(this, position, name, site, password, infos, sm, user);
		this.apps.add(app);
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			if (Regex.isEmail(entry.getValue()) == true)
				this.user.addEmailIfNeeded(entry.getValue(), sm);
		}
		this.user.getDashboardManager().addApp(app);
		db.commitTransaction(transaction);
		return app;
	}
	
	public LogwithApp addLogwithApp(String name, Website site, App logwith, ServletManager sm) throws GeneralException {
		int position = this.apps.size();
		if (logwith.getType().equals("LogwithApp") || logwith.getType().equals("ClassicApp")) {
			LogwithApp app = LogwithApp.createLogwithApp(this, position, name, site, (WebsiteApp)logwith, sm);
			this.apps.add(app);
			this.user.getDashboardManager().addApp(app);
			return app;
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This app is not a Classic or Logwith app.");
	}

	public WebsiteApp addEmptyApp(String name, Website site, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		int position = this.apps.size();
		WebsiteApp app = WebsiteApp.createEmptyApp(this, position, name, site, sm); 
		this.apps.add(app);
		this.user.getDashboardManager().addApp(app);
		db.commitTransaction(transaction);
		return app;
	}

	public void removeWithPassword(String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		if (this.apps.size() > 0) {
			if (password == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "Password confirmation needed.");
			this.user.getKeys().isGoodPassword(password);
		}
		this.removeFromDB(sm);
		db.commitTransaction(transaction);
	}

	public void removeApp(App app, ServletManager sm) throws GeneralException {
		this.apps.remove(app);
		app.removeFromDB(sm);
		this.updateAppsIndex(sm);
	}

	public void addApp(App newApp) {
		this.apps.add(newApp);
		this.user.getDashboardManager().addApp(newApp);
	}
	
	public boolean isGroupProfile() {
		return this.groupProfile != null;
	}
}
