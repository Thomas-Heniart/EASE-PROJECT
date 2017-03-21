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

public class WebsiteVisited {

	/**
	 * Load all WebsiteVisited instance from database order by count desc
	 * @param DataBaseConnection db
	 * @param ServletContext context
	 * @return List<WebsiteVisited>
	 * @throws GeneralException when select statement fail
	 */
	public static List<WebsiteVisited> loadWebsitesVisited(DataBaseConnection db, ServletContext context) throws GeneralException {
		DatabaseResult rs = db.prepareRequest("SELECT * FROM websitesVisited ORDER BY count DESC;").get();
		List<WebsiteVisited> res = new LinkedList<WebsiteVisited>();
		while(rs.next()) {
			int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
			res.add(new WebsiteVisited(rs.getString(1), single_id, rs.getString(2), rs.getInt(3)));
		}
		return res;
	}
	
	/**
	 * Create a new WebsiteVisited in database and return its instance
	 * @param String url
	 * @param int count
	 * @param ServletManager sm
	 * @return WebsiteVisited
	 * @throws GeneralException when database insert fail
	 */
	public static WebsiteVisited createWebsiteVisited(String url, int count, ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO websitesVisited values(?, ?, ?);");
		request.setNull();
		request.setString(url);
		request.setInt(count);
		String db_id = request.set().toString();
		int single_id = sm.getNextSingle_id();
		return new WebsiteVisited(db_id, single_id, url, count);
	}
	
	protected String db_id;
	protected int single_id;
	protected String url;
	protected int count;
	
	public WebsiteVisited(String db_id, int single_id, String url, int count) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.url = url;
		this.count = count;
	}
	
	public String getDb_id() {
		return this.db_id;
	}
	
	public int getSingle_id() {
		return this.single_id;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public int getCount() {
		return this.count;
	}
	
	/**
	 * Increase instance count in database
	 * @param int count
	 * @param ServletManager sm
	 * @throws GeneralException when update statement fail
	 */
	public void increaseCount(int count, ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websitesVisited SET count = ? WHERE id = ?;");
		request.setInt(this.count + count);
		request.setInt(db_id);
		request.set();
		this.count += count;
	}
	
	/**
	 * Delete instance from database
	 * @param ServletManager sm
	 * @throws GeneralException when delete statement fail
	 */
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("DELETE FROM websitesVisited WHERE id = ?;");
		request.setInt(db_id);
		request.set();
	}

	/**
	 * Use this method to compare this instance count with an other WebsiteVisited instance
	 * @param WebsiteVisited w2
	 * @return -1 if this count is greater than w2 count, 0 if equals and 1 else
	 */
	public int compareTo(WebsiteVisited w2) {
		if (count < w2.getCount())
			return 1;
		else if (count == w2.getCount())
			return 0;
		else
			return -1;
	}
}
