package com.Ease.Context.Catalog;

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

public class Tag {
	
	public enum Data {
		NOTHING,
		ID,
		TAG_NAME,
		COLOR
	}
	
	public static Tag createTag(String tagName, String tagColor, List<Website> tagSites, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int single_id = ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId();
		int transaction = db.startTransaction();
		int db_id = db.set("INSERT INTO tags VALUES (null, '" + tagName + "', '" + tagColor + "');");
		for (Website site : tagSites)
			db.set("INSERT INTO tagsAndSitesMap values(null, " + db_id + ", " + site.getDb_id() + ");");
		db.commitTransaction(transaction);
		return new Tag(String.valueOf(db_id), single_id, tagName, tagColor);
	}
	
	public static List<Tag> loadTags(DataBaseConnection db, ServletContext context) throws GeneralException {
		List<Tag> tags = new LinkedList<Tag>();
		ResultSet rs = db.get("SELECT * FROM tags");
		try {
			while (rs.next()) {
				String db_id = rs.getString(Data.ID.ordinal());
				String name = rs.getString(Data.TAG_NAME.ordinal());
				String color = rs.getString(Data.COLOR.ordinal());
				int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
				tags.add(new Tag(db_id, single_id, name, color));
			}
			return tags;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	protected String db_id;
	protected int single_id;
	protected String name;
	protected String color;
	protected List<Website> sites;
	
	public Tag(String db_id, int single_id, String tagName, String tagColor) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.name = tagName;
		this.color = tagColor;
		this.sites = new LinkedList<Website> ();
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSingleId() {
		return this.single_id;
	}
	
	public String getDbId() {
		return this.db_id;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void setSites(Map<String, Website> sitesDBmap, DataBaseConnection db) throws GeneralException {
		try {
			ResultSet rs = db.get("SELECT website_id FROM tagsAndSitesMap WHERE tag_id=" + this.db_id + ";");
			while (rs.next()) {
				this.sites.add(sitesDBmap.get(rs.getString(1)));
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, "rs fail...");
		}
	}
	
	public List<Website> getWebsites() {
		return this.sites;
	}
}
