package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;

public class Profile {
	enum ProfileData {
		NOTHING,
		ID,
		USER_ID,
		NAME,
		COLOR,
		DESCRIPTION,
		POSITION,
		CUSTOM,
		COLUMNIDX,
		PROFILEIDX
	}
	public enum ProfilePerm {
		RENAME,
		COLOR,
		MOVE,
		DELETE,
		ADDAPP
	}
	String			id;
	String 			name;
	String			color;
	String			description;
	int				index;
	int				columnIdx;
	int				profileIdx;
	List<App>		apps;
	String			custom;
	
	int				profileId;
	
	//Use this to create a new profile and set it in database
	public Profile(String name, String color, String desc, User user, String custom, ServletContext context, boolean side) throws SessionException{
		DataBase db = (DataBase)context.getAttribute("DataBase");
		this.index = user.getProfiles().size();
		profileId = user.getNextProfileId();
		if (side == false) {
			columnIdx = user.getMostEmptyColumn();
			profileIdx = user.getProfilesDashboard().get(columnIdx).size();
		} else {
			columnIdx = 0;
			profileIdx = 0;
		}
		try {
			db.set("INSERT INTO profiles VALUES (NULL, '" + user.getId() + "', '" + name + "', '" + color + "', '" + desc + "', " + user.getProfiles().size() + ", " + ((custom != null) ? custom : "NULL") + ", " + columnIdx + ", "+ profileIdx + ");");
			this.name = name;
			this.color = color;
			this.description = desc;
			this.custom = custom;
			apps = new LinkedList<App>();
			custom = null;
			ResultSet rs = db.get("SELECT MAX(profile_id) FROM profiles;");
				rs.next();
				this.id = rs.getString(1);
		} catch (SQLException e) {
			throw new SessionException("Impossible to insert new user in data base.");
		}
	}

	//Use this to load profile with a ResultSet from database
	public Profile(ResultSet rs, User user, ServletContext context) throws SessionException{
		try {
			boolean needUpdate = false;
			id = rs.getString(ProfileData.ID.ordinal());
			name = rs.getString(ProfileData.NAME.ordinal());
			color = rs.getString(ProfileData.COLOR.ordinal());
			description = rs.getString(ProfileData.DESCRIPTION.ordinal());
			String positionString = rs.getString(ProfileData.POSITION.ordinal());
			if (positionString == null){
				needUpdate = true;
				index = user.getProfiles().size();
			} else {
				index = Integer.parseInt(positionString);	
			}
			custom = rs.getString(ProfileData.CUSTOM.ordinal());
			String columnIdxString;
			if ((columnIdxString = rs.getString(ProfileData.COLUMNIDX.ordinal())) == null || columnIdxString.equals("null") == true) {
				columnIdx = user.getMostEmptyColumn();
				needUpdate = true;
			} else {
				columnIdx = Integer.parseInt(columnIdxString);
			}
			String profileIdxString;
			if ((profileIdxString = rs.getString(ProfileData.PROFILEIDX.ordinal())) == null || profileIdxString.equals("null") == true) {
				profileIdx = user.getProfilesDashboard().get(columnIdx).size();
				needUpdate = true;
			} else {
				profileIdx = Integer.parseInt(profileIdxString);
			}
			apps = new LinkedList<App>();
			profileId = user.getNextProfileId();
			if (needUpdate == true)
				updateInDB(context);
			loadApps(context, user);
			
		} catch (SQLException e) {
			throw new SessionException("Impossible to get all profile info.");
		} catch (NumberFormatException e) {
			throw new SessionException("Impossible to get profile index.");
		}
	}
	
	// GETTER
	
	public String getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getColor(){
		return color;
	}
	public String getDescription(){
		return description;
	}
	public List<App> getApps(){
		return apps;
	}
	public List<App> getAccounts(){
		return apps;
	}
	public int getIndex() {
		return index;
	}
	public int getProfileId(){
		return profileId;
	}
	public int getColumnIdx(){
		return columnIdx;
	}
	public int getProfileIdx(){
		return profileIdx;
	}
	
	public boolean isCustom(String cust){
		if (this.custom == null)
			return false;
		if (cust.equals(this.custom))
			return true;
		return false;
	}
	public boolean isCustom(){
		if (this.custom == null)
			return false;
		return true;
	}
	public int getYSize() {
		int size = 2;
		if (apps.size() > 6) {
			size = (apps.size() + 2) / 3;
		}
		return size;
	}
	
	
	// SETTER
	
	public void setName(String name) {
		this.name = name;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setDescription(String desc) {
		description = desc;
	}
	public void setIndex(int ind){
		index = ind;
	}
	
	public void setProfileIdx(int idx) {
		profileIdx = idx;
	}
	public void setColumnIdx(int idx) {
		columnIdx = idx;
	}
	
	// UTILS
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		try {
			db.set("UPDATE profiles SET name='" + name + "', `color`='"+ color + "', description='" + description + "', position='" + index + "', columnIdx=" + columnIdx + ", profileIdx=" + profileIdx + " WHERE `profile_id`='"+ id + "';");	
		} catch (SQLException e) {
			throw new SessionException("Impossible to update profile in data base.");
		}
	}
	
	public void loadApps(ServletContext context, User user) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		try {
			ResultSet rs = db.get("SELECT * FROM apps WHERE profile_id='" + id + "';");
			while (rs.next()) {
				App app = new App(rs, this, user, context);
				apps.add(app);
				user.getApps().add(app);
				
			}
		} catch (SQLException e) {
			throw new SessionException("Impossible to load all accounts.");
		}
		apps.sort(new Comparator<App>() {
			@Override
			public int compare(App a, App b){
				return a.getIndex() - b.getIndex();
			}
		});
		updateIndex(context);
	}
	
	public void addApp(App app){
		apps.add(app);
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		for (int i = 0; i < apps.size(); ++i) {
			apps.get(i).deleteFromDB(context);
		}
		try {
			db.set("DELETE FROM profiles WHERE profile_id='" + id + "';");
		} catch (SQLException e) {
			throw new SessionException("Impossible to delete profile in data base.");
		}
	}
	
	public void updateIndex(ServletContext context) throws SessionException {
		for (int i = 0; i < apps.size(); ++i) {
			if (apps.get(i).getIndex() != i){
				apps.get(i).setIndex(i);
				apps.get(i).updateInDB(context);
			}
		}
	}
	
	public boolean havePerm(ProfilePerm perm, ServletContext context) throws SessionException {
		if (custom == null || custom.equals("null"))
			return true;
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		try{
			ResultSet rs;
			if ((rs = db.get("select perm from customProfiles where id=" + custom + ";")) == null) {					
				throw new SessionException("Can't get perm. 1");
			}
			rs.next();
			int champ = Integer.parseInt(rs.getString(1));
			if ((champ >> perm.ordinal()) % 2 == 1){
				return true;
			}
		} catch (SQLException e) {
			throw new SessionException("Can't get perm. 2");
		} catch (NumberFormatException e) {
			throw new SessionException("Can't get perm. 3");
		}
		
		return false;
	}
}