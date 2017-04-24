package com.Ease.NewDashboard.Profile;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App.App;

import java.util.*;

/**
 * Created by thomas on 24/04/2017.
 */
public class ProfileAppManager {
    protected List<ProfileApp> profileApps;
    protected Map<Integer, ProfileApp> profileAppIdMap;

    public ProfileAppManager() {
        this.profileApps = new LinkedList<ProfileApp>();
        this.profileAppIdMap = new HashMap<Integer, ProfileApp>();
    }

    public void populate(Profile profile) {
        HibernateQuery query = new HibernateQuery();
        query.queryString("SELECT p FROM ProfileApp p WHERE p.profile.db_id = :id ORDER BY p.position ASC");
        query.setParameter("id", profile.getDb_id());
        profileApps = query.list();

        /* Put all profiles */
        for(ProfileApp profileApp : profileApps) {
            profileApp.setProfile(profile);
            this.profileAppIdMap.put(profileApp.getDb_id(), profileApp);
        }
        query.commit();
        System.out.println("ProfileAppManager apps size: " + profileApps.size());
        for (ProfileApp profileApp : this.profileApps) {
            System.out.println(profileApp.toString());
        }
    }

    private void addProfileApp(ProfileApp profileApp) {
        this.profileApps.add(profileApp);
        this.profileAppIdMap.put(profileApp.getDb_id(), profileApp);
    }

    public void addApp(App app, Profile profile) {
        HibernateQuery query = new HibernateQuery();
        ProfileApp profileApp = new ProfileApp(profile, app, this.profileApps.size());
        query.saveOrUpdateObject(profileApp);
        query.commit();
        this.addProfileApp(profileApp);
    }
}
