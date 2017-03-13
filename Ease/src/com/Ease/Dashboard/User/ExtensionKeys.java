package com.Ease.Dashboard.User;

import java.util.LinkedList;
import java.util.List;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class ExtensionKeys {
	
	
	public static ExtensionKeys loadExtensionKeys(User user, ServletManager sm) throws GeneralException {
		List<String> keys = new LinkedList<String>();
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM usersPrivateExtensions where user_id= ?;");
		request.setInt(user.getDBid());
		DatabaseResult rs = request.get();
		while (rs.next()) {
			keys.add(rs.getString(3));
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
			DatabaseRequest request = db.prepareRequest("INSERT INTO usersPrivateExtensions VALUES(NULL, ?, ?);");
			request.setInt(user.getDBid());
			request.setString(clientKey);
			request.set();
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
					DatabaseRequest request = db.prepareRequest("DELETE FROM usersPrivateExtensions WHERE user_id = ? AND extension_key = ?;");
					request.setInt(user.getDBid());
					request.setString(clientKey);
					request.set();
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
