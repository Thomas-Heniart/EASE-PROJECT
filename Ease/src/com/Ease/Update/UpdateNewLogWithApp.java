package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
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
	
	public static Update loadUpdateNewLogWithApp(String update_id, String update_new_account_id, User user, Website website, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		ResultSet rs = db.get("SELECT * FROM updateNewLogWithApp WHERE update_new_account_id = " + update_new_account_id + ";");
		try {
			rs.next();
			String logWithApp_id = rs.getString(Data.LOGWITH_APP_ID.ordinal());
			LogwithApp logWithApp = (LogwithApp) user.getAppWithDBid(logWithApp_id);
			return new UpdateNewLogWithApp(update_id, website, logWithApp, idGenerator.getNextId());
		} catch(SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
	
	protected LogwithApp logWithApp;
	
	public UpdateNewLogWithApp(String db_id, Website website, LogwithApp logWithApp, int single_id) {
		super(db_id, website, single_id);
		this.logWithApp = logWithApp;
	}

}
