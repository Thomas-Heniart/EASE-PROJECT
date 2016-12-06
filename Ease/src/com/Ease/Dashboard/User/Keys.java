package com.Ease.Dashboard.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;

public class Keys {
	enum Data {
		NOTHING,
		ID,
		PASSWORD,
		SALTEASE,
		SALTPERSO,
		KEYUSER
	}
	public static Keys loadKeys(String id, String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM userKeys WHERE id=" + id + ";");
		try {
			rs.next();
			String db_id = rs.getString(Data.ID.ordinal());
			String hashed_password = rs.getString(Data.PASSWORD.ordinal());
			String saltEase = rs.getString(Data.SALTEASE.ordinal());
			String saltPerso = rs.getString(Data.SALTPERSO.ordinal());
			String crypted_keyUser = rs.getString(Data.KEYUSER.ordinal());
			String hashedPass = Hashing.SHA(password, saltEase);
			if (hashedPass.equals(hashed_password) == false) {
				throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
			}
			String keyUser = AES.decryptUserKey(crypted_keyUser, password, saltPerso);
			return new Keys(db_id, hashed_password, saltEase, saltPerso, keyUser);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static Keys loadKeysWithoutPassword(String id, String keyUser, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM userKeys WHERE id=" + id + ";");
		try {
			rs.next();
			String db_id = rs.getString(Data.ID.ordinal());
			String hashed_password = rs.getString(Data.PASSWORD.ordinal());
			String saltEase = rs.getString(Data.SALTEASE.ordinal());
			String saltPerso = rs.getString(Data.SALTPERSO.ordinal());
			return new Keys(db_id, hashed_password, saltEase, saltPerso, keyUser);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static Keys createKeys(String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String saltEase = AES.generateSalt();
		String saltPerso = AES.generateSalt();
		String keyUser = AES.keyGenerator();
		String crypted_keyUser = AES.encryptUserKey(keyUser, password, saltPerso);
		String hashed_password = Hashing.SHA(password, saltEase);
		String db_id = db.set("INSERT INTO userKeys VALUES(NULL, '" + hashed_password + "', '" + saltEase + "', '" + saltPerso + "', '" + crypted_keyUser + "');").toString();
		return new Keys(db_id, hashed_password, saltEase, saltPerso, keyUser);
	}
	
	protected String 	db_id;
	protected String	hashed_password;
	protected String	saltEase;
	protected String	saltPerso;
	protected String	keyUser;
	
	public Keys(String db_id, String hashed_password, String saltEase, String saltPerso, String keyUser) {
		this.db_id = db_id;
		this.hashed_password = hashed_password;
		this.saltEase = saltEase;
		this.saltPerso = saltPerso;
		this.keyUser = keyUser;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM userKeys WHERE id=" + this.db_id + ";");
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return db_id;
	}
	
	public String getKeyUser() {
		return keyUser;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public boolean isGoodPassword(String password) throws GeneralException {
		String hashedPass = Hashing.SHA(password, saltEase);
		return hashedPass.equals(hashed_password);
	}
	
	public void changePassword(String new_password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String new_hashed_password = Hashing.SHA(new_password, saltEase);
		String new_crypted_keyUser = AES.encryptUserKey(keyUser, new_password, saltPerso);
		db.set("UPDATE userKeys SET password='" + new_hashed_password + "', saltEase='" + saltEase + "', saltPerso='" + saltPerso + "', keyUser='" + new_crypted_keyUser + "' WHERE id=" + this.db_id + ");");
		this.hashed_password = new_hashed_password;
	}
	
	public String encrypt(String data) throws GeneralException {
		return AES.encrypt(data, this.keyUser);
	}
	public String decrypt(String data) throws GeneralException {
		return AES.decrypt(data, this.keyUser);
	}
}
