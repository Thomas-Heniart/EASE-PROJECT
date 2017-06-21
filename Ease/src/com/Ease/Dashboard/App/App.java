package com.Ease.Dashboard.App;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Servlets.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletContext;

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

    public static List<App> loadApps(Profile profile, ServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        List<App> apps = new LinkedList<App>();
        DatabaseRequest request = db.prepareRequest("SELECT apps.*, position, sharedApps.id AS shared_app_id, team_id FROM apps JOIN profileAndAppMap ON apps.id = profileAndAppMap.app_id AND profileAndAppMap.profile_id = ? LEFT JOIN sharedApps ON apps.id = sharedApps.pinned_app_id ORDER BY position;");
        request.setInt(profile.getDBid());
        DatabaseResult rs = request.get();
        String db_id;
        int position;
        String insertDate;
        AppInformation infos;
        GroupApp groupApp = null;
        App app = null;
        while (rs.next()) {
            db_id = rs.getString(Data.ID.ordinal());
            position = rs.getInt(Data.POSITION.ordinal());
            insertDate = rs.getString(Data.INSERT_DATE.ordinal());
            infos = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), db);
            String groupAppId = rs.getString(Data.GROUP_APP_ID.ordinal());
            if (groupAppId != null) {
                groupApp = GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupAppId);
            }

            switch (rs.getString(Data.TYPE.ordinal())) {
                case "linkApp":
                    app = LinkApp.loadLinkApp(db_id, profile, position, insertDate, infos, groupApp, sm.getServletContext(), db);
                    break;
                case "websiteApp":
                    app = WebsiteApp.loadWebsiteApp(db_id, profile, position, insertDate, infos, groupApp, sm.getServletContext(), db);
                    break;
                default:
                    throw new GeneralException(ServletManager.Code.InternError, "This app type dosen't exist.");
            }
            if (app.getPosition() != apps.size())
                app.setPosition(apps.size(), db);

            Integer shared_app_id = rs.getInt("shared_app_id");
            if (shared_app_id > 0) {
                System.out.println("App pinned");
                Integer team_id = rs.getInt("team_id");
                TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
                Team team = teamManager.getTeamWithId(team_id);
                SharedApp sharedApp = team.getAppManager().getSharedApp(shared_app_id);
                sharedApp.setPinned_app(app);
                app.setSharedApp_pinned(sharedApp);
            }
            apps.add(app);
            groupApp = null;
        }
        return apps;
    }

    public static List<ShareableApp> loadShareableAppsForTeam(Team team, ServletContext context, DataBaseConnection db) throws HttpServletException {
        try {
            List<ShareableApp> shareableApps = new LinkedList<>();
            DatabaseRequest request = db.prepareRequest("SELECT * FROM apps JOIN shareableApps ON apps.id = shareableApps.id WHERE team_id = ?;");
            request.setInt(team.getDb_id());
            DatabaseResult rs = request.get();
            String db_id;
            String insertDate;
            String description;
            AppInformation infos;
            ShareableApp shareableApp = null;
            while (rs.next()) {
                db_id = rs.getString("apps.id");
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
                shareableApp.setOrigin(rs.getString("origin_type"), rs.getInt("origin_id"));
                TeamUser teamUser_owner = team.getTeamUserWithId(rs.getInt("teamUser_owner_id"));
                Integer channel_id = rs.getInt("channel_id");
                Channel channel = null;
                if (channel_id != null && channel_id > 0) {
                    channel = team.getChannelWithId(channel_id);
                    shareableApp.setChannel(channel);
                }
                shareableApp.setTeamUser_owner(teamUser_owner);
                shareableApps.add(shareableApp);
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
            String db_id;
            String insertDate;
            AppInformation infos;
            SharedApp sharedApp = null;
            while (rs.next()) {
                db_id = rs.getString("apps.id");
                insertDate = rs.getString("insert_date");
                infos = AppInformation.loadAppInformation(rs.getString("app_info_id"), db);
                switch (rs.getString("type")) {
                    case "linkApp":
                        sharedApp = LinkApp.loadLinkApp(db_id, null, null, insertDate, infos, null, context, db);
                        break;
                    case "websiteApp":
                        sharedApp = WebsiteApp.loadWebsiteApp(db_id, null, null, insertDate, infos, null, context, db);
                        if (((App) sharedApp).isClassicApp()) {
                            ((App) sharedApp).setReceived(rs.getBoolean("received"));
                            sharedApp.setAdminHasAccess(rs.getBoolean("adminHasAccess"));
                        }

                        break;
                    default:
                        throw new GeneralException(ServletManager.Code.InternError, "This app type dosen't exist.");
                }
                Integer teamUser_tenant_id = rs.getInt("teamUser_tenant_id");
                sharedApp.setTeamUser_tenant(shareableApp.getTeamUser_owner().getTeam().getTeamUserWithId(teamUser_tenant_id));
                sharedApp.setHolder(shareableApp);
                sharedApp.setCanSeeInformation(false/* rs.getBoolean("can_see_information") */);
                sharedApps.add(sharedApp);
            }
            return sharedApps;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static String createApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, db);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String insertDate = dateFormat.format(date);
        DatabaseRequest request = db.prepareRequest("INSERT INTO apps VALUES (NULL, ?, ?, ?, NULL);");
        request.setString(insertDate);
        request.setString(type);
        request.setInt(infos.getDb_id());
        String appDBid = request.set().toString();
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

    public static String createSharedApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, Integer team_id, Integer channel_id, Integer team_user_tenant_id, App holder, boolean received, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, db);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String registrationDate = dateFormat.format(date);
        DatabaseRequest request = db.prepareRequest("INSERT INTO apps VALUES (NULL, ?, ?, ?, NULL);");
        request.setString(registrationDate);
        request.setString(type);
        request.setInt(infos.getDb_id());
        String appDBid = request.set().toString();
        if (profile != null && position != null) {
            request = db.prepareRequest("INSERT INTO profileAndAppMap values (NULL, ?, ?, ?)");
            request.setInt(profile.getDBid());
            request.setInt(appDBid);
            request.setInt(position);
            request.set();
        }
        request = db.prepareRequest("INSERT INTO sharedApps values (?, ?, ?, ?, ?, ?, 0, ?, NULL);");
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

    protected String db_id;
    protected Profile profile;
    protected Integer position;
    protected AppInformation informations;
    protected GroupApp groupApp;
    protected String insertDate;
    protected int single_id;
    protected boolean received = true;
    protected SharedApp sharedApp_pinned;

    /* Interface ShareableApp */
    protected List<SharedApp> sharedApps = new LinkedList<>();
    protected HashMap<Integer, SharedApp> sharedAppIdMap = new HashMap<>();
    protected TeamUser teamUser_owner;
    protected List<TeamUser> tenant_teamUsers = new LinkedList<>();
    protected Channel channel;
    protected String description;
    protected JSONObject origin = new JSONObject();

    /* Interface SharedApp */
    protected ShareableApp holder;
    protected TeamUser teamUser_tenant;
    protected Boolean canSeeInformation;
    protected boolean adminHasAccess;
    protected App pinned_app;


    public App(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id) {
        this.db_id = db_id;
        this.profile = profile;
        this.position = position;
        this.informations = infos;
        this.groupApp = groupApp;
        this.insertDate = insertDate;
        this.single_id = single_id;
    }

    public App(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, ShareableApp holder) {
        this.db_id = db_id;
        this.profile = profile;
        this.position = position;
        this.informations = infos;
        this.groupApp = groupApp;
        this.insertDate = insertDate;
        this.single_id = single_id;
        this.holder = holder;
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        if (this.groupApp != null && (this.groupApp.isCommon() == true || !this.groupApp.getPerms().havePermission(AppPermissions.Perm.DELETE.ordinal())))
            throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permission to remove this app.");
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM profileAndAppMap WHERE app_id = ?;");
        request.setInt(db_id);
        request.set();
        System.out.println("App pined: " + this.sharedApp_pinned == null);
        System.out.println("My id: " + this.getSingleId());
        if (this.sharedApp_pinned != null)
            this.sharedApp_pinned.unpin(db);
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

    public String getDBid() {
        return db_id;
    }

    public int getSingleId() {
        return single_id;
    }

    public void setSingleId(int singleId) {
        this.single_id = singleId;
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

    public void setSharedApp_pinned(SharedApp sharedApp) {
        System.out.println("App pinned id " + this.getSingleId());
        System.out.println("SharedApp is null: " + (sharedApp == null));
        this.sharedApp_pinned = sharedApp;
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

    /* For sancho le robot */
    public boolean isEmpty() {
        return false;
    }

    public void fillJson(JSONObject json) {
        json.put("position", this.position);
        json.put("name", this.informations.getName());
        json.put("singleId", this.single_id);
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
        jsonObject.put("singleId", this.single_id);
        return jsonObject;
    }

    /* New version */
    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getAppInformation().getName());
        jsonObject.put("id", this.getSingleId());
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
            if (this.pinned_app != null) {
                Profile profile = this.pinned_app.getProfile();
                profile.getUser().getDashboardManager().removeAppWithSingleId(this.pinned_app.getSingleId(), db);
                //this.pinned_app.removeFromDB(db);
            }
            DatabaseRequest request = db.prepareRequest("DELETE FROM sharedApps WHERE id = ?;");
            request.setInt(this.getDBid());
            request.set();
            this.removeFromDB(db);
            db.commitTransaction(transaction);
            //this.getHolder().removeSharedApp(this);
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
        res.put("profile_id", (this.getPinned_app() == null) ? -1 : this.getPinned_app().getProfile().getSingleId());
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
    public App createPinned_app(Profile profile, String keyUser, DataBaseConnection db) throws HttpServletException {
        App app = null;
        if (this.pinned_app != null)
            throw new HttpServletException(HttpStatus.BadRequest, "App already pinned.");
        if (this.isEmpty())
            throw new HttpServletException(HttpStatus.InternError, "There are no empty shared app.");
        try {
            int transaction = db.startTransaction();
            if (this.isClassicApp())
                app = ClassicApp.createClassicApp((ClassicApp) this, profile, keyUser, db);
            else
                app = LinkApp.createLinkApp((LinkApp) this, profile, db);
            DatabaseRequest request = db.prepareRequest("UPDATE sharedApps SET pinned_app_id = ? WHERE id = ?;");
            request.setInt(app.getDBid());
            request.setInt(this.getDBid());
            request.set();
            db.commitTransaction(transaction);
            this.setPinned_app(app);
            pinned_app.setSharedApp_pinned(this);
            return app;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
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
            DatabaseRequest request = db.prepareRequest("DELETE FROM shareableApps WHERE id = ?;");
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
    public List<SharedApp> getSharedApps() {
        return this.sharedApps;
    }

    @Override
    public void setSharedApps(List<SharedApp> sharedApps) {
        for (SharedApp sharedApp : sharedApps)
            this.addSharedApp(sharedApp);
    }

    @Override
    public void addSharedApp(SharedApp sharedApp) {
        this.sharedAppIdMap.put(Integer.valueOf(((App) sharedApp).getDBid()), sharedApp);
        this.sharedApps.add(sharedApp);
    }

    @Override
    public SharedApp getSharedAppWithId(Integer sharedApp_id) throws HttpServletException {
        SharedApp sharedApp = this.sharedAppIdMap.get(sharedApp_id);
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
            System.out.println("Shared apps size: " + this.getSharedApps().size());
            for (SharedApp sharedApp : this.getSharedApps())
                receivers.add(sharedApp.getSharedJSON());
            res.put("receivers", receivers);
            res.put("origin", this.getOrigin());
            res.put("description", this.getDescription());
            res.put("name", this.getAppInformation().getName());
            return res;
        } catch (ParseException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public JSONObject getNeededParams(PostServletManager sm) {
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
    public void setOrigin(String origin_type, Integer origin_id) {
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
        this.sharedAppIdMap.remove(((App) sharedApp).getDBid());
        this.getSharedApps().remove(sharedApp);
    }

    @Override
    public App getPinned_app() {
        return this.pinned_app;
    }

    @Override
    public void setPinned_app(App pinned_app) {
        this.pinned_app = pinned_app;
    }

    @Override
    public void unpin(DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE sharedApps SET pinned_app_id = NULL WHERE id = ?;");
            request.setInt(this.getDBid());
            request.set();
            this.pinned_app = null;
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
        this.setOrigin(origin_type, origin_id);
        team.getAppManager().addShareableApp(this);
    }
}