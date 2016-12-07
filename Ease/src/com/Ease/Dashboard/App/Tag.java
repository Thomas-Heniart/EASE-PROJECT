package com.Ease.Dashboard.App;

import java.util.LinkedList;
import java.util.List;

public class Tag {
	
	protected String tagName;
	protected String tagColor;
	protected List<Website> tagSites;
	
	public Tag(String tagName, String tagColor) {
		this.tagName = tagName;
		this.tagColor = tagColor;
		this.tagSites = new LinkedList<Website> ();
	}
}
