package com.Ease.Dashboard.App;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

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
	
	public static Website getWebsite(int single_id, ServletManager sm) throws GeneralException {
		@SuppressWarnings("unchecked")
		Map<Integer, Website> websitesMap = (Map<Integer, Website>)sm.getContextAttr("websites");
		Website site = websitesMap.get(single_id);
		if (site == null)
			throw new GeneralException(ServletManager.Code.InternError, "This website dosen't exist!");
		return site;
	}
	
	
	public static List<Website> loadWebsite(DataBaseConnection db, ServletContext context) throws GeneralException {
		try {
			List<Website> websites = new LinkedList<Website>();
			ResultSet rs = db.get("SELECT * FROM websites;");
			while (rs.next()) {
				String db_id = rs.getString(WebsiteData.ID.ordinal());
				List<WebsiteInformation> website_informations = WebsiteInformation.loadInformations(db_id, db);
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
				int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
				websites.add(new Website(db_id, single_id, name, loginUrl, folder, sso, noLogin, website_homepage, hidden, ratio, position, insertDate, locked, lockedExpiration, website_informations));
			}
			return websites;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
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
