package com.Ease.dashboard;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class AppInformation {
	
	public static AppInformation createAppInformation(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO appInformations values (null, '" + name + "');");
		return new AppInformation(String.valueOf(db_id), name);
	}
	
	protected String db_id;
	protected String name;
	
	public AppInformation(String db_id, String name) {
		this.db_id = db_id;
		this.name = name;
	}
	
	public String getName() {
		return this.getName();
	}
	
	public void setName(String name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE appInformations SET name='" + name + "' WHERE id=" + this.db_id + ";");
		this.name = name;
	}
}
