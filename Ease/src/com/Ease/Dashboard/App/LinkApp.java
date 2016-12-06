package com.Ease.Dashboard.App;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Context.Group.GroupLinkApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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
	

	public static LinkApp createPersonnalLinkApp(String name, Profile profile, String link, String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction;
		int position = profile.getApps().size();
		transaction = db.startTransaction();
		AppInformation informations = AppInformation.createAppInformation(name, sm);
		LinkAppInformation link_app_informations = LinkAppInformation.createLinkAppInformation(link, imgUrl, sm);
		int app_id = db.set("INSERT INTO apps values (null, " + profile.getDBid() + ", " + position + ", default, null, 'LinkApp', 1, " + informations.getDb_id() + ", null);");
		db.set("INSERT INTO linkApps values (null, " + app_id + ", " + link_app_informations.getDb_id() + ", null);");
		db.commitTransaction(transaction);
		return new LinkApp(profile, position, sm.getNextSingleId(), String.valueOf(app_id), true, informations, link_app_informations, null);
	}
	
	public static LinkApp createLinkAppWithGroup(Profile profile, String name, String link, String imgUrl, GroupLinkApp groupLinkApp, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		AppInformation appInfo;
		LinkAppInformation linkAppInfo;
		if (groupLinkApp.isCommon()) {
			appInfo = groupLinkApp.getAppInfo();
			linkAppInfo = groupLinkApp.getLinkAppInfo();
		} else {
			appInfo = AppInformation.createAppInformation(name, sm);
			linkAppInfo = LinkAppInformation.createLinkAppInformation(link, imgUrl, sm);
		}
		int transaction = db.startTransaction();
		int position = profile.getApps().size();
		String db_id = App.insertNewAppInDb(profile, position, "LinkApp", appInfo, groupLinkApp, sm);
		db.set("INSERT INTO linkApps values (null, " + db_id + ", " + linkAppInfo.getDb_id() + ", " + groupLinkApp.getDb_id() + ");");
		db.commitTransaction(transaction);
		return new LinkApp(profile, position, sm.getNextSingleId(), db_id, true, appInfo, linkAppInfo, groupLinkApp);	
	}
	
	public String createLinkAppWithGroupForUnconnected(String profile_id, String name, String link, String imgUrl, GroupLinkApp groupLinkApp, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String linkAppInfo_id;
		String appInfo_id;
		if (groupLinkApp.isCommon()) {
			appInfo_id = groupLinkApp.getAppInfo().getDb_id();
			linkAppInfo_id = groupLinkApp.getLinkAppInfo().getDb_id();
		} else {
			appInfo_id = AppInformation.createAppInformationForUnconnected(name, sm);
			linkAppInfo_id = LinkAppInformation.createLinkAppInformationForUnconnected(link, imgUrl, sm);
		}
		int transaction = db.startTransaction();
		int position = profile.getApps().size();
		String db_id = App.insertNewAppInDb(profile_id, position, "LinkApp", appInfo_id, groupLinkApp, sm);
		db.set("INSERT INTO linkApps values (null, " + db_id + ", " + linkAppInfo_id + ", " + groupLinkApp.getDb_id() + ");");
		db.commitTransaction(transaction);
		return db_id;	
	}

	protected LinkAppInformation link_app_informations;
	
	public LinkApp(Profile profile, int position, int single_id, String db_id, boolean working, AppInformation informations, LinkAppInformation link_app_informations, GroupLinkApp groupLinkApp) {
		this.profile = profile;
		this.position = position;
		this.single_id = single_id;
		this.db_id = db_id;
		this.working = working;
		this.informations = informations;
		this.link_app_informations = link_app_informations;
		this.groupApp = groupLinkApp;
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
					GroupLinkApp.load
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
