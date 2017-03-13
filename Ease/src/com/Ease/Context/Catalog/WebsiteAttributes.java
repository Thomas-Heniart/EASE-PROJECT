package com.Ease.Context.Catalog;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsiteAttributes {
	
	public static WebsiteAttributes createWebsiteAttributes(DataBaseConnection db) throws GeneralException {
		String db_id = db.prepareRequest("INSERT INTO websiteAttributes values (null, 0, null, default, 1, 1);").set().toString();
		return new WebsiteAttributes(db_id, false, true, true);
	}
	
	public static WebsiteAttributes loadWebsiteAttributes(String db_id, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM websiteAttributes WHERE id= ?;");
		request.setInt(db_id);
		DatabaseResult rs = request.get();
		rs.next();
		return new WebsiteAttributes(db_id, rs.getBoolean("locked"), rs.getBoolean("new"), rs.getBoolean("work"));
	}
	
	protected String db_id;
	protected boolean locked;
	protected boolean isNew;
	protected boolean work;
	
	public WebsiteAttributes(String db_id, boolean locked, boolean isNew, boolean work) {
		this.db_id = db_id;
		this.locked = locked;
		this.isNew = isNew;
		this.work = work;
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
	}
}
