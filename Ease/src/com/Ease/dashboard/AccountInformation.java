package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class AccountInformation {
	
	public enum AccountInformationData {
		NOTHING,
		ID,
		ACCOUNT_ID,
		INFORMATION_NAME,
		INFORMATION_VALUE
	}
	
	public static List<AccountInformation> createAccountInformations(String account_id, Map<String, String> account_informations, ServletManager sm) throws GeneralException {
		List<AccountInformation> informations = new LinkedList<AccountInformation> ();
		for (Map.Entry<String, String> entry : account_informations.entrySet()) {
			informations.add(createAccountInformation(account_id, entry.getKey(), entry.getValue(), sm));
		}
		return informations;
	}
	
	public static AccountInformation createAccountInformation(String account_id, String information_name, String information_value, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		if (information_name.equals("password")) {
			//Todo crypting
		}
		int db_id = db.set("INSERT INTO accountsInformations values (null, " + account_id + ", " + information_name + ", " + information_value + ");");
		return new AccountInformation(String.valueOf(db_id), sm.getNextSingleId(), account_id, information_name, information_value);
	}
	
	public static List<AccountInformation> loadInformations(String account_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		List<AccountInformation> account_informations = new LinkedList<AccountInformation>();
		ResultSet rs = db.get("SELECT * FROM accountsInformations WHERE account_id=" + account_id + ";");
		try {
			while (rs.next()) {
				String db_id = rs.getString(AccountInformationData.ID.ordinal());
				String information_name = rs.getString(AccountInformationData.INFORMATION_NAME.ordinal());
				String information_value = rs.getString(AccountInformationData.INFORMATION_VALUE.ordinal());
				if (information_name.equals("password")) {
					//Decrypt to do
				}
				account_informations.add(new AccountInformation(db_id, sm.getNextSingleId(), account_id, information_name, information_value));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return account_informations;
	}
	
	protected String db_id;
	protected int single_id;
	protected String account_id;
	protected String information_name;
	protected String information_value;
	
	public AccountInformation(String db_id, int single_id, String account_id, String information_name, String information_value) {
		this.db_id = db_id;
		this.single_id = single_id;
		this.account_id = account_id;
		this.information_name = information_name;
		this.information_value = information_value;
	}
	
	public String getInformationName() {
		return this.information_name;
	}
	
	public String getInformationValue() {
		return this.information_value;
	}
	
	public void setInformation_value(String information_value, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE accountsInformations SET information_value='" + information_value + "' WHERE id=" + this.db_id + ";");
		this.information_value = information_value;
	}
	
	public void removeFromDb(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM accountsInformations WHERE id=" + this.db_id + ";");
	}
}
