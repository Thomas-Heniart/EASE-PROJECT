package com.Ease.Context.Catalog;

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

import com.Ease.Context.Variables;
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
		RATIO,
		POSITION,
		WEBSITE_ATTRIBUTES_ID
	}

	public static Website getWebsite(int single_id, ServletManager sm) throws GeneralException {
		@SuppressWarnings("unchecked")
		Map<Integer, Website> websitesMap = (Map<Integer, Website>)sm.getContextAttr("websites");
		Website site = websitesMap.get(single_id);
		if (site == null)
			throw new GeneralException(ServletManager.Code.InternError, "This website dosen't exist!");
		return site;
	}

	public static Website createWebsite(String url, String name, String homePage, String folder, boolean haveLoginButton, String[] haveLoginWith, Catalog catalog, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM websites WHERE folder = '"+ folder+"' OR website_name='"+name+"';");
		try {
			if (rs.next()){
				throw new GeneralException(ServletManager.Code.UserMiss, "This website already exists");
			}
			int transaction  = db.startTransaction();
			WebsiteAttributes attributes = WebsiteAttributes.createWebsiteAttributes(db);

			String db_id = db.set("INSERT INTO websites VALUES (null, '"+ url +"', '"+ name +"', '" + folder + "', NULL, 0, '"+ homePage +"', 0, 1, "+ attributes.getDbId() +");").toString();
			WebsiteInformation loginInfo = WebsiteInformation.createInformation(db_id, "login", "text", db);
			List<WebsiteInformation> infos = new LinkedList<WebsiteInformation>();
			infos.add(loginInfo);

			if(haveLoginButton){
				db.set("INSERT INTO loginWithWebsites VALUES (null, "+ db_id +");");
			}

			List<Website> loginWithWebsites = new LinkedList<Website>();
			if(haveLoginWith != null){
				for(int i=0;i<haveLoginWith.length;i++){
					ResultSet rs2 = db.get("SELECT id FROM loginWithWebsites WHERE website_id = "+haveLoginWith[i]+";");
					if(rs2.next()){
						String id = db.set("INSERT INTO websitesLogWithMap VALUES (null, "+db_id+", "+rs2.getString(1)+");").toString();
					}
					loginWithWebsites.add(catalog.getWebsiteWithDBid(haveLoginWith[i]));
				}
			}
			IdGenerator idGenerator = (IdGenerator)sm.getContextAttr("idGenerator");
			db.commitTransaction(transaction);
			return new Website(db_id, idGenerator.getNextId(), name, url, folder, null, false, homePage, 0, 1, infos, attributes, loginWithWebsites);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}


	public static List<Website> loadWebsites(DataBaseConnection db, Map<String, Sso> ssoDbIdMap, ServletContext context) throws GeneralException {
		try {
			List<Website> websites = new LinkedList<Website>();
			ResultSet rs = db.get("SELECT * FROM websites");
			while (rs.next()) {
				String db_id = rs.getString(WebsiteData.ID.ordinal());
				List<WebsiteInformation> website_informations = WebsiteInformation.loadInformations(db_id, db);
				String loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
				String name = rs.getString(WebsiteData.NAME.ordinal());
				String folder = rs.getString(WebsiteData.FOLDER.ordinal());
				Sso sso = ssoDbIdMap.get(rs.getString(WebsiteData.SSO.ordinal()));
				boolean noLogin = rs.getBoolean(WebsiteData.NO_LOGIN.ordinal());
				String website_homepage = rs.getString(WebsiteData.WEBSITE_HOMEPAGE.ordinal());
				int ratio = rs.getInt(WebsiteData.RATIO.ordinal());
				int position = rs.getInt(WebsiteData.POSITION.ordinal());
				String websiteAttributesId = rs.getString(WebsiteData.WEBSITE_ATTRIBUTES_ID.ordinal());
				WebsiteAttributes websiteAttributes = null;
				if (websiteAttributesId != null)
					websiteAttributes = WebsiteAttributes.loadWebsiteAttributes(websiteAttributesId, db);
				int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
				Website site = new Website(db_id, single_id, name, loginUrl, folder, sso, noLogin, website_homepage, ratio, position, website_informations, websiteAttributes);
				websites.add(site);
				sso.addWebsite(site);
			}
			return websites;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	///// Check if website exist when scrapp

	public static JSONArray existsInDb(String websiteHost, ServletManager sm) throws GeneralException{
		DataBaseConnection db = sm.getDB();
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		ResultSet rs = db.get("select * from websites where noLogin=0;");
		JSONArray result = new JSONArray();
		try {
			while(rs.next()){
				String loginUrl = rs.getString(WebsiteData.LOGIN_URL.ordinal());
				websiteHost = websiteHost.toLowerCase();
				loginUrl = loginUrl.toLowerCase();
				if(loginUrl.contains(websiteHost)){
					result.add(String.valueOf((catalog.getWebsiteWithDBid(rs.getString(WebsiteData.ID.ordinal())).getSingleId())));
				}
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return result;
	}

	public static String existsInDbFacebook(String appName, ServletManager sm) throws GeneralException{
		DataBaseConnection db = sm.getDB();
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		ResultSet rs = db.get("select * from websites where id in (select website_id from websitesLogWithMap where website_logwith_id in (select id from loginWithWebsites where website_id in (select id from websites where website_name='Facebook')));");
		try {
			while(rs.next()){
				String name = rs.getString(WebsiteData.NAME.ordinal());
				appName = appName.toLowerCase();
				name = name.toLowerCase();
				if(appName.contains(name)){
					return String.valueOf((catalog.getWebsiteWithDBid(rs.getString(WebsiteData.ID.ordinal())).getSingleId()));
				}
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}

	public static String existsInDbLinkedin(String appName, ServletManager sm) throws GeneralException{
		DataBaseConnection db = sm.getDB();
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		ResultSet rs = db.get("select * from websites where id in (select website_id from websitesLogWithMap where website_logwith_id in (select id from loginWithWebsites where website_id in (select id from websites where website_name='Linkedin')));");
		try {
			while(rs.next()){
				String name = rs.getString(WebsiteData.NAME.ordinal());
				appName = appName.toLowerCase();
				name = name.toLowerCase();
				if(appName.contains(name)){
					return String.valueOf((catalog.getWebsiteWithDBid(rs.getString(WebsiteData.ID.ordinal())).getSingleId()));
				}
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}

	//// -------

	protected String db_id;
	protected int single_id;
	protected String name;
	protected String loginUrl;
	protected String folder;
	protected int position;
	protected Sso sso;
	protected boolean noLogin;
	protected String website_homepage;
	protected int ratio;
	protected WebsiteAttributes websiteAttributes;
	protected List<WebsiteInformation> website_informations;
	protected List<Website> loginWithWebsites;

	public Website(String db_id, int single_id, String name, String loginUrl, String folder, Sso sso, boolean noLogin, String website_homepage, int ratio, int position, List<WebsiteInformation> website_informations, WebsiteAttributes websiteAttributes) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.loginUrl = loginUrl;
		this.folder = folder;
		this.sso = sso;
		this.noLogin = noLogin;
		this.website_homepage = website_homepage;
		this.ratio = ratio;
		this.website_informations = website_informations;
		this.name = name;
		this.position = position;
		this.loginWithWebsites = new LinkedList<Website>();
		this.websiteAttributes = websiteAttributes;
	}

	public Website(String db_id, int single_id, String name, String loginUrl, String folder, Sso sso, boolean noLogin, String website_homepage, int ratio, int position, List<WebsiteInformation> website_informations, WebsiteAttributes websiteAttributes, List<Website> loginWithWebsites) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.loginUrl = loginUrl;
		this.folder = folder;
		this.sso = sso;
		this.noLogin = noLogin;
		this.website_homepage = website_homepage;
		this.ratio = ratio;
		this.website_informations = website_informations;
		this.name = name;
		this.position = position;
		this.loginWithWebsites = loginWithWebsites;
		this.websiteAttributes = websiteAttributes;
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

	public Sso getSso() {
		return this.sso;
	}

	public String getName() {
		return this.name;
	}

	public String getFolder() {
		return Variables.WEBSITES_PATH + this.folder +"/";
	}

	public String getAbsolutePath(){
		return Variables.PROJECT_PATH + this.getFolder();
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

	public boolean isInPublicCatalog() {
		return this.websiteAttributes != null;
	}

	public boolean noLogin() {
		return this.noLogin;
	}

	public boolean work() {
		return this.websiteAttributes.isWorking();
	}

	public boolean isNew() {
		return this.websiteAttributes.isNew();
	}

	public JSONObject getJSON(ServletManager sm) throws GeneralException{
		JSONParser parser = new JSONParser();
		try {
			JSONObject a = (JSONObject) parser.parse(new FileReader(this.getAbsolutePath() + "connect.json"));
			a.put("loginUrl",loginUrl);
			a.put("website_name", this.name);
			a.put("siteSrc", this.getFolder());
			return a;
		} catch (IOException | ParseException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	public boolean loginUrlMatch(String url) {
		String[] loginUrlSplitted = this.loginUrl.split("\\/*\\/");
		return url.contains(loginUrlSplitted[1]);
	}
}
