package com.Ease.User;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Importation.ImportedAccount;
import com.Ease.Metrics.ConnectionMetric;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.ProfileInformation;
import com.Ease.NewDashboard.SsoGroup;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users")
public class User {
    public static final int MAX_PROFILE = 4;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registration_date = new Date();

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Set<UserEmail> userEmailSet = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Profile> profileSet = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SsoGroup> ssoGroupSet = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TeamUser> teamUsers = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Notification> notificationSet = ConcurrentHashMap.newKeySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @MapKey(name = "id")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Map<Long, ImportedAccount> importedAccountMap = new ConcurrentHashMap<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Administrator administrator;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_registration_emails_id")
    private UserPostRegistrationEmails userPostRegistrationEmails = new UserPostRegistrationEmails();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_information_id")
    private PersonalInformation personalInformation = new PersonalInformation();

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

    public synchronized Set<Profile> getProfileSet() {
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

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public synchronized Map<Long, ImportedAccount> getImportedAccountMap() {
        return importedAccountMap;
    }

    public void setImportedAccountMap(Map<Long, ImportedAccount> importedAccountMap) {
        this.importedAccountMap = importedAccountMap;
    }

    public UserPostRegistrationEmails getUserPostRegistrationEmails() {
        return userPostRegistrationEmails;
    }

    public void setUserPostRegistrationEmails(UserPostRegistrationEmails userPostRegistrationEmails) {
        this.userPostRegistrationEmails = userPostRegistrationEmails;
    }

    public PersonalInformation getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(PersonalInformation personalInformation) {
        this.personalInformation = personalInformation;
    }

    public boolean isAdmin() {
        return this.getAdministrator() != null;
    }

    public JSONObject getJson() throws HttpServletException {
        JSONObject res = new JSONObject();
        res.put("email", this.getEmail());
        res.put("username", this.getUsername());
        res.put("first_name", this.getPersonalInformation().getFirst_name());
        res.put("last_name", this.getPersonalInformation().getLast_name());
        res.put("phone_number", this.getPersonalInformation().getPhone_number());
        JSONArray teams = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers()) {
            Team team = teamUser.getTeam();
            if (!team.isActive())
                continue;
            JSONObject teamObject = team.getSimpleJson();
            teamObject.put("disabled", teamUser.isDisabled() || teamUser.getState() == 1);
            teamObject.put("departure_date", teamUser.getDepartureDate() == null ? JSONObject.NULL : teamUser.getDepartureDate().getTime());
            teamObject.put("state", teamUser.getState());
            teams.put(teamObject);
        }
        res.put("teams", teams);
        res.put("status", this.getUserStatus().getJson());
        res.put("connection_lifetime", this.getOptions().getConnection_lifetime());
        res.put("background_picture", this.getOptions().isBackground_picked());
        res.put("new_feature_seen", this.getUserStatus().isNewFeatureSeen());
        res.put("registration_date", this.getRegistration_date() == null ? JSONObject.NULL : this.getRegistration_date().getTime());
        return res;
    }

    public void addUserEmail(UserEmail userEmail) {
        this.getUserEmailSet().add(userEmail);
    }

    public void removeUserEmail(UserEmail userEmail) {
        this.getUserEmailSet().remove(userEmail);
    }

    public void addProfile(Profile profile) {
        this.getProfileSet().add(profile);
    }

    public void removeProfile(Profile profile) {
        this.getProfileSet().remove(profile);
    }

    public JSONArray getProfileListJson() {
        JSONArray res = new JSONArray();

        this.getProfileList().forEach(profiles1 -> {
            JSONArray tmp = new JSONArray();
            profiles1.stream().sorted(Comparator.comparingInt(Profile::getPosition_index)).forEach(profile -> tmp.put(profile.getJson()));
            res.put(tmp);
        });
        return res;
    }

    public List<List<Profile>> getProfileList() {
        List<List<Profile>> profiles = new LinkedList<>();
        for (int i = 0; i < MAX_PROFILE; i++)
            profiles.add(new LinkedList<>());
        this.getProfileSet().stream().forEach(profile -> profiles.get(profile.getColumn_index()).add(profile));
        return profiles;
    }

