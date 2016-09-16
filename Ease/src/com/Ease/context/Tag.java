package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.context.Color.ColorData;

public class Tag {
	enum TagData {
		NOTHING, ID, NAME, COLOR_ID
	}

	protected String name;
	protected String color_id;
	protected String color;
	protected List<Site> sites;
	protected int id;

	public Tag(ResultSet rs, ServletContext context) {
		try {
			id = Integer.parseInt(rs.getString(TagData.ID.ordinal()));
			name = rs.getString(TagData.NAME.ordinal());
			color_id = rs.getString(TagData.COLOR_ID.ordinal());
			setColor(context);
			sites = new LinkedList<Site>();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor_id() {
		return color_id;
	}

	public void setColor_id(String color_id) {
		this.color_id = color_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setColor(ServletContext context) throws SQLException {
		DataBase db = (DataBase) context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT colors.color FROM colors, tags WHERE id=" + color_id + ";");
		rs.next();
		this.color = rs.getString(
				1); /* 1 because there is only one column in response */
	}

	public String getColor() {
		return color;
	}

	public void setSites(ServletContext context) throws SQLException {
		SiteManager siteManager = (SiteManager) context.getAttribute("Sites");
		DataBase db = (DataBase) context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT website_id FROM TagAndSiteMap WHERE tag_id=" + id + ";");
		while (rs.next()) {
			Site tmpSite = siteManager.getSiteById(new Integer(rs.getString(1)));
			addSite(tmpSite);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Tag other = (Tag) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void addSite(Site site) {
		if (!sites.contains(site))
			sites.add(site);
	}

	public boolean containsSite(Site site) {
		return sites.contains(site);
	}

	public List<Site> getSites() {
		return sites;
	}
}
