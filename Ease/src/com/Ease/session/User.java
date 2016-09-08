package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
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
		TUTO
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
	List<Profile>	profiles;
	
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
		} else if (db.set("INSERT INTO users VALUES (NULL, '" + fName + "', '" + lName + "', '" + email + "', '" + tel + "', '" + hashedPassword + "', '" + saltEase + "', '" + saltPerso + "', '" + keyCrypted + "', 0);")
				!= 0) {
			throw new SessionException("Impossible to insert new user in data base.");
		} else {
			this.firstName = fName;
			this.lastName = lName;
			this.email = email;
			this.tel = tel;
			this.password = pass;
			this.tuto = "0";
			profiles = new LinkedList<Profile>();
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
			if ((keyUser = AES.decryptUserKey(rs.getString(UserData.KEYUSER.ordinal()), pass, saltPerso)) == null)
				throw new SessionException("Can't decrypt key.");
			profiles = new LinkedList<Profile>();
			loadProfiles(context, keyUser);
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

	// UTILS
	
	public void loadProfiles(ServletContext context, String userKey) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT * FROM profiles WHERE user_id='" + id + "';");
		try {
			while (rs.next()) {
				Profile profile = new Profile(rs, this, userKey, context);
				profiles.add(profile);
			}
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
		if (db.set("UPDATE users SET firstName='" + firstName + "', `lastName`='"+ lastName + "', email='" + email + "', `tel`='"+ tel + "', `tuto`='"+ tuto + "' WHERE `user_id`='"+ id + "';")
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
}