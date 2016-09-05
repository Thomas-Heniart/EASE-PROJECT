package com.Ease.session;

import javax.servlet.ServletContext;

import com.Ease.context.Site;

public class App {
	protected String	name;
	protected String	type;
	protected String	id;
	protected Site		site;
	protected int		index; 
	
	//Setter
	
	public void setIndex(int ind) {
		index = ind;
	}
	
	//Getter
	
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
}