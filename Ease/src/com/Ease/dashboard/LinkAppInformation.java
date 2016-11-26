package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class LinkAppInformation extends AppInformation {
	
	public enum LoadData {
		NAME,
		LINK,
		IMG_URL
	}
	
	public static LinkAppInformation createLinkAppInformation(String name, String link, String imgUrl,
			ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		int db_id = db.set("INSERT INTO appInformations (null, '" + name + "');");
		db.set("INSERT INTO linkAppInformations (null, " + db_id + ", '" + link + "', '" + imgUrl + "');");
		db.commitTransaction(transaction);
		return new LinkAppInformation(String.valueOf(db_id), name, link, imgUrl);
	}

	public static LinkAppInformation loadLinkAppInformation(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT name, url, imgUrl FROM appInformations JOIN linkAppInformations ON appsInformations.id = linkAppInformations.app_information_id WHERE linkAppInformations.app_information_id = " + db_id + ";");
		try {
			rs.next();
			return new LinkAppInformation(db_id, rs.getString(LoadData.NAME.ordinal()), rs.getString(LoadData.LINK.ordinal()), rs.getString(LoadData.IMG_URL.ordinal()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	protected String link;
	protected String imgUrl;

	public LinkAppInformation(String db_id, String name, String link, String imgUrl) {
		super(db_id, name);
		this.link = link;
		this.imgUrl = imgUrl;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public void setLink(String link, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE linkAppInformations SET url='" + link + "' WHERE app_information_id = " + this.db_id + "");
		this.link = link;
	}
	
	public String getImgUrl() {
		return this.imgUrl;
	}
	
	public void setImgUrl(String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE linkAppInformations SET img_url='" + imgUrl + "' WHERE app_information_id = " + this.db_id + "");
		this.imgUrl = imgUrl;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		db.set("DELETE FROM linkAppInformations WHERE app_information_id = " + this.db_id + ";");
		super.removeFromDb(sm);
		db.commitTransaction(transaction);
	}
}
