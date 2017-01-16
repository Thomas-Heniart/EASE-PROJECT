package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
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
	
	public static Update loadUpdateNewAccount(String update_id, User user, DataBaseConnection db, ServletContext context) throws GeneralException {
		ResultSet rs = db.get("SELECT * FROM updateNewAccount WHERE update_id = " + update_id + ";");
		Catalog catalog = (Catalog) context.getAttribute("catalog");
		try {
			rs.next();
			String db_id = rs.getString(Data.ID.ordinal());
			Website website = catalog.getWebsiteWithDBid(rs.getString(Data.WEBSITE_ID.ordinal()));
			String type = rs.getString(Data.TYPE.ordinal());
			switch(type) {
			case "updateNewLogWithApp":
				return UpdateNewLogWithApp.loadUpdateNewLogWithApp(update_id, db_id, user, website, db, context);
				
			case "updateNewClassicApp":
				return UpdateNewClassicApp.loadUpdateNewClassicApp(update_id, db_id, user, website, db, context);
				
			default:
				throw new GeneralException(ServletManager.Code.InternError, "No such type possible");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static String createUpdateNewAccount(User user, Website website, String type, Map<String, Object> elevator, DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		String update_id = Update.createUpdate(user, "updateNewAccount", db);
		elevator.put("update_id", update_id);
		String updateNewAccount_id = db.set("INSERT INTO updateNewAccount values (null, " + update_id + ", " + website.getDb_id() + ", '" + type + "'").toString();
		db.commitTransaction(transaction);
		return updateNewAccount_id;
	}
	
	protected Website website;
	
	public UpdateNewAccount(String db_id, Website website, int single_id) {
		super(db_id, single_id);
		this.website = website;
	}

}
