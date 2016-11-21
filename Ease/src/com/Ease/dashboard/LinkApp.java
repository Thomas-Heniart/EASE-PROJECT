package com.Ease.dashboard;

public class LinkApp extends App {
	public static createLinkApp(String name, Profile profile, String link, String imgUrl, ServletManager sm) throws GeneralException {
		//todo
	}
	
	protected String link;
	protected String imgUrl;
	
	public LinkApp(String name, Profile profile, Permissions permissions, int position, int single_id, String db_id, String link, String imgUrl) {
		this.name = name;
		this.profile = profile;
		this.link = link;
		this.imgUrl = imgUrl;
		this.position = position;
		this.permissions = permissions;
		this.single_id = single_id;
		this.db_id = db_id;
	}
	
	public void setLink(String link, ServletManager sm) throws GeneralException {
		//Todo db
		this.link = link;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public void setImgUrl(String imgUrl, ServletManager sm) throws GeneralException {
		//Todo db
		this.imgUrl = imgUrl;
	}
	
	public String getImgUrl() {
		return this.imgUrl;
	}
}
