package com.Ease.Context.Catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sso {

	protected List<Website> websites;
	protected Map<String, Website> websitesDBmap;
	protected Map<Integer, Website> websitesIDmap;
	protected String		name;
	
	public Sso(String name, List<Website> websites) {
		this.name = name;
		this.websites = websites;
		this.websitesDBmap = new HashMap<String, Website>();
		this.websitesIDmap = new HashMap<Integer, Website>();
		for (Website site : websites) {
			this.websitesDBmap.put(site.getDb_id(), site);
			this.websitesIDmap.put(site.getSingleId(), site);
		}
	}
}
