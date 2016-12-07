package com.Ease.Dashboard.App;

import java.util.LinkedList;
import java.util.List;

public class Tag {
	
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
