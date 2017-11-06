package com.Ease.NewDashboard;

import com.Ease.Hibernate.HibernateQuery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

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

    public void addApp(App app) {
        this.getAppMap().put(app.getDb_id(), app);
    }

    /**
     * @return a JSONObect representing this profile with a JSONArray of app ids ordered by app position
     */
    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        JSONArray app_ids = new JSONArray();
        this.getAppMap().values().stream().sorted(Comparator.comparingInt(App::getPosition)).forEach(app -> app_ids.add(app.getDb_id()));
        res.put("app_ids", app_ids);
        res.put("name", this.getProfileInformation().getName());
        res.put("column_index", this.getColumn_index());
        return res;
    }

    public Stream<App> getApps() {
        return this.getAppMap().values().stream().sorted(Comparator.comparingInt(App::getPosition));
    }

    public void removeAppAndUpdatePositions(App app, HibernateQuery hibernateQuery) {
        int position = app.getPosition();
        this.getAppMap().values().stream().filter(app1 -> app != app1 && app1.getPosition() >= position).forEach(app1 -> {
            app1.setPosition(app1.getPosition() - 1);
            hibernateQuery.saveOrUpdateObject(app1);
        });
        this.getAppMap().remove(app.getDb_id());
    }

    public void updateAppPositions(App app, Integer position, HibernateQuery hibernateQuery) {
        if (app.getPosition().equals(position))
            return;
        if (app.getPosition() < position) {
            this.getApps().filter(app1 -> app != app1 && app1.getPosition() > app.getPosition() && app1.getPosition() <= position).forEach(app1 -> {
                app1.setPosition(app1.getPosition() - 1);
                hibernateQuery.saveOrUpdateObject(app1);
            });
        } else {
            this.getApps().filter(app1 -> app != app1 && app1.getPosition() >= position && app1.getPosition() < app.getPosition()).forEach(app1 -> {
                app1.setPosition(app1.getPosition() + 1);
                hibernateQuery.saveOrUpdateObject(app1);
            });
        }
        app.setPosition(position >= this.getAppMap().size() ? this.getAppMap().size() - 1 : position);
        hibernateQuery.saveOrUpdateObject(app);
    }

    public void addAppAndUpdatePositions(App app, Integer position, HibernateQuery hibernateQuery) {
        this.addApp(app);
        this.getAppMap().values().stream().filter(app1 -> app != app1 && app1.getPosition() >= position).forEach(app1 -> {
            app1.setPosition(app1.getPosition() + 1);
            hibernateQuery.saveOrUpdateObject(app1);
        });
        app.setPosition(position >= this.getAppMap().size() ? this.getAppMap().size() - 1 : position);
        hibernateQuery.saveOrUpdateObject(app);
    }
}