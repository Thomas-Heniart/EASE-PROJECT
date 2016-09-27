package com.Ease.data;

import javax.servlet.ServletContext;

import com.Ease.context.Site;
import com.Ease.context.SiteManager;

public class CustomApp {
	protected String 	id;
	protected Site		website;
	protected int		pos;
	protected String	name;
	
	CustomApp(String id, String website_id, int pos, String name, ServletContext context){
		this.id = id;
		this.website = ((SiteManager)context.getAttribute("siteManager")).get(website_id);
		this.pos = pos;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public Site getSite(){
		return website;
	}
	public int getPosition() {
		return pos;
	}
	public String getName() {
		return name;
	}
}
