package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class User {
	enum Data {
		NOTHING,
		ID,
		FIRSTNAME,
		LASTNAME,
		EMAIL,
		KEYSID,
		OPTIONSID,
		REGISTRATIONDATE,
		STATUSID
	}
	public static User loadUser(String email, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT * FROM users where email='" + email + "';");
			String db_id = rs.getString(Data.ID.ordinal());
			String firstName = rs.getString(Data.FIRSTNAME.ordinal());
			String lastName = rs.getString(Data.LASTNAME.ordinal());
			Keys keys = Keys.loadKeys(rs.getString(Data.KEYSID.ordinal()), sm);
			Options options = Keys.loadKeys(rs.getString(Data.OPTIONSID.ordinal()), sm);
			Status status = Keys.loadKeys(rs.getString(Data.STATUSID.ordinal()), sm);
			String registrationDate = rs.getString(Data.REGISTRATIONDATE.ordinal());
			List<UserEmail> emails = UserEmail.loadEmails(db_id, sm);
			User newUser =  new User(db_id, firstName, lastName, email, registrationDate, keys, options, status);
			newUser.setEmails(emails);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	/*public static User loadUserFromId(int id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT * FROM users where id=" + id + ";");
			String db_id = rs.getString(Data.ID.ordinal());
			String firstName = rs.getString(Data.FIRSTNAME.ordinal());
			String lastName = rs.getString(Data.LASTNAME.ordinal());
			String email = rs.getString(Data.EMAIL.ordinal());
			Keys keys = Keys.loadKeys(rs.getString(Data.KEYSID.ordinal()), sm);
			Options options = Keys.loadKeys(rs.getString(Data.OPTIONSID.ordinal()), sm);
			Status status = Keys.loadKeys(rs.getString(Data.STATUSID.ordinal()), sm);
			String registrationDate = rs.getString(Data.REGISTRATIONDATE.ordinal());
			return new User(db_id, firstName, lastName, email, registrationDate, keys, options, status);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}*/
	
	public static User createUser(String email, String firstName, String lastName, String password) throws GeneralException {
		//do your stuff here
	}
	
	protected String	db_id;
	protected String	first_name;
	protected String	last_name;
	protected String	email;
	protected String	registration_date;
	protected Keys		keys;
	protected Options	opt;
	protected Status	status;
	protected List<List<Profile>> profiles_column;
	protected int		max_single_id;
	protected List<UserEmail> emails;
	
	public User(String db_id, String first_name, String last_name, String email, String registration_date, Keys keys, Options opt, Status status) {
		this.db_id = db_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.registration_date = registration_date;
		this.keys = keys;
		this.opt = opt;
		this.status = status;
		this.profiles_column = new LinkedList<List<Profile>>();
		for (int i = 0; i < 5; ++i) {
			this.profiles_column.add(new LinkedList<Profile>()); 
		}
		this.max_single_id = 0;
		this.emails = new LinkedList<UserEmail>();
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM users WHERE id=" + this.db_id + ";");
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid(){
		return this.db_id;
	}
	
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String first_name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE FROM users set firstName='" + first_name + "' WHERE id=" + this.db_id + ";");
		this.first_name = first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String last_name, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE FROM users set lastName='" + last_name + "' WHERE id=" + this.db_id + ";");
		this.last_name = last_name;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String registration_date() {
		return this.registration_date;
	}
	
	public void setEmails(List<UserEmail> emails) {
		this.emails = emails;
	}
	
	public List<List<Profile>> getProfilesColumn() {
		return this.profiles_column;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public int getNextSingleId() {
		this.max_single_id++;
		return max_single_id;
	}
	
	public void removeEmail(UserEmail email) {
		this.emails.remove(email);
	}
	
	public void updateProfilesIndex(ServletManager sm)  throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (int i = 0; i < profiles_column.size(); ++i) {
			for (int j = 0; j < profiles_column.get(i).size(); ++j) {
				if (profiles_column.get(i).get(j).getPositionIdx() != j) {
					profiles_column.get(i).get(j).setPositionIdx(j, sm);
				}
				if (profiles_column.get(i).get(j).getColumnIdx() != i) {
					profiles_column.get(i).get(j).setColumnIdx(i, sm);
				}
			}
		}
		db.commitTransaction(transaction);
	}
}
