package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Dashboard.User.UserEmail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.RSA;

public class UpdateManager {
	
	protected List<Update> updates;
	protected Map<String, Update> updatesDBMap;
	protected Map<Integer, Update> updatesIDMap;
	protected User user;

	public UpdateManager(ServletManager sm, User user) throws GeneralException {
		updates = Update.loadUpdates(user, sm);
		updatesDBMap = new HashMap<String, Update>();
		updatesIDMap = new HashMap<Integer, Update>();
		this.user = user;
		for (Update update : updates) {
			this.addUpdateInMaps(update);
		}
		this.user = user;
	}

	private void addUpdateInMaps(Update newUpdate) {
		this.updatesDBMap.put(newUpdate.getDbId(), newUpdate);
		this.updatesIDMap.put(newUpdate.getSingledId(), newUpdate);
	}

	public Update getUpdateWithDbId(String db_id) throws GeneralException {
		Update update = this.updatesDBMap.get(db_id);
		if (update == null)
			throw new GeneralException(ServletManager.Code.InternError, "This update does not exist");
		return update;
	}

	public Update getUpdateWithSingleId(Integer single_id) throws GeneralException {
		Update update = this.updatesIDMap.get(single_id);
		if (update == null)
			throw new GeneralException(ServletManager.Code.InternError, "This update does not exist");
		return update;
	}

	public List<Update> getUpdates() {
		return this.updates;
	}

	public boolean addUpdateFromJsonConnected(String jsonString, ServletManager sm) throws GeneralException {
		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		String type = (String) json.get("type");
		String url = (String) json.get("website");
		if (type.equals("classic")) {
			String login = (String) json.get("username");
			UserEmail userEmail = user.getUserEmails().get(login);
			Website website = this.findWebsiteInCatalogWithLoginUrl(url, sm);
			ClassicApp existingApp = this.findClassicAppWithLoginAndWebsite(login, website);
			String password = (String) json.get("password");
			String keyDate = (String) json.get("keyDate");
			password = RSA.Decrypt(password, Integer.parseInt(keyDate));
			password = this.user.encrypt(password);
			if (existingApp != null) {
				if (this.checkRemovedUpdates(existingApp, password, sm))
					return true;
				this.addUpdate(UpdateNewPassword.createUpdateNewPassword(this.user, existingApp, password, userEmail, sm));
				return true;
			} else {
				if (this.checkRemovedUpdates(website, login, sm))
					return true;
				this.addUpdate(UpdateNewClassicApp.createUpdateNewClassicApp(this.user, website, login, password, userEmail, sm));
				return true;
			}
		} else if (type.equals("logwith")) {
			
			String login = (String) json.get("username");
			String logWithAppName = (String) json.get("logwith");
			Website logwithAppWebsite = this.findWebsiteInCatalogWithName(logWithAppName, sm);
			Website website = this.findWebsiteInCatalogWithLoginUrl(url, sm);
			WebsiteApp logwithApp = (WebsiteApp) this.findClassicAppWithLoginAndWebsite(login, logwithAppWebsite);
			if (this.checkRemovedUpdates(website, logwithApp, login, sm))
				return true;
			this.addUpdate(UpdateNewLogWithApp.createUpdateNewLogWithApp(user, website, logwithApp, sm));
			return true;
			
		} else {
			throw new GeneralException(ServletManager.Code.ClientError, "Update type wtf...");
		}
	}
	
