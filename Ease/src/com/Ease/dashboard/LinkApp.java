package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class LinkApp extends App {
	
	enum LinkAppData {
		NOTHING,
		ID,
		APP_ID,
		LINK,
		IMG_URL
	}
	
	public enum LoadData {
		NAME,
		PROFILE_ID,
		POSITION,
		PERMISSION_ID,
		WORK,
		URL,
		IMG_URL
	}
	
	public static LinkApp createLinkApp(String name, Profile profile, String link, String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Permissions permissions = AppPermissions.loadDefaultAppPermissions(sm);
		int transaction;
		int position = profile.getNextPosition();
		transaction = db.startTransaction();
		int app_id = db.set("INSERT INTO apps values (null, '" + name + "' , " + profile.getDb_id() + ", " + position + ", " + permissions.getDBid() + ", 'LinkApp', 1);");
		db.set("INSERT INTO linkApps values (null, " + app_id + ", '" + link + "', '" + imgUrl + "');");
		db.commitTransaction(transaction);
		return new LinkApp(name, profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), link, imgUrl, true);
	}
	
	protected String link;
	protected String imgUrl;
	
	public LinkApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, String link, String imgUrl, boolean working) {
		this.name = name;
		this.profile = profile;
		this.link = link;
		this.imgUrl = imgUrl;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
		this.working = working;
	}
	
	public LinkApp(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT id, name, profile_id, position, permission_id, work, url, img_url FROM apps JOIN websiteApps ON apps.id = websiteApps.app_id WHERE apps.id = " + db_id + ";");
		try {
			if (rs.next()) {
				this.name = rs.getString(LoadData.NAME.ordinal());
				this.profile = Profile.loadProfile(rs.getString(LoadData.PROFILE_ID.ordinal()), sm);
				this.position = rs.getInt(LoadData.POSITION.ordinal());
				this.permissions = AppPermissions.loadAppPermissions(rs.getString(LoadData.PERMISSION_ID.ordinal()), sm);
				this.working = rs.getBoolean(LoadData.WORK.ordinal());
				this.single_id = sm.getNextSingleId();
				this.link = rs.getString(LoadData.URL.ordinal());
				this.imgUrl = rs.getString(LoadData.IMG_URL.ordinal());
				this.db_id = db_id;
			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void setLink(String link, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE linkApps SET url = '" + link + "';");
		this.link = link;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public void setImgUrl(String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE linkApps SET img_url = '" + imgUrl + "';");
		this.imgUrl = imgUrl;
	}
	
	public String getImgUrl() {
		return this.imgUrl;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM linkApps WHERE id = " + this.getDb_id() + ";");
		super.removeFromDb(sm);
		db.commitTransaction(transaction);
	}
}
