package com.Ease.NewDashboard;

import javax.persistence.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "column_idx")
    private Integer column_index;

    @Column(name = "position_idx")
    private Integer position_index;

    @OneToOne
    @JoinColumn(name = "profile_info_id")
    private ProfileInformation profileInformation;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "profile")
    @MapKey(name = "db_id")
    private Map<Integer, App> appMap = new ConcurrentHashMap<>();

    public Profile() {

    }

    public Profile(Integer user_id, Integer column_index, Integer position_index, ProfileInformation profileInformation) {
        this.user_id = user_id;
        this.column_index = column_index;
        this.position_index = position_index;
        this.profileInformation = profileInformation;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getColumn_index() {
        return column_index;
    }

    public void setColumn_index(Integer column_index) {
        this.column_index = column_index;
    }

    public Integer getPosition_index() {
        return position_index;
    }

    public void setPosition_index(Integer position_index) {
        this.position_index = position_index;
    }

    public ProfileInformation getProfileInformation() {
        return profileInformation;
    }

    public void setProfileInformation(ProfileInformation profileInformation) {
        this.profileInformation = profileInformation;
    }

    public Map<Integer, App> getAppMap() {
        return appMap;
    }

    public void setAppMap(Map<Integer, App> appMap) {
        this.appMap = appMap;
    }
}