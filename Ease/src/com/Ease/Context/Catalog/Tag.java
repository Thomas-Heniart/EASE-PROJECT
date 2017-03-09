package com.Ease.Context.Catalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class Tag {
	
	public enum Data {
		NOTHING,
		ID,
		TAG_NAME,	
		COLOR,
		PRIORITY
	}
	
	public static Tag createTag(String tagName, String tagColor, List<Website> tagSites, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int single_id = ((IdGenerator)sm.getContextAttr("idGenerator")).getNextId();
		int transaction = db.startTransaction();
		int db_id = db.set("INSERT INTO tags VALUES (null, '" + tagName + "', '" + tagColor + "', 2);");
		for (Website site : tagSites)
			db.set("INSERT INTO tagsAndSitesMap values(null, " + db_id + ", " + site.getDb_id() + ");");
		db.commitTransaction(transaction);
		return new Tag(String.valueOf(db_id), single_id, tagName, tagColor);
	}
	
	public static List<Tag> loadTags(DataBaseConnection db, ServletContext context) throws GeneralException {
		List<Tag> tags = new LinkedList<Tag>();
		ResultSet rs = db.get("SELECT * FROM tags ORDER BY priority");
		try {
			while (rs.next()) {
				String db_id = rs.getString(Data.ID.ordinal());
				String name = rs.getString(Data.TAG_NAME.ordinal());
				String color = rs.getString(Data.COLOR.ordinal());
				int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
				Tag newTag = new Tag(db_id, single_id, name, color); 
				newTag.loadGroupIds(db);
				tags.add(newTag);
			}
			return tags;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void loadGroupIds(DataBaseConnection db) throws GeneralException {
		ResultSet rs = db.get("SELECT group_id FROM tagsAndGroupsMap JOIN groups ON (tagsAndGroupsMap.group_id = groups.id) WHERE tag_id = " + this.db_id + ";");
		try {
			while(rs.next()) {
				String parent_id = rs.getString(1);
				this.groupIds.add(parent_id);
				this.loadSubGroupIds(parent_id, db);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void loadSubGroupIds(String parent_id, DataBaseConnection db) throws GeneralException {
		ResultSet rs = db.get("SELECT id FROM groups WHERE parent = " + parent_id + ";");
		try {
			while(rs.next()) {
				String newParent_id = rs.getString(1);
				this.groupIds.add(newParent_id);
				this.loadSubGroupIds(newParent_id, db);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	protected String db_id;
	protected int single_id;
	protected String name;
	protected String color;
	protected List<Website> sites;
	protected List<String> groupIds;
	
	public Tag(String db_id, int single_id, String tagName, String tagColor) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.name = tagName;
		this.color = tagColor;
		this.sites = new LinkedList<Website> ();
		this.groupIds = new LinkedList<String> ();
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSingleId() {
		return this.single_id;
	}
	
	public void setSingleId(int singleId) {
		this.single_id = singleId;		
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
	
	public JSONArray search(JSONArray result, String search) {
		for (Website site : this.sites) {
			if (site.getName().toUpperCase().startsWith(search.toUpperCase()) && site.work()) {
				result.add(String.valueOf(site.getSingleId()));
			}
		}
		return result;
	}
	
	public boolean containsGroupId(String group_id) {
		return this.groupIds.contains(group_id);
	}
	
	public boolean isPublic() {
		return this.groupIds.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJSON(List<String> colors) {
		JSONObject res = new JSONObject();
		res.put("name", this.name);
		res.put("singleId", this.single_id);
		res.put("color", colors.get(Integer.parseInt(this.color) - 1));
		return res;
	}

	public void addWebsite(Website website, ServletManager sm) throws GeneralException {
		if (this.sites.contains(website))
			throw new GeneralException(ServletManager.Code.ClientWarning, "Tag already set");
		DataBaseConnection db = sm.getDB();
		db.set("INSERT INTO tagsAndSitesMap VALUES (" + this.db_id + ", " + website.getDb_id() + ");");
		this.sites.add(website);
		
	}
	
	public void removeWebsite(Website website, ServletManager sm) throws GeneralException {
		if (!this.sites.contains(website))
			throw new GeneralException(ServletManager.Code.ClientWarning, "No such tag for this site");
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM tagsAndSitesMap WHERE tag_id = " + this.db_id + " AND website_id = " + website.getDb_id() + ";");
		this.sites.remove(website);
	}

	public void refresh(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM tags WHERE id = " + this.db_id + ";");
		try {
			if (!rs.next())
				throw new GeneralException(ServletManager.Code.InternError, "This tag does not exist");
			this.name = rs.getString(Data.TAG_NAME.ordinal());
			this.color = rs.getString(Data.COLOR.ordinal());
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
}
