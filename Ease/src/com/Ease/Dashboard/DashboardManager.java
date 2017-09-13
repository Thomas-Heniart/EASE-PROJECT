package com.Ease.Dashboard;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.AccountInformation;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.Profile.ProfilePermissions;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.*;
import org.json.simple.JSONArray;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DashboardManager {
    protected User user;
    protected List<App> apps = new LinkedList<>();
    protected List<List<Profile>> profiles;
    protected Map<Integer, App> appIdMap = new HashMap<>();
    protected Map<Integer, Profile> profileIDMap = new HashMap<>();
    private HashMap<Integer, WebsiteApp> websiteAppsDBMap = new HashMap<>();

    public DashboardManager(User user, ServletContext context, DataBaseConnection db) throws GeneralException, HttpServletException {
        this.user = user;
        this.profiles = Profile.loadProfiles(user, context, db);
        this.initializeProfilesAndApps();
    }

    private void initializeProfilesAndApps() {
        for (List<Profile> profileColumn : this.profiles) {
            for (Profile profile : profileColumn) {
                this.addProfileToMaps(profile);
                for (App app : profile.getApps())
                    this.addApp(app);
            }
        }
    }

    public Profile addProfile(String name, String color, DataBaseConnection db) throws GeneralException {
        int column = this.getMostEmptyProfileColumn();
        Profile newProfile = Profile.createPersonnalProfile(this.user, column, this.profiles.get(column).size(), name, color, db);
        this.profiles.get(column).add(newProfile);
        this.addProfileToMaps(newProfile);

        return newProfile;
    }

    public void addProfileToMaps(Profile profile) {
        this.profileIDMap.put(profile.getDBid(), profile);
    }

    public void addApp(App app) {
        this.apps.add(app);
        this.appIdMap.put(app.getDBid(), app);
        if (app.getType().equals("ClassicApp") || app.getType().equals("LogwithApp")) {
            this.websiteAppsDBMap.put(((WebsiteApp) app).getWebsiteAppDBid(), (WebsiteApp) app);
        }
    }

    private void removeApp(App app, DataBaseConnection db) throws GeneralException, HttpServletException {
        //user.getUpdateManager().removeAllUpdateWithThisApp(app, db);
        Profile profile = app.getProfile();
        this.apps.remove(app);
        this.appIdMap.remove(app.getDBid());
        profile.removeApp(app, db);
    }

    public void removeAppFromCollections(App app) {
        Profile profile = app.getProfile();
        this.apps.remove(app);
        this.appIdMap.remove(app.getDBid());
        profile.removeApp(app);
    }

    public void removeAppWithId(Integer db_id, DataBaseConnection db) throws GeneralException, HttpServletException {
        App app = this.appIdMap.get(db_id);
        this.removeApp(app, db);
        if (app.getType().equals("ClassicApp")) {
            for (AccountInformation info : ((ClassicApp) app).getAccount().getAccountInformations()) {
                if (Regex.isEmail(info.getInformationValue()) == true) {
                    String email = info.getInformationValue();
                    if (this.user.getEmails().get(email) != null && this.user.getEmails().get(email).removeIfNotUsed(db))
                        this.user.getEmails().remove(email);
                }
            }

        }
    }

	/*
     * Profiles getters and setters
	 */

    public List<List<Profile>> getProfiles() {
        return this.profiles;
    }

    public List<Profile> getProfilesList() {
        List<Profile> profiles = new LinkedList<Profile>();
        for (int i = 1; i < this.profiles.size(); i++) {
            List<Profile> column = this.profiles.get(i);
            for (Profile profile : column) {
                if (profile != null)
                    profiles.add(profile);
            }
        }
        return profiles;
    }

    public Profile getProfile(int single_id) throws GeneralException {
        Profile profile = this.profileIDMap.get(single_id);
        if (profile == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This profile's single_id dosen't exist.");
        return profile;
    }

    public Profile getProfileWithId(Integer db_id) throws HttpServletException {
        Profile profile = this.profileIDMap.get(db_id);
        if (profile == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This profile does not exist.");
        return profile;
    }

    public Profile getProfileFromApp(Integer db_id) throws GeneralException {
        for (List<Profile> column : this.profiles) {
            for (Profile profile : column) {
                for (App app : profile.getApps()) {
                    if (app.getDBid() == (db_id))
                        return profile;
                }
            }
        }
        throw new GeneralException(ServletManager.Code.ClientError, "This app's single_id dosen't exist.");
    }

    public int getMostEmptyProfileColumn() {
        int col = 0;
        int minSize = -1;
        for (List<Profile> column : this.profiles) {
            int colSize = 0;
            if (this.profiles.indexOf(column) != 0) {
                for (Profile profile : column) {
                    colSize += profile.getSize();
                }
                if (minSize == -1 || colSize < minSize) {
                    minSize = colSize;
                    col = this.profiles.indexOf(column);
                }
            }
        }
        return col;
    }

	/*
     * Apps getter and setters
	 */

    /* For sancho le robot */
    public List<App> getApps() {
        return this.apps;
    }

    public App getAppWithId(Integer db_id) throws GeneralException {
        App app = this.appIdMap.get(db_id);
        if (app == null)
            throw new GeneralException(ServletManager.Code.ClientError, "No such single_id for apps");
        return app;
    }

	
	/* Utils */

    public void updateProfilesIndex(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        for (int i = 0; i < profiles.size(); ++i) {
            for (int j = 0; j < profiles.get(i).size(); ++j) {
                if (profiles.get(i).get(j).getPositionIdx() != j) {
                    profiles.get(i).get(j).setPositionIdx(j, sm);
                }
                if (profiles.get(i).get(j).getColumnIdx() != i) {
                    profiles.get(i).get(j).setColumnIdx(i, sm);
                }
            }
        }
        db.commitTransaction(transaction);
    }

    public void replaceApp(App app) throws GeneralException {
        this.apps.set(app.getPosition(), app);
        this.appIdMap.put(app.getDBid(), app);
        app.getProfile().getApps().set(app.getPosition(), app);
    }

    public void removeProfileWithPassword(int single_id, String password, ServletManager sm) throws GeneralException, HttpServletException {
        Profile profile = profileIDMap.get(single_id);
        if (profile == null)
            throw new GeneralException(ServletManager.Code.InternError, "This profile dosen't exist.");
        profile.removeWithPassword(password, sm);
        this.removeProfile(profile);
        this.updateProfilesIndex(sm);
    }

    private void removeProfile(Profile profile) throws GeneralException {
        for (List<Profile> profileColumn : this.profiles) {
            for (Profile tmpProfile : profileColumn) {
                if (tmpProfile == profile) {
                    profileColumn.remove(tmpProfile);
                    this.profileIDMap.remove(tmpProfile.getDBid());
                    return;
                }

            }
        }
        throw new GeneralException(ServletManager.Code.ClientError, "This profile does not exist");
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        List<App> apps = new LinkedList<App>();
        for (App app : this.getApps()) {
            if (app.getType().equals("LogwithApp")) {
                app.getProfile().removeApp(app, db);
            } else {
                apps.add(app);
            }
        }
        for (App app : apps) {
            app.getProfile().removeApp(app, db);
        }
        for (List<Profile> column : this.profiles) {
            for (Profile profile : column) {
                profile.removeFromDB(db);
            }
        }
        db.commitTransaction(transaction);
    }

    public void moveProfile(int profileId, int columnIdx, int position, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Profile profile = this.getProfile(profileId);
        if (columnIdx < 1 || columnIdx >= Profile.MAX_COLUMN)
            throw new GeneralException(ServletManager.Code.ClientError, "Wrong columnIdx.");
        if (position < 0 || position > this.profiles.get(columnIdx).size())
            throw new GeneralException(ServletManager.Code.ClientError, "Wrong position.");
        if (columnIdx == profile.getColumnIdx() && position > profile.getPositionIdx()) {
            position--;
        }
        this.profiles.get(profile.getColumnIdx()).remove(profile);
        this.profiles.get(columnIdx).add(position, profile);
        this.updateProfilesIndex(sm);
        db.commitTransaction(transaction);
    }

    public void moveApp(Integer appId, int profileIdDest, int positionDest, DataBaseConnection db) throws GeneralException {
        int transaction = db.startTransaction();
        App app = this.appIdMap.get(appId);
        Profile profileDest = this.getProfile(profileIdDest);
        if (positionDest < 0 || positionDest > profileDest.getApps().size())
            throw new GeneralException(ServletManager.Code.ClientError, "PositionDest fucked.");
        if (profileDest == app.getProfile()) {
            profileDest.getApps().remove(app);
            profileDest.getApps().add(positionDest, app);
            profileDest.updateAppsIndex(db);
        } else {
            if (profileDest.getGroupProfile() != null
                    && (profileDest.getGroupProfile().isCommon() == true || !profileDest.getGroupProfile().getPerms()
                    .havePermission(ProfilePermissions.Perm.ADDAPP.ordinal())))
                throw new GeneralException(ServletManager.Code.ClientWarning,
                        "You don't have the permission to add app in this profile.");
            Profile profileSrc = app.getProfile();
            if (profileSrc.getGroupProfile() != null && !(profileSrc.getGroupProfile().isCommon() == false || !profileSrc
                    .getGroupProfile().getPerms().havePermission(ProfilePermissions.Perm.MOVE_APP_OUTSIDE.ordinal())))
                throw new GeneralException(ServletManager.Code.ClientWarning,
                        "You don't have the permission to move app out of this profile.");
            profileSrc.getApps().remove(app);
            profileSrc.updateAppsIndex(db);
            profileDest.getApps().add(positionDest, app);
            profileDest.updateAppsIndex(db);
        }
        db.commitTransaction(transaction);
    }

    public App getWebsiteAppWithId(Integer logwithDBid) throws GeneralException {
        App app = this.websiteAppsDBMap.get(logwithDBid);
        if (app == null)
            throw new GeneralException(ServletManager.Code.ClientError, "No such website_app");
        return app;
    }

    public ClassicApp findClassicAppWithLoginAndWebsite(String login, Website website) {
        for (App app : this.apps) {
            if (!app.isClassicApp())
                continue;
            String appLogin = ((ClassicApp) app).getAccount().getInformationNamed("login");
            if (appLogin.equals(login) && ((WebsiteApp) app).getSite().getName().equals(website.getName()))
                return (ClassicApp) app;
        }
        return null;
    }

    public List<ClassicApp> findClassicAppsWithWebsite(Website website) {
        List<ClassicApp> classicApps = new LinkedList<>();
        for (App app : this.apps) {
            if (!app.isClassicApp())
                continue;
            ClassicApp classicApp = (ClassicApp) app;
            if (classicApp.getSite() != website)
                continue;
            classicApps.add(classicApp);
        }
        return classicApps;
    }

    public JSONArray getJson() {
        JSONArray json = new JSONArray();
        for (List<Profile> column : this.profiles) {
            JSONArray columnJson = new JSONArray();
            for (Profile profile : column) {
                columnJson.add(profile.getJSON());
            }
            json.add(columnJson);
        }
        return json;
    }

    public List<WebsiteApp> getWebsiteApps() {
        return new LinkedList<WebsiteApp>(this.websiteAppsDBMap.values());
    }

    public void decipherApps(ServletManager sm) throws GeneralException {
        for (App app : this.apps) {
            if (!app.isClassicApp() || app.isDisabled())
                continue;
            ClassicApp classicApp = (ClassicApp) app;
            if (app.isPinned())
                classicApp.getAccount().decipherWithTeamKeyIfNeeded(app.getTeamUser_tenant().getDeciphered_teamKey());
            else {
                classicApp.getAccount().update_ciphering_if_needed(sm);
                classicApp.getAccount().decipher(this.user);
            }
        }
    }

    public void decipherApps(com.Ease.Utils.Servlets.ServletManager sm) throws HttpServletException {
        try {
            for (App app : this.apps) {
                if (!app.isClassicApp() || app.isDisabled())
                    continue;
                ClassicApp classicApp = (ClassicApp) app;
                if (app.isPinned())
                    classicApp.getAccount().decipherWithTeamKeyIfNeeded(app.getTeamUser_tenant().getDeciphered_teamKey());
                else {
                    classicApp.getAccount().update_ciphering_if_needed(sm);
                    classicApp.getAccount().decipher(this.user);
                }
            }
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }

    public List<ClassicApp> getClassicApps() {
        List<ClassicApp> classicApps = new LinkedList<>();
        for (App app : this.apps) {
            if (app.isClassicApp())
                classicApps.add((ClassicApp) app);
        }
        return classicApps;
    }

    public JSONArray getProfilesJson() {
        JSONArray res = new JSONArray();
        for (Profile profile : this.getProfilesList())
            res.add(profile.getJson());
        return res;
    }
}
