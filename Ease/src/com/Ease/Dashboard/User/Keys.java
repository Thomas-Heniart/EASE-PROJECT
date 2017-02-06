package com.Ease.Dashboard.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Context.ServerKey;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.Crypto.Hashing;

public class Keys {
	enum Data {
		NOTHING,
		ID,
		PASSWORD,
		SALTEASE,
		SALTPERSO,
		KEYUSER,
		BACKUPKEY
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
			String keyUser;
			//-- Pour mettre à jour la crypto (nouveau hashage et nouveau salage.
			if(saltEase != null){
				System.out.println("reset keys");
				String hashedPass = Hashing.SHA(password, saltEase);
				if (hashedPass.equals(hashed_password) == false) {
					throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
				}
				keyUser = AES.oldDecryptUserKey(crypted_keyUser, password, saltPerso);
				String newSalt = AES.generateSalt();
				crypted_keyUser = AES.encryptUserKey(keyUser, password, newSalt);
				hashed_password = Hashing.hash(password);
				saltEase = null;
				saltPerso = newSalt;
				ServerKey serverKey = (ServerKey) sm.getContextAttr("serverKey");
				String backUpKey = AES.encrypt(keyUser, serverKey.getKeyServer());
				db.set("UPDATE userKeys SET password='"+hashed_password+"', saltEase=null, saltPerso='"+newSalt+"', keyUser='"+crypted_keyUser+"', backUpKey='"+backUpKey+"' WHERE id="+id+";");
			} else {
			//-- Ne garder que le else quand tout le monde sera à jour
				if(!Hashing.compare(password, hashed_password)){
					throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
				}
				keyUser = AES.decryptUserKey(crypted_keyUser, password, saltPerso);
			}
			
			return new Keys(db_id, hashed_password, saltPerso, keyUser);
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
			String saltPerso = rs.getString(Data.SALTPERSO.ordinal());
			return new Keys(db_id, hashed_password, saltPerso, keyUser);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static Keys createKeys(String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String saltPerso = AES.generateSalt();
		String keyUser = AES.keyGenerator();
		String crypted_keyUser = AES.encryptUserKey(keyUser, password, saltPerso);
		String hashed_password = Hashing.hash(password);
		ServerKey serverKey = (ServerKey) sm.getContextAttr("serverKey");
		String backUpKey = AES.encrypt(keyUser, serverKey.getKeyServer());
		String db_id = db.set("INSERT INTO userKeys VALUES(NULL, '" + hashed_password + "', null, '" + saltPerso + "', '" + crypted_keyUser + "', '"+backUpKey+"');").toString();
		return new Keys(db_id, hashed_password, saltPerso, keyUser);
	}
	
	protected String 	db_id;
	protected String	hashed_password;
	protected String	saltPerso;
	protected String	keyUser;
	
	public Keys(String db_id, String hashed_password, String saltPerso, String keyUser) {
		this.db_id = db_id;
		this.hashed_password = hashed_password;
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
		String hashedPass = Hashing.hash(password);
		return hashedPass.equals(hashed_password);
	}
	
	public void changePassword(String new_password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String new_hashed_password = Hashing.hash(new_password);
		String new_crypted_keyUser = AES.encryptUserKey(keyUser, new_password, saltPerso);
		db.set("UPDATE userKeys SET password='" + new_hashed_password + "', saltEase=null, saltPerso='" + saltPerso + "', keyUser='" + new_crypted_keyUser + "' WHERE id=" + this.db_id + ";");
		this.hashed_password = new_hashed_password;
	}
	
	public String encrypt(String data) throws GeneralException {
		return AES.encrypt(data, this.keyUser);
	}
	public String decrypt(String data) throws GeneralException {
		return AES.decrypt(data, this.keyUser);
	}
	
	public static void passwordLost(String email, String userId, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		try {
			String code = CodeGenerator.generateNewCode();
			String fName = null;
			ResultSet rs = db.get("SELECT * FROM passwordLost WHERE user_id=" + userId + ";");
			if (rs.next())
				db.set("UPDATE passwordLost SET linkCode = '" + code + "', dateOfRequest = NOW() WHERE user_id=" + userId + ";");
			else
				db.set("INSERT INTO passwordLost values (" + userId + ", '" + code + "', default);");
			rs = db.get("SELECT firstName FROM users WHERE id = " + userId + ";");
			if (rs.next())
				fName = rs.getString(1);
			else
				throw new GeneralException(ServletManager.Code.ClientWarning, "This user does not exist");
			SendGridMail passwordLostEmail = new SendGridMail("Agathe @Ease", "contact@ease.space");
			passwordLostEmail.sendPasswordLostEmail(email, fName, code);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		db.commitTransaction(transaction);
	}
	
	public static void resetPassword(String userId, String newPassword, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		try {
			ResultSet rs3 = db.get("SELECT * FROM passwordLost WHERE user_id=" + userId + ";");
			if (rs3.next()) {
				ResultSet rs = db.get("SELECT * FROM profiles WHERE user_id=" + userId + ";");
				while (rs.next()) {
					ResultSet rs2 = db.get("SELECT * FROM apps WHERE profile_id=" + rs.getString(Profile.Data.ID.ordinal()) + ";");
					while (rs2.next()) {
						if (rs2.getString(App.Data.TYPE.ordinal()).equals("websiteApp")) {
							WebsiteApp.Empty(rs2.getString(App.Data.ID.ordinal()), sm);
						}
					}
				}
				rs = db.get("SELECT key_id FROM users WHERE id=" + userId + ";");
				rs.next();
				String saltPerso = AES.generateSalt();
				String keyUser = AES.keyGenerator();
				String crypted_keyUser = AES.encryptUserKey(keyUser, newPassword, saltPerso);
				String hashed_password = Hashing.hash(newPassword);
				db.set("UPDATE userKeys SET password='" + hashed_password + "', saltEase=null, saltPerso='" + saltPerso + "', keyUser='" + crypted_keyUser + "' WHERE id=" + rs.getString(1) + ";");
				db.set("DELETE FROM passwordLost WHERE user_id=" + userId + ";");
			} else {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You did not ask for password resetting.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		db.commitTransaction(transaction);
	}

	public static boolean checkCodeValidity(String userId, String code, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM passwordLost WHERE user_id = " + userId + " AND linkCode = '" + code + "' AND NOW() <= DATE_ADD(dateOfRequest, INTERVAL 2 HOUR)");
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
