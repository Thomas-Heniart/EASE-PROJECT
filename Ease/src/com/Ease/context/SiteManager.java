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

	public void clearSites() {
		sites.clear();
	}

	public void clearTags() {
		tags.clear();
	}

	public List<Site> getSitesList() {
		return sites;
	}
	
	public List<Site> getSso(String ssoId){
		List<Site> res = new LinkedList<Site>();
		Iterator<Site> iterator = sites.iterator();
		while (iterator.hasNext()) {
			Site tmpSite = iterator.next();
			if (ssoId == tmpSite.getSso())
				res.add(tmpSite);
		}
		return res;
	}
	
	public List<Site> getSso(Site site){
		List<Site> res = new LinkedList<Site>();
		String ssoId = site.getSso();
		if(ssoId==null){
			res.add(site);
			return res;
		}
		Iterator<Site> iterator = sites.iterator();
		while (iterator.hasNext()) {
			Site tmpSite = iterator.next();
			if (ssoId == tmpSite.getSso())
				res.add(tmpSite);
		}
		return res;
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

	public Tag getTagForSearch(String search, List<Tag> selectedTags) {
		List<Tag> allTags = tags;
		allTags.removeAll(selectedTags);
		Iterator<Tag> it = allTags.iterator();
		while (it.hasNext()) {
			Tag tmpTag = it.next();
			if (tmpTag.getName().toLowerCase().equals(search.toLowerCase()))
				return tmpTag;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getSitesListJson() {
		JSONArray res = new JSONArray();
		Iterator<Site> iterator = sites.iterator();
		while (iterator.hasNext()) {
			Site tmpSite = iterator.next();
			/*JSONArray tmpJson = new JSONArray();
			tmpJson.add(tmpSite.getId());
			tmpJson.add("");*/
			res.add(tmpSite.getId());
		}
			
		return res;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getSitesListJsonWithSearchAndTags(String search, String[] selectedIds) {
		// If everything is empty then returns all websites
		if (selectedIds.length == 0 && (search.isEmpty() || search == null))
			return getSitesListJson();

		List<Tag> selectedTags = new LinkedList<Tag>();
		JSONArray res = new JSONArray();
		List<Site> sitesToShow = new LinkedList<Site>();

		// Convert string to int
		for (int i = 0; i < selectedIds.length; i++) {
			selectedTags.add(getTagById(Integer.parseInt(selectedIds[i])));
		}

		if (selectedTags.size() == 0) {
			Tag searchTag = getTagForSearch(search, selectedTags);
			if (searchTag != null) {
				List<Site> tagSites = searchTag.getSites();
				Iterator<Site> it = tagSites.iterator();
				while (it.hasNext()) {
					Site tmpSite = it.next();
					if (!sitesToShow.contains(tmpSite)) {
						sitesToShow.add(tmpSite);
						/*JSONArray jsonArray = tmpSite.getJson();
						jsonArray.add("hasSomeTags");*/
						res.add(tmpSite.getId());
					}
				}
			}
			Iterator<Site> it = sites.iterator();
			while (it.hasNext()) {
				Site tmpSite = it.next();
				if (tmpSite.getName().toLowerCase().contains(search.toLowerCase()))
					if (!sitesToShow.contains(tmpSite)) {
						sitesToShow.add(tmpSite);
						/*JSONArray jsonArray = tmpSite.getJson();
						jsonArray.add("hasSomeTags");*/
						res.add(tmpSite.getId());
					}

			}
			return res;
		}

		// get sites with all tags
		Iterator<Site> siteIterator = sites.iterator();
		while (siteIterator.hasNext()) {
			Site tmpSite = siteIterator.next();
			if (tmpSite.hasAllTags(selectedTags)) {
				if (tmpSite.getName().toLowerCase().matches("*"+ search.toLowerCase() +"*") || search.isEmpty()) {
					sitesToShow.add(tmpSite);
					res.add(tmpSite.getId());
				}
			}
		}
		return res;
	}
}
