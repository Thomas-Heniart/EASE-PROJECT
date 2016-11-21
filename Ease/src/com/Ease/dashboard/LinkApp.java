package com.Ease.dashboard;

import com.Ease.session.User;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class LinkApp extends App {
	public static LinkApp createLinkApp(String name, Profile profile, String link, String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Permissions permissions = AppPermissions.loadDefaultAppPermissions(sm);
		int transaction;
		int position = profile.getNextPosition();
		transaction = db.startTransaction();
		Integer app_id = db.set("INSERT INTO apps values (null, '" + name + "' , " + profile.getDb_id() + ", " + position + ", " + permissions.getDBid() + ", 'LinkApp', 1);");
		db.set("INSERT INTO linkApps values (null, " + app_id + ", '" + link + "', '" + imgUrl + "');");
		db.commitTransaction(transaction);
		return new LinkApp(name, profile, permissions, position, sm.getNextAppId(), app_id, link, imgUrl);
	}
	
	protected String link;
	protected String imgUrl;
	
	public LinkApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, String link, String imgUrl) {
		this.name = name;
		this.profile = profile;
		this.link = link;
		this.imgUrl = imgUrl;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
	}
	
	public void setLink(String link, ServletManager sm) throws GeneralException {
		//Todo db
		this.link = link;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public void setImgUrl(String imgUrl, ServletManager sm) throws GeneralException {
		//Todo db
		this.imgUrl = imgUrl;
	}
	
	public String getImgUrl() {
		return this.imgUrl;
	}
}
