package com.Ease.Dashboard.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class ExtensionKeys {
	
	
	public static ExtensionKeys loadExtensionKeys(User user, ServletManager sm) throws GeneralException {
		List<String> keys = new LinkedList<String>();
		try {
			DataBaseConnection db = sm.getDB();
			ResultSet rs = db.get("SELECT * FROM usersPrivateExtensions where user_id=" + user.getDBid() + ";");
			while (rs.next()) {
				keys.add(rs.getString(2));
			}
		} catch (GeneralException e) {
			throw e;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return (new ExtensionKeys(keys, user));
	}
	
	protected User user;
	protected List<String> keys;
	
	public ExtensionKeys(List<String> keys, User user) {
		this.keys = keys;
		this.user = user;
	}
	
	public List<String> getKeys() {
		return keys;
	}
	
	public boolean haveThisKey(String clientKey) {
		for (String key : keys) {
			if (clientKey.equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	public void addKey(String clientKey, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			for (String key : keys) {
				if (clientKey.equals(key)) {
					throw new GeneralException(ServletManager.Code.ClientError, "This extension key already exist.");
				}
			}
			db.set("INSERT INTO usersPrivateExtensions VALUES(NULL, " + user.getDBid() + ", '" + clientKey + "');");
			keys.add(clientKey);
		} catch (GeneralException e) {
			throw e;
		}
	}
	
	public void removeKey(String clientKey, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			for (String key : keys) {
				if (clientKey.equals(key)) {
					db.set("REMOVE FROM usersPrivateExtensions WHERE user_id=" + user.getDBid() + " AND key='" + clientKey + "';");
					keys.remove(clientKey);
					return ;
				}
			}
			throw new GeneralException(ServletManager.Code.ClientError, "This extension key dosoen't exist.");
		} catch (GeneralException e) {
			throw e;
		}
	}
 }
