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
		APP_INFO_ID,
		LINK_APP_INFO_ID,
		GROUP_ID
	}
	

	public static LinkApp createLinkApp(String name, Profile profile, String link, String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction;
		int position = profile.getNextPosition();
		transaction = db.startTransaction();
		AppInformation informations = AppInformation.createAppInformation(name, sm);
		LinkAppInformation link_app_informations = LinkAppInformation.createLinkAppInformation(link, imgUrl, sm);
		int app_id = db.set("INSERT INTO apps values (null, " + profile.getDb_id() + ", " + position + ", default, null, 'LinkApp', 1, " + informations.getDb_id() + ", null);");
		db.set("INSERT INTO linkApps values (null, " + app_id + ", " + link_app_informations.getDb_id() + ", null);");
		db.commitTransaction(transaction);
		return new LinkApp(profile, position, sm.getNextSingleId(), String.valueOf(app_id), true, informations, link_app_informations);
	}

	protected LinkAppInformation link_app_informations;
	
	public LinkApp(Profile profile, int position, int single_id, String db_id, boolean working, AppInformation informations, LinkAppInformation link_app_informations) {
		this.profile = profile;
		this.position = position;
		this.single_id = single_id;
		this.db_id = db_id;
		this.working = working;
		this.informations = informations;
		this.link_app_informations = link_app_informations;
	}
	
	public LinkApp(String db_id, Profile profile, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT position, work, app_info_id, link_app_info_id, group_link_app_id FROM apps JOIN linkApps ON apps.id = linkApps.app_id WHERE apps.id = " + db_id + ";");
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
				} else {
				}
				String app_info_id = rs.getString(LoadData.APP_INFO_ID.ordinal());
				this.informations = AppInformation.loadAppInformation(app_info_id, sm);
				String link_app_info_id = rs.getString(LoadData.LINK_APP_INFO_ID.ordinal());
				this.link_app_informations = LinkAppInformation.loadLinkAppInformation(link_app_info_id, sm);
			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void setLink(String link, ServletManager sm) throws GeneralException {
		this.link_app_informations.setLink(link, sm);
	}
	
	public String getLink() {
		return this.link_app_informations.getLink();
	}
	
	public void setImgUrl(String imgUrl, ServletManager sm) throws GeneralException {
		this.link_app_informations.setImgUrl(imgUrl, sm);
	}
	
	public String getImgUrl() {
		return this.link_app_informations.getImgUrl();
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		this.informations.removeFromDb(sm);
		db.set("DELETE FROM linkApps WHERE id = " + this.getDb_id() + ";");
		super.removeFromDb(sm);
		db.commitTransaction(transaction);
	}
}
