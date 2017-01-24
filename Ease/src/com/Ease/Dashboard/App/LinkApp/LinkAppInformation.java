package com.Ease.Dashboard.App.LinkApp;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class LinkAppInformation {
	
	public enum LoadData {
		NOTHING,
		ID,
		LINK,
		IMG_URL
	}
	
	/*
	 * 
	 * Loader And Creator
	 * 
	 */
	
	public static LinkAppInformation createLinkAppInformation(String link, String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO linkAppInformations values (NULL, '" + link + "', '" + imgUrl + "');");
		return new LinkAppInformation(String.valueOf(db_id), link, imgUrl);
	}

	public static String createLinkAppInformationForUnconnected(String link, String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO linkAppInformations values (NULL, '" + link + "', '" + imgUrl + "');"); 
		return String.valueOf(db_id);
	}
	
	public static LinkAppInformation loadLinkAppInformation(String db_id, DataBaseConnection db) throws GeneralException {
		ResultSet rs = db.get("SELECT * FROM linkAppInformations WHERE id = " + db_id + ";");
		try {
			rs.next();
			return new LinkAppInformation(db_id, rs.getString(LoadData.LINK.ordinal()), rs.getString(LoadData.IMG_URL.ordinal()));
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	/*
	 * 
	 * Constructor
	 * 
	 */
	
	protected String db_id;
	protected String link;
	protected String imgUrl;

	public LinkAppInformation(String db_id, String link, String imgUrl) {
		this.db_id = db_id;
		this.link = link;
		this.imgUrl = imgUrl;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM linkAppInformations WHERE id = " + this.db_id + ";");
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */
	
	public String getDb_id() {
		return this.db_id;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public void setLink(String link, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE linkAppInformations SET url='" + link + "' WHERE id = " + this.db_id + "");
		this.link = link;
	}
	
	public String getImgUrl() {
		return this.imgUrl;
	}
	
	public void setImgUrl(String imgUrl, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE linkAppInformations SET img_url='" + imgUrl + "' WHERE id = " + this.db_id + "");
		this.imgUrl = imgUrl;
	}
}
