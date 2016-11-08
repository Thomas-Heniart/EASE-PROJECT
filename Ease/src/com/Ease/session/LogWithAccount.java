package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;


public class LogWithAccount extends Account {
	enum LogWithAccountData {
		NOTHING,
		APP_ID,
		ACCOUNT_ID
	}
	public String app_id;
	
	// Create logWith;
	public LogWithAccount(String app_id, ServletContext context) throws SessionException {
		
		DataBase db = (DataBase)context.getAttribute("DataBase");

		if (db.set("INSERT INTO accounts VALUES (NULL);")
				!= 0) {
			throw new SessionException("Impossible to insert new account in data base.");
		}
		if (db.set("INSERT INTO logWithAccounts VALUES ("+ app_id + ",  LAST_INSERT_ID());") !=0){
			throw new SessionException("Impossible to insert new logwith account in data base.");
		}

		ResultSet rs = db.get("SELECT LAST_INSERT_ID();");
		if (rs == null)
			throw new SessionException("Impossible to insert new logwith account in data base. (no rs)");
		else {
			try {
				rs.next();
				this.id = rs.getString(1);
				this.app_id = app_id;
				this.type = "LogWithAccount";
			} catch (SQLException e) {
				throw new SessionException("Impossible to insert new logwith account in data base. (no str1)");
			}
		}	
		
	}
	
	// Load logWith;
	public LogWithAccount(ResultSet rs, User user, ServletContext context) throws SessionException {
		try {
			type = "LogWithAccount";
			id = rs.getString(LogWithAccountData.ACCOUNT_ID.ordinal());
			app_id = rs.getString(LogWithAccountData.APP_ID.ordinal());
			
		} catch (SQLException e) {
			throw new SessionException("Impossible to get all logwith account info.");
		}
	}
	
	// Getter
	
	public String getLogWithAppId(){
		return app_id;
	}
	
	public App getLogWithApp(User user){
		for(int i=0; i<user.getApps().size(); i++){
			if(user.getApps().get(i).getId().equals(app_id))
				return user.getApps().get(i);
		}
		return null;
	}
	
	// Setter

	public void setLogWithAppId(String app_id){
		this.app_id = app_id;
	}
	
	// UTILS
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		if (db.set("UPDATE logWithAccounts SET logWithApp=" + app_id + " WHERE account_id="+ id + ";")
				!= 0)
			throw new SessionException("Impossible to update logWith in data base.");
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("DELETE FROM logWithAccounts WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete logwith account in data base.");
		if (db.set("DELETE FROM accounts WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete account in data base.");
	}

	@Override
	public Map<String, String> getVisibleInformations() {
		return new HashMap<String, String>();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
}

