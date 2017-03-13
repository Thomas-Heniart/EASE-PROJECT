package com.Ease.Context.Catalog;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
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
		DatabaseRequest request = db.prepareRequest("INSERT INTO tags VALUES (null, ?, ?, 2);");
		request.setString(tagName);
		request.setString(tagColor);
		int db_id = request.set();
		for (Website site : tagSites) {
			request = db.prepareRequest("INSERT INTO tagsAndSitesMap values(null, ?, ?);");
			request.setInt(db_id);
			request.setInt(site.getDb_id());
			request.set();
		}
		db.commitTransaction(transaction);
		return new Tag(String.valueOf(db_id), single_id, tagName, tagColor);
	}
	
	public static List<Tag> loadTags(DataBaseConnection db, ServletContext context) throws GeneralException {
		List<Tag> tags = new LinkedList<Tag>();
		DatabaseResult rs = db.prepareRequest("SELECT * FROM tags ORDER BY priority").get();
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
	}
	
	public void loadGroupIds(DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT group_id FROM tagsAndGroupsMap JOIN groups ON (tagsAndGroupsMap.group_id = groups.id) WHERE tag_id = ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		while(rs.next()) {
			String parent_id = rs.getString(1);
			this.groupIds.add(parent_id);
			this.loadSubGroupIds(parent_id, db);
		}
	}
	
	public void loadSubGroupIds(String parent_id, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT id FROM groups WHERE parent = ?;");
		request.setInt(parent_id);
		DatabaseResult rs = request.get();
		while(rs.next()) {
			String newParent_id = rs.getString(1);
			this.groupIds.add(newParent_id);
			this.loadSubGroupIds(newParent_id, db);
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
		DatabaseRequest request = db.prepareRequest("SELECT website_id FROM tagsAndSitesMap WHERE tag_id= ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		while (rs.next())
			this.sites.add(sitesDBmap.get(rs.getString(1)));
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
		DatabaseRequest request = db.prepareRequest("INSERT INTO tagsAndSitesMap VALUES (?, ?);");
		request.setInt(db_id);
		request.setInt(website.getDb_id());
		request.set();
		this.sites.add(website);
		
	}
	
	public void removeWebsite(Website website, ServletManager sm) throws GeneralException {
		if (!this.sites.contains(website))
			throw new GeneralException(ServletManager.Code.ClientWarning, "No such tag for this site");
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("DELETE FROM tagsAndSitesMap WHERE tag_id = ? AND website_id = ?;");
		request.setInt(db_id);
		request.setInt(website.getDb_id());
		request.set();
		this.sites.remove(website);
	}

	public void refresh(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM tags WHERE id = ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		if (!rs.next())
			throw new GeneralException(ServletManager.Code.InternError, "This tag does not exist");
		this.name = rs.getString(Data.TAG_NAME.ordinal());
		this.color = rs.getString(Data.COLOR.ordinal());
	}
}
