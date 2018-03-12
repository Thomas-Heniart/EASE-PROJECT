package com.Ease.NewDashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.ClickOnApp;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "apps")
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class App {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @OneToOne(cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "app_info_id")
    private AppInformation appInformation;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "position")
    private Integer position;

    @OneToOne(mappedBy = "app")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private TeamCardReceiver teamCardReceiver;

    @Column(name = "new")
    private boolean newApp = true;

    @Column(name = "insert_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insert_date = new Date();

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

    public Date getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
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

    public boolean isAnyApp() {
        return false;
    }

    public boolean isSoftwareApp() {
        return false;
    }

    public Account getAccount() {
        return null;
    }

    public void setAccount(Account account) {
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
        if (this.getTeamCardReceiver() != null) {
            JSONObject tmp = this.getTeamCardReceiver().getJson();
            tmp.keySet().forEach(o -> res.put(String.valueOf(o), tmp.get(String.valueOf(o))));
        }
        return res;
    }

    public JSONObject getWebSocketJson() {
        JSONObject res = new JSONObject();
        res.put("app", this.getJson());
        return res;
    }

    public JSONObject getRestJson() {
        return this.getJson();
    }

    public void decipher(String symmetric_key, String team_key) throws HttpServletException {
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

    public boolean hasBeenClickedForDays(int number_of_days, HibernateQuery hibernateQuery) {
        return this.getClickMetric(hibernateQuery).hasBeenClickedForDays(number_of_days);
    }

    public ClickOnApp getClickMetric(HibernateQuery hibernateQuery) {
        Calendar calendar = Calendar.getInstance();
        return ClickOnApp.getMetricForApp(this.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), hibernateQuery);
    }

    public boolean isEmpty() {
        return false;
    }


}