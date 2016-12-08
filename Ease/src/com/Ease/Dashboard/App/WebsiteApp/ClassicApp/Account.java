package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	public static Account loadAccount(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM accounts WHERE id = " + db_id + ";");
		try {
			if (rs.next()) {
				String password = rs.getString(Data.PASSWORD.ordinal());
				List<AccountInformation> infos = AccountInformation.loadInformations(db_id, sm);
				boolean shared = rs.getBoolean(Data.SHARED.ordinal());
				return new Account(db_id, password, shared, infos);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		throw new GeneralException(ServletManager.Code.InternError, "This account dosen't exist.");
	}
	
	public static Account createAccount(String password, boolean shared, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO accounts values (null, '" + user.encrypt(password) + "', " + (shared ? 1 : 0) + ");");
		return new Account(String.valueOf(db_id), password, shared);
	}
	
	protected String 				db_id;
	protected String 				password;
	protected boolean 				shared;
	protected List<AccountInformation>	infos;
	
	public Account(String db_id, String password, boolean shared, List<AccountInformation> infos) {
		this.db_id = db_id;
		this.password = password;
		this.shared = shared;
		this.infos = infos;
	}
	
}
