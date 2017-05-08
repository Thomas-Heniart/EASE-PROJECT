package com.Ease.Dashboard.App;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.Ease.Context.Catalog.Website;
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

public class App {

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
                    app = LinkApp.loadLinkApp(db_id, profile, position, insertDate, infos, groupApp, sm);
                    break;
                case "websiteApp":
                    app = WebsiteApp.loadWebsiteApp(db_id, profile, position, insertDate, infos, groupApp, sm);
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

    public static List<SharedApp> loadSharedApps(Integer teamUser_tenant_id, ServletManager sm) throws GeneralException {
        List<SharedApp> sharedApps = new LinkedList<>();
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT apps.* FROM apps JOIN appAndSharedAppMap ON apps.id = appAndSharedAppMap.shared_app_id AND appAndSharedAppMap.team_user_tenant_id = ?;");
        request.setInt(teamUser_tenant_id);
        DatabaseResult rs = request.get();
        String db_id;
        int position;
        String insertDate;
        AppInformation infos;
        GroupApp groupApp = null;
        SharedApp app = null;
        while (rs.next()) {
            db_id = rs.getString(Data.ID.ordinal());
            insertDate = rs.getString(Data.INSERT_DATE.ordinal());
            infos = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), db);
            String groupAppId = rs.getString(Data.GROUP_APP_ID.ordinal());
            if (groupAppId != null) {
                groupApp = GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupAppId);
            }

            switch (rs.getString(Data.TYPE.ordinal())) {
                case "linkApp":
                    app = LinkApp.loadLinkApp(db_id, null, null, insertDate, infos, groupApp, sm);
                    break;
                case "websiteApp":
                    app = WebsiteApp.loadWebsiteApp(db_id, null, null, insertDate, infos, groupApp, sm);
                    break;
                default:
                    throw new GeneralException(ServletManager.Code.InternError, "This app type dosen't exist.");
            }
            sharedApps.add(app);
            groupApp = null;
        }
        return sharedApps;
    }

    public static List<ShareableApp> loadShareableApps(Integer teamUser_tenant_id, ServletManager sm) throws GeneralException {
        List<ShareableApp> shareableApps = new LinkedList<>();
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT apps.* FROM apps JOIN appAndSharedAppMap ON apps.id = appAndSharedAppMap.app_id AND appAndSharedAppMap.team_user_tenant_id = ?;");
        request.setInt(teamUser_tenant_id);
        DatabaseResult rs = request.get();
        String db_id;
        int position;
        String insertDate;
        AppInformation infos;
        GroupApp groupApp = null;
        ShareableApp app = null;
        while (rs.next()) {
            db_id = rs.getString(Data.ID.ordinal());
            insertDate = rs.getString(Data.INSERT_DATE.ordinal());
            infos = AppInformation.loadAppInformation(rs.getString(Data.APP_INFO_ID.ordinal()), db);
            String groupAppId = rs.getString(Data.GROUP_APP_ID.ordinal());
            if (groupAppId != null) {
                groupApp = GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupAppId);
            }

            switch (rs.getString(Data.TYPE.ordinal())) {
                case "linkApp":
                    app = LinkApp.loadLinkApp(db_id, null, null, insertDate, infos, groupApp, sm);
                    break;
                case "websiteApp":
                    app = WebsiteApp.loadWebsiteApp(db_id, null, null, insertDate, infos, groupApp, sm);
                    break;
                default:
                    throw new GeneralException(ServletManager.Code.InternError, "This app type dosen't exist.");
            }
            shareableApps.add(app);
            groupApp = null;
        }
        return shareableApps;
    }

    public static String createApp(Profile profile, int position, String name, String type, Map<String, Object> elevator, ServletManager sm) throws GeneralException {
        return createApp(profile, position, name, type, elevator, true, false, sm);

    }

    public static String createApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, boolean shareable, boolean shared, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, sm);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String registrationDate = dateFormat.format(date);
        DatabaseRequest request = db.prepareRequest("INSERT INTO apps VALUES (NULL, ?, ?, ?, NULL, ?, ?);");
        request.setString(registrationDate);
        request.setString(type);
        request.setInt(infos.getDb_id());
        request.setBoolean(shareable);
        request.setBoolean(shared);
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

    public static String createSharedApp(Profile profile, Integer position, String name, String type, Map<String, Object> elevator, boolean shareable, boolean shared, Integer team_user_owner_id, Integer team_user_tenant_id, App holder, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        AppInformation infos = AppInformation.createAppInformation(name, sm);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String registrationDate = dateFormat.format(date);
        DatabaseRequest request = db.prepareRequest("INSERT INTO apps VALUES (NULL, ?, ?, ?, NULL, ?, ?);");
        request.setString(registrationDate);
        request.setString(type);
        request.setInt(infos.getDb_id());
        request.setBoolean(shareable);
        request.setBoolean(shared);
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
        request = db.prepareRequest("INSERT INTO profileAndAppMap values (NULL, ?, ?, ?, ?)");
        request.setInt(team_user_owner_id);
        request.setInt(team_user_tenant_id);
        request.setInt(appDBid);
        request.setInt(holder.getDBid());
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
    protected boolean shared;
    protected boolean shareable;
    protected ShareableApp holder;
    protected List<SharedApp> sharedApps = new LinkedList<>();
    protected HashMap<Integer, SharedApp> sharedAppIdMap = new HashMap<>();

    public App(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id) {
        this.db_id = db_id;
        this.profile = profile;
        this.position = position;
        this.informations = infos;
        this.groupApp = groupApp;
        this.insertDate = insertDate;
        this.single_id = single_id;
        this.shareable = true;
        this.shared = false;
    }

    public App(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, boolean shared, boolean shareable) {
        this.db_id = db_id;
        this.profile = profile;
        this.position = position;
        this.informations = infos;
        this.groupApp = groupApp;
        this.insertDate = insertDate;
        this.single_id = single_id;
        this.shareable = true;
        this.shared = false;
        this.shared = shared;
        this.shareable = shareable;
    }

    public App(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, boolean shareable, boolean shared, ShareableApp holder) {
        this.db_id = db_id;
        this.profile = profile;
        this.position = position;
        this.informations = infos;
        this.groupApp = groupApp;
        this.insertDate = insertDate;
        this.single_id = single_id;
        this.shareable = true;
        this.shared = false;
        this.shared = shared;
        this.shareable = shareable;
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

    public int getPosition() {
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
        jsonObject.put("isShared", this.shared);
        jsonObject.put("isShareable", this.shareable);
        return jsonObject;
    }

    public JSONArray getAccountInformationsJson() {
        return new JSONArray();
    }

    public boolean isShared() {
        return this.shared;
    }

    public boolean isShareable() {
        return this.shareable;
    }
}