package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;

public class Site implements Comparable<Site> {
	enum SiteData {
		NOTHING, 
		ID, 
		URL, 
		NAME, 
		FOLDER, 
		HAVELOGINBUTTON, 
		HAVELOGINWITH, 
		SSO, 
		NOLOGIN, 
		HOMEPAGE, 
		HIDDEN, 
		RATIO, 
		POSITION, 
		INSERTDATE
	}

	protected String id;
	protected String name;
	protected String url;
	protected String folder;
	protected String homePageUrl;
	protected boolean haveLoginButton;
	protected String[] haveLoginWith;
	protected String sso;
	protected boolean noLogin;
	protected boolean hidden;
	protected int ratio;
	protected String position;
	protected List<Tag> tags;
	protected List<SiteInformation> informations; //String one = info_name And String two = info_type
	protected boolean newSite;
	
	public Site(ResultSet rs, DataBase db) {
		setUpFromRs(rs, db);
	}
		
	public Site(ResultSet rs, DataBase db, boolean isNew) {
		setUpFromRs(rs, db);
		newSite = true;
	}
	
	public void setUpFromRs(ResultSet rs, DataBase db) {
		try {
			tags = new LinkedList<Tag>();
			id = rs.getString(SiteData.ID.ordinal());
			name = rs.getString(SiteData.NAME.ordinal());
			url = rs.getString(SiteData.URL.ordinal());
			homePageUrl = rs.getString(SiteData.HOMEPAGE.ordinal());
			folder = rs.getString(SiteData.FOLDER.ordinal());;
			haveLoginButton = (rs.getString(SiteData.HAVELOGINBUTTON.ordinal()).equals("1")) ? true : false;
			String tmp = rs.getString(SiteData.HAVELOGINWITH.ordinal());
			haveLoginWith = (tmp != null) ? tmp.split(",") : "".split(",");
			sso = rs.getString(SiteData.SSO.ordinal());
			noLogin = (rs.getString(SiteData.NOLOGIN.ordinal()).equals("1")) ? true : false;
			hidden = (rs.getString(SiteData.HIDDEN.ordinal()).equals("1")) ? true : false;
			ratio = Integer.parseInt(rs.getString(SiteData.RATIO.ordinal()));
			position = rs.getString(SiteData.POSITION.ordinal());
			informations = new LinkedList<SiteInformation>();
			newSite = false;
			ResultSet informationsRs = db.get("SELECT information_name, information_type FROM websitesInformations WHERE website_id = " + this.id + ";");
			while (informationsRs.next())
				informations.add(new SiteInformation(informationsRs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setTags(ServletContext context) throws SQLException {
		DataBase db = (DataBase) context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT tag_id FROM TagAndSiteMap WHERE website_id=" + id.toString() + ";");
		SiteManager siteManager = ((SiteManager) context.getAttribute("siteManager"));
		tags = new LinkedList<Tag>();
		while (rs.next()) {
			tags.add(siteManager.getTagById(Integer.parseInt(rs.getString(1))));
		}
	}

	public List<Tag> getTags() {
		return tags;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getHomePageUrl() {
		return homePageUrl;
	}
	
	public String getFolder() {
		return folder;
	}
	
	public String getSso(){
		return sso;
	}

	public List<SiteInformation> getInformations() {
		return informations;
	}
	
	public boolean haveLoginButton() {
		return haveLoginButton;
	}

	public boolean haveLoginWith(Site site) {
		for (int i = 0; i < haveLoginWith.length; ++i) {
			if (haveLoginWith[i].equals(site.getId()))
				return true;
		}
		return false;
	}
	
	public boolean noLogin(){
		return noLogin;
	}
	
	public boolean isHidden(){
		return hidden;
	}
	
	public String getPosition() {
		return position;
	}

	public String getLoginWith() {
		String ret = "";
		for (int i = 0; i < haveLoginWith.length; ++i) {
			ret += haveLoginWith[i];
			if (i < haveLoginWith.length - 1)
				ret += ",";
		}
		return ret;
	}
	
	public boolean isNew() {
		return this.newSite;
	}

	public JSONArray getJson() {
		JSONArray res = new JSONArray();
		res.add(id);
		res.add(folder);
		res.add(this.getLoginWith());
		res.add(name);
		res.add(sso);
		return res;
	}

	public boolean hasTags(List<Tag> tags) {
		if (tags.isEmpty())
			return false;
		for (int i = 0; i < tags.size(); i++)
			if (this.tags.contains(tags.get(i)))
				return true;
		return false;
	}

	public boolean hasAllTags(List<Tag> selectedTags) {
		if (tags.isEmpty())
			return false;
		else {
			if (selectedTags.size() > tags.size()) {
				return false;
			} else {
				return tags.containsAll(selectedTags);
			}
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void increaseRatio() {
		this.ratio++;
	}
	
	public void decreaseRatio() {
		this.ratio--;
	}
	
	public void increasePosition() {
		String newPosition = Integer.toString(Integer.parseInt(position) + 1);
		position = newPosition;
	}
	
	public void beFirst() {
		this.position = "1";
	}

	@Override
	public int compareTo(Site o) {
		String tmpPos = this.position;
		this.position = o.position;
		o.position = tmpPos;
		if (o.isNew())
			return 1;
		if (this.isNew())
			return -1;
		if (this.ratio <= o.ratio) 
			return 1;
		return -1;
	}
	
	@Override
	public String toString() {
		return this.id + " : " + this.ratio;
		
	}
}
