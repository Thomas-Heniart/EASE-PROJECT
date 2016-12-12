package com.Ease.Context.Catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Dashboard.App.Website;
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
	
	public Catalog(DataBaseConnection db, ServletContext context) throws GeneralException {
		
		List<Website> websites = Website.loadWebsite(db, context);
		websiteDBmap = new HashMap<String, Website>();
		websiteIDmap = new HashMap<Integer, Website>();
		for (Website site : websites) {
			websiteDBmap.put(site.getDb_id(), site);
			websiteIDmap.put(site.getSingleId(), site);
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
}
