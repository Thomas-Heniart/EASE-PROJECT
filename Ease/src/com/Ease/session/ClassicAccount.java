package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.data.AES;

public class ClassicAccount extends Account{
	enum ClassicAccountData {
		NOTHING,
		ACCOUNT_ID
	}
	private Map<String, String> accountInformations;
	private String passwd;

	//Use this to create a new account and set it in database
	public ClassicAccount(Map<String, String> accountInformations, User user, ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		String cryptedPassword;
		if ((cryptedPassword = AES.encrypt(accountInformations.get("password"), user.getUserKey())) == null){
			throw new SessionException("Can't encrypt password.");
		}
		//boolean transaction = db.start();
		if (db.set("INSERT INTO accounts VALUES (NULL);")
				!= 0) {
			throw new SessionException("Impossible to insert new account in data base.");
		}
		if (db.set("INSERT INTO classicAccounts VALUES (LAST_INSERT_ID());") !=0){
			throw new SessionException("Impossible to insert new classic account in data base.");
		}
		ResultSet rs = db.get("SELECT LAST_INSERT_ID();");
		if (rs == null)
			throw new SessionException("Impossible to insert new classic account in data base. (no rs)");
		else {
			try {
				rs.next();
				this.accountInformations = accountInformations;
				this.id = rs.getString(1);
				this.type = "ClassicAccount";
				for (Map.Entry<String, String> entry : this.accountInformations.entrySet()) {
					if (entry.getKey().equals("password"))
						db.set("INSERT INTO ClassicAccountsInformations VALUES (NULL, " + this.id + ", '" + entry.getKey() + "', '" + cryptedPassword + "');");
					else
						db.set("INSERT INTO ClassicAccountsInformations VALUES (NULL, " + this.id + ", '" + entry.getKey() + "', '" + entry.getValue() + "');");
				}
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
			accountInformations = new HashMap<String, String>();
			DataBase db = (DataBase)context.getAttribute("DataBase");
			ResultSet informationsRs = db.get("SELECT information_name, information_value FROM ClassicAccountsInformations WHERE account_id = " + this.id +";");
			while(informationsRs.next()) {
				String info_name = informationsRs.getString(1);
				String info_value = informationsRs.getString(2);
				if (info_name.equals("password")) {
					passwd = AES.decrypt(info_value, user.getUserKey());
					if (passwd == null)
						throw new SessionException("Can't decrypt website password. " + id);
					this.accountInformations.put(info_name, passwd);
				} else
					this.accountInformations.put(info_name, info_value);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SessionException("Impossible to get all classic account info.");
		}
	}

	// GETTER

	public String getLogin() {
		return this.accountInformations.get("login");
	}
	public String getPassword() {
		return this.passwd;
	}
	public String getInfo(String info){
		return this.accountInformations.get(info);
	}

	public void setLogin(String login) {
		this.accountInformations.put("login", login);
	}
	
	public void setPassword(String password) {
		this.accountInformations.put("password", password);
	}
	
	public Map<String, String> getVisibleInformations() {
		Map<String, String> res = new HashMap<String, String>();
		for(Map.Entry<String, String> entry : this.accountInformations.entrySet()) {
			if (!entry.getKey().equals("password"))
				res.put(entry.getKey(), entry.getValue());
		}
		return res;
	}
	
	public void updateWithInformations(Map<String, String> informations) {
		for(Map.Entry<String, String> entry : informations.entrySet()){
				this.accountInformations.put(entry.getKey(), entry.getValue());
		}
	}
	
	// UTILS

	public void updateInDB(ServletContext context, String keyUser) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		String cryptedPassword = "";
		if ((cryptedPassword = AES.encrypt(this.accountInformations.get("password"), keyUser)) == null) {
			throw new SessionException("Can't encrypt password.");
		}
		for (Map.Entry<String, String> entry : this.accountInformations.entrySet()) {
			if (!entry.getKey().equals("password")) {
				if (db.set("UPDATE ClassicAccountsInformations SET information_value = '" + entry.getValue() + "' WHERE information_name = '" + entry.getKey() + "' AND account_id =  " + this.id + ";") != 0)
					throw new SessionException("Impossible to update classic account in data base.");
			}
				
			else {
				if (db.set("UPDATE ClassicAccountsInformations SET information_value = '" + cryptedPassword + "' WHERE information_name = '" + entry.getKey() + "' AND account_id =  " + this.id + ";") != 0)
					throw new SessionException("Impossible to update classic account in data base.");
			}
				
		}
	}

	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		for (Map.Entry<String, String> entry : this.accountInformations.entrySet()) {
			if (!entry.getKey().equals("password")) {
				if (db.set("UPDATE ClassicAccountsInformations SET information_value = '" + entry.getValue() + "' WHERE information_name = '" + entry.getKey() + "' AND account_id =  " + this.id + ";") != 0)
					throw new SessionException("Impossible to update classic account in data base.");
			}
		}
	}

	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("DELETE FROM ClassicAccountsInformations WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete classic account informations in db.");
		if (db.set("DELETE FROM classicAccounts WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete classic account in data base.");
		if (db.set("DELETE FROM accounts WHERE account_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete account in data base.");
	}
}