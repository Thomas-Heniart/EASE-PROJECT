package com.Ease.dashboard;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
	public static User loadUser(String email, String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Map<String, Group> groups = (Map<String, Group>) sm.getContextAttr("groups");
		try {
			ResultSet rs = db.get("SELECT * FROM users where email='" + email + "';");
			rs.next();
			String db_id = rs.getString(Data.ID.ordinal());
			String firstName = rs.getString(Data.FIRSTNAME.ordinal());
			String lastName = rs.getString(Data.LASTNAME.ordinal());
			Keys keys = Keys.loadKeys(rs.getString(Data.KEYSID.ordinal()), password, sm);
			Option options = Option.loadOption(rs.getString(Data.OPTIONSID.ordinal()), sm);
			//Status status = Status.loadStatus(rs.getString(Data.STATUSID.ordinal()), sm);
			List<UserEmail> emails = UserEmail.loadEmails(db_id, sm);
			User newUser =  new User(db_id, firstName, lastName, email, keys, options, null, emails);
			newUser.loadProfiles(sm);
			ResultSet rs2 = db.get("SELECT group_id FROM groupsAndUsersMap WHERE user_id=" + newUser.getDBid() + ";");
			rs2.next();
			Group userGroup = groups.get(rs2.getString(1));
			if (userGroup != null)
				userGroup.connectUser(newUser);
			return newUser;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static User createUser(String email, String firstName, String lastName, String password, String code, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Group group = Invitation.verifyInvitation(email, code, sm);
		Option opt = Option.createOption(sm);
		//Status status = Status.createStatus(sm);
		List<UserEmail> emails = new LinkedList<UserEmail>();
		Keys keys = Keys.createKeys(password, sm);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String registrationDate = dateFormat.format(date);
		String db_id = db.set("INSERT INTO users VALUES(NULL, '" + firstName + "', '" + lastName + "', '" + email + "', " + keys.getDBid() + ", " + opt.getDb_id() + ", '" + registrationDate + "');").toString();
		User newUser = new User(db_id, null, null, email, null, opt, null, emails);
		newUser.getProfilesColumn().get(0).add(Profile.createPersonnalProfile(newUser, 0, 0, "Side", "#000000", sm));
		newUser.getProfilesColumn().get(1).add(Profile.createPersonnalProfile(newUser, 1, 0, "Perso", "#000000", sm));
		if (group != null) {
			group.addUser(newUser, sm);
			group.loadContent(newUser, sm);
		}
		UserEmail userEmail = UserEmail.createUserEmail(email, newUser, sm);
		if (code != null)
			userEmail.beVerified(sm);
		newUser.getUserEmails().add(userEmail);
		db.commitTransaction(transaction);
		return newUser;
	}
	
	protected String	db_id;
	protected String	first_name;
	protected String	last_name;
	protected String	email;
	protected Keys		keys;
	protected Option	opt;
	//protected Status	status;
	protected List<List<Profile>> profiles_column;
	protected int		max_single_id;
	protected List<UserEmail> emails;
	protected Map<String, Session> websockets;
	
	public User(String db_id, String first_name, String last_name, String email, Keys keys, Option opt, /*Status*/ String status, List<UserEmail> emails) {
		this.db_id = db_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.keys = keys;
		this.opt = opt;
		//this.status = status;
		this.emails = emails;
		this.profiles_column = new LinkedList<List<Profile>>();
		for (int i = 0; i < 5; ++i) {
			this.profiles_column.add(new LinkedList<Profile>()); 
		}
		this.max_single_id = 0;
		this.emails = new LinkedList<UserEmail>();
		this.websockets = new HashMap<String, Session>();
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
	
	public Keys getKeys() {
		return keys;
	}
	
	public Option getOptions() {
		return opt;
	}
	
	/*public Status getStatus() {
		return status;
	}*/
	
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
		//this.status.removeFromDB(sm);
		db.commitTransaction(transaction);
	}
	
	public String encrypt(String password) throws GeneralException {
		return this.keys.encrypt(password);
	}
	
	public String decrypt(String password) throws GeneralException {
		return this.keys.decrypt(password);
	}
	
	public Map<String, Session> getWebsockets() {
		return this.websockets;
	}

	public void removeWebsocket(Session session) {
		this.websockets.remove(session.getId());
	}

	public void addWebsocket(Session session) throws GeneralException {
		try {
			session.getBasicRemote().sendText(String.valueOf(this.getNextSingleId()));
			this.websockets.put(session.getId() , session);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			websockets.remove(session);
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public int getMostEmptyProfileColumn() {
		int col = 0;
		int minSize = -1;
		for (List<Profile> column : this.profiles_column) {
			int colSize = 0;
			if (this.profiles_column.indexOf(column) != 0){
				for (Profile profile: column) {
					colSize += profile.getSize();
				}
				if (minSize == - 1 || colSize < minSize) {
					minSize = colSize;
					col = this.profiles_column.indexOf(column);
				}
			}
		}
		return col;
	}
	
	public void deconnect(ServletManager sm) {
		Map<String, Group> groups = (Map<String, Group>) sm.getContextAttr("groups");
		Map<String, User> users = (Map<String, User>) sm.getContextAttr("users");
		for (Map.Entry<String, Group> entry : groups.entrySet())
			entry.getValue().removeUser(this);
		users.remove(this.email);
	}
}
