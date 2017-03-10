package com.Ease.Update;

import java.util.HashMap;
import java.util.Map;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;

public class ClassicUpdateInformation {

	public enum Data {
		NOTHING,
		ID,
		UPDATE_NEW_CLASSIC_APP_ID,
		INFORMATION_NAME,
		INFORMATION_VALUE
	}
	
	public static Map<String, String> loadClassicUpdateInformations(String updateNewClassicApp_id, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM classicUpdateInformations WHERE update_new_classic_app_id = ?;");
		request.setInt(updateNewClassicApp_id);
		DatabaseResult rs = request.get();
		Map<String, String> informations = new HashMap<String, String>();
		String information_name;
		String information_value;
		while (rs.next()) {
			information_name = rs.getString(Data.INFORMATION_NAME.ordinal());
			information_value = rs.getString(Data.INFORMATION_VALUE.ordinal());
			informations.put(information_name, information_value);
		}
		return informations;
	}

	public static void createInformations(String updateNewClassicApp_id, Map<String, String> updateInformations, DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		for (Map.Entry<String, String> entry : updateInformations.entrySet()) {
			db.set("INSERT INTO classicUpdateInformations values (null, " + updateNewClassicApp_id + ", '" + entry.getKey() + "', '" + entry.getValue() + "');");
		}
		db.commitTransaction(transaction);
	}

	public static void deleteFromDb(String newAccountUpdateDbId, DataBaseConnection db) throws GeneralException {
		int transaction = db.startTransaction();
		db.set("DELETE FROM classicUpdateInformations WHERE update_new_classic_app_id IN (SELECT id FROM updateNewClassicApp WHERE update_new_account_id = " + newAccountUpdateDbId + ");");
		db.commitTransaction(transaction);
	}
}
