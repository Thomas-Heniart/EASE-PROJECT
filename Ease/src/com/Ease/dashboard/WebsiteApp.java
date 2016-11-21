package com.Ease.dashboard;

import com.Ease.context.Site;

public class WebsiteApp extends App {
	public static WebsiteApp createEmptyApp(String name, Profile profile, Site site, ServletManager sm) throws GeneralException {
		
	}
	
	protected Site site;
	
	public WebsiteApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, Site site) {
		this.name = name;
		this.profile = profile;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
		this.site = site;
	}
	
	public Site getSite() {
		return this.site;
	}
}
