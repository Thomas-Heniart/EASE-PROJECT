package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Dashboard.User.UserEmail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class UpdateNewClassicApp extends UpdateNewAccount {

	public enum Data{
		NOTHING,
		ID,
		UPDATE_NEW_ACCOUNT_ID,
		PASSWORD
	}
	
	public static Update loadUpdateNewClassicApp(String update_id, String update_new_account_id, User user, Website website, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		ResultSet rs = db.get("SELECT * FROM updateNewClassicApp WHERE update_new_account_id = " + update_new_account_id + ";");
		try {
			rs.next();
			String db_id = rs.getString(Data.ID.ordinal());
			String password = rs.getString(Data.PASSWORD.ordinal());
			Map<String, String> updateInformations = ClassicUpdateInformation.loadClassicUpdateInformations(db_id, db);
			UserEmail email = user.getEmails().get(updateInformations.get("login"));
			return new UpdateNewClassicApp(update_id, update_new_account_id, website, password, updateInformations, email,idGenerator.getNextId(), user);
		} catch(SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static UpdateNewClassicApp createUpdateNewClassicApp(User user, Website website, Map<String, String> updateInformations, String password, UserEmail email, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		Map<String, Object> elevator = new HashMap<String, Object>();
		int transaction = db.startTransaction();
		String updateNewAccount_id = UpdateNewAccount.createUpdateNewAccount(user, website, "updateNewClassicApp", elevator, db);
		String updateNewClassicApp_id = db.set("INSERT INTO updateNewClassicApp values (null, " + updateNewAccount_id + ", '" + password + "');").toString();
		ClassicUpdateInformation.createInformations(updateNewClassicApp_id, updateInformations, db);
		db.commitTransaction(transaction);
		String update_id = (String) elevator.get("update_id");
		return new UpdateNewClassicApp(update_id, updateNewAccount_id, website, password, updateInformations, email, idGenerator.getNextId(), user);
	}
	
	public static Update createUpdateNewClassicApp(User user, Website website, String login, String password, UserEmail email, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		Map<String, Object> elevator = new HashMap<String, Object>();
		int transaction = db.startTransaction();
		String updateNewAccount_id = UpdateNewAccount.createUpdateNewAccount(user, website, "updateNewClassicApp", elevator, db);
		String updateNewClassicApp_id = db.set("INSERT INTO updateNewClassicApp values (null, " + updateNewAccount_id + ", '" + password + "');").toString();
		Map<String, String> updateInformations = new HashMap<String, String>();
		updateInformations.put("login", login);
		ClassicUpdateInformation.createInformations(updateNewClassicApp_id, updateInformations, db);
		db.commitTransaction(transaction);
		String update_id = (String) elevator.get("update_id");
		return new UpdateNewClassicApp(update_id, updateNewAccount_id, website, password, updateInformations, email,idGenerator.getNextId(), user);
		
	}
	
	protected Map<String, String> updateInformations;
	protected String password;
	protected UserEmail email;
	
	public UpdateNewClassicApp(String db_id, String update_new_account_id, Website website, String password, Map<String, String> updateInformations, UserEmail email, int single_id, User user) {
		super(db_id, update_new_account_id, website, single_id, user);
		this.password = password;
		this.email = email;
		this.updateInformations = updateInformations;
		this.type = "UpdateNewClassicApp";
	}
	
	public String getInformation(String information_name) {
		return this.updateInformations.get(information_name);
	}
	
	public Map<String, String> getInfos() {
		return updateInformations;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void deleteFromDb(DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		ClassicUpdateInformation.deleteFromDb(this.db_id, db);
		db.set("DELETE FROM updateNewClassicApp WHERE update_new_account_id = " + this.update_new_account_id + ";");
		super.deleteFromDb(db);
		db.commitTransaction(transaction);
	}
	
	public JSONObject getJson() throws GeneralException {
		JSONObject json = new JSONObject();
		json.put("type", "newClassicApp");
		json.put("singleId", this.single_id);
		json.put("login", this.updateInformations.get("login"));
		json.put("passwordLength", user.decrypt(password).length());
		json.put("websiteImg", this.website.getFolder() + "logo.png");
		json.put("websiteId", this.website.getSingleId());
		json.put("email", ((email != null) ? ((email.isVerified()) ? "verified" : "unverified") : "no"));
		json.put("websiteName", this.website.getName());
		
		return json;
	}
	
	public boolean haveVerifiedEmail() {
		if (email != null) {
			if (email.isVerified())
				return true;
		}
		return false;
	}
}
