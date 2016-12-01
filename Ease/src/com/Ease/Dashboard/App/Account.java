package com.Ease.Dashboard.App;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Account {
	public enum Data {
		NOTHING,
		ID,
		PASSWORD,
		SHARED
	}
	
	public static Account loadAccount(String db_id, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM accounts WHERE id = " + db_id + ";");
		try {
			if (rs.next()) {
				String password = rs.getString(Data.PASSWORD.ordinal());
				boolean shared = rs.getBoolean(Data.SHARED.ordinal());
				return new Account(db_id, user.decrypt(password), shared);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}
	
	public static Account createAccount(String password, boolean shared, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO accounts values (null, '" + user.encrypt(password) + "', " + (shared ? 1 : 0) + ");");
		return new Account(String.valueOf(db_id), password, shared);
	}
	
	protected String db_id;
	protected String password;
	protected boolean shared;
	
	public Account(String db_id, String password, boolean shared) {
		this.db_id = db_id;
		this.password = password;
		this.shared = shared;
	}
	
	public String getDb_id() {
		return this.db_id;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM accounts WHERE id=" + this.db_id + ";");
	}
}
