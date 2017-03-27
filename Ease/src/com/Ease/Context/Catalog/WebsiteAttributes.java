package com.Ease.Context.Catalog;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsiteAttributes {
	
	public static WebsiteAttributes createWebsiteAttributes(DataBaseConnection db) throws GeneralException {
		String db_id = db.prepareRequest("INSERT INTO websiteAttributes values (null, 0, null, default, 1, 1, default, 0);").set().toString();
		return new WebsiteAttributes(db_id, false, true, true, 0, false);
	}
	
	public static WebsiteAttributes loadWebsiteAttributes(String db_id, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM websiteAttributes WHERE id= ?;");
		request.setInt(db_id);
		DatabaseResult rs = request.get();
		rs.next();
		return new WebsiteAttributes(db_id, rs.getBoolean("locked"), rs.getBoolean("new"), rs.getBoolean("work"), rs.getInt("visits"), rs.getBoolean("blacklisted"));
	}
	
	protected String db_id;
	protected boolean locked;
	protected boolean isNew;
	protected boolean work;
	protected int visits;
	protected boolean blacklisted;
	
	public WebsiteAttributes(String db_id, boolean locked, boolean isNew, boolean work, int visits, boolean blacklisted) {
		this.db_id = db_id;
		this.locked = locked;
		this.isNew = isNew;
		this.work = work;
		this.blacklisted = blacklisted;
	}

	public boolean isWorking() {
		return this.work;
	}
	
	public boolean isNew() {
		return this.isNew;
	}
	
	public String getDbId(){
		return db_id;
	}

	public int getVisits() {
		return visits;
	}
	
	public void increaseVisits(int count, ServletManager sm) throws GeneralException {
		if (this.blacklisted)
			return;
		DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET visits = ? WHERE id = ?;");
		request.setInt(visits + count);
		request.setInt(db_id);
		request.set();
		this.visits += count;
	}
	
	public void setVisits(int visits, ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET visits = ? WHERE id = ?;");
		request.setInt(visits);
		request.setInt(db_id);
		request.set();
		this.visits = visits;
		
	}
	
	public void turnOff(ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET work = ? WHERE id = ?");
		request.setBoolean(false);
		request.setInt(db_id);
		request.set();
		this.work = false;
	}
	
	public void turnOn(ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET work = ? WHERE id = ?");
		request.setBoolean(true);
		request.setInt(db_id);
		request.set();
		this.work = true;
	}
	
	public void refresh(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM websiteAttributes WHERE id = ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		if (!rs.next())
			throw new GeneralException(ServletManager.Code.InternError, "Those attributes does not exist");
		this.locked = rs.getBoolean("locked");
		this.isNew = rs.getBoolean("new");
		this.work = rs.getBoolean("work");
		this.visits = rs.getInt("visits");
	}

	public void blacklist(ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET blacklisted = ? WHERE id = ?;");
		request.setBoolean(true);
		request.setInt(db_id);
		request.set();
		this.blacklisted = true;
	}

	public void whitelist(ServletManager sm) throws GeneralException {
		DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET blacklisted = ? WHERE id = ?;");
		request.setBoolean(false);
		request.setInt(db_id);
		request.set();
		this.blacklisted = false;
	}

	public Boolean isBlacklisted() {
		return this.blacklisted;
	}
}
