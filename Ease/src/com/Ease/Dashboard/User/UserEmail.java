package com.Ease.Dashboard.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class UserEmail {
	
	public enum UserEmailData {
		NOTHING,
		ID,
		USER_ID,
		EMAIL,
		VERIFIED
	}
	
	public static List<UserEmail> loadEmails(String user_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		List<UserEmail> emails = new LinkedList<UserEmail>();
		ResultSet rs = db.get("SELECT * FROM usersEmails WHERE user_id=" + user_id + ";");
		try {
			while(rs.next()) {
				String db_id = rs.getString(UserEmailData.ID.ordinal());
				String email = rs.getString(UserEmailData.EMAIL.ordinal());
				boolean verified = rs.getBoolean(UserEmailData.VERIFIED.ordinal());
				emails.add(new UserEmail(db_id, email, verified));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return emails;
	}
	
	public static UserEmail createUserEmail(String email, User user, boolean verified, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = db.set("INSERT INTO usersEmails VALUES(NULL, " + user.getDBid() + ", '" + email + "', " + (verified ? 1 : 0) + ");").toString();
		return new UserEmail(db_id, email, verified);
	}
	
	protected String db_id;
	protected String email;
	protected boolean verified;
	
	public UserEmail(String db_id, String email, boolean verified) {
		this.db_id = db_id;
		this.email = email;
		this.verified = verified;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM usersEmailsPending WHERE userEmail_id=" + this.db_id + ";");
		db.set("DELETE FROM usersEmails where id=" + this.db_id + ";");
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return db_id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean isVerified() {
		return verified;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public boolean removeIfNotUsed(String user_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet emailRs = db.get("SELECT count(distinct usersEmails.email), usersEmails.verified FROM (((apps join profiles ON apps.profile_id = profiles.profile_id) JOIN users on profiles.user_id = users.user_id) JOIN usersEmails ON users.user_id = usersEmails.user_id ) JOIN ClassicAccountsInformations ON apps.account_id = ClassicAccountsInformations.account_id AND usersEmails.email = ClassicAccountsInformations.information_value WHERE users.user_id = " + user_id + " AND usersEmails.email = '" + email+"';");
		try {
			if (emailRs.next()) {
				int ct = emailRs.getInt(1);
				boolean verif = emailRs.getBoolean(2);
				if (ct == 0 && !verif) {
					db.set("DELETE FROM usersEmails WHERE user_id=" + user_id + " AND email='" + email + "' AND verified=0;");
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return false;
	}
	
	public void verifie(String code, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT * FROM usersEmailsPending WHERE userEmail_id=" + this.db_id + ";");
			if (rs.next()) {
				String verificationCode = rs.getString(3);
				if (verificationCode.equals(code)) {
					int transaction = db.startTransaction();
					db.set("DELETE FROM usersEmailsPending WHERE userEmail_id=" + this.db_id + ";");
					this.beVerified(sm);
					db.commitTransaction(transaction);
				}
				else {
					throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong verification code.");
				}
			} else {
				throw new GeneralException(ServletManager.Code.ClientWarning, "This email not need to be verified.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void beVerified(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE usersEmails SET verified=1 WHERE id=" + this.db_id + ";");
		this.verified = true;
	}
}
