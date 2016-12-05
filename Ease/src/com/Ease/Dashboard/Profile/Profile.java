package com.Ease.Dashboard.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import com.Ease.Context.Group.GroupProfile;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
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
			while (rs.next()) {
				db_id = rs.getString(Data.ID.ordinal());
				columnIdx = rs.getInt(Data.COLUMN_IDX.ordinal());
				posIdx = rs.getInt(Data.POSITION_IDX.ordinal());
				String groupProfileId = rs.getString(Data.GROUP_PROFILE_ID.ordinal());
				groupProfile = (groupProfileId == null) ? null : GroupProfile.getGroupProfile(groupProfileId, sm);
				infos = ProfileInformation.loadProfileInformation(rs.getString(Data.PROFILE_INFO_ID.ordinal()), db);
				single_id = user.getNextSingleId();
				profilesColumn.get(columnIdx).add(new Profile(db_id, user, columnIdx, posIdx, groupProfile, infos, single_id));
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
		int single_id = user.getNextSingleId();
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
		int single_id = user.getNextSingleId();
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
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		if (this.groupProfile == null || this.groupProfile.isCommon() == false) {
			this.infos.removeFromDB(sm);
		}
		db.set("DELETE FROM profiles WHERE id=" + this.db_id + ";");
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
	}
	
	public int getPositionIdx() {
		return this.posIdx;
	}
	public void setPositionIdx(int idx, ServletManager sm) throws GeneralException{
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE profiles SET position_idx=" + idx + " WHERE id=" + this.db_id + ";");
	}
	
	public GroupProfile getGroupProfile() {
		return this.groupProfile;
	}
	public String getName() {
		return this.infos.getName();
	}
	public void setName(String name, ServletManager sm) throws GeneralException {
		this.infos.setName(name, sm);
	}
	public String getColor() {
		return this.infos.color;
	}
	public void setColor(String color, ServletManager sm) throws GeneralException {
		this.infos.setColor(color, sm);
	}
	
	public int getSingleId() {
		return single_id;
	}
	public List<App> getApps() {
		return apps;
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
		res.put("id", this.db_id);
		res.put("user_id", this.user.getDBid());
		res.put("column", this.columnIdx);
		res.put("position", this.posIdx);
		res.put("group_profile_id", (this.groupProfile == null) ? "null" : this.groupProfile.getDBid());
		return res;
	}
	
	public String getJSONString() {
		return this.getJSON().toString();
	}
	
	public static int getSizeForUnconnected(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT COUNT(*) apps WHERE profile_id=" + db_id + ";");
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
}
