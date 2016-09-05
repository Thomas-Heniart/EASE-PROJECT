package com.Ease.context;

import java.util.LinkedList;
import java.util.List;

public class SiteManager {
	protected List<Site> sites;
	
	public SiteManager() {
		sites = new LinkedList<Site>();
	}
	
	public void add(Site site) {
		sites.add(site);
	}
	
	public Site get(String id) {
		for (int i = 0; i < sites.size(); ++i) {
			if (((Site)sites.get(i)).getId().equals(id))
				return sites.get(i);
		}
		return null;
	}
	public List<Site> getSitesList(){
		return sites;
	}
}
