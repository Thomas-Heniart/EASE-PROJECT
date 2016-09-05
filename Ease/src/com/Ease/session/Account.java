package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.data.AES;

public class Account extends App{
	enum AccountData {
		NOTHING,
		ID,
		PROFILE_ID,
		SITE_ID,
		LOGIN,
		PASSWORD,
		POSITION,
		NAME
	}
	String	login;
	String	password;
	
	//Use this to create a new account and set it in database
	public Account(String name, String login, String password, Site site, Profile profile, String userKey, ServletContext context) throws SessionException {
		type = "Account";
		DataBase db = (DataBase)context.getAttribute("DataBase");
		String cryptedPassword;
		if ((cryptedPassword = AES.encrypt(password, userKey)) == null){
			throw new SessionException("Can't encrypt password.");
		}
		if (db.set("INSERT INTO accounts VALUES (NULL, " + profile.getId() + ", " + site.getId() + ", '" + login + "', '" + cryptedPassword  + "', '" + profile.getApps().size() + "', '" + name + "');")
				!= 0) {
			throw new SessionException("Impossible to insert new account in data base.");
		} else {
			this.login = login;
			this.password = password;
			this.site = site;
			this.index = profile.getApps().size();
			this.name = name;
			ResultSet rs = db.get("SELECT MAX(account_id) FROM accounts;");
			if (rs == null)
				throw new SessionException("Impossible to insert new account in data base. (no rs)");
			else {
				try {
					rs.next();
					this.id = rs.getString(1);
				} catch (SQLException e) {
					throw new SessionException("Impossible to insert new account in data base. (no str1)");
				}
			}
		}
	}
	
	//Use this to load account with a ResultSet from database
	public Account(ResultSet rs, Profile profile, String keyUser, ServletContext context) throws SessionException {
		try {
			type = "Account";
			id = rs.getString(AccountData.ID.ordinal());
			login = rs.getString(AccountData.LOGIN.ordinal());
			name = rs.getString(AccountData.NAME.ordinal());
			if ((password = AES.decrypt(rs.getString(AccountData.PASSWORD.ordinal()), keyUser)) == null)
				throw new SessionException("Can't decrypt website password.");
			site = ((SiteManager)context.getAttribute("Sites")).get(rs.getString(AccountData.SITE_ID.ordinal()));
			String tmp = rs.getString(AccountData.POSITION.ordinal());
			if (tmp == null) {
				index = profile.getApps().size();
				updateInDB(context, keyUser);
			} else {
				index = Integer.parseInt(tmp);	
			}
		} catch (SQLException e) {
			throw new SessionException("Impossible to get all account info.");
		} catch (NumberFormatException e) {
			throw new SessionException("Impossible to get account index.");
		}
	}
	
	// GETTER
	
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}	
	// SETTER
	
	public void setLogin(String login) {
		this.login = login;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	// UTILS
	
	public void updateInDB(ServletContext context, String keyUser) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		String cryptedPassword = "";
		if ((cryptedPassword = AES.encrypt(password, keyUser)) == null) {
			throw new SessionException("Can't encrypt password.");
		}
		if (db.set("UPDATE accounts SET login='" + login + "', `password`='"+ cryptedPassword + "', website_id='" + site.getId() + "', position='" + index + "', name = '" + name + "' WHERE `account_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update account in data base.");
	}
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("UPDATE accounts SET login='" + login + "', website_id='" + site.getId() + "', position='" + index + "', name = '" + name + "' WHERE `account_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update account in data base.");
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("DELETE FROM accounts WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete account in data base.");
	}
}