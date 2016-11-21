package com.Ease.dashboard;

public class ClassicApp extends WebsiteApp {
	public static ClassicApp createClassicApp(String name, Profile profile, Site site, Map<String, String> informations, ServletManager sm) throws GeneralException {
		
	}
	
	protected Account account;
	
	public ClassicApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, Site site, Account account) {
		super(name, profile, permissions, single_id, db_id, site);
		this.account = account;
	}
	
	public Account getAccount() {
		return this.account;
	}
}
