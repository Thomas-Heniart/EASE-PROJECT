package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.Website;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class ClassicApp extends WebsiteApp {
	
	public enum ClassicAppData {
		NOTHING,
		ID,
		WEBSITE_APP_ID,
		ACCOUNT_ID,
		GROUP_CLASSIC_APP_ID
	}
	
	public static ClassicApp createClassicApp(String name, Profile profile, Website site, String password, Map<String, String> informations, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		int position = profile.getApps().size();
		Account account = Account.createAccount(password, false, profile.getUser(), sm);
		List<AccountInformation> account_informations = AccountInformation.createAccountInformations(account.getDb_id(), informations, sm);
		AppInformation app_information = AppInformation.createAppInformation(name, sm);
		int app_id = db.set("INSERT INTO apps values (null, " + profile.getDBid() + ", " + position + ", default, null, 'ClassicApp', 1, null);");
		int website_app_id = db.set("INSERT INTO websiteApps values (null, " + site.getDb_id() + ", " + app_id + ", null, 'ClassicApp');");
		db.set("INSERT INTO classicApps values (null, " + website_app_id + ", " + account.getDb_id()  + ");");
		db.commitTransaction(transaction);
		return new ClassicApp(profile, position, sm.getNextSingleId(), String.valueOf(app_id), true, site, account, account_informations, app_information);
	}
	
	protected Account account;
	protected List<AccountInformation> account_informations;
	
	public ClassicApp(Profile profile, int position, int single_id, String db_id, boolean working, Website site, Account account, List<AccountInformation> account_informations, AppInformation app_information) {
		super(profile, position, single_id, db_id, working, site, app_information);
		this.account = account;
		this.account_informations = account_informations;
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