    public JSONArray getProfilesJson() {
        JSONArray res = new JSONArray();
        this.getProfileSet().forEach(profile -> res.put(profile.getJson()));
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
        App app = (App) hibernateQuery.get(App.class, app_id);
        if (app == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such app");
        Profile profile = app.getProfile();
        if ((app.getTeamCardReceiver() != null && !this.getTeamUsers().contains(app.getTeamCardReceiver().getTeamUser())) || (profile != null && !profile.getUser().equals(this)))
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
            Stream<Profile> column = this.getProfileSet().stream().filter(profile1 -> !profile1.equals(profile) && profile1.getColumn_index().equals(column_index));
            if (position > old_position) {
                column.filter(profile1 -> profile1.getPosition_index() >= old_position && profile1.getPosition_index() <= position).forEach(profile1 -> {
                    profile1.setPosition_index(profile1.getPosition_index() - 1);
                    hibernateQuery.saveOrUpdateObject(profile1);
                });
            } else {
                column.filter(profile1 -> profile1.getPosition_index() >= position && profile1.getPosition_index() <= old_position).forEach(profile1 -> {
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
            profile1.setPosition_index(profile1.getPosition_index() != null && profile1.getPosition_index() > 0 ? profile1.getPosition_index() - 1 : 0);
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

    public TeamUser getTeamUserOrNull(Team team) {
        return this.getTeamUsers().stream().filter(teamUser1 -> teamUser1.getTeam().equals(team)).findAny().orElse(null);
    }

    public TeamUser getTeamUserOrNull(Integer team_id) {
        return this.getTeamUsers().stream().filter(teamUser1 -> teamUser1.getTeam().getDb_id().equals(team_id)).findAny().orElse(null);
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

    public List<Cookie> getCookies() {
        List<Cookie> cookies = new ArrayList<>();
        Cookie cookie = new Cookie("email", this.getEmail());
        Cookie cookie1 = new Cookie("fname", Base64.encodeBase64String(this.getUsername().getBytes(Charset.forName("utf8"))));
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie1.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath("/");
        cookie1.setPath("/");
        cookies.add(cookie);
        cookies.add(cookie1);
        return cookies;
    }

    public Set<Team> getTeams() {
        return this.getTeamUsers().stream().map(TeamUser::getTeam).collect(Collectors.toSet());
    }

    public void removeSsoGroup(SsoGroup ssoGroup) {
        this.getSsoGroupSet().remove(ssoGroup);
    }

    public App getApp(App app) {
        return this.getApps().stream().filter(app::equals).findFirst().orElse(null);
    }

    public void addSsoGroup(SsoGroup ssoGroup) {
        this.getSsoGroupSet().add(ssoGroup);
    }

    public void removeTeamUser(TeamUser teamUser) {
        this.getTeamUsers().remove(teamUser);
    }

    public void addImportedAccount(ImportedAccount importedAccount) {
        this.getImportedAccountMap().put(importedAccount.getId(), importedAccount);
    }

    public void removeImportedAccount(ImportedAccount importedAccount) {
        this.getImportedAccountMap().remove(importedAccount.getId());
    }

    public ImportedAccount getImportedAccount(Long id) {
        return this.getImportedAccountMap().get(id);
    }

    public void trackConnection(HibernateQuery hibernateQuery) {
        Calendar calendar = Calendar.getInstance();
        ConnectionMetric connectionMetric = ConnectionMetric.getMetric(this.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_YEAR), hibernateQuery);
        connectionMetric.setConnected(true);
        hibernateQuery.saveOrUpdateObject(connectionMetric);
    }

    public boolean wasConnected(int days_connected, int days_range, HibernateQuery hibernateQuery) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days_range);
        int was_connected = 0;
        for (int i = 0; i < days_range; i++) {
            if (ConnectionMetric.getMetric(this.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_YEAR), hibernateQuery).isConnected())
                was_connected++;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return was_connected >= days_connected;
    }

    public void finalizeRegistration(String password, String access_code, String first_name, String last_name, String phone_number) throws HttpServletException {
        this.getUserKeys().setHashed_password(Hashing.hash(password));
        this.getUserKeys().setKeyUser(AES.encryptUserKey(this.getUserKeys().getDecipheredKeyUser(access_code), password, this.getUserKeys().getSaltPerso()));
        this.getUserKeys().setAccess_code_hash(null);
        this.getUserStatus().setRegistered(true);
        this.getUserStatus().setNewFeatureSeen(true);
        this.getUserStatus().setOnboardingStep(1);
        this.getPersonalInformation().setFirst_name(first_name);
        this.getPersonalInformation().setLast_name(last_name);
        this.getPersonalInformation().setPhone_number(phone_number);
    }

    /* Hack for api/v1/admin/upgrade/es-137 */
    public Profile getOrCreatePersonalProfile(HibernateQuery hibernateQuery) throws HttpServletException {
        Profile profile = this.getProfileSet().stream().filter(profile1 -> profile1.getChannel() == null).findFirst().orElse(null);
        if (profile == null) {
            int column = ThreadLocalRandom.current().nextInt(0, 4);
            int columnSize = Math.toIntExact(this.getProfileSet().stream().filter(profile1 -> profile1.getColumn_index().equals(column)).count());
            profile = new Profile(this, column, columnSize, new ProfileInformation("Me"));
            hibernateQuery.saveOrUpdateObject(profile);
            this.addProfile(profile);
            this.moveProfile(profile.getDb_id(), column, 0, hibernateQuery);
        }
        return profile;
    }
}