package com.Ease.session;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.context.Site;

public class App {
	protected String	name;
	protected String	type;
	protected String	id;
	protected Site		site;
	protected int		index; 
	protected int		profileIndex;
	
	protected int appId;
	protected int profileId;
	
	//Setter
	
	public void setIndex(int ind) {
		index = ind;
	}
	public void setAppId(int id) {
		appId = id;
	}
	
	//Getter
	public int getAppId(){
		return appId;
	}
	public int getProfileId() {
		return profileId;
	}
	
	public String getType() {
		return type;
	}
	public String getId() {
		return id;
	}
	public Site getSite() {
		return site;
	}
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	public int getProfileIndex() {
		return profileIndex;
	}
	
	public void setName(String str) {
		name = str;
	}
	public void deleteFromDB(ServletContext context) throws SessionException {
		throw new SessionException("This is an app, this is not supposed to append! 1");
	}
	public void updateInDB(ServletContext context) throws SessionException {
		throw new SessionException("This is an app, this is not supposed to append! 2");
	}
	public void updateInDB(ServletContext context, String keyUser) throws SessionException {
		throw new SessionException("This is an app, this is not supposed to append! 3");
	}
	public void updateProfileIdnDB(ServletContext context, String id, int prId) throws SessionException {
		throw new SessionException("This is an app, this is not supposed to append! 4");
	}
}