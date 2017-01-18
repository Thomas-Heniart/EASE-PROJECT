package com.Ease.Update;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.User.User;
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
		this.user = user;
		for (Update update : updates) {
			this.addUpdateInMaps(update);
		}
	}
	
	private void addUpdateInMaps(Update newUpdate) {
		this.updatesDBMap.put(newUpdate.getDbId(), newUpdate);
		this.updatesIDMap.put(newUpdate.getSingledId(), newUpdate);
	}
	
	public Update getUpdateWithDbId(String db_id) throws GeneralException {
		Update update =  this.updatesDBMap.get(db_id);
		if (update == null)
			throw new GeneralException(ServletManager.Code.InternError, "This update does not exist");
		return update;
	}
	
	public Update getUpdateWithSingleId(Integer single_id) throws GeneralException {
		Update update =  this.updatesIDMap.get(single_id);
		if (update == null)
			throw new GeneralException(ServletManager.Code.InternError, "This update does not exist");
		return update;
	}
	
	public List<Update> getUpdates() {
		return this.updates;
	}

	public void addUpdateFromJson(JSONObject json, ServletManager sm) throws GeneralException {
		String type = (String) json.get("type");
		String urlOrName = (String) json.get("website");
		Website website;
		String login = (String) json.get("user");
		switch(type) {
		case "classic":
			website = this.findWebsiteInCatalogWithLoginUrl(urlOrName, sm);
			if (website == null)
				return;
			ClassicApp existingApp = this.findClassicAppWithLoginAndWebsite(login, website);
			String password = (String) json.get("password");
			String keyDate = (String) json.get("keyDate");
			password = RSA.Decrypt(password, Integer.parseInt(keyDate));
			password = this.user.encrypt(password);
			if (existingApp == null)
				this.addUpdate(UpdateNewClassicApp.createUpdateNewClassicApp(this.user, website, login, password, sm));
			else
				this.addUpdate(UpdateNewPassword.createUpdateNewPassword(this.user, existingApp, password, sm));
			break;
		
		case"logwith":
			String logWithAppName = (String) json.get("logwith");
			Website logwithAppWebsite = this.findWebsiteInCatalogWithName(logWithAppName, sm);
			website = this.findWebsiteInCatalogWithName(urlOrName, sm);
			if (website == null)
				return;
			WebsiteApp logwithApp = (WebsiteApp) this.findClassicAppWithLoginAndWebsite(login, logwithAppWebsite);
			this.addUpdate(UpdateNewLogWithApp.createUpdateNewLogWithApp(user, website, logwithApp, sm));
			break;
			
		default:
			throw new GeneralException(ServletManager.Code.ClientError, "This update type does not exist");
		}
	}
	
	private Website findWebsiteInCatalogWithLoginUrl(String url, ServletManager sm) {
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
}