	public boolean addUpdateFromJsonDeconnected(String jsonString, ServletManager sm) throws GeneralException {
		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		String type = (String) json.get("type");
		if (type.equals("classic")) {
			String login = (String) json.get("username");
			UserEmail userEmail;
			if ((userEmail = user.getUserEmails().get(login)) != null) {
				String urlOrName = (String) json.get("website");
				Website website = this.findWebsiteInCatalogWithLoginUrl(urlOrName, sm);
				ClassicApp existingApp = this.findClassicAppWithLoginAndWebsite(login, website);
				String password = (String) json.get("password");
				String keyDate = (String) json.get("keyDate");
				password = RSA.Decrypt(password, Integer.parseInt(keyDate));
				password = this.user.encrypt(password);
				if (existingApp != null) {
					if (this.checkRemovedUpdates(existingApp, password, sm))
						return true;
					this.addUpdate(UpdateNewPassword.createUpdateNewPassword(this.user, existingApp, password, userEmail, sm));
					return true;
				} else {
					if (this.checkRemovedUpdates(website, login, sm))
						return true;
					this.addUpdate(UpdateNewClassicApp.createUpdateNewClassicApp(this.user, website, login, password, userEmail, sm));
					return true;
				}
			} else {
				return false;
			}
		} else if (type.equals("logwith")) {
			
			String login = (String) json.get("login");
			String logWithAppName = (String) json.get("logwith");
			Website logwithAppWebsite = this.findWebsiteInCatalogWithName(logWithAppName, sm);
			String urlOrName = (String) json.get("website");
			Website website = this.findWebsiteInCatalogWithName(urlOrName, sm);
			WebsiteApp logwithApp = (WebsiteApp) this.findClassicAppWithLoginAndWebsite(login, logwithAppWebsite);
			if (this.checkRemovedUpdates(website, logwithApp, login, sm))
				return true;
			this.addUpdate(UpdateNewLogWithApp.createUpdateNewLogWithApp(user, website, logwithApp, sm));
			return true;
			
		} else {
			throw new GeneralException(ServletManager.Code.ClientError, "Update type wtf...");
		}
		/*JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		String type = (String) json.get("type");
		String urlOrName = (String) json.get("website");
>>>>>>> 1e32a96e93abdd2da8150620f3493690b4c5fc42
		Website website;
		String login = (String) json.get("user");
		switch (type) {
		case "classic":
			website = this.findWebsiteInCatalogWithLoginUrl(url, sm);
			if (website == null)
				return false;
			ClassicApp existingApp = this.findClassicAppWithLoginAndWebsite(login, website);
			String password = (String) json.get("password");
			String keyDate = (String) json.get("keyDate");
			password = RSA.Decrypt(password, Integer.parseInt(keyDate));
			password = this.user.encrypt(password);
			if (existingApp == null) {
				if (this.checkRemovedUpdates(website, login, sm))
					return true;
				this.addUpdate(UpdateNewClassicApp.createUpdateNewClassicApp(this.user, website, login, password, sm));
			} else if (user.haveThisEmail((String)json.get("login"))){
				if (this.checkRemovedUpdates(existingApp, password, sm))
					return true;
				this.addUpdate(UpdateNewPassword.createUpdateNewPassword(this.user, existingApp, password, sm));
			} else
				return false;
			break;

		case "logwith":
			String logWithAppName = (String) json.get("logwith");
			Website logwithAppWebsite = this.findWebsiteInCatalogWithName(logWithAppName, sm);
			website = this.findWebsiteInCatalogWithLoginUrl(url, sm);
			if (website == null)
				return false;
			WebsiteApp logwithApp = (WebsiteApp) this.findClassicAppWithLoginAndWebsite(login, logwithAppWebsite);
			if (logwithApp == null)
				return false;
			if (this.checkRemovedUpdates(website, logwithApp, login, sm))
				return true;
			this.addUpdate(UpdateNewLogWithApp.createUpdateNewLogWithApp(user, website, logwithApp, sm));
			break;

		default:
			throw new GeneralException(ServletManager.Code.ClientError, "This update type does not exist");
		}
		return true;*/
	}

