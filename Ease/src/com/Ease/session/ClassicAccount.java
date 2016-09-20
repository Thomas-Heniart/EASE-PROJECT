package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.data.AES;

public class ClassicAccount extends Account{
	enum ClassicAccountData {
		NOTHING,
		LOGIN,
		PASSWORD,
		ACCOUNT_ID
	}
	private String	login;
	private	String	password;

	//Use this to create a new account and set it in database
	public ClassicAccount(String login, String password, User user, ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		String cryptedPassword;
		if ((cryptedPassword = AES.encrypt(password, user.getUserKey())) == null){
			throw new SessionException("Can't encrypt password.");
		}
		if (db.set("INSERT INTO accounts VALUES (NULL);")
				!= 0) {
			throw new SessionException("Impossible to insert new account in data base.");
		}
		if (db.set("INSERT INTO classicAccounts VALUES ('"+ login + "', '"+ cryptedPassword +"',  LAST_INSERT_ID());") !=0){
			throw new SessionException("Impossible to insert new classic account in data base.");
		}

		ResultSet rs = db.get("SELECT LAST_INSERT_ID();");
		if (rs == null)
			throw new SessionException("Impossible to insert new classic account in data base. (no rs)");
		else {
			try {
				rs.next();
				this.id = rs.getString(1);
				this.login = login;
				this.password = password;
				this.type = "ClassicAccount";
			} catch (SQLException e) {
				throw new SessionException("Impossible to insert new classic account in data base. (no str1)");
			}
		}	

	}

	//Use this to load account with a ResultSet from database
	public ClassicAccount(ResultSet rs, User user, ServletContext context) throws SessionException {
		try {
			type = "ClassicAccount";
			id = rs.getString(ClassicAccountData.ACCOUNT_ID.ordinal());
			login = rs.getString(ClassicAccountData.LOGIN.ordinal());
			if ((password = AES.decrypt(rs.getString(ClassicAccountData.PASSWORD.ordinal()), user.getUserKey())) == null)
				throw new SessionException("Can't decrypt website password.");
		} catch (SQLException e) {
			throw new SessionException("Impossible to get all classic account info.");
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
		if (db.set("UPDATE classicAccounts SET login='" + login + "', `password`='"+ cryptedPassword + "' WHERE account_id=" + id + ";")
				!= 0)
			throw new SessionException("Impossible to update classic account in data base.");
	}

	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("UPDATE classicAccounts SET login='" + login + "' WHERE account_id=" + id + ";")
				!= 0)
			throw new SessionException("Impossible to update classic account in data base.");
	}

	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("DELETE FROM classicAccounts WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete classic account in data base.");
		if (db.set("DELETE FROM accounts WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete account in data base.");
	}
}