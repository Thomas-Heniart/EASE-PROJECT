package com.Ease.Dashboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.AccountInformation;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.Profile.ProfilePermissions;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

public class DashboardManager {
    protected User user;
    protected List<App> apps;
    protected List<List<Profile>> profiles;
    protected Map<String, App> appsDBMap;
    protected Map<Integer, App> appsIDMap;
    protected Map<String, Profile> profileDBMap;
    protected Map<Integer, Profile> profileIDMap;
    private HashMap<String, WebsiteApp> websiteAppsDBMap;

    public DashboardManager(User user, ServletManager sm) throws GeneralException {
        this.user = user;
        this.apps = new LinkedList<App>();
        this.appsDBMap = new HashMap<String, App>();
        this.appsIDMap = new HashMap<Integer, App>();
        this.websiteAppsDBMap = new HashMap<String, WebsiteApp>();
        this.profileDBMap = new HashMap<String, Profile>();
        this.profileIDMap = new HashMap<Integer, Profile>();
        this.profiles = Profile.loadProfiles(user, sm);
        this.initializeProfilesAndApps();
        System.out.println(this.apps.size());
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

    public Profile addProfile(String name, String color, ServletManager sm) throws GeneralException {
        int column = this.getMostEmptyProfileColumn();
        Profile newProfile = Profile.createPersonnalProfile(this.user, column, this.profiles.get(column).size(), name, color, sm);
        this.profiles.get(column).add(newProfile);
        this.addProfileToMaps(newProfile);

        return newProfile;
    }

    public void addProfileToMaps(Profile profile) {
        this.profileDBMap.put(profile.getDBid(), profile);
        this.profileIDMap.put(profile.getSingleId(), profile);
    }

    public void addApp(App app) {
        this.apps.add(app);
        this.appsDBMap.put(app.getDBid(), app);
        this.appsIDMap.put(app.getSingleId(), app);
        if (app.getType().equals("ClassicApp") || app.getType().equals("LogwithApp")) {
            this.websiteAppsDBMap.put(((WebsiteApp) app).getWebsiteAppDBid(), (WebsiteApp) app);
        }
    }

    private void removeApp(App app, ServletManager sm) throws GeneralException {
        user.getUpdateManager().removeAllUpdateWithThisApp(app, sm);
        Profile profile = app.getProfile();
        this.apps.remove(app);
        this.appsDBMap.remove(app.getDBid());
        this.appsIDMap.remove(app.getSingleId());
        profile.removeApp(app, sm);
    }

    public void removeAppWithSingleId(int single_id, ServletManager sm) throws GeneralException {
        App app = this.appsIDMap.get(single_id);
        this.removeApp(app, sm);
        if (app.getType().equals("ClassicApp")) {
            for (AccountInformation info : ((ClassicApp) app).getAccount().getAccountInformations()) {
                if (Regex.isEmail(info.getInformationValue()) == true) {
                    String email = info.getInformationValue();
                    if (this.user.getEmails().get(email) != null && this.user.getEmails().get(email).removeIfNotUsed(sm))
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

    public Profile getProfileFromApp(int single_id) throws GeneralException {
        for (List<Profile> column : this.profiles) {
            for (Profile profile : column) {
                for (App app : profile.getApps()) {
                    if (app.getSingleId() == single_id)
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

    public App getAppWithDBid(String db_id) throws GeneralException {
        App app = this.appsDBMap.get(db_id);
        if (app == null)
            throw new GeneralException(ServletManager.Code.ClientError, "No such db_id for apps");
        return app;
    }

    public App getAppWithID(Integer single_id) throws GeneralException {
        App app = this.appsIDMap.get(single_id);
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
        this.appsDBMap.put(app.getDBid(), app);
        this.appsIDMap.put(app.getSingleId(), app);
        app.getProfile().getApps().set(app.getPosition(), app);
    }

    public void removeProfileWithPassword(int single_id, String password, ServletManager sm) throws GeneralException {
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
                    this.profileDBMap.remove(tmpProfile.getDBid());
                    this.profileIDMap.remove(tmpProfile.getSingleId());
                    return;
                }

            }
        }
        throw new GeneralException(ServletManager.Code.ClientError, "This profile does not exist");
    }

    public void removeFromDB(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        List<App> apps = new LinkedList<App>();
        for (App app : this.getApps()) {
            if (app.getType().equals("LogwithApp")) {
                app.getProfile().removeApp(app, sm);
            } else {
                apps.add(app);
            }
        }
        for (App app : apps) {
            app.getProfile().removeApp(app, sm);
        }
        for (List<Profile> column : this.profiles) {
            for (Profile profile : column) {
                profile.removeFromDB(sm);
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

    public void moveApp(int appId, int profileIdDest, int positionDest, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        App app = this.appsIDMap.get(appId);
        Profile profileDest = this.getProfile(profileIdDest);
        if (positionDest < 0 || positionDest > profileDest.getApps().size())
            throw new GeneralException(ServletManager.Code.ClientError, "PositionDest fucked.");
        if (profileDest == app.getProfile()) {
            profileDest.getApps().remove(app);
            profileDest.getApps().add(positionDest, app);
            profileDest.updateAppsIndex(sm);
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
            profileSrc.updateAppsIndex(sm);
            profileDest.getApps().add(positionDest, app);
            profileDest.updateAppsIndex(sm);
        }
        db.commitTransaction(transaction);
    }

    public App getWebsiteAppWithDBid(String logwithDBid) throws GeneralException {
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
			if (!app.isClassicApp())
			    continue;
			ClassicApp classicApp = (ClassicApp) app;
			classicApp.getAccount().update_ciphering_if_needed(sm);
			classicApp.getAccount().decipher(sm.getUser());
        }
    }
}
