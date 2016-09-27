package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.data.AES;

abstract class Account {
	
	protected String id;
	protected String type;
	
	// GETTER
	
	public String getType() {
		return type;
	}
	public String getId() {
		return id;
	}
	
	public static Account getAccount(String accountId, User user, ServletContext context) throws SessionException{
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		ResultSet rs = db.get("SELECT * FROM accounts WHERE account_id = "+ accountId + ";");
		try {
			if(rs.next()){
				ResultSet classicRs = db.get("SELECT * FROM classicAccounts WHERE account_id = "+ accountId + ";");
				if(classicRs.next()){
					return new ClassicAccount(classicRs, user, context);
				} else {
					ResultSet logwithRs = db.get("SELECT * FROM logWithAccounts WHERE account_id = "+ accountId + ";");
					if(logwithRs.next()){
						return new LogWithAccount(logwithRs, user, context);
					}
				}
			}
			return null;
		} catch (SQLException e) {
			throw new SessionException("Can't find account");
		}
	}
	// SETTER
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		throw new SessionException("This is an account, this is not supposed to happen!");
	}
}