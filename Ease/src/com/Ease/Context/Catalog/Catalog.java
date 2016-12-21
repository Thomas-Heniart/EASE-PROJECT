package com.Ease.Context.Catalog;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Catalog {
	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected Map<String, Website> websiteDBmap;
	protected Map<Integer, Website> websiteIDmap;
	protected List<Website> websites;
	protected Map<String, Tag> tagDBmap;
	protected Map<Integer, Tag> tagIDmap;
	protected List<Tag> tags;
	
	public Catalog(DataBaseConnection db, ServletContext context) throws GeneralException {
		
		websites = Website.loadWebsites(db, context);
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
		}
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
			if (website.isInPublicCatalog())
				res.add(website);
		});
		return res;
	}
	
	public List<Tag> getTags() {
		return this.tags;
	}

	public String search(String search, String[] tags) throws GeneralException {
		String result = "";
		if (tags.length <= 0) {
			for (Website site : this.websites) {
				if (site.getName().startsWith(search)) {
					result += site.getSingleId();
					result += " ";
				}
			}
		} else {
			Tag tag;
			for (String tagName : tags) {
				tag = this.tagIDmap.get(tagName);
				if (tag != null) {
					result += tag.search(search);
				} else {
					throw new GeneralException(ServletManager.Code.ClientWarning, "This tag dosen't exist.");
				}
			}
		}
		return result;
	}
}
