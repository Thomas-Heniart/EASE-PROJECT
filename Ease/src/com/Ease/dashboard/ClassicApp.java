package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class ClassicApp extends WebsiteApp {
	
	public enum ClassicAppData {
		NOTHING,
		ID,
		WEBSITE_APP_ID,
		ACCOUNT_ID,
		GROUP_CLASSIC_APP_ID
	}
	
	public static ClassicApp createClassicApp(String name, Profile profile, Website site, Map<String, String> informations, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		int position = profile.getNextPosition();
		Permissions permissions = AppPermissions.loadPersonnalAppPermissions(sm);
		Account account = Account.createAccount(informations, profile.getUser(), sm);
		int app_id = db.set("INSERT INTO apps values (null, " + profile.getDb_id() + ", " + position + ", default, null, 'ClassicApp', 1, null);");
		int website_app_id = db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ", null, 'ClassicApp');");
		int classic_app_id = db.set("INSERT INTO classicApps values (null, " + website_app_id + ", " + account.getDb_id()  + ");");
		
		db.commitTransaction(transaction);
		return new ClassicApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), true, site, account);
	}
	
	protected Account account;
	
	public ClassicApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, boolean working, Website site, Account account) {
		super(name, profile, permissions, position, single_id, db_id, working, site);
		this.account = account;
	}
	
	public ClassicApp(String app_id, Profile profile, ServletManager sm) throws GeneralException {
		super(app_id, profile, sm);
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT classicApps.id FROM (apps JOIN websiteApps ON apps.id = websiteApps.app_id) JOIN classicApps ON classicApps.website_app_id = websiteApps.id WHERE apps.id = " + db_id + ";");
		try {
			if (rs.next()) {
				this.account = Account.loadAccount(rs.getString(1), profile.getUser(), sm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
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
