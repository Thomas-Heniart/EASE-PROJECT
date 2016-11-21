package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.Ease.context.Site;
import com.Ease.session.Account;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class ClassicApp extends WebsiteApp {
	
	public enum ClassicAppData {
		NOTHING,
		ID,
		WEBSITE_APP_ID,
		WEBSITE_ID
	}
	
	protected static ClassicApp loadContent(String name, Profile profile, Permissions permissions, int position, String db_id, boolean working, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ResultSet rs = db.get("SELECT classicApps.id, website_app_id, website_id FROM classicApps JOIN websiteApps ON website_app_id = websiteApps.id WHERE websiteApps.app_id = " + db_id + ";");
		try {
			if (rs.next()) {
				Site site = Site.loadSite(rs.getString(ClassicAppData.WEBSITE_ID.ordinal()), sm);
				Account account = Account.loadAccount(rs.getString(ClassicAppData.ID.ordinal()), sm);
				return new ClassicApp(name, profile, permissions, position, sm.getNextSingleId(), db_id, working, site, account);
			}
			db.commitTransaction(transaction);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}
	
	public static ClassicApp createClassicApp(String name, Profile profile, Site site, Map<String, String> informations, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Permissions permissions = AppPermissions.loadDefaultAppPermissions(sm);
		int position = profile.getNextPosition();
		Integer app_id = db.set("INSERT INTO apps values (null, '" + name + "' , " + profile.getDb_id() + ", " + position + ", " + permissions.getDBid() + ", 'LinkApp', 1);");
		Integer website_app_id = db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ");");
		Integer classic_app_id = db.set("INSERT INTO classicApps values (null, " + website_app_id + ");");
		for (Map.Entry<String, String> entry : informations.entrySet())
			db.set("INSERT INTO accounts values (null, " + classic_app_id + ", '" + entry.getKey() + "', '" + entry.getValue() + "');");
		db.commitTransaction(transaction);
		Account account = new Account(classic_app_id, informations);
		return new ClassicApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), true, site, account);
	}
	
	protected Account account;
	
	public ClassicApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, boolean working, Site site, Account account) {
		super(name, profile, permissions, position, single_id, db_id, working, site);
		this.account = account;
	}
	
	public Account getAccount() {
		return this.account;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		this.account.removeFromDb(sm);
		db.set("DELETE FROM classicApps WHERE id = " + this.getDb_id() + ";");
		super.removeFromDb(sm);
		db.commitTransaction(transaction);
	}
}
