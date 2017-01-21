package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Dashboard.User.UserEmail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class UpdateNewPassword extends Update {

	public enum Data {
		NOTHING, 
		ID, 
		CLASSIC_APP_ID, 
		NEW_PASSWORD
	}

	public static Update loadUpdateNewPassword(String update_id, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		ResultSet rs = db.get("SELECT * FROM updateNewPassword WHERE update_id = " + update_id + ";");
		try {
			rs.next();
			ClassicApp classicApp = (ClassicApp) user.getDashboardManager().getAppWithDBid(rs.getString(Data.CLASSIC_APP_ID.ordinal()));
			String newPassword = rs.getString(Data.NEW_PASSWORD.ordinal());
			UserEmail email = user.getEmails().get(classicApp.getAccount().getInformationNamed("login"));
			return new UpdateNewPassword(update_id, classicApp, newPassword, idGenerator.getNextId(), email);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static UpdateNewPassword createUpdateNewPassword(User user, ClassicApp classicApp, String newPassword, UserEmail email, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		int transaction = db.startTransaction();
		String update_id = Update.createUpdate(user, "updateNewPassword", db);
		db.set("INSERT INTO updateNewPassword values (null, " + update_id + ", " + classicApp.getDBid() + ", '" + newPassword + "');");
		db.commitTransaction(transaction);
		return new UpdateNewPassword(update_id, classicApp, newPassword, idGenerator.getNextId(), email);
	}

	protected String newPassword;
	protected ClassicApp classicApp;
	protected UserEmail email;

	public UpdateNewPassword(String db_id, ClassicApp classicApp, String newPassword, int single_id, UserEmail email) {
		super(db_id, single_id);
		this.classicApp = classicApp;
		this.newPassword = newPassword;
		this.email = email;
	}
	
	public void deleteFromDb(DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		db.set("DELETE FROM updateNewPassword WHERE account_id = " + this.db_id + ";");
		super.deleteFromDb(db);
		db.commitTransaction(transaction);
	}
	
	public JSONObject getJson() throws GeneralException {
		JSONObject json = new JSONObject();
		json.put("type", "newPassword");
		json.put("email", ((email != null) ? ((email.isVerified()) ? "verified" : "unverified") : "no"));
		json.put("appId", classicApp.getSingleId());
		json.put("singleId", this.single_id);
		json.put("login", classicApp.getAccount().getInformationNamed("login"));
		json.put("passwordLength", newPassword.length());
		json.put("websiteImg", classicApp.getSite().getFolder() + "logo.png");
		json.put("websiteId", classicApp.getSite().getSingleId());	
		json.put("websiteName", classicApp.getSite().getName());
		return json;
	}
	
	public String getPassword() {
		return newPassword;
	}
	
	public boolean haveVerifiedEmail() {
		if (email != null) {
			if (email.isVerified())
				return true;
		}
		return false;
	}
	
	public ClassicApp getApp() {
		return classicApp;
	}

}
