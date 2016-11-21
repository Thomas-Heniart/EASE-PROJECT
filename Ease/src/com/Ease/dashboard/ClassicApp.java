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
	
	public ClassicApp(String app_id, ServletManager sm) throws GeneralException {
		super(app_id, sm);
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT classicApps.id FROM (apps JOIN websiteApps ON apps.id = websiteApps.app_id) JOIN classicApps ON classicApps.website_app_id = websiteApps.id WHERE apps.id = " + db_id + ";");
		if (rs.next()) {
			this.account = Account.loadAccount(rs.getString(1), sm);
		}
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
