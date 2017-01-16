package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Update {
	
	public enum Data {
		NOTHING,
		ID,
		USER_ID,
		TYPE
	}
	
	public static List<Update> loadUpdates(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		List<Update> updates = new LinkedList<Update>();
		try {
			ResultSet rs = db.get("SELECT * FROM updates WHERE user_id = " + user.getDBid() + ";");
			try {
				String db_id;
				String type;
				Update update;
				while (rs.next()) {
					db_id = rs.getString(Data.ID.ordinal());
					type = rs.getString(Data.TYPE.ordinal());
					switch (type) {
					case "updateNewPassword":
						update = UpdateNewPassword.loadUpdateNewPassword(db_id, user, sm);
						break;
					
					case "updateNewAccount":
						update = UpdateNewAccount.loadUpdateNewAccount(db_id, user, sm);
						break;
					
					default:
						throw new GeneralException(ServletManager.Code.InternError, "No such type");
					}
					updates.add(update);
				}
			} catch (SQLException e) {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
			return updates;
		} catch(GeneralException e) {
			throw e;
		}
		
	}
	
	public static String createUpdate(User user, String type, DataBaseConnection db) throws GeneralException {
		return db.set("INSERT INTO updates values (null, " + user.getDBid() + ", '" + type + "');").toString();
	}
	
	protected String db_id;
	protected String type;
	protected int single_id;
	protected User user;
	
	public Update(String db_id, int single_id) {
		this.db_id = db_id;
		//this.type = type;
		this.single_id = single_id;
		//this.user = user;
	}
}
