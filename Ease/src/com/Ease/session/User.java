package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.context.SiteManager;
import com.Ease.data.AES;
import com.Ease.data.Hashing;


public class User {
	public enum UserData {
		NOTHING,
		ID,
		FIRSTNAME,
		LASTNAME,
		EMAIL,
		TEL,
		PASSWORD,
		SALTEASE,
		SALTPERSO,
		KEYUSER,
		TUTO,
		BACKGROUND
	}
	String			id;
	String			firstName;
	String			lastName;
	String			email;
	String			tel;
	String			password;
	String			saltEase;
	String			saltPerso;
	String			keyUser;
	String			tuto;
	String 			background;
	List<Profile>	profiles;
	List<String>	group_ids;
	
	int				maxProfileId;
	int				maxAppId;
	List<App>		apps;
	
	//Use this to create a new user and set it in database
	public User(String fName, String lName, String email, String tel, String pass, ServletContext context) throws SessionException {
		
		DataBase db = (DataBase)context.getAttribute("DataBase");
		String hashedPassword;
		String keyCrypted;
		if ((saltEase = Hashing.generateSalt()) == null){
			throw new SessionException("Can't create salt.");
		} else if ((saltPerso = AES.generateSalt()) == null){
			throw new SessionException("Can't create salt.");
		} else if ((keyUser = AES.keyGenerator()) == null){
			throw new SessionException("Can't create key.");
		} else if ((keyCrypted = AES.encryptUserKey(keyUser, pass, saltPerso)) == null) {
			throw new SessionException("Can't encrypt key.");
		} else if ((hashedPassword = Hashing.SHA(pass, saltEase)) == null) {
			throw new SessionException("Can't hash password.");
		} else if (db.set("INSERT INTO users VALUES (NULL, '" + fName + "', '" + lName + "', '" + email + "', '" + tel + "', '" + hashedPassword + "', '" + saltEase + "', '" + saltPerso + "', '" + keyCrypted + "', 0, 0);")
				!= 0) {
			throw new SessionException("Impossible to insert new user in data base.");
		} else {
			this.firstName = fName;
			this.lastName = lName;
			this.email = email;
			this.tel = tel;
			this.password = pass;
			this.tuto = "0";
			this.background = "logo";
			profiles = new LinkedList<Profile>();
			maxProfileId = 0;
			maxAppId = 0;
			apps = new LinkedList<App>();
			group_ids = new LinkedList<String>();
			ResultSet rs = db.get("SELECT MAX(user_id) FROM users;");
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
	
	//Use this to load user with a ResultSet from database
	public User(ResultSet rs, String pass, ServletContext context) throws SessionException {
		try {
			id = rs.getString(UserData.ID.ordinal());
			firstName = rs.getString(UserData.FIRSTNAME.ordinal());
			lastName = rs.getString(UserData.LASTNAME.ordinal());
			email = rs.getString(UserData.EMAIL.ordinal());
			tel = rs.getString(UserData.TEL.ordinal());
			password = rs.getString(UserData.PASSWORD.ordinal());
			saltEase = rs.getString(UserData.SALTEASE.ordinal());
			saltPerso = rs.getString(UserData.SALTPERSO.ordinal());
			tuto = rs.getString(UserData.TUTO.ordinal());
			background = (rs.getString(UserData.BACKGROUND.ordinal()).equals("1")) ? "picture" : "logo";
			if ((keyUser = AES.decryptUserKey(rs.getString(UserData.KEYUSER.ordinal()), pass, saltPerso)) == null)
				throw new SessionException("Can't decrypt key.");
			profiles = new LinkedList<Profile>();
			maxProfileId = 0;
			maxAppId = 0;
			apps = new LinkedList<App>();
			group_ids = new LinkedList<String>();
			loadProfiles(context);
			checkForGroup(context);
		} catch (SQLException e) {
			throw new SessionException("Impossible to get all User info.");
		}
	}

	// GETTER
	
	public String getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getTel() {
		return tel;
	}
	public String getPassword() {
		return password;
	}
	public List<Profile> getProfiles(){
		return profiles;
	}
	public String getSaltEase(){
		return saltEase;
	}
	public String getSaltPerso(){
		return saltPerso;
	}
	public String getUserKey(){
		return keyUser;
	}
	public String getTuto() {
		return tuto;
	}
	public String getBackground(){
		return background;
	}
	public int getNextProfileId(){
		maxProfileId++;
		return maxProfileId - 1;
	}
	public int getNextAppId() {
		maxAppId++;
		return maxAppId;
	}
	public List<App> getApps(){
		return apps;
	}
	public App getApp(int id){
		int i = 0;
		while (i < apps.size()){
			if (id == apps.get(i).getAppId())
				return apps.get(i);
			i++;
		}
		return null;
	}
	public Profile getProfile(int id){
		int i = 0;
		while (i < profiles.size()){
			if (id == profiles.get(i).getProfileId())
				return profiles.get(i);
			i++;
		}
		return null;
	}
	
	public List<String> getGroupIds() {
		return group_ids;
	}
	
	// SETTER
	
	public void setFirstName(String fName) {
		firstName = fName;
	}
	public void setLastName(String lName) {
		lastName = lName;
	}
	public void setEmail(String mail) {
		email = mail;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setPassword(String pass) {
		password = pass;
	}
	public void tutoComplete() {
		this.tuto = "1";
	}
	public void changeBackground(){
		if( this.background == "picture") {
			this.background = "logo";
		} else {
			this.background = "picture";
		}
	}

	// UTILS
	
	public void loadProfiles(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT * FROM profiles WHERE user_id='" + id + "';");
		try {
			while (rs.next()) {
				Profile profile = new Profile(rs, this, context);
				profiles.add(profile);
			}
			profiles.sort(new Comparator<Profile>() {
				@Override
				public int compare(Profile a, Profile b){
					return a.getIndex() - b.getIndex();
				}
			});
			updateIndex(context);
		} catch (SQLException e) {
			throw new SessionException("Impossible to load all profiles.");
		}
	}
	
	public void addProfile(Profile profile){
		profiles.add(profile);
	}
	
	public void updateInDB(ServletContext context, String pass) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		String cryptedKeyUser = AES.encryptUserKey(keyUser, pass, saltPerso);
		if (db.set("UPDATE users SET firstName='" + firstName + "', `lastName`='"+ lastName + "', email='" + email + "', `tel`='"+ tel + "', `password`='"+ password + "', `keyUser`='"+ cryptedKeyUser +"' WHERE `user_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update user in data base.");
	}
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("UPDATE users SET firstName='" + firstName + "', `lastName`='"+ lastName + "', email='" + email + "', `tel`='"+ tel + "', `tuto`='"+ tuto + "', bckgrndPic="+ ((background == "picture") ? "1" : "0") +" WHERE `user_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update user in data base.");
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		for (int i = 0; i < profiles.size(); ++i){
			profiles.get(i).deleteFromDB(context);
		}
		if (db.set("DELETE FROM users WHERE user_id='" + id + "';")
				!= 0)
			throw new SessionException("Impossible to delete user in data base.");
	}
	
	public void updateIndex(ServletContext context) throws SessionException{
		for (int i = 0; i < profiles.size(); ++i) {
			if (profiles.get(i).getIndex() != i){
				profiles.get(i).setIndex(i);
				profiles.get(i).updateInDB(context);
			}
		}
	}

	public void moveProfileAt(ServletContext context, int profileIndex, int index) throws SessionException {
		
		Profile profile = profiles.get(profileIndex);
		profiles.remove(profileIndex);
		profiles.add(index, profile);
		updateIndex(context);
	}
	
	public Boolean isAdmin(ServletContext context){
		DataBase db = (DataBase)context.getAttribute("DataBase");

		try {
			if (db.connect() != 0){
				return false;
			} else {		
				ResultSet rs;
				if ((rs = db.get("select * from admins where email = '" + email + "';")) == null || !rs.next()) {
					return false;
				} else {
					return true;
				}
			}

		} catch (SQLException e) {
			return false;
		}
	}

	public void checkForGroup(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");

		try {
			if (db.connect() != 0){
				return ;
			} else {		
				ResultSet rs;
				if ((rs = db.get("select group_id from GroupAndUserMap where user_id=" + id + ";")) == null) {					
					throw new SessionException("Can't get groups. 1");
				}
				while (rs.next()){
					String group_id = rs.getString(1);
					loadGroup(context, group_id);
					group_ids.add(group_id);
				}
			}

		} catch (SQLException e) {
			throw new SessionException("Can't get groups. 0");
		}
	}
	
	public Profile haveThisCustomProfile(String cust){
		for (int i = 0; i < profiles.size(); ++i){
			if (profiles.get(i).isCustom(cust) == true)
				return profiles.get(i);
		}
		return null;
	}
	public App haveThisCustomApp(String cust){
		for (int i = 0; i < apps.size(); ++i){
			if (apps.get(i).isCustom(cust) == true)
				return apps.get(i);
		}
		return null;
	}
	
	public Profile loadGroup(ServletContext context, String group_id) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
	
		try{
			ResultSet rs;
			if ((rs = db.get("select * from groups where id=" + group_id + ";")) == null) {					
				throw new SessionException("Can't get groups. 1");
			}
			rs.next();
			String parent = rs.getString(3);
			Profile profile = null;
			if (parent != null && !parent.equals("null")){
				profile = loadGroup(context, parent);
			}
			if ((rs = db.get("select * from customProfiles where group_id=" + group_id + ";")) == null) {					
				throw new SessionException("Can't get groups. 2");
			}
			if (rs.next()){
				String customProfileId = rs.getString(1);
				if ((profile = haveThisCustomProfile(customProfileId)) == null){
					profile = new Profile(rs.getString(2), rs.getString(3), "", this, customProfileId, context);
					profiles.add(profile);
				}
			}
			if ((rs = db.get("select * from AppAndGroupMap where group_id=" + group_id + ";")) == null) {					
				throw new SessionException("Can't get groups. 3");
			}
			while (rs.next()){
				String customAppId = rs.getString(2);
				App app = null;
				if ((app = haveThisCustomApp(customAppId)) == null){
					ResultSet rs2 = db.get("select * from customApps where id=" + customAppId +";");
					rs2.next();
					app = new App(rs2.getString(4), ((SiteManager)context.getAttribute("siteManager")).get(rs2.getString(2)), profile, customAppId, this, context);
					apps.add(app);
					profile.addApp(app);
				}
			}
			return profile;
		} catch (SQLException e) {
			throw new SessionException("Can't get groups. 3");
		}
	}
}