package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Website {
	public enum WebsiteData {
		NOTHING,
		ID,
		LOGIN_URL,
		NAME,
		FOLDER,
		SSO,
		NO_LOGIN,
		WEBSITE_HOMEPAGE,
		HIDDEN,
		RATIO,
		POSITION,
		INSERT_DATE,
		LOCKED,
		LOCKED_EXPIRATION
	}
	
	public static Website loadWebsite(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		ResultSet rs = db.get("SELECT * FROM websites WHERE id=" + db_id + ";");
		List<WebsiteInformation> website_informations = WebsiteInformation.loadInformations(db_id, sm);
		try {
			if (rs.next()) {
				String loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
				String name = rs.getString(WebsiteData.NAME.ordinal());
				String folder = rs.getString(WebsiteData.FOLDER.ordinal());
				int sso = rs.getInt(WebsiteData.SSO.ordinal());
				boolean noLogin = rs.getBoolean(WebsiteData.NO_LOGIN.ordinal());
				String website_homepage = rs.getString(WebsiteData.WEBSITE_HOMEPAGE.ordinal());
				boolean hidden = rs.getBoolean(WebsiteData.HIDDEN.ordinal());
				int ratio = rs.getInt(WebsiteData.RATIO.ordinal());
				int position = rs.getInt(WebsiteData.POSITION.ordinal());
				String insertDate = rs.getString(WebsiteData.INSERT_DATE.ordinal());
				boolean locked = rs.getBoolean(WebsiteData.LOCKED.ordinal());
				String lockedExpiration = rs.getString(WebsiteData.LOCKED_EXPIRATION.ordinal());
				db.commitTransaction(transaction);
				return new Website(db_id, sm.getNextSingleId(), name, loginUrl, folder, sso, noLogin, website_homepage, hidden, ratio, position, insertDate, locked, lockedExpiration, website_informations);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}
	
	protected String db_id;
	protected int single_id;
	protected String name;
	protected String loginUrl;
	protected String folder;
	protected int position;
	protected int sso;
	protected boolean noLogin;
	protected String website_homepage;
	protected boolean hidden;
	protected int ratio;
	protected String insertDate;
	protected boolean locked;
	protected String lockedExpiration;
	protected List<WebsiteInformation> website_informations;
	
	public Website(String db_id, int single_id, String name, String loginUrl, String folder, int sso, boolean noLogin, String website_homepage, boolean hidden, int ratio, int position, String insertDate, boolean locked, String lockedExpiration, List<WebsiteInformation> website_informations) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.loginUrl = loginUrl;
		this.folder = folder;
		this.sso = sso;
		this.noLogin = noLogin;
		this.website_homepage = website_homepage;
		this.hidden = hidden;
		this.ratio = ratio;
		this.insertDate = insertDate;
		this.locked = locked;
		this.website_informations = website_informations;
		this.name = name;
		this.position = position;
	}
	
	public String getDb_id() {
		return this.db_id;
	}
}
