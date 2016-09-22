package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;

public class App {
	enum AppData {
		NOTHING,
		ID,
		ACCOUNT_ID,
		WEBSITE_ID,
		PROFILE_ID,
		POSITION,
		NAME
	}
	protected String	name;
	protected String	id;
	protected Account 	account;
	protected Site		site;
	protected int		index; 
	protected int		profileIndex;
	
	protected int appId;
	protected int profileId;
	
	//Use this to create a new app as classic account and set it in database
	public App(String name, String login, String password, Site site, Profile profile, User user, ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		Account account = new ClassicAccount(login, password, user, context);
		
		if (db.set("INSERT INTO apps VALUES (NULL, "+ account.getId() +", "+ site.getId() + ", " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "');")
				!= 0) {
			throw new SessionException("Impossible to insert new app in data base.");
		}
		
		ResultSet rs = db.get("SELECT LAST_INSERT_ID();");
		if (rs == null){
			throw new SessionException("Impossible to insert new app in data base. (no rs)");
		}
		try {
			rs.next();
			this.id = rs.getString(1);
			this.account = account;
			this.site = site;
			this.index = profile.getApps().size();
			this.name = name;
			this.profileIndex = profile.getIndex();
			this.profileId = profile.getProfileId();
			appId = user.getNextAppId();
		} catch (SQLException e) {
			throw new SessionException("Impossible to insert new account in data base. (no str1)");
		}
		
	}
	
	public App(String name, Account account, Site site, Profile profile, User user, ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		if (db.set("INSERT INTO apps VALUES (NULL, "+ account.getId() +", "+ site.getId() + ", " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "');")
				!= 0) {
			throw new SessionException("Impossible to insert new app in data base.");
		}
		
		ResultSet rs = db.get("SELECT LAST_INSERT_ID();");
		if (rs == null){
			throw new SessionException("Impossible to insert new app in data base. (no rs)");
		}
		try {
			rs.next();
			this.id = rs.getString(1);
			this.account = account;
			this.site = site;
			this.index = profile.getApps().size();
			this.name = name;
			this.profileIndex = profile.getIndex();
			this.profileId = profile.getProfileId();
			appId = user.getNextAppId();
		} catch (SQLException e) {
			throw new SessionException("Impossible to insert new account in data base. (no str1)");
		}
		
	}
	
	//Use this to create a new app as logWith account and set it in database
	public App(String name, String app_id, Site site, Profile profile, User user, ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		Account account = new LogWithAccount(app_id, context);
		
		if (db.set("INSERT INTO apps VALUES (NULL, "+ account.getId() +", "+ site.getId() + ", " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "');")
				!= 0) {
			throw new SessionException("Impossible to insert new app in data base.");
		}
		
		ResultSet rs = db.get("SELECT LAST_INSERT_ID();");
		if (rs == null){
			throw new SessionException("Impossible to insert new app in data base. (no rs)");
		}
		try {
			rs.next();
			this.id = rs.getString(1);
			this.account = account;
			this.site = site;
			this.index = profile.getApps().size();
			this.name = name;
			this.profileIndex = profile.getIndex();
			this.profileId = profile.getProfileId();
			appId = user.getNextAppId();
		} catch (SQLException e) {
			throw new SessionException("Impossible to insert new account in data base. (no str1)");
		}
		
	}
	
	//Use this to load app with a ResultSet from database
	public App(ResultSet rs, Profile profile, User user, ServletContext context) throws SessionException {
		try {
			id = rs.getString(AppData.ID.ordinal());
			name = rs.getString(AppData.NAME.ordinal());
			site = ((SiteManager)context.getAttribute("siteManager")).get(rs.getString(AppData.WEBSITE_ID.ordinal()));
			String tmp = rs.getString(AppData.POSITION.ordinal());
			appId = user.getNextAppId();
			this.profileId = profile.getProfileId();
			this.profileIndex = profile.getIndex();
			String accountId = rs.getString(AppData.ACCOUNT_ID.ordinal());
			account = Account.getAccount(accountId, user, context);
			if (tmp == null) {
				index = profile.getApps().size();
				updateInDB(context);
			} else {
				index = Integer.parseInt(tmp);	
			}
		} catch (SQLException e) {
			throw new SessionException("Impossible to get all account info.");
		} catch (NumberFormatException e) {
			throw new SessionException("Impossible to get account index.");
		}
	}
	
	
	//Setter
	
	public void setIndex(int ind) {
		index = ind;
	}
	public void setAppId(int id) {
		appId = id;
	}
	
	public void setAccount(Account account){
		this.account = account;
	}
	
	//Getter
	public Account getAccount(){
		return account;
	}
	
	public String getLogin(){
		if(account.getType().equals("ClassicAccount")){
			return ((ClassicAccount) account).getLogin();
		} 
		return null;
	}
	
	public int getAppId(){
		return appId;
	}
	public int getProfileId() {
		return profileId;
	}
	
	public String getType() {
		return account.getType();
	}
	public String getId() {
		return id;
	}
	public Site getSite() {
		return site;
	}
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	public int getProfileIndex() {
		return profileIndex;
	}
	
	public void setName(String str) {
		name = str;
	}
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("UPDATE apps SET account_id=" + account.getId() + ", website_id="+ site.getId() +", position="+ index +", name='"+ name +"' WHERE app_id=" + id + ";")
				!= 0)
			throw new SessionException("Impossible to update app in data base.");
	}
	
	public void updateProfileInDB(ServletContext context, String idd, int profileId) throws SessionException{
		DataBase db = (DataBase)context.getAttribute("DataBase");
		this.profileId = profileId;
		if (db.set("UPDATE apps SET profile_id='" + idd + "' WHERE `app_id`='"+ id + "';")
				!= 0)
			throw new SessionException("Impossible to update app in data base.");
	}
	
	public void deleteFromDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("DELETE FROM apps WHERE app_id=" + id + ";") != 0)
			throw new SessionException("Impossible to delete app in data base.");
		ResultSet rs = db.get("SELECT * FROM apps where account_id="+ account.getId() +";");
		try {
			if(!rs.next()) account.deleteFromDB(context);
		} catch (SQLException e) {
			throw new SessionException("Impossible to delete app in data base.");
		}
		
	}
}