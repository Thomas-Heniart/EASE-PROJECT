package com.Ease.Dashboard.Profile;

import com.Ease.Catalog.Website;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Profile {

    public enum Data {
        NOTHING,
        ID,
        USER_ID,
        COLUMN_IDX,
        POSITION_IDX,
        GROUP_PROFILE_ID,
        PROFILE_INFO_ID,
    }

    public static int MAX_COLUMN = 5;
    /*
     *
	 * Loader and Creator
	 * 
	 */

    public static List<List<Profile>> createPersonnalProfiles(User user, DataBaseConnection db) throws GeneralException {
        List<List<Profile>> profilesColumn = new LinkedList<List<Profile>>();
        for (int i = 0; i < MAX_COLUMN; ++i) {
            profilesColumn.add(new LinkedList<Profile>());
        }
        profilesColumn.get(0).add(Profile.createPersonnalProfile(user, 0, 0, "Side", "#000000", db));
        profilesColumn.get(1).add(Profile.createPersonnalProfile(user, 1, 0, "Me", "#373B60", db));
        return profilesColumn;
    }

    public static List<List<Profile>> loadProfiles(User user, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        List<List<Profile>> profilesColumn = new LinkedList<List<Profile>>();
        for (int i = 0; i < MAX_COLUMN; ++i) {
            profilesColumn.add(new LinkedList<Profile>());
        }
        DatabaseRequest request = db.prepareRequest("SELECT * FROM profiles WHERE user_id= ?;");
        request.setInt(user.getDBid());
        DatabaseResult rs = request.get();
        Integer db_id;
        int columnIdx;
        int posIdx;
        GroupProfile groupProfile;
        ProfileInformation infos;
        List<App> apps;
        while (rs.next()) {
            db_id = rs.getInt(Data.ID.ordinal());
            columnIdx = rs.getInt(Data.COLUMN_IDX.ordinal());
            posIdx = rs.getInt(Data.POSITION_IDX.ordinal());
            String groupProfileId = rs.getString(Data.GROUP_PROFILE_ID.ordinal());
            groupProfile = (groupProfileId == null) ? null : GroupManager.getGroupManager(context).getGroupProfileFromDBid(groupProfileId);
            infos = ProfileInformation.loadProfileInformation(rs.getString(Data.PROFILE_INFO_ID.ordinal()), db);
            Profile profile = new Profile(db_id, user, columnIdx, posIdx, groupProfile, infos);
            apps = App.loadApps(profile, context, db);
            profile.setApps(apps);
            profilesColumn.get(columnIdx).add(profile);
        }
        for (List<Profile> column : profilesColumn) {
            column.sort(new Comparator<Profile>() {
                @Override
                public int compare(Profile a, Profile b) {
                    return a.getPositionIdx() - b.getPositionIdx();
                }
            });
        }
        return profilesColumn;
    }

    private static Integer createProfileInDb(DataBaseConnection db, String userId, int columnIdx, int posIdx, String groupProfileId, String infoId) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("INSERT INTO profiles VALUES(NULL, ?, ?, ?, ?, ?);");
        request.setInt(userId);
        request.setInt(columnIdx);
        request.setInt(posIdx);
        if (groupProfileId == null)
            request.setNull();
        else
            request.setInt(groupProfileId);
        request.setInt(infoId);
        return request.set();
    }

    public static Profile createProfileWithGroup(User user, int columnIdx, int posIdx, GroupProfile groupProfile, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        ProfileInformation info;
        if (groupProfile.isCommon()) {
            info = groupProfile.getInfo();
        } else {
            info = ProfileInformation.createProfileInformation(groupProfile.getName(), groupProfile.getColor(), db);
        }
        Integer db_id = createProfileInDb(db, user.getDBid(), columnIdx, posIdx, groupProfile.getDBid(), info.getDBid());
        Profile profile = new Profile(db_id, user, columnIdx, posIdx, groupProfile, info);
        db.commitTransaction(transaction);
        return profile;
    }

    public static Integer createProfileWithGroupForUnconnected(String db_id, int columnIdx, int posIdx, GroupProfile groupProfile, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        String info_id;
        if (groupProfile.isCommon()) {
            info_id = groupProfile.getInfo().getDBid();
        } else {
            info_id = ProfileInformation.createProfileInformationForUnconnected(groupProfile.getName(), groupProfile.getColor(), sm);
        }
        Integer id = createProfileInDb(db, db_id, columnIdx, posIdx, groupProfile.getDBid(), info_id);
        db.commitTransaction(transaction);
        return id;
    }

    public static Profile createPersonnalProfile(User user, int columnIdx, int posIdx, String name, String color, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        ProfileInformation info = ProfileInformation.createProfileInformation(name, color, db);
        Integer db_id = createProfileInDb(db, user.getDBid(), columnIdx, posIdx, null, info.getDBid());
        Profile profile = new Profile(db_id, user, columnIdx, posIdx, null, info);
        db.commitTransaction(transaction);
        return profile;
    }

    public static void removeProfileForUnconnected(Integer db_id, String info_id, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM profiles WHERE id = ?;");
        request.setInt(db_id);
        request.set();
        if (info_id != null) {
            request = db.prepareRequest("DELETE FROM profilesInformations WHERE id = ?;");
            request.setInt(info_id);
            request.set();
        }
        db.commitTransaction(transaction);
    }

	
	/*
     *
	 * Constructor
	 * 
	 */

    protected Integer db_id;
    protected User user;
    protected int columnIdx;
    protected int posIdx;
    protected GroupProfile groupProfile;
    protected ProfileInformation infos;
    protected List<App> apps;

    public Profile(Integer db_id, User user, int columnIdx, int posIdx, GroupProfile groupProfile, ProfileInformation infos) {
        this.db_id = db_id;
        this.user = user;
        this.columnIdx = columnIdx;
        this.posIdx = posIdx;
        this.groupProfile = groupProfile;
        this.infos = infos;
        this.apps = new LinkedList<App>();
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        if (this.groupProfile != null && (this.groupProfile.isCommon() || !this.groupProfile.getPerms().havePermission(ProfilePermissions.Perm.DELETE.ordinal()))) {
            throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permission to remove this profile.");
        }
        int transaction = db.startTransaction();
        for (App app : apps) {
            if (app.isPinned())
                app.unpin(db);
            else
                app.removeFromDB(db);
        }
        DatabaseRequest request = db.prepareRequest("DELETE FROM profiles WHERE id = ?;");
        request.setInt(db_id);
        request.set();
        if (this.groupProfile == null || this.groupProfile.isCommon() == false)
            this.infos.removeFromDB(db);
        db.commitTransaction(transaction);
    }

	/*
     *
	 * Getter and Setter
	 * 
	 */

    public Integer getDBid() {
        return this.db_id;
    }

    public User getUser() {
        return this.user;
    }

    public int getColumnIdx() {
        return this.columnIdx;
    }

    public void setColumnIdx(int idx, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE profiles SET column_idx = ? WHERE id = ?;");
        request.setInt(idx);
        request.setInt(db_id);
        request.set();
        this.columnIdx = idx;
    }

    public int getPositionIdx() {
        return this.posIdx;
    }

    public void setPositionIdx(int idx, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE profiles SET position_idx = ? WHERE id = ?;");
        request.setInt(idx);
        request.setInt(db_id);
        request.set();
        this.posIdx = idx;
    }

    public GroupProfile getGroupProfile() {
        return this.groupProfile;
    }

    public String getName() {
        return this.infos.getName();
    }

    public void setName(String name, ServletManager sm) throws GeneralException {
        if (this.groupProfile == null || (!this.groupProfile.isCommon() && this.groupProfile.getPerms().havePermission(ProfilePermissions.Perm.RENAME.ordinal())))
            this.infos.setName(name, sm);
        else
            throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permissions to change the profile's name.");
    }

    public String getColor() {
        return this.infos.color;
    }

    public void setColor(String color, ServletManager sm) throws GeneralException {
        if (this.groupProfile == null || (!this.groupProfile.isCommon() && this.groupProfile.getPerms().havePermission(ProfilePermissions.Perm.COLOR.ordinal())))
            this.infos.setColor(color, sm);
        else
            throw new GeneralException(ServletManager.Code.ClientWarning, "You have not the permissions to change the profile's color.");
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }
	
	/*
	 * 
	 * Utils
	 * 
	 */

    public int getSize() {
        int size;
        if (apps.size() < 4)
            size = 2;
        else {
            size = (apps.size() + 2) / 3;
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getJSON() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDBid());
        res.put("color", this.infos.getColor());
        res.put("name", this.infos.getName());
        res.put("column", this.columnIdx);
        res.put("index", this.posIdx);
        if (this.groupProfile != null) {
            res.put("groupProfile", this.groupProfile.getJson());
        }
        JSONArray array = new JSONArray();
        for (App app : this.apps) {
            JSONObject jsonApp = new JSONObject();
            app.fillJson(jsonApp);
            array.add(jsonApp);
        }
        res.put("apps", array);
        return res;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDBid());
        res.put("name", this.infos.getName());
        JSONArray apps = new JSONArray();
        for (App app : this.getApps())
            apps.add(app.getJson());
        res.put("apps", apps);
        return res;
    }

    public static int getSizeForUnconnected(String db_id, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT COUNT(*) FROM apps WHERE profile_id= ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            int ret = rs.getInt(1);
            if (ret < 4)
                return 2;
            return (ret + 2) / 3;
        } else {
            throw new GeneralException(ServletManager.Code.InternError, "Bizare.");
        }
    }

    public void updateAppsIndex(DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        for (int i = 0; i < apps.size(); ++i) {
            if (apps.get(i).getPosition() != i) {
                apps.get(i).setPosition(i, db);
            }
            if (apps.get(i).getProfile() != this) {
                apps.get(i).setProfile(this, db);
            }
        }
        db.commitTransaction(transaction);
    }

    public boolean havePermission(ProfilePermissions.Perm perm) {
        if (this.groupProfile == null || (!this.groupProfile.isCommon() && this.groupProfile.getPerms().havePermission(perm.ordinal())))
            return true;
        return false;
    }

    public ClassicApp addClassicApp(String name, Website site, Map<String, String> infos, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        int position = this.apps.size();
        ClassicApp app = ClassicApp.createClassicApp(this, position, name, site, infos, sm, user);
        this.apps.add(app);
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            if (Regex.isEmail(entry.getValue()) == true)
                this.user.addEmailIfNeeded(entry.getValue(), sm);
        }
        this.user.getDashboardManager().addApp(app);
        db.commitTransaction(transaction);
        return app;
    }

    public LogwithApp addLogwithApp(String name, Website site, App logwith, ServletManager sm) throws GeneralException {
        int position = this.apps.size();
        if (logwith.getType().equals("LogwithApp") || logwith.getType().equals("ClassicApp")) {
            LogwithApp app = LogwithApp.createLogwithApp(this, position, name, site, (WebsiteApp) logwith, sm);
            this.apps.add(app);
            this.user.getDashboardManager().addApp(app);
            return app;
        }
        throw new GeneralException(ServletManager.Code.ClientError, "This app is not a Classic or Logwith app.");
    }

    public WebsiteApp addEmptyApp(String name, Website site, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        int position = this.apps.size();
        WebsiteApp app = WebsiteApp.createEmptyApp(this, position, name, site, db);
        this.apps.add(app);
        this.user.getDashboardManager().addApp(app);
        db.commitTransaction(transaction);
        return app;
    }

    public void removeWithPassword(String password, ServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        if (this.apps.size() > 0) {
            if (password == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Password confirmation needed.");
            if (!this.user.getKeys().isGoodPassword(password))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Password does not match");
        }
        this.removeFromDB(db);
        db.commitTransaction(transaction);
    }

    public void removeApp(App app) {
        this.apps.remove(app);
    }

    public void removeApp(App app, DataBaseConnection db) throws GeneralException, HttpServletException {
        this.apps.remove(app);
        app.removeFromDB(db);
        this.updateAppsIndex(db);
    }

    public void addApp(App newApp) {
        this.apps.add(newApp);
        this.user.getDashboardManager().addApp(newApp);
    }

    public boolean isGroupProfile() {
        return this.groupProfile != null;
    }
}
