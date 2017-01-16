package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.User.User;
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

	public static Update loadUpdateNewPassword(String update_id, User user, DataBaseConnection db, ServletContext context) throws GeneralException {
		IdGenerator idGenerator = (IdGenerator) context.getAttribute("idGenerator");
		ResultSet rs = db.get("SELECT * FROM updateNewPassword WHERE update_id = " + update_id + ";");
		try {
			rs.next();
			ClassicApp classicApp = (ClassicApp) user.getAppWithDBid(rs.getString(Data.CLASSIC_APP_ID.ordinal()));
			String newPassword = rs.getString(Data.NEW_PASSWORD.ordinal());
			return new UpdateNewPassword(update_id, classicApp, newPassword, idGenerator.getNextId());
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static UpdateNewPassword createUpdateNewPassword(User user, ClassicApp classicApp, String newPassword, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		int transaction = db.startTransaction();
		String update_id = Update.createUpdate(user, "updateNewPassword", db);
		db.set("INSERT INTO updateNewPassword values (null, " + update_id + ", " + classicApp.getDBid() + ", '" + newPassword + "');");
		db.commitTransaction(transaction);
		return new UpdateNewPassword(update_id, classicApp, newPassword, idGenerator.getNextId());
	}

	protected String newPassword;
	protected ClassicApp classicApp;

	public UpdateNewPassword(String db_id, ClassicApp classicApp, String newPassword, int single_id) {
		super(db_id, single_id);
		this.classicApp = classicApp;
		this.newPassword = newPassword;

	}

}
