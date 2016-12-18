package com.Ease.Context.Catalog;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Dashboard.App.Tag;
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
}
