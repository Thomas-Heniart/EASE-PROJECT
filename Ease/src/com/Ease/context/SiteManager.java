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
		return sites;
	}
	
	public List<Tag> getTagsList() {
		return tags;
	}

	public Site getSiteById(int siteId) {
		Iterator<Site> iterator = sites.iterator();
		while (iterator.hasNext()) {
			Site tmpSite = iterator.next();
			if (siteId == Integer.parseInt(tmpSite.getId()))
				return tmpSite;
		}
		return null;
	}

	public Tag getTagById(int tagId) {
		Iterator<Tag> iterator = tags.iterator();
		while (iterator.hasNext()) {
			Tag tmpTag = iterator.next();
			if (tagId == tmpTag.getId())
				return tmpTag;
		}
		return null;
	}

	public void addNewTag(Tag tag) {
		tags.add(tag);
	}

	public void setTagsForSites(ServletContext context) throws SQLException {
		for (int i = 0; i < sites.size(); i++)
			sites.get(i).setTags(context);
	}

	public void setSitesForTags(ServletContext context) throws SQLException {
		Iterator<Tag> iterator = tags.iterator();
		while (iterator.hasNext())
			iterator.next().setSites(context);
	}

	public JSONArray searchSitesWith(String search) {
		JSONArray res = new JSONArray();
		Iterator<Site> iterator = sites.iterator();
		while (iterator.hasNext()) {
			Site tmpSite = iterator.next();
			if (tmpSite.getName().toUpperCase().startsWith(search.toUpperCase()))
				res.add(tmpSite.getJson());
		}
		return res;
	}

	public JSONArray getSitesListJson() {
		JSONArray res = new JSONArray();
		Iterator<Site> iterator = sites.iterator();
		while (iterator.hasNext())
			res.add(iterator.next().getJson());
		return res;
	}

	public JSONArray getSitesWithTags(String[] selectedIds) {
		List<Tag> selectedTags = new LinkedList<Tag>();
		JSONArray res = new JSONArray();

		// Convert string to int
		for (int i = 0; i < selectedIds.length; i++) {
			selectedTags.add(getTagById(Integer.parseInt(selectedIds[i])));
		}

		Iterator<Site> siteIterator = sites.iterator();
		while (siteIterator.hasNext()) {
			Site tmpSite = siteIterator.next();
			if (tmpSite.hasAllTags(selectedTags)) {
				res.add(tmpSite.getJson());
			}
		}
		siteIterator = sites.iterator();
		while (siteIterator.hasNext()) {
			Site tmpSite = siteIterator.next();
			if (!res.contains(tmpSite.getJson()))
				if (tmpSite.hasTags(selectedTags)) {
					System.out.println(tmpSite.getName() + " has at least one of selected tags" );
					res.add(tmpSite.getJson());
				}
		}
		return res;
	}
}
