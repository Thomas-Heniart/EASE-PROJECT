package com.Ease.Context.Catalog;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		LOCKED_EXPIRATION,
		NEW
	}
	
	public static Website getWebsite(int single_id, ServletManager sm) throws GeneralException {
		@SuppressWarnings("unchecked")
		Map<Integer, Website> websitesMap = (Map<Integer, Website>)sm.getContextAttr("websites");
		Website site = websitesMap.get(single_id);
		if (site == null)
			throw new GeneralException(ServletManager.Code.InternError, "This website dosen't exist!");
		return site;
	}
	
	
	public static List<Website> loadWebsites(DataBaseConnection db, ServletContext context) throws GeneralException {
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
				boolean locked = rs.getBoolean(WebsiteData.LOCKED.ordinal());
				String lockedExpiration = rs.getString(WebsiteData.LOCKED_EXPIRATION.ordinal());
				boolean isNew = rs.getBoolean(WebsiteData.NEW.ordinal());
				int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
				websites.add(new Website(db_id, single_id, name, loginUrl, folder, sso, noLogin, website_homepage, hidden, ratio, position, locked, lockedExpiration, isNew, website_informations));
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
	protected boolean locked;
	protected boolean isNew;
	protected String lockedExpiration;
	protected List<WebsiteInformation> website_informations;
	protected List<Website> loginWithWebsites;
	
	public Website(String db_id, int single_id, String name, String loginUrl, String folder, int sso, boolean noLogin, String website_homepage, boolean hidden, int ratio, int position, boolean locked, String lockedExpiration, boolean isNew, List<WebsiteInformation> website_informations) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.loginUrl = loginUrl;
		this.folder = folder;
		this.sso = sso;
		this.noLogin = noLogin;
		this.website_homepage = website_homepage;
		this.hidden = hidden;
		this.ratio = ratio;
		this.locked = locked;
		this.website_informations = website_informations;
		this.name = name;
		this.position = position;
		this.isNew = isNew;
		this.loginWithWebsites = new LinkedList<Website>();
	}
	
	public String getDb_id() {
		return this.db_id;
	}
		
	public List<WebsiteInformation> getInfos() {
		return website_informations;
	}
	
	public Map<String, String> getNeededInfos(ServletManager sm) throws GeneralException {
		Map<String, String> infos = new HashMap<String, String>();
		for (WebsiteInformation info : website_informations) {
			String value = sm.getServletParam(info.getInformationName(), true);
			if (value == null || value.isEmpty()) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong info: " + info.getInformationName() + ".");
			}
			infos.put(info.getInformationName(), value);
		}
		return infos;
	}
	
	public void loadLoginWithWebsites(DataBaseConnection db, Catalog catalog) throws GeneralException {
		ResultSet rs = db.get("SELECT loginWithWebsites.website_id FROM loginWithWebsites JOIN websitesLogWithMap ON loginWithWebsites.id = website_logwith_id WHERE websitesLogWithMap.website_id=" + this.db_id + ";");
		try {
			while (rs.next()) {
				this.loginWithWebsites.add(catalog.getWebsiteWithDBid(rs.getString(1)));
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	public int getSingleId() {
		return this.single_id;
	}
	
	public int getSso() {
		return this.sso;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFolder() {
		return this.folder;
	}
	
	public String getUrl() {
		return this.loginUrl;
	}
	
	public String getHomePageUrl() {
		return this.website_homepage;
	}
	
	public String getLoginWith() {
		String res = "";
		Iterator<Website> it = this.loginWithWebsites.iterator();
		while (it.hasNext()) {
			res += it.next().getSingleId();
			if (it.hasNext())
				res += ",";
		}
		return res;
	}
	
	public List<WebsiteInformation> getInformations() {
		return this.website_informations;
	}
	
	public boolean noLogin() {
		return this.noLogin;
	}
	
	public boolean isHidden() {
		return this.hidden;
	}
	
	public boolean isNew() {
		return this.isNew;
	}
	
	public JSONObject getJSON(ServletManager sm) throws GeneralException{
		JSONParser parser = new JSONParser();
		try {
			JSONObject a = (JSONObject) parser.parse(new FileReader(sm.getRealPath(this.folder + "connect.json")));
			a.put("loginUrl",loginUrl);
			return a;
		} catch (IOException | ParseException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
