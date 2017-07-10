package com.Ease.Update;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class UpdateNewLogWithApp extends UpdateNewAccount {

	public enum Data {
		NOTHING,
		ID,
		UPDATE_NEW_ACCOUNT_ID,
		LOGWITH_APP_ID
	}
	
	public static Update loadUpdateNewLogWithApp(String update_id, String update_new_account_id, User user, Website website, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM updateNewLogWithApp WHERE update_new_account_id = ?;");
		request.setInt(update_new_account_id);
		DatabaseResult rs = request.get();
		rs.next();
		Integer logWithApp_id = rs.getInt(Data.LOGWITH_APP_ID.ordinal());
		WebsiteApp logWithApp = (WebsiteApp) user.getDashboardManager().getAppWithId(logWithApp_id);
		return new UpdateNewLogWithApp(update_id, update_new_account_id, website, logWithApp, 0, user);
	}
	
	public static UpdateNewLogWithApp createUpdateNewLogWithApp(User user, Website website, WebsiteApp logWithApp, DataBaseConnection db) throws GeneralException {
		Map<String, Object> elevator = new HashMap<String, Object>();
		int transaction = db.startTransaction();
		String updateNewAccount_id = UpdateNewAccount.createUpdateNewAccount(user, website, "updateNewLogWithApp", elevator, db);
		DatabaseRequest request = db.prepareRequest("INSERT INTO updateNewLogWithApp values (null, ?, ?);");
		request.setInt(updateNewAccount_id);
		request.setInt(logWithApp.getDBid());
		request.set();
		db.commitTransaction(transaction);
		String update_id = (String) elevator.get("update_id");
		return new UpdateNewLogWithApp(update_id, updateNewAccount_id, website, logWithApp, 0, user);
	}
	
	protected WebsiteApp logWithApp;
	
	public UpdateNewLogWithApp(String db_id, String update_new_account_id, Website website, WebsiteApp logWithApp, int single_id, User user) {
		super(db_id, update_new_account_id, website, single_id, user);
		this.logWithApp = logWithApp;
		this.type = "UpdateNewLogWithApp";
	}
	
	public void deleteFromDb(DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		DatabaseRequest request = db.prepareRequest("DELETE FROM updateNewLogWithApp WHERE update_new_account_id = ?;");
		request.setInt(this.update_new_account_id);
		request.set();
		super.deleteFromDb(db);
		db.commitTransaction(transaction);
	}
		
	public JSONObject getJson() throws GeneralException {
		JSONObject json = new JSONObject();
		json.put("type", "newLogWithApp");
		json.put("singleId", this.single_id);
		json.put("login", ((ClassicApp)this.logWithApp).getAccount().getInformationNamed("login"));
		json.put("websiteImg", this.website.getFolder() + "logo.png");
		json.put("logWithImg", logWithApp.getSite().getFolder() + "logo.png");
		json.put("logWithName", logWithApp.getName());
		json.put("logWithId", logWithApp.getDBid());
		json.put("websiteId", this.website.getDb_id());
		json.put("websiteName", this.website.getName());
		return json;
	}
	
	public WebsiteApp getLogWithApp() {
		return logWithApp;
	}
	
	public boolean matchJson(JSONObject json, User user) {
		String login = (String) json.get("username");
		String website = (String) json.get("logwith");
		ClassicApp app = (ClassicApp)this.logWithApp;
		return this.logWithApp.getSite().getName().equals(website) && app.getAccount().getInformationNamed("login").equals(login) && super.matchJson(json, user);
	}
}
