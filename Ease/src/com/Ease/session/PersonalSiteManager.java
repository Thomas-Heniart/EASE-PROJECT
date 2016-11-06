package com.Ease.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.Ease.context.Site;
import com.Ease.context.SiteManager;

public class PersonalSiteManager {
	protected Map<String, String> sites;
	
	public PersonalSiteManager(SiteManager siteManager) {
		Iterator<Site> it = siteManager.getAllSites().iterator();
		this.sites = new HashMap<String, String>();
		while (it.hasNext()) {
			Site tmpSite = it.next();
			sites.put(tmpSite.getId(), tmpSite.getPosition());
		}
	}
	
	public Map<String, String> getSites() {
		return sites;
	}
	
	public Set<String> getSitesIds() {
		return sites.keySet();
	}
	
	public Collection<String> getSitesPositions() {
		return sites.values();
	}
	
	public String getPositionForSite(String id) {
		return sites.get(id);
	}
	
	public void updatePositionForSite(String id, String position) {
		sites.put(id, position);
	}
}
