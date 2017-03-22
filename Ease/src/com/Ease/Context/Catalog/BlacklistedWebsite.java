package com.Ease.Context.Catalog;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class BlacklistedWebsite {
	
	/**
	 * Load all BlackListedWebsite from database order by count desc
	 * @param DataBaseConnection db
	 * @param ServletContext context
	 * @return List<BlacklistedWebsite>
	 * @throws GeneralException when a database request fail
	 */
	public static List<BlacklistedWebsite> loadBlacklistedWebsites(DataBaseConnection db, ServletContext context) throws GeneralException {
		List<BlacklistedWebsite> res = new LinkedList<BlacklistedWebsite>();
		DatabaseResult rs = db.prepareRequest("SELECT * FROM blacklistedWebsites ORDER BY count DESC;").get();
		while(rs.next()) {
			int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
			res.add(new BlacklistedWebsite(rs.getString(1), single_id, rs.getString(2), rs.getInt(3)));
		}
		return res;
	}
	
	/**
	 * Create a new BlacklistedWebsite in database and return it
	 * @param String url
	 * @param int count
	 * @param ServletManager sm
	 * @return BlacklistedWebsite
	 * @throws GeneralException when insertion fail
	 */
	public static BlacklistedWebsite createBlacklistedWebsite(String url, int count, ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO blacklistedWebsites values(?, ?, ?);");
		request.setNull();
		request.setString(url);
		request.setInt(count);
		String db_id = request.set().toString();
		int single_id = ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId();
		return new BlacklistedWebsite(db_id, single_id, url, count);
	}
	
	/**
	 * Create a new BlacklistedWebsite in database and return it
	 * @param WebsiteVisited websiteVisited
	 * @param ServletManager sm
	 * @return BlacklistedWebsite
	 * @throws GeneralException when insertion fail
	 */
	public static BlacklistedWebsite createBlackListedWebsiteFromWebsiteVisited(WebsiteVisited websiteVisited, ServletManager sm) throws GeneralException {
		return createBlacklistedWebsite(websiteVisited.getUrl(), websiteVisited.getCount(), sm);
	}
	
	protected String url;
	protected int count;
	protected String db_id;
	protected int single_id;
	
	public BlacklistedWebsite(String db_id, int single_id, String url, int count) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.url = url;
		this.count = count;
	}
	
	public String getDb_id() {
		return db_id;
	}
	
	public int getSingle_id() {
		return single_id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public int getCount() {
		return count;
	}
	
	/**
	 * Delete this instance from database
	 * @param ServletManager sm
	 * @throws GeneralException when deletion fail
	 */
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("DELETE FROM blacklistedWebsites WHERE id = ?");
		request.setInt(db_id);
		request.set();
	}
	
	/**
	 * Use this method to compare this instance count with an other BlacklistedWebsite instance
	 * @param BlacklistedWebsite w2
	 * @return -1 if this count is greater than w2 count, 0 if equals and 1 else
	 */
	public int compareTo(BlacklistedWebsite w2) {
		if (count < w2.getCount())
			return 1;
		else if (count == w2.getCount())
			return 0;
		else
			return -1;
	}
}
