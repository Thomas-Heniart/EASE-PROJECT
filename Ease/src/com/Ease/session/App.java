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
		NAME,
		CUSTOM
	}
	public enum AppPerm {
		RENAME,
		MODIFY,
		MOVE,
		CHANGEPROFILE,
		SHOWINFO,
		DELETE
	}
	protected String	name;
	protected String	id;
	protected Account 	account;
	protected Site		site;
	protected int		index; 
	protected int		profileIndex;
	protected String	custom;
	
	protected int appId;
	protected int profileId;
	private String dataLogin;
	
	//Use this to create a new app as classic account and set it in database
	public App(String name, String login, String password, Site site, Profile profile, User user, ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		Account account = new ClassicAccount(login, password, user, context);
		
		if (db.set("INSERT INTO apps VALUES (NULL, "+ account.getId() +", "+ site.getId() + ", " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "', NULL);")
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
			this.custom = null;
		} catch (SQLException e) {
			throw new SessionException("Impossible to insert new account in data base. (no str1)");
		}
		
	}
	
	//Use this to create a new app as logWith account and set it in database
	public App(String name, String app_id, Site site, Profile profile, User user, ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		Account account = new LogWithAccount(app_id, context);
		
		if (db.set("INSERT INTO apps VALUES (NULL, "+ account.getId() +", "+ site.getId() + ", " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "', NULL);")
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
			this.custom = null;
		} catch (SQLException e) {
			throw new SessionException("Impossible to insert new account in data base. (no str1)");
		}
		
	}
	
	//Use this to create a new app as sso account and set it in database
		public App(String name, Account account, Site site, Profile profile, User user, ServletContext context) throws SessionException {
			DataBase db = (DataBase)context.getAttribute("DataBase");
			
			if (db.set("INSERT INTO apps VALUES (NULL, "+ account.getId() +", "+ site.getId() + ", " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "', NULL);")
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
				this.custom = null;
			} catch (SQLException e) {
				throw new SessionException("Impossible to insert new account in data base. (no str1)");
			}
			
		}
	
	//Use this to create a new app without account and set it in database
		public App(String name, Site site, Profile profile, String custom, User user, ServletContext context) throws SessionException {
			DataBase db = (DataBase)context.getAttribute("DataBase");
			ResultSet rs1 = db.get("SELECT haveLoginWith FROM websites WHERE website_id = " + site.getId() + ";");
			try {
				if (rs1.next()) {
					if (!(rs1.getString(1) == null && rs1.getString(1).equals("null")))
						this.dataLogin = rs1.getString(1);
					else
						this.dataLogin = "false";
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (db.set("INSERT INTO apps VALUES (NULL, NULL, "+ site.getId() + ", " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "', " + custom + ");")
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
				this.account = null;
				this.site = site;
				this.index = profile.getApps().size();
				this.name = name;
				this.profileIndex = profile.getIndex();
				this.profileId = profile.getProfileId();
				appId = user.getNextAppId();
				this.custom = custom;
			} catch (SQLException e) {
				throw new SessionException("Impossible to insert new app in data base. (no str1)");
			}
			
		}
		
	//Use this to create a new app link and set it in database
		public App(String name, String link, Profile profile, String custom, User user, ServletContext context) throws SessionException {
			DataBase db = (DataBase)context.getAttribute("DataBase");
			
			
			if (db.set("INSERT INTO apps VALUES (NULL, NULL, NULL, " + profile.getId() + ", '" + profile.getApps().size() + "', '" + name + "', " + custom + ");")
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
				this.account = new LinkAccount(link, user, context);
				this.site = null;
				this.index = profile.getApps().size();
				this.name = name;
				this.profileIndex = profile.getIndex();
				this.profileId = profile.getProfileId();
				appId = user.getNextAppId();
				this.custom = custom;
			} catch (SQLException e) {
				throw new SessionException("Impossible to insert new app in data base. (no str1)");
			}			
		}
	
	//Use this to load app with a ResultSet from database
	public App(ResultSet rs, Profile profile, User user, ServletContext context) throws SessionException {
		try {
			id = rs.getString(AppData.ID.ordinal());
			name = rs.getString(AppData.NAME.ordinal());
			String siteId = rs.getString(AppData.WEBSITE_ID.ordinal());
			if(siteId != null) site = ((SiteManager)context.getAttribute("siteManager")).get(rs.getString(AppData.WEBSITE_ID.ordinal()));
			else site = null;
			String tmp = rs.getString(AppData.POSITION.ordinal());
			appId = user.getNextAppId();
			this.profileId = profile.getProfileId();
			this.profileIndex = profile.getIndex();
			String accountId = rs.getString(AppData.ACCOUNT_ID.ordinal());
			this.custom = rs.getString(AppData.CUSTOM.ordinal());
			account = Account.getAccount(accountId, user, context);
			DataBase db = (DataBase)context.getAttribute("DataBase");
			if(siteId != null) {
				ResultSet rs1 = db.get("SELECT haveLoginWith FROM websites where website_id = " + site.getId() + ";");
				try {
					if (rs1.next()) {
						if (!(rs1.getString(1) == null || rs1.getString(1).equals("null")))
							this.dataLogin = rs1.getString(1);
						else
							this.dataLogin = "false";
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				this.dataLogin = "false";
			}
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
	
	public String getDataLogin() {
		return this.dataLogin;
	}
	
	public int getAppId(){
		return appId;
	}
	public int getProfileId() {
		return profileId;
	}
	
	public String getType() {
		if (account == null)
			return ("NoAccount");
		return account.getType();
	}
	public boolean isEmpty() {
		if (account == null)
			return true;
		return false;
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
	
	public boolean isCustom(String cust){
		if (this.custom == null)
			return false;
		if (cust.equals(this.custom))
			return true;
		return false;
	}
	public boolean isCustom(){
		if (this.custom == null)
			return false;
		return true;
	}
	
	public void updateInDB(ServletContext context) throws SessionException {
		DataBase db = (DataBase)context.getAttribute("DataBase");
		if (db.set("UPDATE apps SET account_id=" + ((account==null) ? "null" : account.getId()) + ", website_id="+ ((site == null) ? "null" : site.getId()) +", position="+ index +", name='"+ name +"' WHERE app_id=" + id + ";") != 0)
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
		if (account != null) {
			ResultSet rs = db.get("SELECT * FROM apps where account_id="+ account.getId() +";");
			try {
				if(!rs.next()) account.deleteFromDB(context);
			} catch (SQLException e) {
				throw new SessionException("Impossible to delete app in data base.");
			}
		}
		
	}
	
	public boolean havePerm(AppPerm perm, ServletContext context) throws SessionException {
		if (custom == null || custom.equals("null"))
			return true;
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		try{
			ResultSet rs;
			if ((rs = db.get("select perm from customApps where id=" + custom + ";")) == null) {					
				throw new SessionException("Can't get perm. 1");
			}
			rs.next();
			int champ = Integer.parseInt(rs.getString(1));
			if ((champ >> perm.ordinal()) % 2 == 1){
				return true;
			}
		} catch (SQLException e) {
			throw new SessionException("Can't get perm. 2");
		} catch (NumberFormatException e) {
			throw new SessionException("Can't get perm. 3");
		}
		
		return false;
	}
}