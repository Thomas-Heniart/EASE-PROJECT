package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	public static Update loadUpdateNewAccount(String update_id, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM updateNewAccount WHERE update_id = " + update_id + ";");
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		try {
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
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	protected Website website;
	
	public UpdateNewAccount(String db_id, Website website, int single_id) {
		super(db_id, single_id);
		this.website = website;
	}

}
