package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Account {
	public enum AccountData {
		NOTHING,
		ID,
		CLASSIC_APP_ID
	}
	
	public static Account loadAccount(String classicAppId, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		
		int transaction = db.startTransaction();
		ResultSet rs = db.get("SELECT id FROM accounts WHERE classic_app_id = " + classicAppId + ";");
		try {
			if (rs.next()) {
				String db_id = rs.getString(AccountData.ID.ordinal());
				List<AccountInformation> account_informations = AccountInformation.loadInformations(db_id, user, sm);
				db.commitTransaction(transaction);
				return new Account(db_id, sm.getNextSingleId(), classicAppId, account_informations);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
		return null;
	}
	
	public static Account createAccount(String classicAppId, Map<String, String> account_informations, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		int db_id = db.set("INSERT INTO accounts values (null, " + classicAppId + ");");
		List<AccountInformation> accountInformations = AccountInformation.createAccountInformations(String.valueOf(db_id), account_informations, user, sm);
		db.commitTransaction(transaction);
		return new Account(String.valueOf(db_id), sm.getNextSingleId(), classicAppId, accountInformations);
	}
	protected String db_id;
	protected int singled_id;
	protected String classicAppId;
	protected List<AccountInformation> account_informations;
	
	public Account(String db_id, int single_id, String classicAppId, List<AccountInformation> account_informations) {
		this.db_id = db_id;
		this.singled_id = single_id;
		this.classicAppId = classicAppId;
		this.account_informations = account_informations;
	}
	
	public Account(String db_id, int single_id, String classicAppId) {
		this.db_id = db_id;
		this.singled_id = single_id;
		this.classicAppId = classicAppId;
		this.account_informations = new LinkedList<AccountInformation>();
	}
	
	public void setAccountInformations(List<AccountInformation> account_informations) {
		this.account_informations = account_informations;
	}
	
	public void updateAccountInformation(String information_name, String information_value, User user, ServletManager sm) throws GeneralException {
		Iterator<AccountInformation> it = this.account_informations.iterator();
		while(it.hasNext()) {
			AccountInformation tmpInfo = it.next();
			if (tmpInfo.getInformationName().equals(information_name)) {
				tmpInfo.setInformation_value(information_value, user, sm);
				return;
			}
		}
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Iterator<AccountInformation> it = this.account_informations.iterator();
		while (it.hasNext()) {
			it.next().removeFromDb(sm);
		}
		db.set("DELETE FROM accounts WHERE id=" + this.db_id + ";");
		db.commitTransaction(transaction);
	}
}
