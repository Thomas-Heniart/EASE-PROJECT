package com.Ease.NewDashboard;

import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "apps")
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class App {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_info_id")
    private AppInformation appInformation;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "position")
    private Integer position;

    @OneToOne(mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeamCardReceiver teamCardReceiver;

    @Column(name = "new")
    private boolean newApp = true;

    public App() {

    }

    public App(AppInformation appInformation) {
        this.appInformation = appInformation;
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

    public TeamCardReceiver getTeamCardReceiver() {
        return teamCardReceiver;
    }

    public void setTeamCardReceiver(TeamCardReceiver teamCardReceiver) {
        this.teamCardReceiver = teamCardReceiver;
    }

    public boolean isNewApp() {
        return newApp;
    }

    public void setNewApp(boolean newApp) {
        this.newApp = newApp;
    }

    public abstract String getLogo();

    public abstract String getType();

    public boolean isClassicApp() {
        return false;
    }

    public boolean isWebsiteApp() {
        return false;
    }

    public boolean isLinkApp() {
        return false;
    }

    public boolean isLogWithApp() {
        return false;
    }

    public boolean isSsoApp() {
        return false;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("logo", this.getLogo());
        res.put("name", this.getAppInformation().getName());
        res.put("type", this.getType());
        res.put("new", this.isNewApp());
        res.put("profile_id", this.getProfile() == null ? -1 : this.getProfile().getDb_id());
        res.put("position", this.getPosition() == null ? -1 : this.getPosition());
        if (this.getTeamCardReceiver() != null)
            res.putAll(this.getTeamCardReceiver().getJson());
        return res;
    }

    public JSONObject getRestJson() {
        return this.getJson();
    }

    public void decipher(String symmetric_key) throws HttpServletException {
        return;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        return db_id.equals(app.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public JSONArray getConnectionJson(String public_key) throws HttpServletException {
        return new JSONArray();
    }
}