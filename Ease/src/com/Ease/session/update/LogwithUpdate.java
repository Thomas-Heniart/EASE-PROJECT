package com.Ease.session.update;

import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.session.App;
import com.Ease.session.SessionException;
import com.Ease.session.User;

public class LogwithUpdate extends Update {
	public enum LogwithUpdateData {
		NOTHING,
		ID,
		UPDATE_ID,
		LOGWITH
	}
	App			logwith;
	
	public LogwithUpdate(String DBid, String websiteId, String login, App app, String knowId)  throws SessionException {
		this.DBid = DBid;
		this.knowId = knowId;
		this.websiteId = websiteId;
		this.login = login;
		this.type = "logwith";
		this.logwith = app;
	}
	
	public App getLogWith() {
		return logwith;
	}
	
	public void removeFromDB(ServletContext context) {
		DataBase db = (DataBase) context.getAttribute("DataBase");
		
		boolean transaction = false;
		try {
			transaction = db.start();
			db.set("DELETE FROM classicUpdates WHERE update_id=" + this.DBid + ";");
			db.set("DELETE FROM Updates WHERE id=" + this.DBid + ";");
			db.set("INSERT INTO removedUpdate values(NULL, " + this.websiteId + ", " + this.login + ", " + logwith.getSite().getId() + ");");
			db.commit(transaction);
		} catch (SQLException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
public static LogwithUpdate CreateLogwithUpdate(String websiteId, String login, int appId, User user, ServletContext context) throws SessionException {
		
		DataBase db = (DataBase) context.getAttribute("DataBase");
		
		LogwithUpdate update;
		boolean transaction = false;
		
		try {
			transaction = db.start();
			String DBid = db.set("INSERT INTO updates VALUES (" + user.getId() + ", " + websiteId + ", " + login + ", 'classic');").toString();
			db.set("INSERT INTO classicUpdates VALUES (" + DBid + ", " + appId + ");");
			update = new LogwithUpdate(DBid, websiteId, login, user.getApp(appId), user.getNextKnowId());
			db.commit(transaction);
			return update;
		} catch (SQLException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SessionException("db error");
		} 
	}
}