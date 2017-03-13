package com.Ease.Update;

import java.util.Map;

import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class UpdateNewAccount extends Update {

	public enum Data {
		NOTHING,
		ID,
		UPDATE_ID,
		WEBSITE_ID,
		TYPE
	}
	
	public static Update loadUpdateNewAccount(String update_id, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM updateNewAccount WHERE update_id = ?;");
		request.setInt(update_id);
		DatabaseResult rs = request.get();
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		rs.next();
		String db_id = rs.getString(Data.ID.ordinal());
		Website website = catalog.getWebsiteWithDBid(rs.getString(Data.WEBSITE_ID.ordinal()));
		String type = rs.getString(Data.TYPE.ordinal());
		switch(type) {
		case "updateNewLogWithApp":
			return UpdateNewLogWithApp.loadUpdateNewLogWithApp(update_id, db_id, user, website, sm);

		case "updateNewClassicApp":
			return UpdateNewClassicApp.loadUpdateNewClassicApp(update_id, db_id, user, website, sm);

		default:
			throw new GeneralException(ServletManager.Code.InternError, "No such type possible");
		}
	}
	
	public static String createUpdateNewAccount(User user, Website website, String type, Map<String, Object> elevator, DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		String update_id = Update.createUpdate(user, "updateNewAccount", db);
		elevator.put("update_id", update_id);
		DatabaseRequest request = db.prepareRequest("INSERT INTO updateNewAccount values (null, ?, ?, ?);");
		request.setInt(update_id);
		request.setInt(website.getDb_id());
		request.setString(type);
		String updateNewAccount_id = request.set().toString();
		db.commitTransaction(transaction);
		return updateNewAccount_id;
	}
	
	protected String update_new_account_id;
	protected Website website;
	
	public UpdateNewAccount(String db_id, String update_new_account_id, Website website, int single_id, User user) {
		super(db_id, single_id, user);
		this.update_new_account_id = update_new_account_id;
		this.website = website;
		this.type = "UpdateNewAccount";
	}
	
	public void deleteFromDb(DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		DatabaseRequest request = db.prepareRequest("DELETE FROM updateNewAccount WHERE update_id = ?;");
		request.setInt(db_id);
		request.set();
		super.deleteFromDb(db);
		db.commitTransaction(transaction);
	}
	
	public JSONObject getJson() throws GeneralException {
		throw new GeneralException(ServletManager.Code.InternError, "GetJson on an updateNewAccount... dufuk?");
	}
	
	public Website getSite() {
		return website;
	}
	
	public boolean matchJson(JSONObject json, User user) {
		String websiteUrl = (String) json.get("website");
		return this.website.loginUrlMatch(websiteUrl); 
	}
}
