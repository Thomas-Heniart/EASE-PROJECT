package com.Ease.NewDashboard.Profile;

import com.Ease.NewDashboard.App.App;
import com.Ease.NewDashboard.Profile.Profile;

import javax.persistence.*;

/**
 * Created by thomas on 20/04/2017.
 */
@Entity
@Table(name = "ProfileAndAppMap")
public class ProfileApp {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @OneToOne
    @JoinColumn(name = "profile_id")
    protected Profile profile;

    @OneToOne
    @JoinColumn(name = "app_id")
    protected App app;

    @Column(name = "position")
    protected Integer position;

    public ProfileApp(Profile profile, App app, Integer position) {
        this.profile = profile;
        this.app = app;
        this.position = position;
    }

    public ProfileApp() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ID: " + this.db_id + ", position: " + this.position + ", " + this.getApp().toString();
    }
}
