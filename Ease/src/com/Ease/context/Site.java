package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Site {
	enum SiteData {
		NOTHING, ID, URL, NAME, FOLDER, HAVELOGINBUTTON, HAVELOGINWITH
	}

	protected String id;
	protected String name;
	protected String url;
	protected String folder;
	protected boolean haveLoginButton;
	protected String[] haveLoginWith;
	protected List<Tag> tags;

	public Site(ResultSet rs) {
		try {
			tags = new LinkedList<Tag>();
			id = rs.getString(SiteData.ID.ordinal());
			name = rs.getString(SiteData.NAME.ordinal());
			url = rs.getString(SiteData.URL.ordinal());
			folder = rs.getString(SiteData.FOLDER.ordinal());
			haveLoginButton = (rs.getString(SiteData.HAVELOGINBUTTON.ordinal()).equals("1")) ? true : false;
			String tmp = rs.getString(SiteData.HAVELOGINWITH.ordinal());
			haveLoginWith = (tmp != null) ? tmp.split(",") : "".split(",");
		} catch (SQLException e) {

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

	public String getFolder() {
		return folder;
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

	public String getLoginWith() {
		String ret = "";
		for (int i = 0; i < haveLoginWith.length; ++i) {
			ret += haveLoginWith[i];
			if (i < haveLoginWith.length - 1)
				ret += ",";
		}
		return ret;
	}

	public JSONArray getJson() {
		JSONArray res = new JSONArray();
		res.add(id);
		res.add(folder);
		res.add(this.getLoginWith());
		res.add(name);
		return res;
	}

	public boolean hasTags(List<Tag> tags) {
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
				return selectedTags.containsAll(tags);
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
}
