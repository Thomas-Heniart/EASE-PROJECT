package com.Ease.session.update;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.session.User;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Update {
	public enum UpdateData {
		NOTHING,
		ID,
		USER_ID,
		WEBSITE_ID,
		LOGIN,
		TYPE
	}
	protected	String	DBid;
	protected	String	knowId;
	protected	String	websiteId;
	protected	String	login;
	protected	String	type;
	
	public String getDBid() {
		return DBid;
	}
	public String getKnowId() {
		return knowId;
	}
	public String getWebsiteId() {
		return websiteId;
	}
	public String getLogin() {
		return login;
	}
	public String getType() {
		return type;
	}
	
	public static void loadUpdates(User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs;
			ResultSet rs2;
			rs = db.get("SELECT * FROM updates where user_id=" + user.getId() + ";");
			while (rs.next()) {
				String update_id = rs.getString(Update.UpdateData.ID.ordinal());
				switch (rs.getString(Update.UpdateData.TYPE.ordinal())) {
					case "classic":
						rs2 = db.get("SELECT * FROM classicUpdates where update_id=" + update_id + ";");
						user.getUpdates().add(new ClassicUpdate(update_id, 
																rs.getString(Update.UpdateData.WEBSITE_ID.ordinal()), 
																rs.getString(Update.UpdateData.LOGIN.ordinal()), 
																rs2.getString(ClassicUpdate.ClassicUpdateData.PASSWORD.ordinal()),
																user.getNextKnowId()));
						rs2.close();
						break;
					case "logwith":
						rs2 = db.get("SELECT * FROM logwithUpdates where update_id=" + update_id + ";");
						user.getUpdates().add(new LogwithUpdate(update_id, 
																rs.getString(Update.UpdateData.WEBSITE_ID.ordinal()), 
																rs.getString(Update.UpdateData.LOGIN.ordinal()), 
																user.getApp(rs2.getInt(LogwithUpdate.LogwithUpdateData.LOGWITH.ordinal())),
																user.getNextKnowId()));
						rs2.close();
						break;
					default:
						throw new GeneralException(ServletManager.Code.InternError, "Unknown update type");
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
