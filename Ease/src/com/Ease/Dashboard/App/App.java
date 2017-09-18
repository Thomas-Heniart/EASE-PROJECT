package com.Ease.Dashboard.App;

import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class App implements ShareableApp, SharedApp {

    public enum Data {
        NOTHING,
        ID,
        INSERT_DATE,
        TYPE,
        APP_INFO_ID,
        GROUP_APP_ID,
        POSITION
    }

	/*
     *
	 * Loader And Creator
	 * 
	 */

    public static List<App> loadApps(Profile profile, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        List<App> apps = new LinkedList<App>();
        DatabaseRequest request = db.prepareRequest("SELECT apps.*, position, sharedApps.id AS shared_app_id, team_id FROM apps JOIN profileAndAppMap ON apps.id = profileAndAppMap.app_id AND profileAndAppMap.profile_id = ? LEFT JOIN sharedApps ON apps.id = sharedApps.id ORDER BY position;");
        request.setInt(profile.getDBid());
        DatabaseResult rs = request.get();
        Integer db_id;
        int position;
        String insertDate;
        AppInformation infos;
        GroupApp groupApp = null;
        App app = null;
        while (rs.next()) {
            db_id = rs.getInt(Data.ID.ordinal());
            position = rs.getInt(Data.POSITION.ordinal());
            insertDate = rs.getString(Data.INSERT_DATE.ordinal());
            infos = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), db);
            String groupAppId = rs.getString(Data.GROUP_APP_ID.ordinal());
            if (groupAppId != null) {
                groupApp = GroupManager.getGroupManager(context).getGroupAppFromDBid(groupAppId);
            }
            Integer shared_app_id = rs.getInt("shared_app_id");
            if (shared_app_id > 0) {
                Integer team_id = rs.getInt("team_id");
                TeamManager teamManager = (TeamManager) context.getAttribute("teamManager");
                Team team = teamManager.getTeamWithId(team_id);
                SharedApp sharedApp = team.getAppManager().getSharedApp(shared_app_id);
                app = (App) sharedApp;
                app.setProfile(profile);
                app.setPosition(position);
            } else {
                switch (rs.getString(Data.TYPE.ordinal())) {
                    case "linkApp":
                        app = LinkApp.loadLinkApp(db_id, profile, position, insertDate, infos, groupApp, context, db);
                        break;
                    case "websiteApp":
                        app = WebsiteApp.loadWebsiteApp(db_id, profile, position, insertDate, infos, groupApp, context, db);
                        break;
                    default:
                        throw new GeneralException(ServletManager.Code.InternError, "This app type doesn't exist.");
                }
            }
            if (app.getPosition() != apps.size())
                app.setPosition(apps.size(), db);
            app.setDisabled(rs.getBoolean("disabled"));
            apps.add(app);
            groupApp = null;
        }
        return apps;
    }

    private void setProfile(Profile profile) {
        this.profile = profile;
    }

    private void setPosition(Integer position) {
        this.position = position;
    }

    public static List<ShareableApp> loadShareableAppsForTeam(Team team, ServletContext context, DataBaseConnection db) throws HttpServletException {
        try {
            List<ShareableApp> shareableApps = new LinkedList<>();
            DatabaseRequest request = db.prepareRequest("SELECT * FROM apps JOIN shareableApps ON apps.id = shareableApps.id WHERE team_id = ?;");
            request.setInt(team.getDb_id());
            DatabaseResult rs = request.get();
            Integer db_id;
            String insertDate;
            String description;
            AppInformation infos;
            ShareableApp shareableApp = null;
            while (rs.next()) {
                db_id = rs.getInt("apps.id");
                insertDate = rs.getString("insert_date");
                description = rs.getString("description");
                infos = AppInformation.loadAppInformation(rs.getString("app_info_id"), db);
                switch (rs.getString("type")) {
                    case "linkApp":
                        shareableApp = LinkApp.loadLinkApp(db_id, null, null, insertDate, infos, null, context, db);
                        break;
                    case "websiteApp":
                        shareableApp = WebsiteApp.loadWebsiteApp(db_id, null, null, insertDate, infos, null, context, db);
                        break;
                    default:
                        throw new GeneralException(ServletManager.Code.InternError, "This app type doesn't exist.");
                }
                shareableApp.setDescription(description);
                shareableApp.setOrigin(rs.getString("origin_type"), rs.getInt("origin_id"), team.getDb_id());
                TeamUser teamUser_owner = team.getTeamUserWithId(rs.getInt("teamUser_owner_id"));
                Integer channel_id = rs.getInt("channel_id");
                Channel channel = null;
                if (channel_id != null && channel_id > 0) {
                    channel = team.getChannelWithId(channel_id);
                    shareableApp.setChannel(channel);
                }
                shareableApp.setTeamUser_owner(teamUser_owner);
                shareableApps.add(shareableApp);
                DatabaseRequest request1 = db.prepareRequest("SELECT team_user_id FROM pendingJoinAppRequests WHERE shareable_app_id = ?;");
                request1.setInt(((App) shareableApp).getDBid());
                DatabaseResult rs2 = request1.get();
                while (rs2.next())
                    shareableApp.addPendingTeamUser(team.getTeamUserWithId(rs2.getInt(1)));
            }
            return shareableApps;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static List<SharedApp> loadSharedAppsForShareableApp(ShareableApp shareableApp, ServletContext context, DataBaseConnection db) throws HttpServletException {
        try {
            List<SharedApp> sharedApps = new LinkedList<>();
            DatabaseRequest request = db.prepareRequest("SELECT * FROM apps JOIN sharedApps ON sharedApps.id = apps.id AND sharedApps.shareable_app_id = ?;");
            request.setInt(((App) shareableApp).getDBid());
            DatabaseResult rs = request.get();
            Integer db_id;
            String insertDate;
            AppInformation infos;
            SharedApp sharedApp = null;
            while (rs.next()) {
                db_id = rs.getInt("apps.id");
                insertDate = rs.getString("insert_date");
                infos = AppInformation.loadAppInformation(rs.getString("app_info_id"), db);
                switch (rs.getString("type")) {
                    case "linkApp":
                        sharedApp = LinkApp.loadLinkApp(db_id, null, null, insertDate, infos, null, context, db);
                        break;
                    case "websiteApp":
                        sharedApp = WebsiteApp.loadWebsiteApp(db_id, null, null, insertDate, infos, null, context, db);
                        if (((App) sharedApp).isClassicApp()) {
                            sharedApp.setReceived(rs.getBoolean("received"));
                            sharedApp.setAdminHasAccess(rs.getBoolean("adminHasAccess"));
                        }
                        break;
                    default:
                        throw new GeneralException(ServletManager.Code.InternError, "This app type doesn't exist.");
                }
                sharedApp.setPinned(rs.getBoolean("pinned"));
                Integer teamUser_tenant_id = rs.getInt("teamUser_tenant_id");
                sharedApp.setTeamUser_tenant(shareableApp.getTeamUser_owner().getTeam().getTeamUserWithId(teamUser_tenant_id));
                sharedApp.setHolder(shareableApp);
                sharedApp.setCanSeeInformation(rs.getBoolean("canSeeInformation"));
                sharedApps.add(sharedApp);
            }
            return sharedApps;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static Integer createApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, db);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String insertDate = dateFormat.format(date);
        DatabaseRequest request = db.prepareRequest("INSERT INTO apps VALUES (NULL, ?, ?, ?, NULL, default);");
        request.setString(insertDate);
        request.setString(type);
        request.setInt(infos.getDb_id());
        Integer appDBid = request.set();
        if (profile != null && position != null) {
            request = db.prepareRequest("INSERT INTO profileAndAppMap values (NULL, ?, ?, ?)");
            request.setInt(profile.getDBid());
            request.setInt(appDBid);
            request.setInt(position);
            request.set();
        }
        elevator.put("appInfos", infos);
        elevator.put("insertDate", insertDate);
        db.commitTransaction(transaction);
        return appDBid;

    }

    public static Integer createSharedApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, Integer team_id, Integer channel_id, Integer team_user_tenant_id, App holder, boolean received, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, db);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String registrationDate = dateFormat.format(date);
        DatabaseRequest request = db.prepareRequest("INSERT INTO apps VALUES (NULL, ?, ?, ?, NULL, default);");
        request.setString(registrationDate);
        request.setString(type);
        request.setInt(infos.getDb_id());
        Integer appDBid = request.set();
        if (profile != null && position != null) {
            request = db.prepareRequest("INSERT INTO profileAndAppMap values (NULL, ?, ?, ?)");
            request.setInt(profile.getDBid());
            request.setInt(appDBid);
            request.setInt(position);
            request.set();
        }
        request = db.prepareRequest("INSERT INTO sharedApps values (?, ?, ?, ?, ?, ?, 0, ?, default);");
        request.setInt(appDBid);
        request.setInt(team_id);
        request.setInt(team_user_tenant_id);
        request.setInt(holder.getDBid());
        if (channel_id == null)
            request.setNull();
        else
            request.setInt(channel_id);
        request.setBoolean(received);
        Boolean canSeeInformation = (Boolean) elevator.get("canSeeInformation");
        if (canSeeInformation == null)
            canSeeInformation = false;
        request.setBoolean(canSeeInformation);
        request.set();
        db.commitTransaction(transaction);
        elevator.put("appInfos", infos);
        elevator.put("insertDate", registrationDate);
        return appDBid;
    }

	/*
     *
	 * Constructor
	 * 
	 */

    protected Integer db_id;
    protected Profile profile;
    protected Integer position;
    protected AppInformation informations;
    protected GroupApp groupApp;
    protected String insertDate;
    protected boolean received = true;
    protected boolean disabled;

    /* Interface ShareableApp */
    protected Map<Integer, SharedApp> sharedApps = new ConcurrentHashMap<>();
    protected TeamUser teamUser_owner;
    protected Channel channel;
    protected String description;
    protected JSONObject origin = new JSONObject();
    protected Map<Integer, TeamUser> pending_teamUsers = new ConcurrentHashMap<>();

    /* Interface SharedApp */
    protected ShareableApp holder;
    protected TeamUser teamUser_tenant;
    protected Boolean canSeeInformation;
    protected boolean adminHasAccess;
    protected boolean pinned = false;


    public App(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate) {
        this.db_id = db_id;
        this.profile = profile;
        this.position = position;
        this.informations = infos;
        this.groupApp = groupApp;
        this.insertDate = insertDate;
    }

    public App(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, ShareableApp holder) {
        this.db_id = db_id;
        this.profile = profile;
        this.position = position;
        this.informations = infos;
        this.groupApp = groupApp;
        this.insertDate = insertDate;
        this.holder = holder;
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        if (this.groupApp != null && (this.groupApp.isCommon() == true || !this.groupApp.getPerms().havePermission(AppPermissions.Perm.DELETE.ordinal())))
            throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permission to remove this app.");
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM profileAndAppMap WHERE app_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM apps WHERE id = ?;");
        request.setInt(db_id);
        request.set();
        if ((this.groupApp == null || this.groupApp.isCommon() == false) && (this.informations != null))
            informations.removeFromDb(db);
        db.commitTransaction(transaction);
    }

	/*
     *
	 * Getter And Setter
	 * 
	 */

    public Integer getDBid() {
        return db_id;
    }

    public String getName() {
        return this.informations.getName();
    }

    public void setName(String name, DataBaseConnection db) throws GeneralException {
        if (this.groupApp == null || (!this.groupApp.isCommon() && this.groupApp.getPerms().havePermission(AppPermissions.Perm.RENAME.ordinal()))) {
            this.informations.setName(name, db);
        } else {
            throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permission to change this app's name.");
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public Integer getPosition() {
        return position;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public AppInformation getAppInformation() {
        return informations;
    }

    public String getType() {
        String name;
        name = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1);
        return name;
    }

    public void setPosition(int pos, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE profileAndAppMap SET position = ? WHERE app_id = ?;");
        request.setInt(pos);
        request.setInt(db_id);
        request.set();
        this.position = pos;
    }

    public void setProfile(Profile profile, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE profileAndAppMap SET profile_id = ? WHERE app_id = ?;");
        request.setInt(profile.getDBid());
        request.setInt(db_id);
        request.set();
        this.profile = profile;
    }

    public boolean isReceived() {
        return this.received;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setDisabled(boolean disabled, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE apps SET disabled = ? WHERE id = ?;");
            request.setBoolean(disabled);
            request.setInt(this.getDBid());
            request.set();
            this.setDisabled(disabled);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public boolean havePerm(AppPermissions.Perm perm) {
        if (this.groupApp != null && (this.groupApp.isCommon() == true || !this.groupApp.getPerms().havePermission(perm.ordinal())))
            return false;
        return true;
    }

    public boolean isClassicApp() {
        return false;
    }

    public JSONArray getJSON(ServletManager sm) throws GeneralException {
        return new JSONArray();
    }

    public String getLogo() {
        throw new IllegalStateException("Shouldn't be there");
    }

    /* For sancho le robot */
    public boolean isEmpty() {
        return false;
    }

    public void fillJson(JSONObject json) {
        json.put("position", this.position);
        json.put("name", this.informations.getName());
        json.put("id", this.getDBid());
        if (this.groupApp != null) {
            JSONObject groupJson = new JSONObject();
            this.groupApp.fillJson(groupJson);
            json.put("groupApp", groupJson);
        }
    }

    /* Old version */
    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        if (position != null)
            jsonObject.put("position", position);
        jsonObject.put("name", this.getAppInformation().getName());
        jsonObject.put("id", this.getDBid());
        return jsonObject;
    }

    /* New version */
    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getAppInformation().getName());
        jsonObject.put("id", this.getDBid());
        return jsonObject;
    }

    public JSONArray getAccountInformationsJson() {
        return new JSONArray();
    }

    /* Interface SharedApp */
    @Override
    public void modifyShared(DataBaseConnection db, JSONObject editJson) throws HttpServletException {
        try {
            String name = (String) editJson.get("name");
            if (name != null && !name.equals(""))
                this.getAppInformation().setName(name, db);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }

    @Override
    public ShareableApp getHolder() {
        return this.holder;
    }

    @Override
    public void setHolder(ShareableApp shareableApp) {
        this.holder = shareableApp;
    }

    @Override
    public void deleteShared(DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            if (this.pinned)
                this.unpin(db);
            DatabaseRequest request = db.prepareRequest("DELETE FROM sharedApps WHERE id = ?;");
            request.setInt(this.getDBid());
            request.set();
            this.removeFromDB(db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError);
        }
    }

    @Override
    public void setTeamUser_tenant(TeamUser teamUser) {
        this.teamUser_tenant = teamUser;
    }

    @Override
    public TeamUser getTeamUser_tenant() {
        return this.teamUser_tenant;
    }

    @Override
    public JSONObject getSharedJSON() {
        JSONObject res = new JSONObject();
        res.put("team_user_id", this.teamUser_tenant.getDb_id());
        res.put("shared_app_id", Integer.valueOf(this.getDBid()));
        res.put("accepted", this.isReceived());
        res.put("profile_id", (this.getProfile() == null) ? -1 : this.getProfile().getDBid());
        return res;
    }

    @Override
    public void setAdminHasAccess(boolean b) {
        this.adminHasAccess = b;
    }

    @Override
    public void setAdminHasAccess(boolean b, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE sharedApps SET adminHasAccess = ? WHERE id = ?;");
            request.setBoolean(b);
            request.setInt(this.getDBid());
            request.set();
            this.setAdminHasAccess(b);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public boolean adminHasAccess() {
        return false;
    }

    @Override
    public void setCanSeeInformation(Boolean b) {
        this.canSeeInformation = b;
    }

    @Override
    public boolean canSeeInformation() {
        return this.canSeeInformation;
    }

    @Override
    public void setReceived(Boolean b) {
        this.received = b;
    }

    @Override
    public void accept(DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE sharedApps SET received = ? WHERE id = ?;");
            request.setBoolean(true);
            request.setInt(this.getDBid());
            request.set();
            this.setReceived(true);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public void setCanSeeInformation(Boolean canSeeInformation, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE sharedApps SET canSeeInformation = ? WHERE id = ?;");
            request.setBoolean(canSeeInformation);
            request.setInt(this.getDBid());
            request.set();
            this.setCanSeeInformation(canSeeInformation);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public void unpin(DataBaseConnection db) throws HttpServletException {
        if (this.getProfile() == null)
            return;
        try {
            int transaction = db.startTransaction();
            DatabaseRequest request = db.prepareRequest("DELETE FROM profileAndAppMap WHERE app_id = ?;");
            request.setInt(this.getDBid());
            request.set();
            this.setPinned(false, db);
            db.commitTransaction(transaction);
            this.getProfile().getUser().getDashboardManager().removeAppFromCollections(this);
            this.setProfile(null);
            this.setPosition(null);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public void pinToDashboard(Profile profile, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            if (this.getProfile() == profile)
                return;
            this.unpin(db);
            DatabaseRequest request = db.prepareRequest("INSERT INTO profileAndAppMap values (null, ?, ?, ?);");
            request.setInt(profile.getDBid());
            request.setInt(this.getDBid());
            request.setInt(profile.getApps().size());
            request.set();
            this.setPinned(true, db);
            db.commitTransaction(transaction);
            this.setProfile(profile);
            this.setPosition(profile.getApps().size());
            profile.addApp(this);
        } catch (GeneralException e) {
            e.printStackTrace();
        }

    }

    public void pinToDashboard(String name, Profile profile, DataBaseConnection db) {
        DatabaseRequest request;
        try {
            if (profile == null) {
                request = db.prepareRequest("DELETE FROM profileAndAppMap WHERE app_id = ?;");
                request.setInt(this.getDBid());
                request.set();
            } else {
                request = db.prepareRequest("SELECT id FROM profileAndAppMap WHERE app_id = ?;");
                request.setInt(this.getDBid());
                DatabaseResult rs = db.get();
                if (rs.next()) {
                    int profile_id = rs.getInt(1);
                    if (profile_id == Integer.valueOf(profile.getDBid()))
                        return;
                    request = db.prepareRequest("UPDATE profileAndAppMap SET profile_id = ?, position = ? WHERE id = ?;");
                    request.setInt(profile.getDBid());
                    request.setInt(rs.getInt(1));
                    request.setInt(profile.getApps().size());
                    request.set();
                    profile.addApp(this);
                } else {
                    request = db.prepareRequest("INSERT INTO profileAndAppMap values (null, ?, ?, ?);");
                    request.setInt(profile.getDBid());
                    request.setInt(this.getDBid());
                    request.setInt(profile.getApps().size());
                    request.set();
                    profile.addApp(this);
                }
            }

            request.set();
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    /* Interface ShareableApp */
    @Override
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException {
        throw new HttpServletException(HttpStatus.BadRequest, "App cannot be instantiate");
    }

    @Override
    public void modifyShareable(DataBaseConnection db, JSONObject editJson, SharedApp sharedApp) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            String description = (String) editJson.get("description");
            if (description == null)
                this.setDescription("", db);
            else
                this.setDescription(description, db);
            String name = (String) editJson.get("name");
            if (name != null)
                this.setName(name, db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public void deleteShareable(DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            List<SharedApp> sharedAppsToDelete = new LinkedList<>();
            for (SharedApp sharedApp : this.getSharedApps()) {
                sharedApp.deleteShared(db);
                sharedAppsToDelete.add(sharedApp);
            }
            for (SharedApp sharedApp : sharedAppsToDelete)
                this.removeSharedApp(sharedApp);
            DatabaseRequest request = db.prepareRequest("DELETE FROM pendingJoinAppRequests WHERE shareable_app_id = ?");
            request.setInt(this.getDBid());
            request.set();
            request = db.prepareRequest("DELETE FROM shareableApps WHERE id = ?;");
            request.setInt(this.getDBid());
            request.set();
            this.removeFromDB(db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError);
        }
    }

    @Override
    public TeamUser getTeamUser_owner() {
        return this.teamUser_owner;
    }

    @Override
    public List<TeamUser> getTeamUser_tenants() {
        List<TeamUser> teamUsers = new LinkedList<>();
        for (SharedApp sharedApp : this.getSharedApps())
            teamUsers.add(sharedApp.getTeamUser_tenant());
        return teamUsers;
    }

    @Override
    public Collection<SharedApp> getSharedApps() {
        if (this.sharedApps == null)
            this.sharedApps = new ConcurrentHashMap<>();
        return this.sharedApps.values();
    }

    @Override
    public void setSharedApps(List<SharedApp> sharedApps) {
        for (SharedApp sharedApp : sharedApps)
            this.addSharedApp(sharedApp);
    }

    @Override
    public void addSharedApp(SharedApp sharedApp) {
        this.sharedApps.put(((App) sharedApp).getDBid(), sharedApp);
    }

    @Override
    public SharedApp getSharedAppWithId(Integer sharedApp_id) throws HttpServletException {
        SharedApp sharedApp = this.sharedApps.get(sharedApp_id);
        if (sharedApp == null) {
            HttpServletException servletException = new HttpServletException(HttpStatus.BadRequest, "Wrong shared app id");
            servletException.printStackTrace();
            throw servletException;
        }
        return sharedApp;
    }

    @Override
    public void setTeamUser_owner(TeamUser teamUser_owner) {
        this.teamUser_owner = teamUser_owner;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public JSONObject getShareableJson() throws HttpServletException {
        try {
            JSONObject res = new JSONObject();
            res.put("id", Integer.valueOf(this.getDBid()));
            res.put("sender_id", this.getTeamUser_owner().getDb_id());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date shared_date = dateFormat.parse(this.getInsertDate());
            DateFormat dateFormat1 = new SimpleDateFormat("MMMM dd, HH:mm", Locale.US);
            res.put("shared_date", dateFormat1.format(shared_date));
            JSONArray receivers = new JSONArray();
            for (SharedApp sharedApp : this.sharedApps.values())
                receivers.add(sharedApp.getSharedJSON());
            /* for (SharedApp sharedApp : this.getSharedApps())
                receivers.add(sharedApp.getSharedJSON()); */
            res.put("receivers", receivers);
            JSONArray waitingTeamUsers = new JSONArray();
            for (TeamUser teamUser : this.getPendingTeamUsers())
                waitingTeamUsers.add(teamUser.getDb_id());
            res.put("sharing_requests", waitingTeamUsers);
            res.put("origin", this.getOrigin());
            res.put("description", this.getDescription());
            res.put("name", this.getAppInformation().getName());
            return res;
        } catch (ParseException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public JSONObject getNeededParams(PostServletManager sm) throws HttpServletException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("canSeeInformation", true);
        jsonObject.put("adminHasAccess", true);
        return jsonObject;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setDescription(String description, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE shareableApps SET description = ? WHERE id = ?;");
            request.setString(description);
            request.setInt(this.getDBid());
            request.set();
            this.setDescription(description);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setOrigin(String origin_type, Integer origin_id, Integer team_id) {
        this.origin.put("team_id", team_id);
        this.origin.put("type", origin_type);
        this.origin.put("id", origin_id);
    }

    @Override
    public JSONObject getOrigin() {
        return this.origin;
    }

    @Override
    public void transferOwnership(TeamUser teamUser_new_owner, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE shareableApps SET teamUser_owner_id = ?;");
            request.setInt(teamUser_new_owner.getDb_id());
            request.set();
            this.setTeamUser_owner(teamUser_new_owner);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public void removeSharedApp(SharedApp sharedApp) {
        this.sharedApps.remove(((App) sharedApp).getDBid());
    }

    @Override
    public void addPendingTeamUser(TeamUser teamUser) {
        this.pending_teamUsers.put(teamUser.getDb_id(), teamUser);
    }

    @Override
    public void addPendingTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        if (this.pending_teamUsers.containsKey(teamUser.getDb_id()) || this.getTeamUser_tenants().contains(teamUser))
            return;
        try {
            DatabaseRequest request = db.prepareRequest("INSERT INTO pendingJoinAppRequests values (null, ?, ?);");
            request.setInt(this.getDBid());
            request.setInt(teamUser.getDb_id());
            request.set();
            this.addPendingTeamUser(teamUser);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public Collection<TeamUser> getPendingTeamUsers() {
        if (pending_teamUsers == null)
            pending_teamUsers = new ConcurrentHashMap<>();
        return this.pending_teamUsers.values();
    }

    @Override
    public void removePendingTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        if (!this.getPendingTeamUsers().contains(teamUser))
            return;
        try {
            DatabaseRequest request = db.prepareRequest("DELETE FROM pendingJoinAppRequests WHERE shareable_app_id = ? AND team_user_id = ?;");
            request.setInt(this.getDBid());
            request.setInt(teamUser.getDb_id());
            request.set();
            this.pending_teamUsers.remove(teamUser);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }


    @Override
    public void setDisableShared(boolean disabled, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            System.out.println("Disable app");
            this.setDisabled(disabled, db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public boolean isPinned() {
        return this.pinned;
    }

    @Override
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public void setPinned(boolean pinned, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE sharedApps set pinned= ? WHERE id = ?;");
            request.setBoolean(pinned);
            request.setInt(this.getDBid());
            request.set();
            this.setPinned(pinned);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public JSONObject getJsonWithoutId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.informations.getName());
        return jsonObject;
    }

    public void becomeShareable(DataBaseConnection db, Team team, TeamUser teamUser_owner, Integer team_user_id, Channel channel, String description) throws GeneralException {
        if (this.teamUser_owner != null)
            return;
        String origin_type = (channel == null) ? "user" : "channel";
        Integer origin_id = (channel == null) ? team_user_id : channel.getDb_id();
        DatabaseRequest databaseRequest = db.prepareRequest("INSERT INTO shareableApps values (?, ?, ?, ?, ?, ?, ?);");
        databaseRequest.setInt(this.getDBid());
        databaseRequest.setInt(team.getDb_id());
        databaseRequest.setInt(teamUser_owner.getDb_id());
        if (channel == null)
            databaseRequest.setNull();
        else
            databaseRequest.setInt(channel.getDb_id());
        databaseRequest.setString((description == null) ? "" : description);
        databaseRequest.setString(origin_type);
        databaseRequest.setInt(origin_id);
        databaseRequest.set();
        this.teamUser_owner = teamUser_owner;
        this.channel = channel;
        this.description = description;
        this.setOrigin(origin_type, origin_id, team.getDb_id());
        team.getAppManager().addShareableApp(this);
    }
}