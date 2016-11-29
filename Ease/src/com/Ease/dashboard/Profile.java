package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

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
	
	public static List<Profile> loadProfiles(User user, ServletManager sm) throws GeneralException {
		List<Profile> profiles = new LinkedList<Profile>();
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
				profiles.add(new Profile(db_id, user, columnIdx, posIdx, groupProfile, infos, single_id));
			}
		} catch (SQLException e){
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return profiles;
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
		String db_id = db.set("INSERT INTO profiles VALUES(NULL, " + user.db_id + ", " + columnIdx + ", " + posIdx + ", " + groupProfile.db_id + ", " + info.getDBid() + ");").toString();
		int single_id = user.getNextSingleId();
		Profile profile = new Profile(db_id, user, columnIdx, posIdx, groupProfile, info, single_id);
		db.commitTransaction(transaction);
		return profile;
	}
	
	public static Profile createPersonnalProfile(User user, int columnIdx, int posIdx, String name, String color, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ProfileInformation info = ProfileInformation.createProfileInformation(name, color, sm);
		String db_id = db.set("INSERT INTO profiles VALUES(NULL, " + user.db_id + ", " + columnIdx + ", " + posIdx + ", NULL, " + info.getDBid() + ");").toString();
		int single_id = user.getNextSingleId();
		Profile profile = new Profile(db_id, user, columnIdx, posIdx, null, info, single_id);
		db.commitTransaction(transaction);
		return profile;
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
	protected int	single_id;
	
	public Profile(String db_id, User user, int columnIdx, int posIdx, GroupProfile groupProfile, ProfileInformation infos, int single_id) {
		this.db_id = db_id;
		this.user = user;
		this.columnIdx = columnIdx;
		this.posIdx = posIdx;
		this.groupProfile = groupProfile;
		this.infos = infos;
		this.single_id = single_id;
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
	
	public int getSingle_id() {
		return single_id;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSON() {
		JSONObject res = new JSONObject();
		res.put("id", this.db_id);
		res.put("user_id", this.user.getDBid());
		res.put("column", this.columnIdx);
		res.put("position", this.positionIdx);
		res.put("group_profile_id", this.groupProfileId);
		return res;
	}
}
