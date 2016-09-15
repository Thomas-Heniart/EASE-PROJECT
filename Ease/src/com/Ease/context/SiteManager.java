package com.Ease.context;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;

public class SiteManager {
	protected List<Site> sites;
	protected List<Tag> tags;

	public SiteManager() {
		sites = new LinkedList<Site>();
		tags = new LinkedList<Tag>();
	}

	public void add(Site site) {
		sites.add(site);
	}

	public Site get(String id) {
		for (int i = 0; i < sites.size(); ++i) {
			if (((Site) sites.get(i)).getId().equals(id))
				return sites.get(i);
		}
		return null;
	}

	public void clear() {
		sites.clear();
	}

	public List<Site> getSitesList() {
		tags.clear();
		return sites;
	}

	public void addTag(Tag tag) {
		if (tags.contains(tag))
			tags.remove(tag);
		else
			tags.add(tag);

	}

	public void setTags(ServletContext context) throws SQLException {
		for (int i = 0; i < sites.size(); i++)
			sites.get(i).setTags(context);
	}
	
	public JSONArray searchSitesWith(String search) {
		JSONArray res = new JSONArray();
		Iterator<Site> iterator = sites.iterator();
		while(iterator.hasNext()) {
			Site tmpSite = iterator.next();
			if(tmpSite.getName().startsWith(search))
				res.add(tmpSite.getJson());
		}
		return res;
	}

	public JSONArray getJson() {
		JSONArray jsonArray = new JSONArray();
		if (tags.isEmpty()) {
			Iterator<Site> siteIterator = sites.iterator();
			while (siteIterator.hasNext()) {
				jsonArray.add(siteIterator.next().getJson());
			}
			return jsonArray;
		}
		Iterator<Site> iterator = sites.iterator();
		while (iterator.hasNext()) {
			Site site = iterator.next();
			List<Tag> siteTags = site.getTags();
			Iterator<Tag> tagIterator = siteTags.iterator();
			while (tagIterator.hasNext()) {
				if (siteTags.contains(tagIterator.next())) {
					jsonArray.add(site.getJson());
					break;
				}
			}
		}
		return jsonArray;
	}
}
