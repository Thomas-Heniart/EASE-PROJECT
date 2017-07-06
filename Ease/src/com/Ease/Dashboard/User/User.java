package com.Ease.Dashboard.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import com.Ease.Context.Catalog.Tag;
import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.DashboardManager;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Update.UpdateManager;
import com.Ease.Utils.*;
import com.Ease.websocketV1.WebSocketManager;
import com.Ease.websocketV1.WebSocketSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class User {

    enum Data {
        NOTHING, ID, FIRSTNAME, EMAIL, KEYSID, OPTIONSID, REGISTRATIONDATE, STATUSID
    }

    @SuppressWarnings("unchecked")
    public static User loadUser(String email, String password, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        Map<String, User> usersMap = (Map<String, User>) context.getAttribute("users");
        User connectedUser = usersMap.get(email);
        if (connectedUser != null) {
            try {
                connectedUser.getKeys().isGoodPassword(password);
            } catch (GeneralException e) {
                throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
            }
            return connectedUser;
        }
        DatabaseRequest request = db.prepareRequest("SELECT users.* FROM users LEFT JOIN teamUsers ON users.id = teamUsers.user_id WHERE users.email = ? OR teamUsers.email = ?");
        request.setString(email);
        request.setString(email);
        DatabaseResult rs = request.get();
        int transaction = db.startTransaction();
        if (rs.next()) {
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
        request = db.prepareRequest("SELECT saw_group FROM groupsAndUsersMap WHERE user_id = ? LIMIT 1;");
        request.setInt(db_id);
        rs2 = request.get();
        boolean sawGroupProfile = false;
        if (rs2.next())
            sawGroupProfile = rs2.getBoolean(1);
        SessionSave sessionSave = SessionSave.createSessionSave(keys.getKeyUser(), db_id, db);
        User newUser = new User(db_id, firstName, email, keys, options, isAdmin, sawGroupProfile,
                sessionSave, status);
        newUser.loadTeamUsers(context);
        newUser.initializeDashboardManager(context, db);
        newUser.loadExtensionKeys(db);
        newUser.loadEmails(db);
        for (App app : newUser.getDashboardManager().getApps()) {
            if (app.getType().equals("LogwithApp")) {
                LogwithApp logwithApp = (LogwithApp) app;
                App websiteApp = newUser.getDashboardManager().getWebsiteAppWithId(logwithApp.getLogwithDBid());
                logwithApp.rempLogwith((WebsiteApp) websiteApp);
            }
        }
        request = db.prepareRequest("SELECT group_id FROM groupsAndUsersMap WHERE user_id= ?;");
        request.setInt(newUser.getDBid());
        rs2 = request.get();
        Group userGroup;
        while (rs2.next()) {
            userGroup = GroupManager.getGroupManager(context).getGroupFromDBid(rs2.getString(1));
            if (userGroup != null)
                newUser.getGroups().add(userGroup);
        }
        return newUser;
    }

    @SuppressWarnings("unchecked")
    public static User createUser(String email, String firstName, String password, Long registration_date, Boolean send_news, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        Option opt = Option.createOption(db);
        Keys keys = Keys.createKeys(password, db);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String registrationDate = dateFormat.format(new Date(registration_date));
        Status status = Status.createStatus(send_news, db);
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
        UserEmail userEmail = UserEmail.createUserEmail(email, newUser, false, db);
        newUser.getUserEmails().put(email, userEmail);
        newUser.passStep("CGU", db);
        newUser.passStep("first_connection", db);
        //newUser.initializeUpdateManager(context, db);
        newUser.sendVerificationEmail(email, true, db);
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
    //	protected Map<String, WebsocketSession> websockets;
    protected List<WebSocketSession> webSocketSessions;
    protected WebSocketManager webSocketManager;
    protected List<Group> groups;
    protected boolean isAdmin;
    protected boolean sawGroupProfile;
    protected Status status;
    protected ExtensionKeys extensionKeys;
    protected UpdateManager updateManager;

    protected SessionSave sessionSave;
    protected DashboardManager dashboardManager;
    protected List<TeamUser> teamUsers = new LinkedList<>();

    public User(String db_id, String first_name, String email, Keys keys, Option opt, boolean isAdmin,
                boolean sawGroupProfile, SessionSave sessionSave, Status status) {
        this.db_id = db_id;
        this.first_name = first_name;
        this.email = email;
        this.keys = keys;
        this.opt = opt;
        this.emails = new HashMap<String, UserEmail>();
        this.webSocketManager = new WebSocketManager();
        this.groups = new LinkedList<Group>();
        this.isAdmin = isAdmin;
        this.sessionSave = sessionSave;
        this.status = status;
        this.sawGroupProfile = sawGroupProfile;
    }

    public void initializeUpdateManager(ServletContext context, DataBaseConnection db) throws GeneralException {
        this.updateManager = new UpdateManager(context, db, this);
    }

    public void initializeDashboardManager(ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        this.dashboardManager = new DashboardManager(this, context, db);
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

    public UpdateManager getUpdateManager() {
        return this.updateManager;
    }

    public DashboardManager getDashboardManager() {
        return this.dashboardManager;
    }

	/*
     * public Status getStatus() { return status; }
	 */

    public Map<String, UserEmail> getUserEmails() {
        return emails;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Set<Infrastructure> getInfras() {
        Set<Infrastructure> infras = new HashSet<Infrastructure>();
        this.groups.forEach((group) -> {
            infras.add(group.getInfra());
        });
        return infras;
    }

    public SessionSave getSessionSave() {
        return sessionSave;
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

    public void passStep(String tutoStep, DataBaseConnection db) throws GeneralException {
        if (tutoStep.equals("saw_group")) {
            for (Group group : this.groups)
                group.tutoStepDone(this.db_id, db);
            this.sawGroupProfile = true;
        } else
            this.status.passStep(tutoStep, db);
    }

    public boolean tutoDone() {
        return this.status.tutoIsDone();
    }

    public boolean appsImported() {
        return this.status.appsImported();
    }

    public boolean allTipsDone() {
        return this.status.allTipsDone();
    }

    public boolean clickOnAppDone() {
        return this.status.clickOnAppDone();
    }

    public boolean moveAppDone() {
        return this.status.moveAppDone();
    }

    public boolean openCatalogDone() {
        return this.status.openCatalogDone();
    }

    public boolean addAnAppDone() {
        return this.status.addAnAppDone();
    }

    public boolean sawGroupProfile() {
        if (this.groups.isEmpty())
            return true;
        else
            return this.sawGroupProfile;
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

    public boolean canSeeTag(Tag tag) {
        for (Group group : this.groups) {
            if (tag.containsGroupId(group.getDBid()))
                return true;
        }
        return tag.isPublic();
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean isInGroup() {
        return !this.groups.isEmpty();
    }

    public void deleteFromDb(ServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        this.dashboardManager.removeFromDB(sm);
        for (UserEmail email : this.emails.values())
            email.removeFromDB(sm);
        this.sessionSave.eraseFromDB(sm.getDB());
        DatabaseRequest request = db.prepareRequest("DELETE FROM admins WHERE user_id = ?;");
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
        request = db.prepareRequest("DELETE FROM users WHERE id= ?;");
        request.setInt(db_id);
        request.set();
        this.keys.removeFromDB(sm);
        this.opt.removeFromDB(sm);
        db.commitTransaction(transaction);
    }

    public int getWebsiteCount(Website website) {
        int res = 0;
        for (WebsiteApp app : this.dashboardManager.getWebsiteApps()) {
            if (app.getSite().equals(website))
                res++;
        }
        return res;
    }

    public boolean isGroupAdmin(Group group) {
        int idx = this.groups.indexOf(group);
        if (idx == -1)
            return false;
        Group tmp = this.groups.get(idx);
        return tmp.isAdmin(this.db_id);
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

    public void loadTeamUsers(ServletContext context) throws HttpServletException, GeneralException {
        HibernateQuery query = new HibernateQuery();
        query.querySQLString("SELECT id, team_id FROM teamUsers WHERE user_id = ?");
        query.setParameter(1, Integer.parseInt(this.db_id));
        List<Object[]> teamUsers = query.list();
        query.commit();
        for (Object[] teamUserIdAndTeamId : teamUsers) {
            Integer teamUser_id = (Integer) teamUserIdAndTeamId[0];
            Integer team_id = (Integer) teamUserIdAndTeamId[1];
            TeamManager teamManager = (TeamManager) context.getAttribute("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            for (Channel channel : team.getChannels()) {
                if (!channel.getTeamUsers().isEmpty())
                    System.out.println("Is that broken ? " + (!team.getTeamUsers().contains(channel.getTeamUsers().get(0))));
            }
            this.teamUsers.add(teamUser);
            teamUser.setDashboard_user(this);
            if (teamUser.isVerified() && !teamUser.isDisabled()) {
                teamUser.decipher_teamKey();
                team.decipherApps(teamUser.getDeciphered_teamKey());
            }
        }
    }

    public TeamUser getTeamUserForTeam(Team team) throws GeneralException {
        for (TeamUser teamUser : this.getTeamUsers()) {
            if (teamUser.getTeam() == team)
                return teamUser;
        }
        throw new GeneralException(ServletManager.Code.ClientError, "Internal problem");
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("first_name", this.getFirstName());
        JSONArray teams = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers())
            teams.add(teamUser.getTeam().getSimpleJson());
        res.put("teams", teams);
        return res;
    }
}
