package com.Ease.Context.Catalog;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsitesVisitedManager {
	
	protected Map<String, Integer> websitesRequestsMap;
	
	public WebsitesVisitedManager(DataBaseConnection db) throws GeneralException {
		this.websitesRequestsMap = new HashMap<String, Integer>();
		DatabaseResult rs = db.prepareRequest("SELECT url, count FROM websitesVisited").get();
		while(rs.next()) {
			String url = rs.getString(1);
			Integer count = rs.getInt(2);
			this.websitesRequestsMap.put(url, count);
		}
	}
	
	public void addWebsiteRequest(String url, Integer count, DataBaseConnection db, Catalog catalog) throws GeneralException {
		if (catalog.haveWebsiteWithLoginUrl(url) || catalog.haveWebsiteWithHostUrl(url))
			return;
		Integer old_count = this.websitesRequestsMap.get(url);
		DatabaseRequest request;
		if (old_count != null) {
			this.websitesRequestsMap.put(url, old_count + count);
			request = db.prepareRequest("UPDATE websitesVisited SET count = ? WHERE url = ?");
			request.setInt(old_count + count);
			request.setString(url);
		}
		else {
			this.websitesRequestsMap.put(url, count);
			request = db.prepareRequest("INSERT INTO websitesVisited values (null, ?, ?);");
			request.setString(url);
			request.setInt(count);
		}
		request.set();
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
		DatabaseRequest request = db.prepareRequest("DELETE FROM websitesVisited WHERE url = ?");
		request.setString(url);
		request.set();
	}
}