	/* Logwith check */
	private boolean checkRemovedUpdates(Website website, WebsiteApp logwithApp, String login, ServletManager sm)
			throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM updatesRemoved WHERE update_id IN (SELECT id FROM updates WHERE user_id = "
				+ this.user.getDBid() + " AND id IN (SELECT update_id FROM updateNewAccount WHERE website_id = "
				+ website.getDb_id()
				+ " AND id IN (SELECT update_new_account_id FROM updateNewLogWithApp WHERE logWith_app_id = "
				+ logwithApp.getDBid() + ")));");
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	/* New password check */
	private boolean checkRemovedUpdates(ClassicApp existingApp, String password, ServletManager sm)
			throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM updatesRemoved WHERE update_id IN (SELECT id FROM updates WHERE user_id = "
				+ this.user.getDBid() + " AND id IN (SELECT update_id FROM updateNewPassword WHERE classic_app_id = "
				+ existingApp.getDBid() + " AND new_password = '" + password + "'));");
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	/* New classic */
	private boolean checkRemovedUpdates(Website website, String login, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM updatesRemoved WHERE update_id IN (SELECT id FROM updates WHERE user_id = "
				+ this.user.getDBid() + " AND id IN (SELECT update_id FROM updateNewAccount WHERE website_id = "
				+ website.getDb_id()
				+ " AND id IN (SELECT update_new_account_id FROM updateNewClassicApp WHERE id IN (SELECT update_new_classic_app_id FROM classicUpdateInformations WHERE information_name = 'login' AND information_value = '"
				+ login + "'))));");
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	private Website findWebsiteInCatalogWithLoginUrl(String url, ServletManager sm) throws GeneralException {
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		return catalog.getWebsiteWithLoginUrl(url);
	}

	private Website findWebsiteInCatalogWithName(String websiteName, ServletManager sm) {
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		return catalog.getWebsiteNamed(websiteName);
	}

	/*
	 * Pour le moment websiteName en attendant de trouver un meilleur bail
	 * 
	 */
	private boolean checkWebsiteInCatalogWithName(String websiteName, ServletManager sm) {
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		return catalog.haveWebsiteNamed(websiteName);
	}

	private boolean checkWebsiteInCatalogWithLoginUrl(String url, ServletManager sm) {
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		return catalog.haveWebsiteWithLoginUrl(url);
	}

	private ClassicApp findClassicAppWithLoginAndWebsite(String login, Website website) {
		return this.user.getDashboardManager().findClassicAppWithLoginAndWebsite(login, website);
	}

	private void addUpdate(Update update) {
		this.updates.add(update);
		this.addUpdateInMaps(update);
	}
	
	public void rejectUpdateWithSingleId(int single_id, ServletManager sm) throws GeneralException {
		Update update = this.getUpdateWithSingleId(single_id);
		update.reject(sm);
		this.updatesDBMap.remove(update.getDbId());
		this.updatesIDMap.remove(single_id);
		this.updates.remove(update);
	}
	
	public JSONArray getUpdatesJson() throws GeneralException {
		JSONArray array = new JSONArray();
		for (Update update: updates) {
			array.add(update.getJson());
		}
		return array;
	}
	
	public String acceptUpdate(int single_id, int profileId, String password, ServletManager sm) throws GeneralException {
		String newAppSingleId;
		Update update = this.getUpdateWithSingleId(single_id);
		if (update == null) {
			throw new GeneralException(ServletManager.Code.ClientError, "This update dosoen't exist.");
		}
		Profile profile;
		if (update.getClass().getName().equals("UpdateNewClassicApp")) {
			if ((profile = user.getDashboardManager().getProfile(profileId)) == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "This profile dosoen't exist.");
			
			UpdateNewClassicApp updateClassicApp = (UpdateNewClassicApp) update;
			if (updateClassicApp.haveVerifiedEmail()) {
				if (password == null) {
					throw new GeneralException(ServletManager.Code.ClientError, "Wrong password");
				}
				password = updateClassicApp.getPassword();
				password = this.user.decrypt(password);
			}
			Map<String, String> infos = updateClassicApp.getInfos();
			App newApp = ClassicApp.createClassicApp(profile, profile.getApps().size(), updateClassicApp.getSite().getName(), updateClassicApp.getSite(), password, infos, sm, user);
			profile.addApp(newApp);
			newAppSingleId = Integer.toString(newApp.getSingleId());
			
			update.reject(sm);
			this.updatesDBMap.remove(update.getDbId());
			this.updatesIDMap.remove(single_id);
			this.updates.remove(update);
		} else if (update.getClass().getName().equals("UpdateNewLogWithApp")) {
			if ((profile = user.getDashboardManager().getProfile(profileId)) == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "This profile dosoen't exist.");
			
			UpdateNewLogWithApp updateLogWithApp = (UpdateNewLogWithApp) update;
			App newApp = LogwithApp.createLogwithApp(profile, profile.getApps().size(), updateLogWithApp.getSite().getName(), updateLogWithApp.getSite(), updateLogWithApp.getLogWithApp(), sm);
			profile.addApp(newApp);
			newAppSingleId = Integer.toString(newApp.getSingleId());
			
			update.reject(sm);
			this.updatesDBMap.remove(update.getDbId());
			this.updatesIDMap.remove(single_id);
			this.updates.remove(update);
		} else if (update.getClass().getName().equals("UpdateNewPassword")) {
			
			UpdateNewPassword updatePassword = (UpdateNewPassword) update;
			if (updatePassword.haveVerifiedEmail()) {
				if (password == null) {
					throw new GeneralException(ServletManager.Code.ClientError, "Wrong password");
				}
				password = updatePassword.getPassword();
				password = this.user.decrypt(password);
			}
			
			updatePassword.getApp().setPassword(password, sm);
			
			newAppSingleId = Integer.toString(updatePassword.getApp().getSingleId());
			
			update.reject(sm);
			this.updatesDBMap.remove(update.getDbId());
			this.updatesIDMap.remove(single_id);
			this.updates.remove(update);
		} else {
			throw new GeneralException(ServletManager.Code.InternError, "Update type wtf...");
		}
		return newAppSingleId;
	}
	
}
