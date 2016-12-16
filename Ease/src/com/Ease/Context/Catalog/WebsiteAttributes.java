package com.Ease.Context.Catalog;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsiteAttributes {
	
	public static WebsiteAttributes createWebsiteAttributes(DataBaseConnection db) throws GeneralException {
		String db_id = db.set("INSERT INTO websiteAttributes values (null, 0, null, 1, 1").toString();
		return new WebsiteAttributes(db_id, false, true, true);
	}
	
	public static WebsiteAttributes loadWebsiteAttributes(String db_id, DataBaseConnection db) throws GeneralException {
		try {
			ResultSet rs = db.get("SELECT * FROM websiteAttributes WHERE id=" + db_id + ";");
			rs.next();
			return new WebsiteAttributes(db_id, rs.getBoolean(rs.findColumn("locked")), rs.getBoolean(rs.findColumn("new")), rs.getBoolean(rs.findColumn("work")));
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
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
}
