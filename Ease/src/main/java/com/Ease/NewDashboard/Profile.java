package com.Ease.NewDashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Slack.SlackAPIWrapper;
import com.github.seratch.jslack.api.methods.SlackApiException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "column_idx")
    private Integer column_index;

    @Column(name = "position_idx")
    private Integer position_index;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_info_id")
    private ProfileInformation profileInformation;

    @OneToMany(mappedBy = "profile")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<App> appSet = ConcurrentHashMap.newKeySet();

    @ManyToOne
    @JoinColumn(name = "teamUser_id")
    private TeamUser teamUser;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public final static Integer MIN_COLUMN_INDEX = 0;
    public final static Integer MAX_COLUMN_INDEX = 3;

    public Profile() {

    }

    public Profile(User user, Integer column_index, Integer position_index, ProfileInformation profileInformation) {
        this.user = user;
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

    public synchronized User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public synchronized Set<App> getAppSet() {
        return appSet;
    }

    public void setAppSet(Set<App> appSet) {
        this.appSet = appSet;
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public void setTeamUser(TeamUser teamUser) {
        if (this.teamUser != null)
            this.teamUser.removeProfile(this);
        this.teamUser = teamUser;
        if (teamUser != null)
            teamUser.addProfile(this);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        if (this.channel != null)
            this.channel.removeProfile(this);
        this.channel = channel;
        if (channel != null)
            channel.addProfile(this);
    }

    public void addApp(App app) {
        this.getAppSet().add(app);
        if (this.getUser() == null)
            return;
        int appsSize = this.getUser().getApps().size();
        if (appsSize == 10 || appsSize == 20) {
            StringBuilder stringBuilder = new StringBuilder("*✅User has ")
                    .append(appsSize)
                    .append(" apps*\nThis user has now more than 10 apps: ")
                    .append(this.getUser().getEmail())
                    .append(", ")
                    .append(this.getUser().getPersonalInformation().getPhone_number() != null ? this.getUser().getPersonalInformation().getPhone_number() : "");
            for (TeamUser teamUser : this.getUser().getTeamUsers())
                stringBuilder
                        .append(" (")
                        .append(teamUser.getTeamUserRole().getRoleName())
                        .append(", ")
                        .append(teamUser.getTeam().getName())
                        .append(")");
            stringBuilder.append("\n=======\n=======\n=======");
            try {
                SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
            } catch (IOException | SlackApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return a JSONObect representing this profile with a JSONArray of app ids ordered by app position
     */
    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        JSONArray app_ids = new JSONArray();
        this.getAppSet().stream().sorted(Comparator.comparingInt(App::getPosition)).forEach(app -> app_ids.put(app.getDb_id()));
        res.put("app_ids", app_ids);
        res.put("name", this.getProfileInformation().getName());
        res.put("column_index", this.getColumn_index());
        res.put("position_index", this.getPosition_index());
        res.put("team_id", this.getTeamUser() == null ? -1 : this.getTeamUser().getTeam().getDb_id());
        return res;
    }

    public JSONObject getWebSocketJson() {
        JSONObject res = new JSONObject();
        res.put("profile", this.getJson());
        return res;
    }

    public Stream<App> getApps() {
        return this.getAppSet().stream().sorted(Comparator.comparingInt(App::getPosition));
    }

    public App getApp(Integer app_id) throws HttpServletException {
        App app = this.getAppSet().stream().filter(app1 -> app1.getDb_id().equals(app_id)).findFirst().orElse(null);
        if (app == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such app");
        return app;
    }

    public void removeApp(App app) {
        this.getAppSet().remove(app);
    }

    public synchronized void removeAppAndUpdatePositions(App app, HibernateQuery hibernateQuery) {
        int position = app.getPosition();
        this.getAppSet().stream().filter(app1 -> !app.equals(app1) && app1.getPosition() >= position).forEach(app1 -> {
            app1.setPosition((app1.getPosition() != null && app1.getPosition() > 0) ? (app1.getPosition() - 1) : 0);
            hibernateQuery.saveOrUpdateObject(app1);
        });
        this.getAppSet().remove(app);
        JSONObject ws_obj = new JSONObject();
        ws_obj.put("app_id", app.getDb_id());
        if (this.getAppSet().isEmpty()) {
            TeamUser teamUser = this.getTeamUser();
            if (teamUser != null)
                teamUser.removeProfile(this);
            this.setTeamUser(null);
            Channel channel = this.getChannel();
            if (channel != null)
                channel.removeProfile(this);
            this.setChannel(null);
            JSONObject ws_obj1 = new JSONObject();
            ws_obj1.put("profile_id", this.getDb_id());
            this.getUser().removeProfileAndUpdatePositions(this, hibernateQuery);
        }

    }

    public synchronized void updateAppPositions(App app, Integer position, HibernateQuery hibernateQuery) {
        int appPosition = app.getPosition();
        this.getAppSet().stream().filter(app1 -> !app.equals(app1) && app1.getPosition() >= appPosition).forEach(app1 -> {
            app1.setPosition((app1.getPosition() != null && app1.getPosition() > 0) ? (app1.getPosition() - 1) : 0);
            hibernateQuery.saveOrUpdateObject(app1);
        });
        this.getAppSet().remove(app);
        this.addApp(app);
        this.getAppSet().stream().filter(app1 -> !app.equals(app1) && app1.getPosition() >= position).forEach(app1 -> {
            app1.setPosition(app1.getPosition() + 1);
            hibernateQuery.saveOrUpdateObject(app1);
        });
        app.setPosition(appPosition > this.getAppSet().size() ? this.getAppSet().size() - 1 : position);
        app.setProfile(this);
        hibernateQuery.saveOrUpdateObject(app);
    }

    public synchronized void addAppAndUpdatePositions(App app, Integer position, HibernateQuery hibernateQuery) {
        this.addApp(app);
        this.getAppSet().stream().filter(app1 -> !app.equals(app1) && app1.getPosition() >= position).forEach(app1 -> {
            app1.setPosition(app1.getPosition() + 1);
            hibernateQuery.saveOrUpdateObject(app1);
        });
        app.setPosition(position > this.getAppSet().size() ? this.getAppSet().size() - 1 : position);
        app.setProfile(this);
        hibernateQuery.saveOrUpdateObject(app);
    }

    public synchronized int getSize() {
        return this.getAppSet().size();
    }

    public boolean isTeamProfile() {
        return this.getTeamUser() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        return db_id.equals(profile.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}