package com.Ease.User;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.SsoGroup;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    public static final int MAX_PROFILE = 4;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "firstName")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registration_date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "key_id")
    private UserKeys userKeys;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "option_id")
    private Options options;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private UserStatus userStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jwt_id")
    private JsonWebToken jsonWebToken;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserEmail> userEmailSet = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Profile> profileSet = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<SsoGroup> ssoGroupSet = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<TeamUser> teamUsers = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Notification> notificationSet = ConcurrentHashMap.newKeySet();

    public User() {

    }

    public User(String username, String email, UserKeys userKeys, Options options, UserStatus userStatus) {
        this.username = username;
        this.email = email;
        this.userKeys = userKeys;
        this.options = options;
        this.userStatus = userStatus;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public UserKeys getUserKeys() {
        return userKeys;
    }

    public void setUserKeys(UserKeys userKeys) {
        this.userKeys = userKeys;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public JsonWebToken getJsonWebToken() {
        return jsonWebToken;
    }

    public void setJsonWebToken(JsonWebToken jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    public Set<UserEmail> getUserEmailSet() {
        return userEmailSet;
    }

    public void setUserEmailSet(Set<UserEmail> userEmailSet) {
        this.userEmailSet = userEmailSet;
    }

    public Set<Profile> getProfileSet() {
        return profileSet;
    }

    public void setProfileSet(Set<Profile> profileSet) {
        this.profileSet = profileSet;
    }

    public Set<SsoGroup> getSsoGroupSet() {
        return ssoGroupSet;
    }

    public void setSsoGroupSet(Set<SsoGroup> ssoGroupSet) {
        this.ssoGroupSet = ssoGroupSet;
    }

    public Set<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(Set<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public Set<Notification> getNotificationSet() {
        return notificationSet;
    }

    public void setNotificationSet(Set<Notification> notificationSet) {
        this.notificationSet = notificationSet;
    }

    public boolean isAdmin() {
        return false;
    }

    public JSONObject getJson() throws HttpServletException {
        JSONObject res = new JSONObject();
        res.put("email", this.getEmail());
        res.put("first_name", this.getUsername());
        JSONArray teams = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers()) {
            JSONObject teamObject = teamUser.getTeam().getSimpleJson();
            teamObject.put("disabled", teamUser.isDisabled() || teamUser.getState() == 1 || (teamUser.getDepartureDate() != null && teamUser.getDepartureDate().getTime() <= new Date().getTime()));
            teamObject.put("state", teamUser.getState());
            teams.add(teamObject);
        }
        res.put("teams", teams);
        res.put("status", this.getUserStatus().getJson());
        res.put("background_picture", this.getOptions().isBackground_picked());
        return res;
    }

    public void addProfile(Profile profile) {
        this.getProfileSet().add(profile);
    }

    public void removeProfile(Profile profile) {
        this.getProfileSet().remove(profile);
    }

    public JSONArray getProfileListJson() {
        JSONArray res = new JSONArray();
        List<List<Profile>> profiles = new LinkedList<>();
        for (int i = 0; i < MAX_PROFILE; i++)
            profiles.add(new LinkedList<>());
        this.getProfileSet().stream().forEach(profile -> profiles.get(profile.getColumn_index()).add(profile));
        profiles.forEach(profiles1 -> {
            JSONArray tmp = new JSONArray();
            profiles1.stream().sorted(Comparator.comparingInt(Profile::getPosition_index)).forEach(profile -> tmp.add(profile.getJson()));
            res.add(tmp);
        });
        return res;
    }

    public JSONArray getProfilesJson() {
        JSONArray res = new JSONArray();
        this.getProfileSet().forEach(profile -> res.add(profile.getJson()));
        return res;
    }

    public Profile getProfile(Integer profile_id) throws HttpServletException {
        Profile profile = this.getProfileSet().stream().filter(profile1 -> profile1.getDb_id().equals(profile_id)).findFirst().orElse(null);
        if (profile == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such profile");
        return profile;
    }

    public Set<App> getApps() {
        return this.getProfileSet().stream().flatMap(profile -> profile.getAppSet().stream()).collect(Collectors.toSet());
    }

    public App getApp(Integer app_id, HibernateQuery hibernateQuery) throws HttpServletException {
        hibernateQuery.queryString("SELECT a FROM App a WHERE a.db_id = :id");
        hibernateQuery.setParameter("id", app_id);
        App app = (App) hibernateQuery.getSingleResult();
        if (app == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such app");
        Profile profile = app.getProfile();
        if ((app.getTeamCardReceiver() != null && this.getTeamUsers().contains(app.getTeamCardReceiver().getTeamUser())) || (profile != null && !profile.getUser().equals(this)))
            throw new HttpServletException(HttpStatus.Forbidden);
        return app;
    }

    public SsoGroup getSsoGroup(Integer sso_group_id) throws HttpServletException {
        SsoGroup ssoGroup = this.getSsoGroupSet().stream().filter(ssoGroup1 -> ssoGroup1.getDb_id().equals(sso_group_id)).findFirst().orElse(null);
        if (ssoGroup == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such SsoGroup");
        return ssoGroup;
    }

    public void moveProfile(Integer profile_id, Integer column_index, Integer position, HibernateQuery hibernateQuery) throws HttpServletException {
        Profile profile = this.getProfile(profile_id);
        if (column_index.equals(profile.getColumn_index())) {
            Integer old_position = profile.getPosition_index();
            if (old_position.equals(position))
                return;
            if (position > old_position) {
                this.getProfileSet().stream().filter(profile1 -> !profile1.equals(profile) && profile1.getColumn_index().equals(column_index) && profile1.getPosition_index() >= old_position && profile1.getPosition_index() <= position).forEach(profile1 -> {
                    profile1.setPosition_index(profile1.getPosition_index() - 1);
                    hibernateQuery.saveOrUpdateObject(profile1);
                });
            } else {
                this.getProfileSet().stream().filter(profile1 -> !profile1.equals(profile) && profile1.getColumn_index().equals(column_index) && profile1.getPosition_index() >= position && profile1.getPosition_index() <= old_position).forEach(profile1 -> {
                    profile1.setPosition_index(profile1.getPosition_index() + 1);
                    hibernateQuery.saveOrUpdateObject(profile1);
                });
            }
            Profile max_position_profile = this.getProfileSet().stream().filter(profile1 -> !profile1.equals(profile) && profile1.getColumn_index().equals(column_index)).max(Comparator.comparingInt(Profile::getPosition_index)).orElse(null);
            if (max_position_profile == null)
                profile.setPosition_index(0);
            else
                profile.setPosition_index(position > max_position_profile.getPosition_index() ? max_position_profile.getPosition_index() + 1 : position);
        } else {
            if (column_index > Profile.MAX_COLUMN_INDEX || column_index < Profile.MIN_COLUMN_INDEX)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid column_index parameter");
            this.getProfileSet().stream().filter(profile1 -> !profile1.equals(profile) && profile1.getColumn_index().equals(profile.getColumn_index()) && profile1.getPosition_index() >= profile.getPosition_index()).forEach(profile1 -> {
                profile1.setPosition_index(profile1.getPosition_index() - 1);
                hibernateQuery.saveOrUpdateObject(profile1);
            });
            this.getProfileSet().stream().filter(profile1 -> !profile.equals(profile1) && profile1.getColumn_index().equals(column_index) && profile1.getPosition_index() >= position).forEach(profile1 -> {
                profile1.setPosition_index(profile1.getPosition_index() + 1);
                hibernateQuery.saveOrUpdateObject(profile1);
            });
            profile.setColumn_index(column_index);
            Profile max_position_profile = this.getProfileSet().stream().filter(profile1 -> !profile1.equals(profile) && profile1.getColumn_index().equals(column_index)).max(Comparator.comparingInt(Profile::getPosition_index)).orElse(null);
            if (max_position_profile == null)
                profile.setPosition_index(0);
            else
                profile.setPosition_index(position > max_position_profile.getPosition_index() ? max_position_profile.getPosition_index() + 1 : position);
        }
        hibernateQuery.saveOrUpdateObject(profile);
    }

    public void removeProfileAndUpdatePositions(Profile profile, HibernateQuery hibernateQuery) {
        Integer column_index = profile.getColumn_index();
        Integer old_position = profile.getPosition_index();
        this.removeProfile(profile);
        this.getProfileSet().stream().filter(profile1 -> profile1.getColumn_index().equals(column_index) && profile1.getPosition_index() >= old_position).forEach(profile1 -> {
            profile1.setPosition_index(profile1.getPosition_index() - 1);
            hibernateQuery.saveOrUpdateObject(profile1);
        });
        hibernateQuery.deleteObject(profile);
    }

    public TeamUser getTeamUser(Team team) throws HttpServletException {
        TeamUser teamUser = this.getTeamUsers().stream().filter(teamUser1 -> teamUser1.getTeam().equals(team)).findAny().orElse(null);
        if (teamUser == null)
            throw new HttpServletException(HttpStatus.BadRequest, "You are not part of this team");
        return teamUser;
    }

    public TeamUser getTeamUser(Integer team_id) throws HttpServletException {
        TeamUser teamUser = this.getTeamUsers().stream().filter(teamUser1 -> teamUser1.getTeam().getDb_id().equals(team_id)).findAny().orElse(null);
        if (teamUser == null)
            throw new HttpServletException(HttpStatus.BadRequest, "You are not part of this team");
        return teamUser;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.getTeamUsers().add(teamUser);
    }

    public Set<String> getVerifiedEmails() {
        return this.getUserEmailSet().stream().filter(UserEmail::isVerified).map(UserEmail::getEmail).collect(Collectors.toSet());
    }

    public Set<String> getUnverifiedEmails() {
        return this.getUserEmailSet().stream().filter(userEmail -> !userEmail.isVerified()).map(UserEmail::getEmail).collect(Collectors.toSet());
    }

    public UserEmail getUserEmail(String email) {
        return this.getUserEmailSet().stream().filter(userEmail -> userEmail.getEmail().equals(email)).findAny().orElse(null);
    }
}