package com.Ease.NewDashboard;

import javax.persistence.*;

@Entity
@Table(name = "apps")
@Inheritance(strategy=InheritanceType.JOINED)
abstract public class App {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_info_id")
    private AppInformation appInformation;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "position")
    private Integer position;

    public App() {

    }

    public App(AppInformation appInformation, String type) {
        this.appInformation = appInformation;
        this.type = type;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public AppInformation getAppInformation() {
        return appInformation;
    }

    public void setAppInformation(AppInformation appInformation) {
        this.appInformation = appInformation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}