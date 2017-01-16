package com.Ease.Update;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class UpdateManager {
	
	protected List<Update> updates;
	protected Map<String, Update> updatesDBMap;
	protected Map<Integer, Update> updatesIDMap;
	
	public UpdateManager(HttpSession session, DataBaseConnection db) throws GeneralException {
		User user = (User) session.getAttribute("user");
		ServletContext context = session.getServletContext();
		updates = Update.loadUpdates(user, db, context);
		for (Update update : updates) {
			this.addUpdateInMaps(update);
		}
	}
	
	public void addUpdateNewPassword(User user, ClassicApp classicApp, String newPassword, ServletManager sm) throws GeneralException {
		Update newUpdate = UpdateNewPassword.createUpdateNewPassword(user, classicApp, newPassword, sm);
		this.addUpdateInMaps(newUpdate);
	}
	
	public void addUpdateNewClassicApp(User user, Website website, Map<String, String> updateInformations, String password, ServletManager sm) throws GeneralException {
		Update newUpdate = UpdateNewClassicApp.createUpdateNewClassicApp(user, website, updateInformations, password, sm);
		this.addUpdateInMaps(newUpdate);
	}
	
	public void addUpdateNewLogWithApp(User user, Website website, LogwithApp logWithApp, ServletManager sm) throws GeneralException {
		Update newUpdate = UpdateNewLogWithApp.createUpdateNewLogWithApp(user, website, logWithApp, sm);
		this.addUpdateInMaps(newUpdate);
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
}
