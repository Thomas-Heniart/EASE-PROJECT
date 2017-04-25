package com.Ease.NewDashboard.Profile;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App.App;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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
        System.out.println("Profile id: " + profile.getDb_id());
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

    public ProfileApp addApp(App app, Profile profile) {
        ProfileApp profileApp = new ProfileApp(profile, app, this.profileApps.size());
        this.addProfileApp(profileApp);
        return profileApp;
    }

    public App getAppWithId(Integer app_id) throws GeneralException {
        for (ProfileApp profileApp : this.profileApps) {
            if (profileApp.getApp().getDb_id() == app_id)
                return profileApp.getApp();
        }
        throw new GeneralException(ServletManager.Code.ClientError, "This app does not exist.");
    }
}
