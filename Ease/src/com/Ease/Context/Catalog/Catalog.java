package com.Ease.Context.Catalog;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;

import com.Ease.Context.Group.Group;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Catalog {
	/*
	 * 
	 * Constructor
	 * 
	 */
	protected Map<String, Sso> ssoDBmap;
	protected Map<Integer, Sso> ssoIDmap;
	protected List<Sso> ssos;
	protected Map<String, Website> websiteDBmap;
	protected Map<Integer, Website> websiteIDmap;
	protected List<Website> websites;
	protected Map<String, Tag> tagDBmap;
	protected Map<Integer, Tag> tagIDmap;
	protected List<Tag> tags;
	
	public static Catalog updateCatalog(DataBaseConnection db, ServletContext context, Catalog oldCatalog) throws GeneralException {
		Catalog newCatalog = new Catalog(db, context);
		newCatalog.updateSingleIdsWith(oldCatalog);
		return newCatalog;
	}
	
	private void updateSingleIdsWith(Catalog oldCatalog) {
		for (Website oldWebsite : oldCatalog.getWebsites()) {
			Website newWebsite = this.websiteDBmap.get(oldWebsite.getDb_id());
			if (newWebsite != null)
				newWebsite.setSingleId(oldWebsite.getSingleId());
		}
		for (Tag oldTag : oldCatalog.getTags()) {
			Tag newTag = this.tagDBmap.get(oldTag.getDbId());
			if (newTag != null)
				newTag.setSingleId(oldTag.getSingleId());
		}
		for (Sso oldSso : oldCatalog.getSsos()) {
			Sso newSso = this.ssoDBmap.get(oldSso.getDbid());
			if (newSso != null)
				newSso.setSingleId(oldSso.getSingleId());
		}
	}

	public Catalog(DataBaseConnection db, ServletContext context) throws GeneralException {
		
		ssos = Sso.loadSsos(db, context);
		ssoDBmap = new HashMap<String, Sso>();
		ssoIDmap = new HashMap<Integer, Sso>();
		for (Sso sso : ssos) {
			ssoDBmap.put(sso.getDbid(), sso);
			ssoIDmap.put(sso.getSingleId(), sso);
		}
		websites = Website.loadWebsites(db, ssoDBmap, context);
		websiteDBmap = new HashMap<String, Website>();
		websiteIDmap = new HashMap<Integer, Website>();
		for (Website site : websites) {
			websiteDBmap.put(site.getDb_id(), site);
			websiteIDmap.put(site.getSingleId(), site);
		}
		for (Website site : websites)
			site.loadLoginWithWebsites(db, this);
		tags = Tag.loadTags(db, context);
		tagDBmap = new HashMap<String, Tag>();
		tagIDmap = new HashMap<Integer, Tag>();
		for (Tag tag : tags) {
			tagDBmap.put(tag.getDbId(), tag);
			tagIDmap.put(tag.getSingleId(), tag);
			tag.setSites(websiteDBmap, db);
		}
	}
	
	public void addWebsite(String url, String name, String homePage, String folder, boolean haveLoginButton, String[] haveLoginWith, ServletManager sm) throws GeneralException {
		Website site = Website.createWebsite(url, name, homePage, folder, haveLoginButton, haveLoginWith, this, sm);
		websiteDBmap.put(site.getDb_id(), site);
		websiteIDmap.put(site.getSingleId(), site);
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */
	
	public Website getWebsiteWithDBid(String db_id) throws GeneralException {
		Website ret = null;
		ret = websiteDBmap.get(db_id);
		if (ret != null)
			return ret;
		throw new GeneralException(ServletManager.Code.InternError, "This website dosen't exist.");
	}
	
	public Website getWebsiteWithSingleId(Integer single_id) throws GeneralException {
		Website ret = null;
		ret = websiteIDmap.get(single_id);
		if (ret != null)
			return ret;
		throw new GeneralException(ServletManager.Code.InternError, "This website dosen't exist.");
	}
	
	public List<Website> getWebsites() {
		return this.websites;
	}
	
	public Website getWebsiteWithName(String name) throws GeneralException {
		for (Website site : this.websites) {
			if (site.getName().equals(name))
				return site;
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This website name dosen't exist.");
	}
	
	public List<Website> getPublicWebsites() {
		List<Website> res = new LinkedList<Website> ();
		this.websites.forEach((website) -> {
			if (website.isInPublicCatalog() && !website.isNew())
				res.add(website);
		});
		return res;
	}
	
	public List<Website> getPublicWebsitesForGroups(List<Group> groups) {
		List<Website> res = new LinkedList<Website> ();
		boolean containsWebsite = false;
		for (Website website : websites) {
			containsWebsite = false;
			for (Group group : groups) {
				if (group.containsWebsite(website) && !website.isNew()) {
					res.add(website);
					containsWebsite = true;
					break;
				}
			}
			if (containsWebsite)
				continue;
			if (website.isInPublicCatalog() && !website.isNew())
				res.add(website);
		}
		return res;
	}
	
	public List<Website> getNewWebsitesForGroups(List<Group> groups) {
		List<Website> res = new LinkedList<Website> ();
		boolean containsWebsite = false;
		for (Website website : websites) {
			containsWebsite = false;
			for (Group group : groups) {
				if (group.containsWebsite(website) && website.isNew()) {
					res.add(website);
					containsWebsite = true;
					break;
				}
			}
			if (containsWebsite)
				continue;
			if (website.isInPublicCatalog() && website.isNew())
				res.add(website);
		}
		return res;
	}
	
	public List<Website> getNewWebsites() {
		List<Website> res = new LinkedList<Website> ();
		this.websites.forEach((website) -> {
			if (website.isInPublicCatalog() && website.isNew())
				res.add(website);
		});
		return res;
	}
	
	public List<Tag> getTags() {
		return this.tags;
	}

	public JSONArray search(String search, JSONArray tags) throws GeneralException {
		JSONArray result = new JSONArray();
		if (tags.size() <= 0) {
			for (Website site : this.websites) {
				if (site.getName().toUpperCase().startsWith(search.toUpperCase())) {
					result.add(String.valueOf(site.getSingleId()));
				}
			}
		} else {
			Tag tag;
			for (Object tagName : tags) {
				tag = this.tagIDmap.get(Integer.parseInt((String)tagName));
				if (tag != null) {
					tag.search(result, search);
				} else {
					throw new GeneralException(ServletManager.Code.ClientError, "This tag dosen't exist.");
				}
			}
		}
		return result;
	}
	
	public Website getWebsiteWithHost(String host) throws GeneralException {
		for (Website site : websites) {
			if (site.getUrl().contains(host)) {
				return site;
			}
		}
		return null;
	}

	public boolean haveWebsiteNamed(String websiteName) {
		for (Website site : this.websites) {
			if (site.getName().equals(websiteName))
				return true;
		}
		return false;
	}
	
	public Website getWebsiteNamed(String websiteName) throws GeneralException {
		for (Website site : this.websites) {
			if (site.getName().toUpperCase().equals(websiteName.toUpperCase()))
				return site;
		}
		throw new GeneralException(ServletManager.Code.ClientError, "We don't have this website");
	}

	public boolean haveWebsiteWithLoginUrl(String url) {
		for (Website site : this.websites) {
			if (site.loginUrlMatch(url))
				return true;
		}
		return false;
	}

	public Website getWebsiteWithLoginUrl(String url) throws GeneralException{
		for (Website site : this.websites) {
			if (site.loginUrlMatch(url))
				return site;
		}
		throw new GeneralException(ServletManager.Code.UserMiss, "This website is not in the catalog.");
	}

	@SuppressWarnings("unchecked")
	public JSONArray getWebsitesJson() {
		JSONArray res = new JSONArray();
		for (Website website : this.websites)
			res.add(website.getJsonForCatalog());
		Collections.reverse(res);
		return res;
	}
	
	private List<Sso> getSsos() {
		return this.ssos;
	}
	
	public JSONArray getSsoJson() {
		JSONArray res = new JSONArray();
		for (Map.Entry<Integer, Sso> entry : this.ssoIDmap.entrySet())
		{
			res.add(entry.getValue().getJson());
		}
		return res;
	}

	public List<Tag> getTagsForWebsiteId(int websiteId) {
		Website website = this.websiteIDmap.get(websiteId);
		List<Tag> res = new LinkedList<Tag>();
		for (Tag tag : this.tags) {
			if (tag.getWebsites().contains(website))
				res.add(tag);
		}
		return res;
	}

	public void addWebsiteTag(int websiteId, int tagId, ServletManager sm) throws GeneralException {
		Website website = this.websiteIDmap.get(websiteId);
		Tag tag = this.tagIDmap.get(tagId);
		if (website == null)
			throw new GeneralException(ServletManager.Code.ClientError, "This website does not exist");
		if (tag == null)
			throw new GeneralException(ServletManager.Code.ClientError, "This tag does not exist");
		tag.addWebsite(website, sm);
		
	}
	
	public void removeWebsiteTag(int websiteId, int tagId, ServletManager sm) throws GeneralException {
		Website website = this.websiteIDmap.get(websiteId);
		Tag tag = this.tagIDmap.get(tagId);
		if (website == null)
			throw new GeneralException(ServletManager.Code.ClientError, "This website does not exist");
		if (tag == null)
			throw new GeneralException(ServletManager.Code.ClientError, "This tag does not exist");
		tag.removeWebsite(website, sm);
		
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
		this.tagDBmap.put(tag.getDbId(), tag);
		this.tagIDmap.put(tag.getSingleId(), tag);
	}
	
	public void refresh(ServletManager sm) throws GeneralException {
		for (Website website : this.websites)
			website.refresh(sm);
		for (Tag tag : this.tags)
			tag.refresh(sm);
		for (Sso sso : this.ssos)
			sso.refresh(sm);
	}
}
