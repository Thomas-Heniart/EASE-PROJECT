package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

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
	
	protected String db_id;
	protected String email;
	protected boolean verified;
	
	public UserEmail(String db_id, String email, Boolean verified) {
		this.db_id = db_id;
		this.email = email;
		this.verified = verified;
	}
	
	public boolean removeIfNeeded(String user_id, ServletManager sm) throws GeneralException {
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
}
