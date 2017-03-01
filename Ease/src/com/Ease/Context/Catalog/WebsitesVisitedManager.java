package com.Ease.Context.Catalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsitesVisitedManager {
	
	protected Map<String, Integer> websitesRequestsMap;
	
	public WebsitesVisitedManager(DataBaseConnection db) throws GeneralException {
		this.websitesRequestsMap = new HashMap<String, Integer>();
		ResultSet rs = db.get("SELECT url, count FROM websitesVisited");
		try {
			while(rs.next()) {
				String url = rs.getString(1);
				Integer count = rs.getInt(2);
				this.websitesRequestsMap.put(url, count);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void addWebsiteRequest(String url, Integer count, DataBaseConnection db, Catalog catalog) throws GeneralException {
		if (catalog.haveWebsiteWithLoginUrl(url) || catalog.haveWebsiteWithHostUrl(url))
			return;
		Integer old_count = this.websitesRequestsMap.get(url);
		if (old_count != null) {
			this.websitesRequestsMap.put(url, old_count + count);
			db.set("UPDATE websitesVisited SET count = " + (old_count + count) +" WHERE url = '" + url + "'");
		}
		else {
			this.websitesRequestsMap.put(url, count);
			db.set("INSERT INTO websitesVisited values (null, '" + url + "', " + count + ")");
		}
	}
	
	public Integer getWebsiteRequestCount(String url) {
		return this.websitesRequestsMap.get(url);
	}
	
	public List<Entry<String, Integer>> getWeightedWebsitesVisited() {
		List<Entry<String, Integer>> l = new LinkedList<Entry<String, Integer>>();
		for(Entry<String, Integer> entry : this.websitesRequestsMap.entrySet())
			l.add(entry);
		Collections.sort(l, new Comparator<Entry<String, Integer>>(){
			@Override
			public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
				return -(e1.getValue().compareTo(e2.getValue()));
			}
		});
		return l;
	}

	public void deleteWebsiteVisited(String url, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (this.websitesRequestsMap.get(url) == null)
			throw new GeneralException(ServletManager.Code.ClientError, "This url does not exist");
		this.websitesRequestsMap.remove(url);
		db.set("DELETE FROM websitesVisited WHERE url = '" + url + "'");
	}
}
