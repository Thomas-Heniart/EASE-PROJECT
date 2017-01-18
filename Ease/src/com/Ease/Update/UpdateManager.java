package com.Ease.Update;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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

	public void addUpdateFromJson(JSONObject json, ServletManager sm) {
		Update newUpdate = Update.createUpdateFromJSON(user, json, sm);
		this.addUpdate(newUpdate);
	}
	
	/*
	 * Pour le moment websiteName en attendant de trouver un meilleur bail
	 * 
	*/
	public boolean checkWebsiteInCatalog(String websiteName, ServletManager sm) {
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		return catalog.haveWebsiteNamed(websiteName);
	}
	
	public boolean findClassicAppWithLogin(String login) {
		this.user.get
	}
	
	private void addUpdate(Update update) {
		this.updates.add(update);
		this.addUpdateInMaps(update);
	}
}
