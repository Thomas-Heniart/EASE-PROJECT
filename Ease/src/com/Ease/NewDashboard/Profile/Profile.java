package com.Ease.NewDashboard.Profile;

import com.Ease.NewDashboard.App.App;
import com.Ease.NewDashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Utils.GeneralException;

import javax.persistence.*;

/**
 * Created by thomas on 20/04/2017.
 */

@Entity
@Table(name = "Profiles")
public class Profile {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "column_idx")
    protected Integer column_idx;

    @Column(name = "position_idx")
    protected Integer position_idx;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_info_id")
    protected ProfileInformation profileInformation;

    @Transient
    protected ProfileAppManager profileAppManager;

    public Profile(Integer column_idx, Integer position_idx, ProfileInformation profileInformation) {
        this.column_idx = column_idx;
        this.position_idx = position_idx;
        this.profileInformation = profileInformation;
    }

    public Profile() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Integer getColumn_idx() {
        return column_idx;
    }

    public void setColumn_idx(Integer column_idx) {
        this.column_idx = column_idx;
    }

    public Integer getPosition_idx() {
        return position_idx;
    }

    public void setPosition_idx(Integer position_idx) {
        this.position_idx = position_idx;
    }

    public ProfileInformation getProfileInformation() {
        return profileInformation;
    }

    public void setProfileInformation(ProfileInformation profileInformation) {
        this.profileInformation = profileInformation;
    }

    public void initializeProfileAppManager() {
        this.profileAppManager = new ProfileAppManager();
    }

    public void populateProfileAppManager() {
        this.profileAppManager = new ProfileAppManager();
        this.profileAppManager.populate(this);
    }

    public ProfileApp addApp(App app) {
        return this.profileAppManager.addApp(app, this);
    }

    public App getAppWithId(Integer app_id) throws GeneralException {
        return this.profileAppManager.getAppWithId(app_id);
    }
}
