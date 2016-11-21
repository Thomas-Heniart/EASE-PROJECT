package com.Ease.session.update;

import com.Ease.session.App;
import com.Ease.session.User;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class LogwithUpdate extends Update {
	public enum LogwithUpdateData {
		NOTHING,
		ID,
		UPDATE_ID,
		LOGWITH
	}
	App		logwith;
	
	public LogwithUpdate(String DBid, String websiteId, String login, App app, String knowId) {
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
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		
		int transaction = db.startTransaction();
		db.set("DELETE FROM classicUpdates WHERE update_id=" + this.DBid + ";");
		db.set("DELETE FROM Updates WHERE id=" + this.DBid + ";");
		db.set("INSERT INTO removedUpdate values(NULL, " + this.websiteId + ", " + this.login + ", " + logwith.getSite().getId() + ");");
		db.commitTransaction(transaction);
	}
	
public static LogwithUpdate CreateLogwithUpdate(String websiteId, String login, int appId, User user, ServletManager sm) throws GeneralException {
		
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String DBid = db.set("INSERT INTO updates VALUES (" + user.getId() + ", " + websiteId + ", " + login + ", 'logwith');").toString();
		db.set("INSERT INTO logwithUpdates VALUES (" + DBid + ", " + appId + ");");
		LogwithUpdate update = new LogwithUpdate(DBid, websiteId, login, user.getApp(appId), user.getNextKnowId());
		db.commitTransaction(transaction);
		return update;
	}
}