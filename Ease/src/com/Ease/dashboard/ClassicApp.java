package com.Ease.dashboard;

import java.util.Map;

import com.Ease.context.Site;
import com.Ease.session.Account;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class ClassicApp extends WebsiteApp {
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
		return new ClassicApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), site, account);
	}
	
	protected Account account;
	
	public ClassicApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, Site site, Account account) {
		super(name, profile, permissions, position, single_id, db_id, site);
		this.account = account;
	}
	
	public Account getAccount() {
		return this.account;
	}
}
