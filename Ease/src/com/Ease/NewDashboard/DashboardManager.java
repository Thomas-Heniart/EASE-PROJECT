package com.Ease.NewDashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONArray;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DashboardManager {
    public static final int MAX_PROFILE = 4;

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

    public Map<Integer, Profile> getProfileMap() {
        return profileMap;
    }

    public Map<Integer, App> getAppMap() {
        return appMap;
    }

    public Profile getProfile(Integer id) throws HttpServletException {
        Profile profile = this.getProfileMap().get(id);
        if (profile == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This profile does not exist");
        return profile;
    }

    public App getApp(Integer id) throws HttpServletException {
        App app = this.getAppMap().get(id);
        if (app == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This app does not exist");
        return app;
    }

    public void addApp(App app) {
        this.getAppMap().put(app.getDb_id(), app);
    }

    public void removeApp(App app) {
        this.getAppMap().remove(app.getDb_id());
    }

    public void addProfile(Profile profile) {
        this.getProfileMap().put(profile.getDb_id(), profile);
    }

    public void removeProfile(Profile profile) {
        this.getProfileMap().remove(profile.getDb_id());
    }

    public void decipher(String keyUser, List<TeamUser> teamUsers) throws HttpServletException {
        for (App app : this.getAppMap().values()) {
            if (app.getTeamCardReceiver() != null) {
                TeamUser teamUser = teamUsers.stream().filter(teamUser1 -> teamUser1.equals(app.getTeamCardReceiver().getTeamUser())).findFirst().get();
                app.decipher(teamUser.getDeciphered_teamKey());
                continue;
            }
            app.decipher(keyUser);
        }
    }

    public JSONArray getProfileListJson() {
        JSONArray res = new JSONArray();
        List<List<Profile>> profiles = new LinkedList<>();
        for (int i = 0; i < MAX_PROFILE; i++)
            profiles.add(new LinkedList<>());
        this.getProfileMap().values().stream().forEach(profile -> profiles.get(profile.getColumn_index()).add(profile));
        profiles.forEach(profiles1 -> {
            JSONArray tmp = new JSONArray();
            profiles1.stream().sorted(Comparator.comparingInt(Profile::getPosition_index)).forEach(profile -> tmp.add(profile.getJson()));
            res.add(tmp);
        });
        return res;
    }

    public JSONArray getProfilesJson() {
        JSONArray res = new JSONArray();
        this.getProfileMap().values().forEach(profile -> res.add(profile.getJson()));
        return res;
    }
}
