package com.Ease.Context.Catalog;

import java.util.List;

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
	
	protected List<Website> websites;
	
	public Catalog(DataBaseConnection db) throws GeneralException {
		this.websites = Website.loadWebsite(db);
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */
	
	public Website getWebsite(String db_id) throws GeneralException {
		for (Website website : websites) {
			if (website.getDb_id() == db_id){
				return website;
			}
		}
		throw new GeneralException(ServletManager.Code.InternError, "This website dosen't exist.");
	}
}
