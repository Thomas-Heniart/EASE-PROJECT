package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Account {
	public enum Data {
		NOTHING,
		ID,
		PASSWORD,
		SHARED
	}
	
	/*
	 * 
	 * Loader And creator
	 * 
	 */
	
	public static Account loadAccount(String db_id, DataBaseConnection db) throws GeneralException {
		ResultSet rs = db.get("SELECT * FROM accounts WHERE id = " + db_id + ";");
		try {
			if (rs.next()) {
				String password = rs.getString(Data.PASSWORD.ordinal());
				List<AccountInformation> infos = AccountInformation.loadInformations(db_id, db);
				boolean shared = rs.getBoolean(Data.SHARED.ordinal());
				return new Account(db_id, password, shared, infos);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		throw new GeneralException(ServletManager.Code.InternError, "This account doesn't exist.");
	}
	
	public static Account createAccount(String password, boolean shared, Map<String, String> informations, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String crypted_password = user.encrypt(password);
		String db_id = db.set("INSERT INTO accounts values (null, '" + crypted_password + "', " + (shared ? 1 : 0) + ");").toString();
		List<AccountInformation> infos = AccountInformation.createAccountInformations(db_id, informations, sm);
		db.commitTransaction(transaction);
		return new Account(db_id, crypted_password, shared, infos);
	}
	
	public static Account createGroupAccount(String password, boolean shared, Map<String, String> informations, Infrastructure infra, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		String crypted_password = infra.encrypt(password, sm);
		String db_id = db.set("INSERT INTO accounts values (null, '" + crypted_password + "', " + (shared ? 1 : 0) + ");").toString();
		List<AccountInformation> infos = AccountInformation.createAccountInformations(db_id, informations, sm);
		db.commitTransaction(transaction);
		return new Account(db_id, crypted_password, shared, infos);
	}
	
	/*
	 * 
	 * Constuctor
	 * 
	 */
	
	protected String 				db_id;
	protected String 				crypted_password;
	protected boolean 				shared;
	protected List<AccountInformation>	infos;
	
	public Account(String db_id, String crypted_password, boolean shared, List<AccountInformation> infos) {
		this.db_id = db_id;
		this.crypted_password = crypted_password;
		this.shared = shared;
		this.infos = infos;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (AccountInformation info : infos) {
			info.removeFromDb(sm);
		}
		db.set("DELETE FROM accounts WHERE id=" + db_id + ";");
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */
	
	public String getDBid() {
		return db_id;
	}
	
	public List<AccountInformation> getAccountInformations(){
		return infos;
	}
	
	public void setPassword(String password, User user, ServletManager sm) throws GeneralException {
		String cryptedPassword = user.encrypt(password);
		this.setEncryptedPassword(cryptedPassword, user, sm);
	}
	
	public void setEncryptedPassword(String cryptedPassword, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		this.crypted_password = cryptedPassword;
		db.set("UPDATE accounts SET password='" + cryptedPassword + "' WHERE id=" + this.db_id + ";");
		
	}
	
	public String getCryptedPassword() {
		return crypted_password;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public JSONObject getJSON(ServletManager sm) throws GeneralException{
		JSONObject obj = new JSONObject();
		obj.put("password", sm.getUser().decrypt(this.crypted_password));
		for(AccountInformation info : this.infos){
			obj.put(info.getInformationName(), info.getInformationValue());
		}
		return obj;
	}
	
	public void editInfos(Map<String, String> infos, ServletManager sm) throws GeneralException {
		String value;
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (AccountInformation info : this.infos) {
			if ((value = infos.get(info.getInformationName())) != null) {
				 info.setInformation_value(value, sm);
			} else {
				throw new GeneralException(ServletManager.Code.ClientError, "Wrong information name : "+ info.getInformationName() +".");
			}
		}
		db.commitTransaction(transaction);
	}
	
	public String getInformationNamed(String info_name) {
		for (AccountInformation info : this.infos) {
			if (info.getInformationName().equals(info_name))
				return info.getInformationValue();
		}
		return null;
	}
}
