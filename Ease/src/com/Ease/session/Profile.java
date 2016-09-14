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
		POSITION
	}
	String			id;
	String 			name;
	String			color;
	String			description;
	int				index;
	List<App>		apps;
	
	int				profileId;
	
	//Use this to create a new profile and set it in database
	public Profile(String name, String color, String desc, User user, ServletContext context) throws SessionException{
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("INSERT INTO profiles VALUES (NULL, '" + user.getId() + "', '" + name + "', '" + color + "', '" + desc + "', " + user.getProfiles().size() + ");")
			!= 0) {
			throw new SessionException("Impossible to insert new profile in data base.");
		} else {
			this.name = name;
			this.color = color;
			this.description = desc;
			this.index = user.getProfiles().size();
			apps = new LinkedList<App>();
			profileId = user.getNextProfileId();
			ResultSet rs = db.get("SELECT MAX(profile_id) FROM profiles;");
			if (rs == null)
				throw new SessionException("Impossible to insert new user in data base.");
			else {
				try {
					rs.next();
					this.id = rs.getString(1);
				} catch (SQLException e) {
					throw new SessionException("Impossible to insert new user in data base.");
				}
			}
		}
	}

	//Use this to load profile with a ResultSet from database
	public Profile(ResultSet rs, User user, ServletContext context) throws SessionException{
		try {
			id = rs.getString(ProfileData.ID.ordinal());
			name = rs.getString(ProfileData.NAME.ordinal());
			color = rs.getString(ProfileData.COLOR.ordinal());
			description = rs.getString(ProfileData.DESCRIPTION.ordinal());
			String tmp = rs.getString(ProfileData.POSITION.ordinal());
			apps = new LinkedList<App>();
			profileId = user.getNextProfileId();
			loadApps(context, user);
			if (tmp == null){
				index = user.getProfiles().size();
				updateInDB(context);
			} else {
				index = Integer.parseInt(tmp);	
			}
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
	
	// UTILS
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("UPDATE profiles SET name='" + name + "', `color`='"+ color + "', description='" + description + "', position='" + index + "' WHERE `profile_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update profile in data base.");
	}
	
	public void loadApps(ServletContext context, User user) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT * FROM accounts WHERE profile_id='" + id + "';");
		try {
			while (rs.next()) {
				Account account = new Account(rs, this, user, context);
				apps.add(account);
				user.getApps().add(account);
				
			}
		} catch (SQLException e) {
			throw new SessionException("Impossible to load all accounts.");
		}
		rs = db.get("SELECT * FROM logWith WHERE profile_id='" + id + "';");
		try {
			while (rs.next()) {
				LogWith logWith = new LogWith(rs, this, user, context);
				apps.add(logWith);
				user.getApps().add(logWith);
			}
		} catch (SQLException e) {
			throw new SessionException("Impossible to load all logWith.");
		}
		apps.sort(new Comparator<App>() {
			@Override
			public int compare(App a, App b){
				return a.getIndex() - b.getIndex();
			}
		});
	}
	
	public void addApp(App app){
		apps.add(app);
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		for (int i = 0; i < apps.size(); ++i) {
			apps.get(i).deleteFromDB(context);
		}
		if (db.set("DELETE FROM profiles WHERE profile_id='" + id + "';") != 0)
			throw new SessionException("Impossible to delete profile in data base.");
	}
	
	public void updateIndex(ServletContext context) throws SessionException {
		for (int i = 0; i < apps.size(); ++i) {
			if (apps.get(i).getIndex() != i){
				apps.get(i).setIndex(i);
				apps.get(i).updateInDB(context);
			}
		}
	}
}