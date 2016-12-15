package com.Ease.Dashboard.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.Profile.ProfilePermissions;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Invitation;
import com.Ease.Utils.ServletManager;
import com.Ease.websocket.WebsocketSession;

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
	@SuppressWarnings("unchecked")
	public static User loadUser(String email, String password, ServletManager sm) throws GeneralException {
		Map<String, User> usersMap = (Map<String, User>) sm.getContextAttr("users");
		User connectedUser = usersMap.get(email);
		if (connectedUser != null)
			return connectedUser;
		try {
			DataBaseConnection db = sm.getDB();
			ResultSet rs = db.get("SELECT * FROM users where email='" + email + "';");
			if (rs.next()) {
				String db_id = rs.getString(Data.ID.ordinal());
				String firstName = rs.getString(Data.FIRSTNAME.ordinal());
				String lastName = rs.getString(Data.LASTNAME.ordinal());
				Keys keys = Keys.loadKeys(rs.getString(Data.KEYSID.ordinal()), password, sm);
				Option options = Option.loadOption(rs.getString(Data.OPTIONSID.ordinal()), sm);
				Map<String, UserEmail> emails = UserEmail.loadEmails(db_id, sm);
				Status status = Status.loadStatus(rs.getString(Data.STATUSID.ordinal()), db);
				ResultSet adminRs = db.get("SELECT user_id FROM admins WHERE user_id = " + db_id + ";");
				boolean isAdmin = adminRs.next();
				SessionSave sessionSave = SessionSave.createSessionSave(keys.getKeyUser(), db_id, sm);
				User newUser =  new User(db_id, firstName, lastName, email, keys, options, emails, isAdmin, sessionSave, status);
				newUser.loadProfiles(sm);
				for (Map.Entry<String, WebsiteApp> entry : newUser.getWebsiteAppsDBmap().entrySet()){
				    if (entry.getValue().getType().equals("LogwithApp")) {
				    	LogwithApp logwithApp = (LogwithApp)entry.getValue();
				    	App app = newUser.getWebsiteAppsDBmap().get(logwithApp.getLogwithDBid());
				    	logwithApp.rempLogwith((WebsiteApp)app);
				    }
				}
				ResultSet rs2 = db.get("SELECT group_id FROM groupsAndUsersMap WHERE user_id=" + newUser.getDBid() + ";");
				Group userGroup;
				while (rs2.next()) {
					userGroup = GroupManager.getGroupManager(sm).getGroupFromDBid(rs2.getString(1));
					if (userGroup != null) {
						newUser.getGroups().add(userGroup);
					}
				}
				((Map<String, User>)sm.getContextAttr("users")).put(email, newUser);
				return newUser;
			} else {
				throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static User loadUserFromCookies(SessionSave sessionSave, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String db_id = sessionSave.getUserId();
		String keyUser = sessionSave.getKeyUser();
		sessionSave.eraseFromDB(sm);
		try {
			
			ResultSet rs = db.get("SELECT * FROM users where id='" + db_id + "';");
			if (rs.next()) {
				String email = rs.getString(Data.EMAIL.ordinal());
				Map<String, User> usersMap = (Map<String, User>) sm.getContextAttr("users");
				User connectedUser = usersMap.get(email);
				if (connectedUser != null)
					return connectedUser;
				String firstName = rs.getString(Data.FIRSTNAME.ordinal());
				String lastName = rs.getString(Data.LASTNAME.ordinal());
				Keys keys = Keys.loadKeysWithoutPassword(rs.getString(Data.KEYSID.ordinal()), keyUser, sm);
				Option options = Option.loadOption(rs.getString(Data.OPTIONSID.ordinal()), sm);
				Map<String, UserEmail> emails = UserEmail.loadEmails(db_id, sm);
				Status status = Status.loadStatus(rs.getString(Data.STATUSID.ordinal()), db);
				ResultSet adminRs = db.get("SELECT user_id FROM admins WHERE user_id = " + db_id + ";");
				boolean isAdmin = adminRs.next();
				SessionSave newSessionSave = SessionSave.createSessionSave(keyUser, db_id, sm);
				User newUser =  new User(db_id, firstName, lastName, email, keys, options, emails, isAdmin, newSessionSave, status);
				newUser.loadProfiles(sm);
				for (Map.Entry<String, WebsiteApp> entry : newUser.getWebsiteAppsDBmap().entrySet()){
				    if (entry.getValue().getType().equals("LogwithApp")) {
				    	LogwithApp logwithApp = (LogwithApp)entry.getValue();
				    	App app = newUser.getWebsiteAppsDBmap().get(logwithApp.getLogwithDBid());
				    	logwithApp.rempLogwith((WebsiteApp)app);
				    }
				}
				ResultSet rs2 = db.get("SELECT group_id FROM groupsAndUsersMap WHERE user_id=" + newUser.getDBid() + ";");
				Group userGroup;
				while (rs2.next()) {
					userGroup = GroupManager.getGroupManager(sm).getGroupFromDBid(rs2.getString(1));
					if (userGroup != null) {
						newUser.getGroups().add(userGroup);
					}
				}
				((Map<String, User>)sm.getContextAttr("users")).put(email, newUser);
				return newUser;
			} else {
				throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static User createUser(String email, String firstName, String lastName, String password, String code, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		List<Group> groups = Invitation.verifyInvitation(email, code, sm);
		Option opt = Option.createOption(sm);
		//Status status = Status.createStatus(sm);
		Map<String, UserEmail> emails = new HashMap<String, UserEmail>();
		Keys keys = Keys.createKeys(password, sm);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String registrationDate = dateFormat.format(date);
		Status status = Status.createStatus(db);
		String db_id = db.set("INSERT INTO users VALUES(NULL, '" + firstName + "', '" + lastName + "', '" + email + "', " + keys.getDBid() + ", " + opt.getDb_id() + ", '" + registrationDate + "', " + status.getDbId() + ");").toString();
		SessionSave sessionSave = SessionSave.createSessionSave(keys.getKeyUser(),  db_id, sm);
		User newUser = new User(db_id, firstName, lastName, email, keys, opt, emails, false, sessionSave, status);
		newUser.getProfileColumns().get(0).add(Profile.createPersonnalProfile(newUser, 0, 0, "Side", "#000000", sm));
		newUser.getProfileColumns().get(1).add(Profile.createPersonnalProfile(newUser, 1, 0, "Perso", "#000000", sm));
		((Map<String, User>)sm.getContextAttr("users")).put(email, newUser);
		for (Group group : groups) {
			group.addUser(email, sm);
			newUser.getGroups().add(group);
		}
		UserEmail userEmail = UserEmail.createUserEmail(email, newUser, !groups.isEmpty(), sm);
		newUser.getUserEmails().put(email, userEmail);
		db.commitTransaction(transaction);
		return newUser;
	}
	
	protected String	db_id;
	protected String	first_name;
	protected String	last_name;
	protected String	email;
	protected Keys		keys;
	protected Option	opt;
	protected List<List<Profile>> profile_columns;
	protected Map<String, App> appsDBmap;
	protected Map<String, WebsiteApp> websiteAppsDBmap;
	protected Map<Integer, App> appsIDmap;
	protected int		max_single_id;
	protected Map<String, UserEmail> emails;
	protected Map<String, WebsocketSession> websockets;
	protected List<Group> groups;
	protected boolean isAdmin;
	protected Status status;
	
	protected SessionSave sessionSave;
	
	public User(String db_id, String first_name, String last_name, String email, Keys keys, Option opt, Map<String, UserEmail> emails, boolean isAdmin, SessionSave sessionSave, Status status) {
		this.db_id = db_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.keys = keys;
		this.opt = opt;
		this.emails = emails;
		this.profile_columns = new LinkedList<List<Profile>>();
		for (int i = 0; i < 5; ++i) {
			this.profile_columns.add(new LinkedList<Profile>()); 
		}
		this.max_single_id = 0;
		this.emails = new HashMap<String, UserEmail>();
		this.websockets = new HashMap<String, WebsocketSession>();
		this.groups = new LinkedList<Group>();
		this.isAdmin = isAdmin;
		this.sessionSave = sessionSave;
		this.appsDBmap = new HashMap<String, App>();
		this.websiteAppsDBmap = new HashMap<String, WebsiteApp>();
		this.appsIDmap = new HashMap<Integer, App>();
		this.status = status;
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
	
	public Map<String, UserEmail> getUserEmails() {
		return emails;
	}
	
	public List<List<Profile>> getProfileColumns() {
		return this.profile_columns;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
	
	public SessionSave getSessionSave() {
		return sessionSave;
	}
	
	public Map<String, App> getAppsDBmap() {
		return appsDBmap;
	}
	
	public Map<String, WebsiteApp> getWebsiteAppsDBmap() {
		return websiteAppsDBmap;
	}
	
	public Map<Integer, App> getAppsIDmap() {
		return appsIDmap;
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
		this.profile_columns = Profile.loadProfiles(this, sm);
	}
	
	public void removeEmail(UserEmail email) {
		this.emails.remove(email);
	}
	
	public void removeProfile(int single_id, ServletManager sm) throws GeneralException {
		Iterator<List<Profile>> it = this.profile_columns.iterator();
		while (it.hasNext()) {
			List<Profile> column = it.next();
			Iterator<Profile> it2 = column.iterator();
			while (it2.hasNext()) {
				Profile profile = it2.next();
				if (profile.getSingleId() == single_id) {
					DataBaseConnection db = sm.getDB();
					int transaction = db.startTransaction();
					profile.removeFromDB(sm);
					column.remove(profile);
					this.updateProfilesIndex(sm);
					db.commitTransaction(transaction);
					return;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.InternError, "This profile dosen't exist.");
	}
	
	public void updateProfilesIndex(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (int i = 0; i < profile_columns.size(); ++i) {
			for (int j = 0; j < profile_columns.get(i).size(); ++j) {
				if (profile_columns.get(i).get(j).getPositionIdx() != j) {
					profile_columns.get(i).get(j).setPositionIdx(j, sm);
				}
				if (profile_columns.get(i).get(j).getColumnIdx() != i) {
					profile_columns.get(i).get(j).setColumnIdx(i, sm);
				}
			}
		}
		db.commitTransaction(transaction);
	}
	
	public Profile getProfile(int single_id) throws GeneralException {
		for (List<Profile> column: this.profile_columns) {
			for (Profile profile: column) {
				if (profile.getSingleId() == single_id)
					return profile;
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This profile's single_id dosen't exist.");
	}
	
	public App getApp(int single_id) throws GeneralException {
		for (List<Profile> column: this.profile_columns) {
			for (Profile profile: column) {
				for (App app: profile.getApps()) {
					if (app.getSingleId() == single_id)
						return app;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This app's single_id dosen't exist.");
	}
	
	public App getAppWithDBid(String DBid) throws GeneralException {
		for (List<Profile> column: this.profile_columns) {
			for (Profile profile: column) {
				for (App app: profile.getApps()) {
					if (app.getDBid() == DBid)
						return app;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.ClientError, "This app's single_id dosen't exist.");
	}
	
	public void removeDefinitly(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		for (Map.Entry<String, UserEmail> entry : emails.entrySet()) {
			entry.getValue().removeFromDB(sm);
		}
		for (List<Profile> column: this.profile_columns) {
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
	
	public Map<String, WebsocketSession> getWebsockets() {
		return this.websockets;
	}

	public void removeWebsocket(Session session) {
		this.websockets.remove(session.getId());
	}
	
	public void removeWebsocket(WebsocketSession session) {
		this.websockets.remove(session.getSessionId());
	}

	public void addWebsocket(WebsocketSession wSession) throws GeneralException {
		this.websockets.put(wSession.getSessionId() , wSession);
	}
	
	public int getMostEmptyProfileColumn() {
		int col = 0;
		int minSize = -1;
		for (List<Profile> column : this.profile_columns) {
			int colSize = 0;
			if (this.profile_columns.indexOf(column) != 0){
				for (Profile profile: column) {
					colSize += profile.getSize();
				}
				if (minSize == - 1 || colSize < minSize) {
					minSize = colSize;
					col = this.profile_columns.indexOf(column);
				}
			}
		}
		return col;
	}
	
	public void deconnect(ServletManager sm) {
		@SuppressWarnings("unchecked")
		Map<String, User> users = (Map<String, User>) sm.getContextAttr("users");
		if (this.websockets.isEmpty())
			users.remove(this.email);
	}
	
	public boolean isAdmin() {
		return this.isAdmin;
	}
	
	public static String findDBid(String email, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT id FROM users WHERE email='" + email + "';");
			if (rs.next()) {
				return (rs.getString(1));
			} else {
				throw new GeneralException(ServletManager.Code.ClientError, "This user dosen't exist."); 
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static int getMostEmptyProfileColumnForUnconnected(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			Integer[] columns = new Integer[5];
			for (int i = 0; i < Profile.MAX_COLUMN; ++i) {
				columns[i] = 0;
			}
			ResultSet rs = db.get("SELECT id, column_idx FROM profiles WHERE user_id=" + db_id + ";");
			while (rs.next()) {
				columns[rs.getInt(2)] += Profile.getSizeForUnconnected(rs.getString(1), sm);
			}
			int col = 1;
			int minSize = -1;
			for (int i = 1; i < Profile.MAX_COLUMN; ++i) {
				if (minSize == - 1 || columns[i] < minSize) {
					minSize = columns[i];
					col = i;
				}
			}
			return col;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static int getColumnNextPositionForUnconnected(String db_id, int column_idx, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("SELECT count(*) FROM profiles WHERE user_id=" + db_id + " AND column_idx=" + column_idx + ";");
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new GeneralException(ServletManager.Code.InternError, "Bizare.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public List<String> getEmails() {
		List<String> res = new LinkedList<String> ();
		for (Map.Entry<String, UserEmail> entry : emails.entrySet()) {
			res.add(entry.getValue().getEmail());
		}
		return res;
	}
	
	public List<String> getVerifiedEmails() {
		List<String> verifiedEmails = new LinkedList<String> ();
		for (Map.Entry<String, UserEmail> entry : emails.entrySet()) {
			if (entry.getValue().isVerified())
				verifiedEmails.add(entry.getValue().getEmail());
		}
		return verifiedEmails;
	}
	
	public List<String> getUnverifiedEmails() {
		List<String> unverifiedEmails = new LinkedList<String> ();
		for (Map.Entry<String, UserEmail> entry : emails.entrySet()) {
			if (!entry.getValue().isVerified())
				unverifiedEmails.add(entry.getValue().getEmail());
		}
		return unverifiedEmails;
	}
	
	public Profile addProfile(String name, String color, ServletManager sm) throws GeneralException {
		int column = this.getMostEmptyProfileColumn();
		Profile newProfile = Profile.createPersonnalProfile(this, column, this.getProfileColumns().get(column).size(), name, color, sm);
		this.profile_columns.get(column).add(newProfile);
		return newProfile;
	}

	public void removeApp(int single_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		App app = this.getApp(single_id);
		Profile profile = app.getProfile();
		profile.getApps().remove(app);
		app.removeFromDB(sm);
		profile.updateAppsIndex(sm);
		db.commitTransaction(transaction);
	}

	public void moveProfile(int profileId, int columnIdx, int position, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		Profile profile = this.getProfile(profileId);
		if (columnIdx < 1 || columnIdx >= Profile.MAX_COLUMN)
			throw new GeneralException(ServletManager.Code.ClientError, "Wrong columnIdx.");
		if (position < 0 || position > this.profile_columns.get(columnIdx).size())
			throw new GeneralException(ServletManager.Code.ClientError, "Wrong position.");
		if (columnIdx == profile.getColumnIdx() && position > profile.getPositionIdx()) {
			position--;
		}
		this.profile_columns.get(profile.getColumnIdx()).remove(profile);
		this.profile_columns.get(columnIdx).add(position, profile);
		this.updateProfilesIndex(sm);
		db.commitTransaction(transaction);
	}
	
	public void moveApp(int appId, int profileIdDest, int positionDest, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		App app = this.getApp(appId);
		Profile profileDest = this.getProfile(profileIdDest);
		if (profileDest == app.getProfile()) {
			if (positionDest > app.getPosition())
				positionDest--;
			profileDest.getApps().remove(app);
			profileDest.getApps().add(positionDest, app);
			profileDest.updateAppsIndex(sm);
		} else {
			if (profileDest.getGroupProfile() == null || (profileDest.getGroupProfile().isCommon() == false && profileDest.getGroupProfile().getPerms().havePermission(ProfilePermissions.Perm.ADDAPP.ordinal())))
				throw new GeneralException(ServletManager.Code.ClientWarning, "You don't have the permission to add app in this profile.");
			Profile profileSrc = app.getProfile();
			if (profileSrc.getGroupProfile() == null || (profileSrc.getGroupProfile().isCommon() == false && profileSrc.getGroupProfile().getPerms().havePermission(ProfilePermissions.Perm.MOVE_APP_OUTSIDE.ordinal())))
				throw new GeneralException(ServletManager.Code.ClientWarning, "You don't have the permission to move app out of this profile.");
			profileSrc.getApps().remove(app);
			profileSrc.updateAppsIndex(sm);
			profileDest.getApps().add(positionDest, app);
			profileDest.updateAppsIndex(sm);
		}
		db.commitTransaction(transaction);
	}

	public void putAllSockets(Map<String, WebsocketSession> sessionWebsockets) throws GeneralException {
		if (ServletManager.debug && sessionWebsockets == null)
			return;
		else if (sessionWebsockets == null)
			throw new GeneralException(ServletManager.Code.ClientError, "Unconnected session is null");
		this.websockets.putAll(sessionWebsockets);
	}
	
	public String toString() {
		return ("User " + this.first_name); 
	}

	public void removeWebsockets(Map<String, WebsocketSession> sessionWebsockets) throws GeneralException {
		if (ServletManager.debug && sessionWebsockets == null)
			return;
		else if (sessionWebsockets == null)
			throw new GeneralException(ServletManager.Code.ClientError, "Browser websockets is null");
		for (Map.Entry<String, WebsocketSession> entry : sessionWebsockets.entrySet())
			this.websockets.remove(entry.getKey());
	}

	public void sendVerificationEmail(String email, ServletManager sm) throws GeneralException {
		if (this.emails.get(email) != null) {
			this.emails.get(email).askForVerification(this, sm);
		} else {
			throw new GeneralException(ServletManager.Code.ClientError, "This email dosen't exist.");
		}
	}

	public void verifieEmail(String email, String code, ServletManager sm) throws GeneralException {
		if (this.emails.get(email) != null) {
			this.emails.get(email).verifie(code, sm);
		} else {
			throw new GeneralException(ServletManager.Code.ClientError, "This email dosen't exist.");
		}
	}
		
	public void passStep(String tutoStep, DataBaseConnection db) throws GeneralException {
		if (tutoStep.equals("saw_group")) {
			for(Group group : this.groups)
				group.tutoStepDone(this.db_id, db);
		}
		else
			this.status.passStep(tutoStep, db);
	}
	
	public boolean tutoDone() {
		return this.status.tutoIsDone();
	}

	public void addEmailIfNeeded(String email, ServletManager sm) throws GeneralException {
		UserEmail userEmail = this.emails.get(email);
		if (userEmail != null)
			return;
		userEmail = UserEmail.createUserEmail(email, this, false, sm);
		this.emails.put(email, userEmail);
		
	}
}
