package com.Ease.dashboard;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

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
	private static int tabId = 0;
	public static User loadUser(String email, String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT * FROM users where email='" + email + "';");
			String db_id = rs.getString(Data.ID.ordinal());
			String firstName = rs.getString(Data.FIRSTNAME.ordinal());
			String lastName = rs.getString(Data.LASTNAME.ordinal());
			Keys keys = Keys.loadKeys(rs.getString(Data.KEYSID.ordinal()), password, sm);
			Option options = Option.loadOption(rs.getString(Data.OPTIONSID.ordinal()), sm);
			Status status = Status.loadStatus(rs.getString(Data.STATUSID.ordinal()), sm);
			String registrationDate = rs.getString(Data.REGISTRATIONDATE.ordinal());
			List<UserEmail> emails = UserEmail.loadEmails(db_id, sm);
			User newUser =  new User(db_id, firstName, lastName, email, registrationDate, keys, options, status, emails);
			newUser.loadProfiles(sm);
			return newUser;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return null;
	}
	
	public static User createUser(String email, String firstName, String lastName, String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Keys keys = Keys.createKeys(password, sm);
		Option opt = Option.createOption(sm);
		Status status = Status.createStatus(sm);
		//String registrationDate = GET_CURRENT_TIME;
		List<UserEmail> emails = new LinkedList<UserEmail>();
		int transaction = db.startTransaction();
		String db_id = db.set("INSERT INTO users VALUES(NULL, '" + firstName + "', '" + lastName + "', '" + email + "', " + keys.getDBid() + ", " + opt.getDb_id() + ", '" + registrationDate + "', " + status.getDBid() + ");").toString();
		User newUser = new User(db_id, firstName, lastName, email, registrationDate, keys, opt, status, emails);
		newUser.getUserEmails().add(UserEmail.createUserEmail(email, newUser, sm));
		db.commitTransaction(transaction);
		return newUser;
	}
	
	protected String	db_id;
	protected String	first_name;
	protected String	last_name;
	protected String	email;
	protected String	registration_date;
	protected Keys		keys;
	protected Option	opt;
	protected Status	status;
	protected List<List<Profile>> profiles_column;
	protected int		max_single_id;
	protected List<UserEmail> emails;
	protected List<Session> sessions;
	
	public User(String db_id, String first_name, String last_name, String email, String registration_date, Keys keys, Option opt, Status status, List<UserEmail> emails) {
		this.db_id = db_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.registration_date = registration_date;
		this.keys = keys;
		this.opt = opt;
		this.status = status;
		this.emails = emails;
		this.profiles_column = new LinkedList<List<Profile>>();
		for (int i = 0; i < 5; ++i) {
			this.profiles_column.add(new LinkedList<Profile>()); 
		}
		this.max_single_id = 0;
		this.emails = new LinkedList<UserEmail>();
		this.sessions = new LinkedList<Session>();
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
	
	public Keys getKeys() {
		return keys;
	}
	
	public Option getOptions() {
		return opt;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public List<UserEmail> getUserEmails() {
		return emails;
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
	
	public void loadProfiles(ServletManager sm) throws GeneralException {
		this.profiles_column = Profile.loadProfiles(this, sm);
	}
	
	public void removeEmail(UserEmail email) {
		this.emails.remove(email);
	}
	
	public void updateProfilesIndex(ServletManager sm) throws GeneralException {
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
	
	public Profile getProfile(int single_id) throws GeneralException {
		for (List<Profile> column: this.profiles_column) {
			for (Profile profile: column) {
				if (profile.getSingleId() == single_id)
					return profile;
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This profile's single_id dosen't exist.");
	}
	
	public App getApp(int single_id) throws GeneralException {
		for (List<Profile> column: this.profiles_column) {
			for (Profile profile: column) {
				for (App app: profile.getApps()) {
					if (app.getSingle_id() == single_id)
						return app;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This app's single_id dosen't exist.");
	}
	
	public void removeDefinitly(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (UserEmail mail: emails) {
			mail.removeFromDB(sm);
		}
		for (List<Profile> column: this.profiles_column) {
			for (Profile profile : column) {
				profile.removeFromDB(sm);
			}
		}
		this.removeFromDB(sm);
		this.keys.removeFromDB(sm);
		this.opt.removeFromDB(sm);
		this.status.removeFromDB(sm);
		db.commitTransaction(transaction);
	}
	
	public String encrypt(String password) {
		return this.keys.encrypt(password);
	}
	
	public String decrypt(String password) {
		return this.keys.decrypt(password);
	}
	
	public List<Session> getSessions() {
		return this.sessions;
	}
	
	public void addInContext(Map<String, User> usersMap) {
		usersMap.put(this.email, this);
	}

	public void removeFromContextIfNeeded(Map<String, User> users) {
		users.remove(this.email, this);
	}

	public boolean removeSession(Session session) {
		this.sessions.remove(session);
		return this.sessions.isEmpty();
	}

	public void addSession(Session session) {
		this.sessions.add(session);
		try {
			session.getBasicRemote().sendText(String.valueOf(tabId++));
		} catch (IOException e) {
			sessions.remove(session);
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
