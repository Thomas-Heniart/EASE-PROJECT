package com.Ease.NewDashboard;

import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DashboardManager {
    private Map<Integer, Profile> profileMap = new ConcurrentHashMap<>();
    private Map<Integer, App> appMap = new ConcurrentHashMap<>();

    public DashboardManager(HibernateQuery hibernateQuery, Integer user_id) {
        hibernateQuery.queryString("SELECT p FROM Profile p WHERE p.user_id = :user_id");
        hibernateQuery.setParameter("user_id", user_id);
        List<Profile> profiles = hibernateQuery.list();
        for (Profile profile : profiles) {
            profileMap.put(profile.getDb_id(), profile);
            appMap.putAll(profile.getAppMap());
        }
    }

    /* JUST for compilation */

    public List<com.Ease.Dashboard.Profile.Profile> getProfilesList() {
        return new LinkedList<>();
    }

    public com.Ease.Dashboard.Profile.Profile getProfile(int i) {
        return null;
    }

    public com.Ease.Dashboard.Profile.Profile addProfile(String isc_paris, String s, DataBaseConnection db) {
        return null;
    }

    public List<com.Ease.Dashboard.App.App> getApps() {
        return new LinkedList<>();
    }

    public void removeAppFromCollections(com.Ease.Dashboard.App.App app) {
    }

    public void addApp(com.Ease.Dashboard.App.WebsiteApp.WebsiteApp app) {
    }

    public void addApp(com.Ease.Dashboard.App.App newApp) {
    }

    public void moveApp(int i, int i1, int i2, DataBaseConnection db) {
    }

    public com.Ease.Dashboard.App.App getAppWithId(int i) {
        return null;
    }

    public void removeAppWithId(int i, DataBaseConnection db) {
    }

    public com.Ease.Dashboard.Profile.Profile getProfileWithId(Integer profile_id) {
        return null;
    }

    public String getProfilesJson() {
        return "";
    }

    public JSONObject getJson() {
        return new JSONObject();
    }

    public com.Ease.Dashboard.Profile.Profile getProfileFromApp(Integer dBid) {
        return null;
    }

    public void replaceApp(ClassicApp newClassicApp) {
    }

    public void removeProfileWithPassword(int i, String password, ServletManager sm) {
    }

    public List<ClassicApp> getClassicApps() {
        return new LinkedList<>();
    }

    public void replaceApp(com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp newLogwithApp) {
    }

    public void moveProfile(int i, int i1, int i2, ServletManager sm) {
    }
}
