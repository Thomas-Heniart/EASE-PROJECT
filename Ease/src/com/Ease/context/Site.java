package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Site {
	enum SiteData {
		NOTHING,
		ID,
		URL,
		NAME,
		FOLDER,
		HAVELOGINBUTTON,
		HAVELOGINWITH
	}
	
	protected String id;
	protected String name;
	protected String url;
	protected String folder;
	protected boolean haveLoginButton;
	protected String[] haveLoginWith;
	
	public Site(ResultSet rs) {
		try {
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
	
	public boolean haveLoginWith(Site site){
		for (int i = 0; i < haveLoginWith.length; ++i){
			if (haveLoginWith[i].equals(site.getId()))
				return true;
		}
		return false;
	}
	
	public String getLoginWith(){
		String ret = "";
		for (int i = 0; i < haveLoginWith.length; ++i) {
			ret += haveLoginWith[i];
			if (i < haveLoginWith.length - 1)
				ret += ",";
		}
		return ret;
	}
}
