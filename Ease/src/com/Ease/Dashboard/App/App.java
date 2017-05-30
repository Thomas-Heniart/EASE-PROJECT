package com.Ease.Dashboard.App;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.Ease.Dashboard.DashboardManager;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.google.common.primitives.UnsignedInts;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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

    public static List<App> loadApps(Profile profile, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        List<App> apps = new LinkedList<App>();
        DatabaseRequest request = db.prepareRequest("SELECT apps.*, position FROM apps JOIN profileAndAppMap ON apps.id = profileAndAppMap.app_id AND profileAndAppMap.profile_id = ? ORDER BY position;");
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
            if (app.getPosition() != apps.size()) {
                app.setPosition(apps.size(), sm);
            }
            apps.add(app);
            groupApp = null;
        }
        return apps;
    }

    public static List<ShareableApp> loadShareableAppsForTeam(Team team, ServletContext context, DataBaseConnection db) throws GeneralException {
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
            TeamUser teamUser_owner = team.getTeamUserWithId(rs.getInt("teamUser_owner_id"));
            Integer channel_id = rs.getInt("channel_id");
            System.out.println("Channel_id: " + channel_id);
            Channel channel = null;
            if (channel_id != null && channel_id > 0) {
                channel = team.getChannelWithId(channel_id);
                shareableApp.setChannel(channel);
            }
            shareableApp.setTeamUser_owner(teamUser_owner);
            shareableApps.add(shareableApp);
        }
        return shareableApps;
    }

    public static List<SharedApp> loadSharedAppsForShareableApp(ShareableApp shareableApp, ServletContext context, DataBaseConnection db) throws GeneralException {
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
                    if (((App) sharedApp).isClassicApp())
                        ((App) sharedApp).setReceived(rs.getBoolean("received"));
                    break;
                default:
                    throw new GeneralException(ServletManager.Code.InternError, "This app type dosen't exist.");
            }
            Integer teamUser_tenant_id = rs.getInt("teamUser_tenant_id");
            System.out.println("Owner: " + shareableApp.getTeamUser_owner().getFirstName());
            System.out.println("Tenant: " + shareableApp.getTeamUser_owner().getTeam().getTeamUserWithId(teamUser_tenant_id).getFirstName());
            sharedApp.setTeamUser_tenant(shareableApp.getTeamUser_owner().getTeam().getTeamUserWithId(teamUser_tenant_id));
            sharedApp.setHolder(shareableApp);
            sharedApps.add(sharedApp);
        }
        return sharedApps;
    }

    public static String createApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, sm);
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
        elevator.put("appInfos", infos);
        elevator.put("insertDate", registrationDate);
        db.commitTransaction(transaction);
        return appDBid;

    }

    public static String createSharedApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, boolean shareable, boolean shared, Integer team_id, Integer channel_id, Integer team_user_tenant_id, App holder, boolean received, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, sm);
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
            elevator.put("appInfos", infos);
            elevator.put("insertDate", registrationDate);
        }
        request = db.prepareRequest("INSERT INTO sharedApps values (?, ?, ?, ?, ?, ?);");
        request.setInt(appDBid);
        request.setInt(team_id);
        request.setInt(team_user_tenant_id);
        request.setInt(holder.getDBid());
        if (channel_id == null)
            request.setNull();
        else
            request.setInt(channel_id);
        request.setBoolean(received);
        request.set();
        db.commitTransaction(transaction);
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

    /* Interface ShareableApp */
    protected List<SharedApp> sharedApps = new LinkedList<>();
    protected HashMap<Integer, SharedApp> sharedAppIdMap = new HashMap<>();
    protected TeamUser teamUser_owner;
    protected List<TeamUser> tenant_teamUsers = new LinkedList<>();
    protected Channel channel;
    protected String description;

    /* Interface SharedApp */
    protected ShareableApp holder;
    protected TeamUser teamUser_tenant;


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

    public void removeFromDB(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        if (this.groupApp != null && (this.groupApp.isCommon() == true || !this.groupApp.getPerms().havePermission(AppPermissions.Perm.DELETE.ordinal())))
            throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permission to remove this app.");
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM profileAndAppMap WHERE app_id = ?;");
        request.setInt(db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM apps WHERE id = ?;");
        request.setInt(db_id);
        request.set();
        if (this.groupApp == null || this.groupApp.isCommon() == false)
            informations.removeFromDb(sm);
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

    public String getName() {
        return this.informations.getName();
    }

    public void setName(String name, ServletManager sm) throws GeneralException {
        if (this.groupApp == null || (!this.groupApp.isCommon() && this.groupApp.getPerms().havePermission(AppPermissions.Perm.RENAME.ordinal()))) {
            this.informations.setName(name, sm);
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

    public void setPosition(int pos, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE profileAndAppMap SET position = ? WHERE app_id = ?;");
        request.setInt(pos);
        request.setInt(db_id);
        request.set();
        this.position = pos;
    }

    public void setProfile(Profile profile, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE profileAndAppMap SET profile_id = ? WHERE app_id = ?;");
        request.setInt(profile.getDBid());
        request.setInt(db_id);
        request.set();
        this.profile = profile;
    }

    public boolean isReceived() {
        return this.received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public void beReceived(DataBaseConnection db) throws GeneralException {
        this.received = true;
        DatabaseRequest request = db.prepareRequest("UPDATE sharedApps SET received = 1 WHERE id = ?;");
        request.setInt(this.db_id);
        request.set();
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

    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        if (position != null)
            jsonObject.put("position", position);
        jsonObject.put("name", this.getAppInformation().getName());
        jsonObject.put("singleId", this.single_id);
        jsonObject.put("type", this.getType());
        return jsonObject;
    }

    public JSONArray getAccountInformationsJson() {
        return new JSONArray();
    }

    /* Interface SharedApp */
    @Override
    public void modifyShared(ServletManager sm, JSONObject editJson) throws GeneralException {
        String name = (String) editJson.get("name");
        if (name != null && !name.equals(""))
            this.getAppInformation().setName(name, sm);
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
    public void deleteShared(ServletManager sm) throws GeneralException {
        this.removeFromDB(sm);
    }

    @Override
    public void setTeamUser_tenant(TeamUser teamUser) {
        teamUser.addSharedApp(this);
        this.teamUser_tenant = teamUser;
    }

    @Override
    public TeamUser getTeamUser_tenant() {
        return this.teamUser_tenant;
    }

    @Override
    public JSONObject getSharedJSON() {
        JSONObject res = new JSONObject();
        res.put("id", this.teamUser_tenant.getDb_id());
        return res;
    }

    /* Interface ShareableApp */
    @Override
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, ServletManager sm) throws GeneralException {
        throw new GeneralException(ServletManager.Code.ClientError, "You shouldn't be there");
    }

    @Override
    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp) throws GeneralException {
        String name = (String) editJson.get("name");
        if (name != null && !name.equals(""))
            this.getAppInformation().setName(name, sm);
    }

    @Override
    public void deleteShareable(ServletManager sm, SharedApp sharedApp) throws GeneralException {
        this.removeFromDB(sm);
    }

    @Override
    public TeamUser getTeamUser_owner() {
        return this.teamUser_owner;
    }

    @Override
    public List<TeamUser> getTeamUser_tenants() {
        return this.tenant_teamUsers;
    }

    @Override
    public List<SharedApp> getSharedApps() {
        return this.sharedApps;
    }

    @Override
    public void setSharedApps(List<SharedApp> sharedApps) {
        System.out.println("Shared apps size: " + sharedApps.size());
        for (SharedApp sharedApp : sharedApps)
            this.addSharedApp(sharedApp);
    }

    @Override
    public void addSharedApp(SharedApp sharedApp) {
        if (!this.tenant_teamUsers.contains(sharedApp.getTeamUser_tenant()))
            this.tenant_teamUsers.add(sharedApp.getTeamUser_tenant());
        this.sharedAppIdMap.put(((App) sharedApp).getSingleId(), sharedApp);
        this.sharedApps.add(sharedApp);
    }

    @Override
    public void setTeamUser_owner(TeamUser teamUser_owner) {
        teamUser_owner.addShareableApp(this);
        this.teamUser_owner = teamUser_owner;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(Channel channel) {
        channel.addSharedApp(this);
        this.channel = channel;
    }

    @Override
    public JSONObject getShareableJson() throws GeneralException {
        try {
            JSONObject res = new JSONObject();
            res.put("sender_id", this.getTeamUser_owner().getDb_id());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date shared_date = dateFormat.parse(insertDate);
            DateFormat dateFormat1 = new SimpleDateFormat("MMMM dd, HH:mm", Locale.US);
            res.put("shared_date", dateFormat1.format(shared_date));
            JSONArray receiver_ids = new JSONArray();
            for (TeamUser teamUser : this.getTeamUser_tenants())
                receiver_ids.add(teamUser.getDb_id());
            res.put("receiver_ids", receiver_ids);
            res.put("purpose", this.getDescription());
            return res;
        } catch (ParseException e) {
            throw new GeneralException(ServletManager.Code.InternError, e);
        }
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public void pinToDashboard(Profile profile, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        this.profile = profile;
        this.position = profile.getApps().size();
        DatabaseRequest request = db.prepareRequest("INSERT INTO profileAndAppMap values (null, ?, ?, ?);");
        request.setInt(profile.getDBid());
        request.setInt(this.db_id);
        request.setInt(position);
        db.commitTransaction(transaction);
        profile.addApp(this);
    }

    public JSONObject getJsonWithoutId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.informations.getName());
        return jsonObject;
    }

    public void becomeShareable(DataBaseConnection db, Team team, TeamUser teamUser_owner, Channel channel, String description) throws GeneralException {
        if (this.teamUser_owner != null)
            return;
        DatabaseRequest databaseRequest = db.prepareRequest("INSERT INTO shareableApps values (?, ?, ?, ?, ?);");
        databaseRequest.setInt(this.getDBid());
        databaseRequest.setInt(team.getDb_id());
        databaseRequest.setInt(teamUser_owner.getDb_id());
        if (channel == null)
            databaseRequest.setNull();
        else
            databaseRequest.setInt(channel.getDb_id());
        databaseRequest.setString((description == null) ? "" : description);
        databaseRequest.set();
        this.teamUser_owner = teamUser_owner;
        this.channel = channel;
        this.description = description;
        team.addShareableApp(this);
        teamUser_owner.addShareableApp(this);
    }
}