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
		NOTHING,
		POSITION,
		WORK,
		INFO_ID,
		GROUP_ID
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
		return new LinkApp(profile, permissions, position, sm.getNextSingleId(), String.valueOf(app_id), link, imgUrl, true);
	}
	
	protected String link;
	protected String imgUrl;
	
	public LinkApp(Profile profile, Permissions permissions, int position, int single_id, String db_id, String link, String imgUrl, boolean working) {
		this.profile = profile;
		this.link = link;
		this.imgUrl = imgUrl;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
		this.working = working;
	}
	
	public LinkApp(String db_id, Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT position, work, link_app_info_id, group_link_app_id FROM apps JOIN linkApps ON apps.id = linkApps.app_id WHERE apps.id = " + db_id + ";");
		try {
			if (rs.next()) {
				this.profile = profile;
				this.position = rs.getInt(LoadData.POSITION.ordinal());
				this.working = rs.getBoolean(LoadData.WORK.ordinal());
				this.single_id = sm.getNextSingleId();
				this.db_id = db_id;
				String group_link_app_id = rs.getString(LoadData.GROUP_ID.ordinal());
				if (!(group_link_app_id == null || group_link_app_id.equals("null"))) {
					ResultSet rs2 = db.get("SELECT permission_id, common FROM groupApps JOIN groupLinkApps ON groupApps.id = groupLinkApps.group_app_id WHERE groupLinkApps.id = " + group_link_app_id + ";");
					rs2.next();
					if(rs2.getBoolean(2))
						this.permissions = AppPermissions.loadCommonAppPermissions(sm);
					else
						this.permissions = AppPermissions.loadAppPermissions(rs2.getString(1), sm);
				} else {
					this.permissions = AppPermissions.loadPersonnalAppPermissions(sm);
				}
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
