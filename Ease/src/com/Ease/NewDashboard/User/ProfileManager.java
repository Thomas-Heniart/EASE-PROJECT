package com.Ease.NewDashboard.User;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile.Profile;
import com.Ease.NewDashboard.Profile.ProfileInformation;
import com.Ease.NewDashboard.UserProfile;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import java.util.*;

/**
 * Created by thomas on 21/04/2017.
 */
public class ProfileManager {
    public static int MAX_COLUMN = 5;

    List<List<Profile>> profileList;
    Map<Integer, Profile> profileMap;

    public ProfileManager() {
        profileList = new LinkedList<List<Profile>>();
        profileMap = new HashMap<Integer, Profile>();
        for (int i = 0; i < MAX_COLUMN; ++i) {
            profileList.add(new LinkedList<Profile>());
        }
    }

    public void createFirstProfilesForUser(User user) {
        HibernateQuery query = new HibernateQuery();
        ProfileInformation profileInformation1 = new ProfileInformation("Side", "#000000");
        Profile profile1 = new Profile(0, 0, profileInformation1);
        profile1.initializeProfileAppManager();
        UserProfile userProfile1 = new UserProfile(user, profile1);
        query.saveOrUpdateObject(profile1);
        query.saveOrUpdateObject(userProfile1);
        query.commit();
        query = new HibernateQuery();
        ProfileInformation profileInformation2 = new ProfileInformation("Me", "#373B60");
        Profile profile2 = new Profile(1, 0, profileInformation2);
        profile2.initializeProfileAppManager();
        UserProfile userProfile2 = new UserProfile(user, profile2);
        query.saveOrUpdateObject(profile2);
        query.saveOrUpdateObject(userProfile2);
        query.commit();
        this.addProfile(profile1);
        this.addProfile(profile2);
    }

    public void populate(Integer user_id) {
        HibernateQuery query = new HibernateQuery();
        query.queryString("SELECT p.profile FROM UserProfile p WHERE p.user.db_id = :id");
        query.setParameter("id", user_id);
        List<Profile> profiles = query.list();

        /* Put all profiles */
        for(Profile profile : profiles) {
            this.addProfile(profile);
            profile.populateProfileAppManager();
        }

        /* Sort profiles */
        for (List<Profile> column : profileList) {
            column.sort(new Comparator<Profile>() {
                @Override
                public int compare(Profile a, Profile b) {
                    return a.getPosition_idx() - b.getPosition_idx();
                }
            });
        }
        query.commit();
        System.out.println("ProfileManager profiles size: " + profileMap.size());
    }

    private void addProfile(Profile profile) {
        profileList.get(profile.getColumn_idx()).add(profile);
        profileMap.put(profile.getDb_id(), profile);
    }


    public Profile getProfileWithId(Integer profile_id) throws GeneralException {
        Profile profile = this.profileMap.get(profile_id);
        if (profile == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This profile does not exist.");
        return profile;
    }
}
