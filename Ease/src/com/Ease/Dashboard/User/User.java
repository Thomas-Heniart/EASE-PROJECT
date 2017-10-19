package com.Ease.Dashboard.User;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.DashboardManager;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Notification.NotificationManager;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.websocketV1.WebSocketManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class User {

    enum Data {
        NOTHING, ID, FIRSTNAME, EMAIL, KEYSID, OPTIONSID, REGISTRATIONDATE, STATUSID
    }

    @SuppressWarnings("unchecked")
    public static User loadUser(String email, String password, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        Map<String, User> usersMap = (Map<String, User>) context.getAttribute("users");
        User connectedUser = usersMap.get(email);
        if (connectedUser != null) {
            if (!connectedUser.getKeys().isGoodPassword(password))
                throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
            return connectedUser;
        }
        DatabaseRequest request = db.prepareRequest("SELECT * FROM users WHERE email = ?");
        request.setString(email);
        DatabaseResult rs = request.get();
        int transaction = db.startTransaction();
        if (rs.next()) {
            if (!email.equals(email.toLowerCase())) {
                request = db.prepareRequest("UPDATE users SET email = ? WHERE id = ?;");
                request.setString(email.toLowerCase());
                request.setInt(rs.getInt("id"));
                request.set();
            }
            Keys keys = Keys.loadKeys(rs.getString("key_id"), password, db);
            User newUser = loadUserWithKeys(rs, keys, context, db);
            db.commitTransaction(transaction);
            return newUser;
        } else {
            throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
        }
    }

    private void loadEmails(DataBaseConnection db) throws GeneralException {
        this.emails = UserEmail.loadEmails(db_id, this, db);
    }

    public static User loadUserFromJWT(JWToken jwToken, com.Ease.Utils.Servlets.ServletManager sm, DataBaseConnection db) throws HttpServletException {
        try {
            String db_id = jwToken.getUser_id();
            String keyUser = jwToken.getKeyUser();
            DatabaseRequest request = db.prepareRequest("SELECT * FROM users WHERE id = ?");
            request.setInt(db_id);
            DatabaseResult rs = request.get();
            int transaction = db.startTransaction();
            if (rs.next()) {
                String email = rs.getString(Data.EMAIL.ordinal());
                Map<String, User> usersMap = (Map<String, User>) sm.getContextAttr("users");
                User connectedUser = usersMap.get(email);
                if (connectedUser != null)
                    return connectedUser;
                Keys keys = Keys.loadKeysWithoutPassword(rs.getString(Data.KEYSID.ordinal()), keyUser, db);
                User newUser = loadUserWithKeys(rs, keys, sm.getServletContext(), db);
                db.commitTransaction(transaction);
                HibernateQuery hibernateQuery = new HibernateQuery();
                for (TeamUser teamUser : newUser.getTeamUsers()) {
                    if (!teamUser.isVerified() && teamUser.getTeamKey() != null) {
                        teamUser.finalizeRegistration();
                        hibernateQuery.saveOrUpdateObject(teamUser);
                    }
                    if (teamUser.isVerified() && teamUser.getTeamKey() != null && teamUser.isDisabled()) {
                        String deciphered_teamKey = RSA.Decrypt(teamUser.getTeamKey(), newUser.getKeys().getPrivateKey());
                        teamUser.setTeamKey(newUser.encrypt(deciphered_teamKey));
                        teamUser.setDeciphered_teamKey(deciphered_teamKey);
                        teamUser.setDisabled(false);
                        hibernateQuery.saveOrUpdateObject(teamUser);
                        for (SharedApp sharedApp : teamUser.getSharedApps()) {
                            sharedApp.setDisableShared(false, db);
                        }
                    }
                }
                hibernateQuery.commit();
                usersMap.put(email, newUser);
                newUser.getDashboardManager().decipherApps(sm);
                return newUser;
            } else {
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid token");
            }
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static User loadUserFromCookies(SessionSave sessionSave, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        String db_id = sessionSave.getUserId();
        String keyUser = sessionSave.getKeyUser();
        sessionSave.eraseFromDB(db);
        DatabaseRequest request = db.prepareRequest("SELECT * FROM users WHERE id = ?");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        int transaction = db.startTransaction();
        if (rs.next()) {
            String email = rs.getString(Data.EMAIL.ordinal());
            Map<String, User> usersMap = (Map<String, User>) context.getAttribute("users");
            User connectedUser = usersMap.get(email);
            if (connectedUser != null)
                return connectedUser;
            Keys keys = Keys.loadKeysWithoutPassword(rs.getString(Data.KEYSID.ordinal()), keyUser, db);
            User newUser = loadUserWithKeys(rs, keys, context, db);
            db.commitTransaction(transaction);
            return newUser;
        } else {
            throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
        }

    }

    private static User loadUserWithKeys(DatabaseResult rs, Keys keys, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        String db_id = rs.getString(Data.ID.ordinal());
        String email = rs.getString(Data.EMAIL.ordinal());
        String firstName = rs.getString(Data.FIRSTNAME.ordinal());
        Option options = Option.loadOption(rs.getString(Data.OPTIONSID.ordinal()), db);
        Status status = Status.loadStatus(rs.getString(Data.STATUSID.ordinal()), db);
        DatabaseRequest request = db.prepareRequest("SELECT user_id FROM admins WHERE user_id = ?;");
        request.setInt(db_id);
        DatabaseResult rs2 = request.get();
        boolean isAdmin = rs2.next();
        SessionSave sessionSave = SessionSave.createSessionSave(keys.getKeyUser(), db_id, db);
        User newUser = new User(db_id, firstName, email, keys, options, isAdmin, false,
                sessionSave, status);
        newUser.loadTeamUsers(context, db);
        newUser.initializeDashboardManager(context, db);
        newUser.initializeNotificationManager();
        newUser.loadEmails(db);
        for (App app : newUser.getDashboardManager().getApps()) {
            if (app.getType().equals("LogwithApp")) {
                LogwithApp logwithApp = (LogwithApp) app;
                App websiteApp = newUser.getDashboardManager().getWebsiteAppWithId(logwithApp.getLogwithDBid());
                logwithApp.rempLogwith((WebsiteApp) websiteApp);
            }
        }
        newUser.loadJWT((Key) context.getAttribute("secret"), db);
        return newUser;
    }

    @SuppressWarnings("unchecked")
    public static User createUser(String email, String firstName, String password, Long registration_date, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        Option opt = Option.createOption(db);
        Keys keys = Keys.createKeys(password, db);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String registrationDate = dateFormat.format(new Date(registration_date));
        Status status = Status.createStatus(db);
        DatabaseRequest request = db.prepareRequest("SELECT * FROM users LEFT JOIN teamUsers ON users.id = teamUsers.user_id WHERE users.email = ? OR teamUsers.email = ?;");
        request.setString(email);
        request.setString(email);
        if (request.get().next())
            throw new HttpServletException(HttpStatus.BadRequest, "Email already taken.");
        request = db.prepareRequest("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)");
        request.setNull();
        request.setString(firstName);
        request.setString(email);
        request.setInt(keys.getDBid());
        request.setInt(opt.getDb_id());
        request.setString(registrationDate);
        request.setInt(status.getDbId());
        String db_id = request.set().toString();
        SessionSave sessionSave = SessionSave.createSessionSave(keys.getKeyUser(), db_id, db);
        User newUser = new User(db_id, firstName, email, keys, opt, false, false, sessionSave, status);
        Profile.createPersonnalProfiles(newUser, db);
        newUser.initializeDashboardManager(context, db);
        newUser.initializeNotificationManager();
        UserEmail userEmail = UserEmail.createUserEmail(email, newUser, true, db);
        newUser.getUserEmails().put(email, userEmail);
        //newUser.initializeUpdateManager(context, db);
        request = db.prepareRequest("DELETE FROM pendingRegistrations WHERE email = ?;");
        request.setString(email);
        request.set();
        request = db.prepareRequest("DELETE FROM userPendingRegistrations WHERE email = ?;");
        request.setString(email);
        request.set();
        db.commitTransaction(transaction);
        return newUser;
    }

    protected String db_id;
    protected String first_name;
    protected String email;
    protected Keys keys;
    protected Option opt;
    protected Map<String, UserEmail> emails;
    protected WebSocketManager webSocketManager;
    //protected List<Group> groups = new LinkedList<>();
    protected boolean isAdmin;
    protected boolean sawGroupProfile;
    protected Status status;
    protected ExtensionKeys extensionKeys;

    protected SessionSave sessionSave;
    private JWToken jwt;
    protected DashboardManager dashboardManager;
    protected List<TeamUser> teamUsers = new LinkedList<>();

    protected NotificationManager notificationManager;

    public User(String db_id, String first_name, String email, Keys keys, Option opt, boolean isAdmin,
                boolean sawGroupProfile, SessionSave sessionSave, Status status) {
        this.db_id = db_id;
        this.first_name = first_name;
        this.email = email;
        this.keys = keys;
        this.opt = opt;
        this.emails = new HashMap<>();
        this.webSocketManager = new WebSocketManager();
        this.isAdmin = isAdmin;
        this.sessionSave = sessionSave;
        this.status = status;
        this.sawGroupProfile = sawGroupProfile;
    }

    private void initializeDashboardManager(ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        this.dashboardManager = new DashboardManager(this, context, db);
    }

    private void initializeNotificationManager() {
        this.notificationManager = new NotificationManager(this.db_id);
    }

	/*
     *
	 * Getter and Setter
	 * 
	 */

    public String getDBid() {
        return this.db_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE users set firstName = ? WHERE id = ?;");
        request.setString(first_name);
        request.setInt(db_id);
        request.set();
        this.first_name = first_name;
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

    public DashboardManager getDashboardManager() {
        return this.dashboardManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    /*
     * public Status getStatus() { return status; }
	 */

    public Map<String, UserEmail> getUserEmails() {
        return emails;
    }

    /* public List<Group> getGroups() {
        return groups;
    }

    public Set<Infrastructure> getInfras() {
        Set<Infrastructure> infras = new HashSet<Infrastructure>();
        this.groups.forEach((group) -> {
            infras.add(group.getInfra());
        });
        return infras;
    } */

    public SessionSave getSessionSave() {
        return sessionSave;
    }

    public JWToken getJwt() {
        return this.jwt;
    }

    public void setJwt(JWToken jwt) {
        this.jwt = jwt;
    }

	/*
     *
	 * Utils
	 * 
	 */

    public void removeEmail(UserEmail email) {
        this.emails.remove(email);
    }

    public String encrypt(String password) throws GeneralException {
        return this.keys.encrypt(password);
    }

    public String decrypt(String password) throws GeneralException {
        return this.keys.decrypt(password);
    }

    // WebSockets implementation

    public WebSocketManager getWebSocketManager() {
        return this.webSocketManager;
    }
    // WebSocket implementation end

    public void deconnect(ServletManager sm) {
        @SuppressWarnings("unchecked")
        Map<String, User> users = (Map<String, User>) sm.getContextAttr("users");
//		if (this.websockets.isEmpty())
        users.remove(this.email);
    }

    public void logoutFromSession(String session_id, ServletContext context, DataBaseConnection db) throws HttpServletException {
        try {
            Map<String, User> sessionIdUserMap = (Map<String, User>) context.getAttribute("sessionIdUserMap");
            sessionIdUserMap.remove(session_id);
            this.getSessionSave().eraseFromDB(db);
            Map<String, User> users = (Map<String, User>) context.getAttribute("users");
            if (sessionIdUserMap.containsValue(this))
                return;
            for (TeamUser teamUser : this.getTeamUsers())
                teamUser.disconnect();
            users.remove(this.getEmail());
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public static String findDBid(String email, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT id FROM users WHERE email= ?;");
        request.setString(email);
        DatabaseResult rs = request.get();
        if (rs.next())
            return (rs.getString(1));
        else
            throw new GeneralException(ServletManager.Code.ClientError, "This user dosen't exist.");
    }

    public static int getMostEmptyProfileColumnForUnconnected(String db_id, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        Integer[] columns = new Integer[5];
        for (int i = 0; i < Profile.MAX_COLUMN; ++i)
            columns[i] = 0;
        DatabaseRequest request = db.prepareRequest("SELECT id, column_idx FROM profiles WHERE user_id= ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        while (rs.next())
            columns[rs.getInt(2)] += Profile.getSizeForUnconnected(rs.getString(1), sm);
        int col = 1;
        int minSize = -1;
        for (int i = 1; i < Profile.MAX_COLUMN; ++i) {
            if (minSize == -1 || columns[i] < minSize) {
                minSize = columns[i];
                col = i;
            }
        }
        return col;
    }

    public static int getColumnNextPositionForUnconnected(String db_id, int column_idx, ServletManager sm)
            throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT count(*) FROM profiles WHERE user_id= ? AND column_idx= ?;");
        request.setInt(db_id);
        request.setInt(column_idx);
        DatabaseResult rs = request.get();
        if (rs.next())
            return rs.getInt(1);
        else
            throw new GeneralException(ServletManager.Code.InternError, "Bizare.");
    }

    public void removeEmailIfNeeded(String email, ServletManager sm) throws GeneralException {
        if (this.emails.get(email) != null && this.emails.get(email).removeIfNotUsed(sm.getDB()))
            this.emails.remove(email);
    }

    public Map<String, UserEmail> getEmails() {
        return this.emails;
    }

    public List<String> getEmailsString() {
        List<String> emails = new LinkedList<String>();
        for (Map.Entry<String, UserEmail> entry : this.emails.entrySet()) {
            emails.add(entry.getValue().getEmail());
        }
        return emails;
    }

    public List<String> getVerifiedEmails() {
        List<String> verifiedEmails = new LinkedList<String>();
        for (Map.Entry<String, UserEmail> entry : emails.entrySet()) {
            if (entry.getValue().isVerified())
                verifiedEmails.add(entry.getValue().getEmail());
        }
        return verifiedEmails;
    }

    public List<String> getUnverifiedEmails() {
        List<String> unverifiedEmails = new LinkedList<String>();
        for (Map.Entry<String, UserEmail> entry : emails.entrySet()) {
            if (!entry.getValue().isVerified())
                unverifiedEmails.add(entry.getValue().getEmail());
        }
        return unverifiedEmails;
    }

    public boolean haveThisEmail(String email) {
        for (Map.Entry<String, UserEmail> entry : emails.entrySet()) {
            if (entry.getValue().getEmail() == email)
                return true;
        }
        return false;
    }

    public String toString() {
        return ("User " + this.first_name);
    }

/*	public void putAllSockets(Map<String, WebsocketSession> sessionWebsockets) throws GeneralException {
        if (ServletManager.debug && sessionWebsockets == null)
			return;
		else if (sessionWebsockets == null)
			throw new GeneralException(ServletManager.Code.ClientError, "Unconnected session is null");
		this.websockets.putAll(sessionWebsockets);
	}


	public void removeWebsockets(Map<String, WebsocketSession> sessionWebsockets) throws GeneralException {
		if (ServletManager.debug && sessionWebsockets == null)
			return;
		else if (sessionWebsockets == null)
			throw new GeneralException(ServletManager.Code.ClientError, "Browser websockets is null");
		for (Map.Entry<String, WebsocketSession> entry : sessionWebsockets.entrySet())
			this.websockets.remove(entry.getKey());
	}*/

    public void sendVerificationEmail(String email, boolean newUser, DataBaseConnection db) throws GeneralException {
        if (this.emails.get(email) != null) {
            this.emails.get(email).askForVerification(this, newUser, db);
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

    public void addEmailIfNeeded(String email, ServletManager sm) throws GeneralException {
        UserEmail userEmail = this.emails.get(email);
        if (userEmail != null)
            return;
        userEmail = UserEmail.createUserEmail(email, this, false, sm.getDB());
        this.emails.put(email, userEmail);
    }

    public void rememberNotIntegratedApp(Object o) {

    }

    public void rememberNotIntegratedFacebookApp(Object o) {

    }

    public void rememberNotIntegratedLinkedinApp(Object o) {

    }

    public void loadExtensionKeys(DataBaseConnection db) throws GeneralException {
        extensionKeys = ExtensionKeys.loadExtensionKeys(this, db);
    }

    public ExtensionKeys getExtensionKeys() {
        return extensionKeys;
    }

    public Status getStatus() {
        return this.status;
    }

    public void deleteFromDb(DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        this.dashboardManager.removeFromDB(db);
        for (UserEmail email : this.emails.values())
            email.removeFromDB(db);
        this.sessionSave.eraseFromDB(db);
        DatabaseRequest request = db.prepareRequest("DELETE FROM admins WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM jsonWebTokens WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM groupsAndUsersMap WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM infrastructuresAdminsMap WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM integrateWebsitesAndUsersMap WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM passwordLost WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM requestedWebsites WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM savedSessions WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM usersPrivateExtensions WHERE user_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM notifications WHERE user_id = ?");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM pendingJoinTeamRequests WHERE user_id = ?");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM users WHERE id= ?;");
        request.setInt(db_id);
        request.set();
        this.keys.removeFromDB(db);
        this.opt.removeFromDB(db);
        db.commitTransaction(transaction);
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void addTeamUser(TeamUser teamUser, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE teamUsers SET user_id = ? WHERE id = ?;");
        request.setInt(this.db_id);
        request.setInt(teamUser.getDb_id());
        request.set();
        this.teamUsers.add(teamUser);
        teamUser.setDashboard_user(this);
    }

    public void addTeamUser(TeamUser teamUser) {
        if (!this.teamUsers.contains(teamUser))
            this.teamUsers.add(teamUser);
    }

    public void loadTeamUsers(ServletContext context, DataBaseConnection db) throws HttpServletException, GeneralException {
        TeamManager teamManager = (TeamManager) context.getAttribute("teamManager");
        DatabaseRequest request = db.prepareRequest("SELECT teamUsers.id, team_id FROM teamUsers JOIN teams ON teamUsers.team_id = teams.id WHERE user_id = ? AND teams.active = 1");
        request.setInt(Integer.parseInt(this.db_id));
        DatabaseResult rs = request.get();
        while (rs.next()) {
            Integer teamUser_id = rs.getInt(1);
            Integer team_id = rs.getInt(2);
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            this.teamUsers.add(teamUser);
            teamUser.setDashboard_user(this);
            if (teamUser.isVerified() && !teamUser.isDisabled()) {
                teamUser.decipher_teamKey();
                team.decipherApps(teamUser.getDeciphered_teamKey());
            }
        }
    }

    public TeamUser getTeamUserForTeam(Team team) throws HttpServletException {
        for (TeamUser teamUser : this.getTeamUsers()) {
            if (teamUser.getTeam() == team)
                return teamUser;
        }
        throw new HttpServletException(HttpStatus.BadRequest, "You are not in this team");
    }

    public JSONObject getJson() throws HttpServletException {
        JSONObject res = new JSONObject();
        res.put("email", this.getEmail());
        res.put("first_name", this.getFirstName());
        JSONArray teams = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers()) {
            JSONObject teamObject = teamUser.getTeam().getSimpleJson();
            teamObject.put("disabled", teamUser.isDisabled() || teamUser.getState() == 1 || (teamUser.getDepartureDate() != null && teamUser.getDepartureDate().getTime() <= new Date().getTime()));
            teamObject.put("state", teamUser.getState());
            teams.add(teamObject);
        }
        res.put("teams", teams);
        res.put("status", this.getStatus().getJson());
        return res;
    }

    public void passStep(String tutoStep, DataBaseConnection db) throws GeneralException {
        this.status.passStep(tutoStep, db);
    }

    public boolean isSchoolUser() {
        return this.email.endsWith("@iscparis.com") || this.email.endsWith("@edhec.com") || this.email.endsWith("ieseg.fr");
    }

    private void loadJWT(Key secret, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("SELECT id FROM jsonWebTokens WHERE user_id = ?;");
            request.setInt(this.getDBid());
            DatabaseResult rs = db.get();
            JWToken jwt;
            if (rs.next()) {
                jwt = JWToken.loadJWTokenWithKeyUser(rs.getInt(1), this.getEmail(), this.getFirstName(), this.getKeys().getKeyUser(), secret, db);
            }
            else
                jwt = JWToken.createJWTokenForUser(this, secret, db);
            this.jwt = jwt;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
