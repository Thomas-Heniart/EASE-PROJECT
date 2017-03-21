package com.Ease.Context.Catalog;

import java.util.LinkedList;
import java.util.List;

import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class BlacklistedWebsite {
	
	/**
	 * Load all BlackListedWebsite from database
	 * @param ServletManager sm
	 * @return List<BlacklistedWebsite>
	 * @throws GeneralException when a database request fail
	 */
	public static List<BlacklistedWebsite> loadBlacklistedWebsites(ServletManager sm) throws GeneralException {
		List<BlacklistedWebsite> res = new LinkedList<BlacklistedWebsite>();
		DatabaseResult rs = sm.getDB().prepareRequest("SELECT * FROM blacklistedWebsites;").get();
		while(rs.next()) {
			int single_id = sm.getNextSingle_id();
			res.add(new BlacklistedWebsite(rs.getString(1), single_id, rs.getString(2)));
		}
		return res;
	}
	
	/**
	 * Create a new BlacklistedWebsite in database and return it
	 * @param String url
	 * @param ServletManager sm
	 * @return BlacklistedWebsite
	 * @throws GeneralException when insertion fail
	 */
	public static BlacklistedWebsite createBlacklistedWebsite(String url, ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO blacklistedWebsites values(?, ?);");
		request.setNull();
		request.setString(url);
		String db_id = request.set().toString();
		int single_id = ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId();
		return new BlacklistedWebsite(db_id, single_id, url);
	}
	
	protected String url;
	protected String db_id;
	protected int single_id;
	
	public BlacklistedWebsite(String db_id, int single_id, String url) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.url = url;
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

}
