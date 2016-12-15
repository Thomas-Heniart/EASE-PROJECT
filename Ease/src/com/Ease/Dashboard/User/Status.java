package com.Ease.Dashboard.User;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Status {
	
	public enum Data {
		NOTHING,
		ID,
		FIRST_CONNECTION,
		CGU,
		CHROME_SCRAPPING,
		CLICK_ON_APP,
		MOVE_APPS,
		OPEN_CATALOG,
		DRAG_AND_DROP,
		TUTO_DONE
	}
	
	public static Status createStatus(DataBaseConnection db) throws GeneralException {
		String db_id = db.set("INSERT INTO status values(null, 0, 0, 0, 0, 0, 0, 0, 0").toString();
		return new Status(db_id, false, false, false, false, false, false, false, false);
	}
	
	public static Status loadStatus(String db_id, DataBaseConnection db) throws GeneralException {
		ResultSet rs = db.get("SELECT * FROM status WHERE id=" + db_id + ";");
		try {
			boolean first_connection = rs.getBoolean(Data.FIRST_CONNECTION.ordinal());
			boolean CGU = rs.getBoolean(Data.CGU.ordinal());
			boolean chrome_scrapping = rs.getBoolean(Data.CHROME_SCRAPPING.ordinal());
			boolean click_on_app = rs.getBoolean(Data.CLICK_ON_APP.ordinal());
			boolean	move_apps = rs.getBoolean(Data.MOVE_APPS.ordinal());
			boolean open_catalog = rs.getBoolean(Data.OPEN_CATALOG.ordinal());
			boolean drag_and_drop = rs.getBoolean(Data.DRAG_AND_DROP.ordinal());
			boolean tuto_done = rs.getBoolean(Data.TUTO_DONE.ordinal());
			return new Status(db_id, first_connection, CGU, chrome_scrapping, click_on_app, move_apps, open_catalog, drag_and_drop, tuto_done);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
	
	protected String db_id;
	protected boolean first_connection;
	protected boolean CGU;
	protected boolean chrome_scrapping;
	protected boolean click_on_app;
	protected boolean move_apps;
	protected boolean open_catalog;
	protected boolean drag_and_drop;
	protected boolean tuto_done;
	
	public Status(String db_id, boolean first_connection, boolean CGU, boolean chrome_scrapping, boolean click_on_app, boolean move_apps, boolean open_catalog, boolean drag_and_drop, boolean tuto_done) {
		this.db_id = db_id;
		this.first_connection = first_connection;
		this.CGU = CGU;
		this.chrome_scrapping = chrome_scrapping;
		this.click_on_app = click_on_app;
		this.move_apps = move_apps;
		this.open_catalog = open_catalog;
		this.drag_and_drop = drag_and_drop;
		this.tuto_done = tuto_done;
	}
	
	public void passStep(String tutoStep, DataBaseConnection db) throws GeneralException {
		Class<?> c = this.getClass();
		try {
			Field f = c.getDeclaredField(tutoStep);
			f.setAccessible(true);
			if ((Boolean) f.get(this))
				return;
			f.set(this, true);
			db.set("UPDATE status SET " + tutoStep + "=1 WHERE id=" + this.db_id + ";");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new GeneralException(ServletManager.Code.ClientError, e);
		}
	}
	
	public boolean tutoIsDone() {
		return this.tuto_done;
	}
}
