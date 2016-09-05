package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;

public class LogWith extends App {
	enum LogWithData {
		NOTHING,
		ID,
		PROFILE_ID,
		SITE_ID,
		ACCOUNT_ID,
		POSITION,
		NAME
	}
	protected String account_id;
	
	// Create logWith;
	public LogWith(String name, String acc_id, Site site, Profile profile, String userKey, ServletContext context) throws SessionException {
		type = "LogWith";
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		if (db.set("INSERT INTO logWith VALUES (NULL, " + profile.getId() + ", " + site.getId() + ", '" + acc_id + "', " + profile.getApps().size() + ", '" + name + "');")
				!= 0) {
			throw new SessionException("Impossible to insert new logWith in data base.");
		} else {
			this.account_id = acc_id;
			this.site = site;
			this.index = profile.getApps().size();
			this.name = name;
			ResultSet rs = db.get("SELECT MAX(logWith_id) FROM logWith;");
			if (rs == null)
				throw new SessionException("Impossible to insert new logWith in data base. (no rs)");
			else {
				try {
					rs.next();
					this.id = rs.getString(1);
				} catch (SQLException e) {
					throw new SessionException("Impossible to insert new logWith in data base. (no str1)");
				}
			}
		}
	}
	
	// Load logWith;
	public LogWith(ResultSet rs, Profile profile, String keyUser, ServletContext context) throws SessionException {
		try {
			type = "LogWith";
			id = rs.getString(LogWithData.ID.ordinal());
			name = rs.getString(LogWithData.NAME.ordinal());
			site = ((SiteManager)context.getAttribute("Sites")).get(rs.getString(LogWithData.SITE_ID.ordinal()));
			account_id = rs.getString(LogWithData.ACCOUNT_ID.ordinal());
			String tmp = rs.getString(LogWithData.POSITION.ordinal());
			if (tmp.equals(null)) {
				index = profile.getApps().size();
				updateInDB(context, keyUser);
			} else {
				index = Integer.parseInt(tmp);
			}
		} catch (SQLException e) {
			throw new SessionException("Impossible to get all logWith info.");
		} catch (NumberFormatException e) {
			throw new SessionException("Impossible to get logWith index.");
		}
	}
	
	// Getter
	
	public Account getAccount(User user){
		for (int i = 0; i < user.getProfiles().size(); i++) {
			for (int j = 0; j < user.getProfiles().get(i).getApps().size(); ++j){
				if (user.getProfiles().get(i).getApps().get(j).getType().equals("Account")
					&& user.getProfiles().get(i).getApps().get(j).getId().equals(account_id)) {
					return ((Account)user.getProfiles().get(i).getApps().get(j));
				}
			}
		}
		return null;
	}
	
	public int getAccountProfileIndex(User user){
		for (int i = 0; i < user.getProfiles().size(); i++) {
			for (int j = 0; j < user.getProfiles().get(i).getApps().size(); ++j){
				if (user.getProfiles().get(i).getApps().get(j).getType().equals("Account")
					&& user.getProfiles().get(i).getApps().get(j).getId().equals(account_id)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int getAccountIndex(User user){
		for (int i = 0; i < user.getProfiles().size(); i++) {
			for (int j = 0; j < user.getProfiles().get(i).getApps().size(); ++j){
				if (user.getProfiles().get(i).getApps().get(j).getType().equals("Account")
					&& user.getProfiles().get(i).getApps().get(j).getId().equals(account_id)) {
					return j;
				}
			}
		}
		return -1;
	}
	
	// Setter

	public void setAccountId(String id){
		account_id = id;
	}
	
	// UTILS
	
	public void updateInDB(ServletContext context, String keyUser) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		if (db.set("UPDATE logWith SET account_id='" + account_id + "', website_id='" + site.getId() + "', position='" + index + "', name = '" + name + "' WHERE `logWith_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update logWith in data base.");
	}
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		if (db.set("UPDATE logWith SET account_id='" + account_id + "', website_id='" + site.getId() + "', position='" + index + "', name = '" + name + "' WHERE `logWith_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update logWith in data base.");
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("DELETE FROM logWith WHERE logWith_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete logWith in data base.");
	}
}