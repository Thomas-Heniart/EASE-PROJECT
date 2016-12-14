package com.Ease.Dashboard.App.WebsiteApp.LogwithApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.App.WebsiteApp.GroupWebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class LogwithApp extends WebsiteApp {
	public enum Data {
		NOTHING,
		ID,
		WEBSITE_APP_ID,
		LOGWITH_APP_ID
	}
	
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static LogwithApp loadLogwithApp(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, String websiteAppDBid, GroupWebsiteApp groupWebsiteApp, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT * from logWithApps WHERE website_app_id=" + websiteAppDBid + ";");
			if (rs.next()) {
				String logwith = rs.getString(Data.LOGWITH_APP_ID.ordinal());
				String logwithDBid = rs.getString(Data.ID.ordinal());
				IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
				return new LogwithApp(db_id, profile, position, infos, groupApp, insertDate, idGenerator.getNextId(), site, websiteAppDBid, groupWebsiteApp, logwith, logwithDBid);
			} 
			throw new GeneralException(ServletManager.Code.InternError, "Classic app not complete in db.");
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static LogwithApp createLogwithApp(Profile profile, int position, String name, Website site, WebsiteApp logwith, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Map<String, Object> elevator = new HashMap<String, Object>();
		String websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "logwithApp", site, elevator, sm);
		String logwithDBid = db.set("INSERT INTO logWithApps VALUES(NULL, " + websiteAppDBid + ", " + logwith.getDBid() + ", NULL);").toString();
		db.commitTransaction(transaction);
		return new LogwithApp((String)elevator.get("appDBid"), profile, position, (AppInformation)elevator.get("appInfos"), null, (String)elevator.get("registrationDate"), ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid, null, logwith.getDBid(), logwithDBid);
	}
	
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String logwithDBid;
	protected String logwith;
	
	public LogwithApp(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, GroupWebsiteApp groupWebsiteApp, String logwith, String logwithDBid) {
		super(db_id, profile, position, infos, groupApp, insertDate, single_id, site, websiteAppDBid, groupWebsiteApp);
		this.logwith = logwith;
		this.logwithDBid = logwithDBid;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM logWithApps WHERE id=" + logwithDBid + ";");
		super.removeFromDB(sm);
		db.commitTransaction(transaction);
	}
	
}
