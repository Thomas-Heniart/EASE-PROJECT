package com.Ease.Dashboard.App;

import java.util.LinkedList;
import java.util.List;

import com.Ease.Context.Catalog.Website;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Tag {
	
	public static Tag createTag(String tagName, String tagColor, List<Website> tagSites, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		int db_id = db.set("INSERT INTO tags VALUES (null, '" + tagName + "', '" + tagColor + "');");
		for (Website site : tagSites)
			db.set("INSERT INTO tagsAndSitesMap values(null, " + db_id + ", " + site.getDb_id() + ");");
		db.commitTransaction(transaction);
		return new Tag(String.valueOf(db_id), sm.getNextSingleId(), tagName, tagColor, tagSites);
	}
	
	public static Map<Integer, Tag> loadTags(ServletManager sm) {
		
	}
	
	protected String db_id;
	protected int single_id;
	protected String tagName;
	protected String tagColor;
	protected List<Website> tagSites;
	
	public Tag(String db_id, int single_id, String tagName, String tagColor, List<Website> tagSites) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.tagName = tagName;
		this.tagColor = tagColor;
		this.tagSites = new LinkedList<Website> ();
	}
}
