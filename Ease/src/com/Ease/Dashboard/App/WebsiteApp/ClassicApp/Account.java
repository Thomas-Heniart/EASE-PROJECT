package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.boot.model.relational.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class Account {
    public enum Data {
        NOTHING,
        ID,
        SHARED,
        LAST_UPDATED_DATE,
        REMINDER_VALUE,
        REMINDER_TYPE
    }

	/*
	 * 
	 * Loader And creator
	 * 
	 */

    public static Account loadAccount(String db_id, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM accounts WHERE id = ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.InternError, "This account doesn't exist.");
        List<AccountInformation> infos = AccountInformation.loadInformations(db_id, db);
        boolean shared = rs.getBoolean(Data.SHARED.ordinal());
        String reminderValue = rs.getString(Data.REMINDER_VALUE.ordinal());
        String reminderType = rs.getString(Data.REMINDER_TYPE.ordinal());
        if (reminderType == null || reminderType.equals(""))
            return new Account(db_id, shared, infos);
        request = db.prepareRequest("SELECT lastUpdateDate + INTERVAL ? " + reminderType + " FROM accounts WHERE id = ?;");
        request.setInt(reminderValue);
        request.setInt(db_id);
        rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "I don't know what to say");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date deadLine = dateFormat.parse(rs.getString(1));
            Date now = new Date();
            return new Account(db_id, shared, infos, (now.compareTo(deadLine) >= 0));
        } catch (ParseException e) {
            throw new GeneralException(ServletManager.Code.InternError, "Parse error");
        }

    }

    public static Account createAccount(boolean shared, Map<String, String> informations, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("INSERT INTO accounts values (null, ?, default, null, null);");
        request.setBoolean(shared);
        String db_id = request.set().toString();
        List<AccountInformation> infos = AccountInformation.createAccountInformations(db_id, informations, sm);
        db.commitTransaction(transaction);
        return new Account(db_id, shared, infos);
    }

    public static Account createAccountSameAs(Account sameAccount, boolean shared, User user, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("INSERT INTO accounts values (null, ?, default, null, null);");
        request.setBoolean(shared);
        String db_id = request.set().toString();
        List<AccountInformation> infos = AccountInformation.createAccountInformationFromAccountInformations(db_id, sameAccount.getAccountInformations(), sm);
        db.commitTransaction(transaction);
        return new Account(db_id, shared, infos);
    }

    public static Account createGroupAccount(String password, boolean shared, Map<String, String> informations, Infrastructure infra, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        //String crypted_password = infra.encrypt(password, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO accounts values (null, ?);");
        request.setBoolean(shared);
        String db_id = request.set().toString();
        List<AccountInformation> infos = AccountInformation.createAccountInformations(db_id, informations, sm);
        db.commitTransaction(transaction);
        return new Account(db_id, shared, infos);
    }
	
	/*
	 * 
	 * Constuctor
	 * 
	 */

    protected String db_id;
    protected boolean shared;
    protected List<AccountInformation> infos;
    protected boolean passwordMustBeUpdated;

    public Account(String db_id, boolean shared, List<AccountInformation> infos) {
        this.db_id = db_id;
        this.shared = shared;
        this.infos = infos;
        this.passwordMustBeUpdated = false;
    }

    public Account(String db_id, boolean shared, List<AccountInformation> infos, boolean b) {
        this.db_id = db_id;
        this.shared = shared;
        this.infos = infos;
        this.passwordMustBeUpdated = b;
    }

    public void removeFromDB(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        for (AccountInformation info : infos) {
            info.removeFromDb(sm);
        }
        DatabaseRequest request = db.prepareRequest("DELETE FROM accounts WHERE id = ?;");
        request.setInt(db_id);
        request.set();
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

    public List<AccountInformation> getAccountInformations() {
        return infos;
    }

    public List<AccountInformation> getAccountInformationsWithoutPassword() {
        List<AccountInformation> res = new LinkedList<AccountInformation>();
        for (AccountInformation info : this.infos) {
            if (info.getInformationName().equals("password"))
                continue;
            res.add(info);
        }
        return res;
    }

    public void setPassword(String password, User user, ServletManager sm) throws GeneralException {
        String cryptedPassword = user.encrypt(password);
        if (this.getInformationNamed("password") == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This account does not have password field");
        for (AccountInformation info : this.infos) {
            if (info.getInformationName().equals("password"))
                info.setInformation_value(cryptedPassword, sm);
        }
    }

    public String getCryptedPassword() throws GeneralException {
        String password = this.getInformationNamed("password");
        if (password == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This account does not have password field");
        return password;
    }
	
	/*
	 * 
	 * Utils
	 * 
	 */

    public JSONObject getJSON(ServletManager sm) throws GeneralException {
        JSONObject obj = new JSONObject();
        //obj.put("password", sm.getUser().decrypt(this.crypted_password));
        for (AccountInformation info : this.infos) {
            String value;
            if (info.getInformationName().equals("password")) {
                value = sm.getUser().decrypt(info.getInformationValue());
            } else {
                value = info.getInformationValue();
            }
            obj.put(info.getInformationName(), value);
        }
        return obj;
    }

    public JSONArray getJSON() {
        JSONArray res = new JSONArray();
        JSONObject tmp;
        for (AccountInformation info : this.infos) {
            if (info.getInformationName().equals("password"))
                continue;
            tmp = new JSONObject();
            tmp.put(info.getInformationName(), info.getInformationValue());
            res.add(tmp);
        }
        return res;
    }

    public void editInfos(Map<String, String> infos, ServletManager sm) throws GeneralException {
        String value;
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        for (AccountInformation info : this.infos) {
            if ((value = infos.get(info.getInformationName())) != null) {
                if (info.getInformationName().equals("password")) {
                    db.prepareRequest("UPDATE accounts SET lastUpdateDate = NOW() WHERE id = ?;").set();
                    this.passwordMustBeUpdated = false;
                }
                info.setInformation_value(value, sm);
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

    public JSONArray getInformationsJSON() {
        JSONArray res = new JSONArray();
        for (AccountInformation info : this.infos) {
            if (!info.getInformationName().equals("password")) {
                JSONObject tmp = new JSONObject();
                tmp.put("name", info.getInformationName());
                tmp.put("value", info.getInformationValue());
                res.add(tmp);
            }
        }
        return res;
    }

    public boolean mustUpdatePassword() {
        return this.passwordMustBeUpdated;
    }
}
