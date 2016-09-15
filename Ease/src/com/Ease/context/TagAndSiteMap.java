package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

public class TagAndSiteMap {
	enum TagAndSiteData {
		NOTHING, ID, TAG_ID, SITE_ID
	}

	protected int id;
	protected String tag_id;
	protected String site_id;
	protected String tag_name;
	protected String site_name;

	public TagAndSiteMap(ResultSet rs, ServletContext context) {
		try {
			id = Integer.parseInt(rs.getString(TagAndSiteData.ID.ordinal()));
			tag_id = rs.getString(TagAndSiteData.TAG_ID.ordinal());
			site_id = rs.getString(TagAndSiteData.SITE_ID.ordinal());
			setNames(context);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void setNames(ServletContext context) throws SQLException {
		DataBase db = (DataBase) context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT tag_name, website_name FROM tags, websites WHERE tag_id=" + tag_id + " AND website_id=" + site_id + ";");
		rs.next();
		tag_name = rs.getString(1);
		site_name = rs.getString(2);
	}

}
