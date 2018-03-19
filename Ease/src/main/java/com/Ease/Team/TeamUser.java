package com.Ease.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.ProfileInformation;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCard.TeamSingleSoftwareCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.User.NotificationFactory;
import com.Ease.User.PendingNotification;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.websocketV1.WebSocketManager;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamUsers")
public class TeamUser {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "email")
    private String email;

    @Column(name = "teamKey")
    private String teamKey;

    @Column(name = "state")
    private Integer state = 0;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "teamUserRole_id")
    private TeamUserRole teamUserRole;

    @Column(name = "username")
    private String username;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation_date = new Date();

    @Column(name = "arrival_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrival_date;

    @Column(name = "departureDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureDate;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "admin_id")
    private Integer admin_id;

    @OneToOne(cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "status_id")
    private TeamUserStatus teamUserStatus;

    @Column(name = "disabled_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date disabledDate;

    @Column(name = "invitation_code")
    private String invitation_code;

    @ManyToMany(mappedBy = "teamUsers")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Channel> channels = ConcurrentHashMap.newKeySet();

    @ManyToMany(mappedBy = "pending_teamUsers")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Channel> pending_channels = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TeamCardReceiver> teamCardReceivers = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Profile> profiles;

    @OneToMany(mappedBy = "teamUser_filler")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TeamSingleCard> teamSingleCardToFillSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser_filler_test")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TeamSingleSoftwareCard> teamSingleSoftwareCardSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<JoinTeamCardRequest> joinTeamCardRequestSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PendingNotification> pendingNotificationSet = ConcurrentHashMap.newKeySet();

    public TeamUser(String email, String username, Date arrival_date, String teamKey, Team team, TeamUserRole teamUserRole) throws HttpServletException {
        this.email = email;
        this.username = username;
        this.teamKey = teamKey;
        this.team = team;
        this.teamUserRole = teamUserRole;
        this.arrival_date = arrival_date;
        this.disabled = false;
        this.teamUserStatus = new TeamUserStatus();
        this.teamUserStatus.setReminder_three_days_sended(false);
    }

    public TeamUser() throws HttpServletException {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeamKey() {
        return this.teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date arrivalDate) {
        this.creation_date = arrivalDate;
    }

    public Date getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(Date arrival_date) {
        this.arrival_date = arrival_date;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public TeamUserRole getTeamUserRole() {
        return this.teamUserRole;
    }

    public void setTeamUserRole(TeamUserRole teamUserRole) {
        this.teamUserRole = teamUserRole;
    }

    public Boolean isActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public boolean isDisabled() {
        return disabled || (this.getDepartureDate() != null && this.getDepartureDate().getTime() <= new Date().getTime());
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(Integer admin_id) {
        this.admin_id = admin_id;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }

    public TeamUserStatus getTeamUserStatus() {
        return teamUserStatus;
    }

    public void setTeamUserStatus(TeamUserStatus teamUserStatus) {
        this.teamUserStatus = teamUserStatus;
    }

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public synchronized Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public synchronized Set<Channel> getPending_channels() {
        return pending_channels;
    }

    public void setPending_channels(Set<Channel> pending_channels) {
        this.pending_channels = pending_channels;
    }

    public synchronized Set<TeamCardReceiver> getTeamCardReceivers() {
        return teamCardReceivers;
    }

    public void setTeamCardReceivers(Set<TeamCardReceiver> teamCardReceivers) {
        this.teamCardReceivers = teamCardReceivers;
    }

    public synchronized Set<TeamSingleCard> getTeamSingleCardToFillSet() {
        return teamSingleCardToFillSet;
    }

    public void setTeamSingleCardToFillSet(Set<TeamSingleCard> teamSingleCardToFillSet) {
        this.teamSingleCardToFillSet = teamSingleCardToFillSet;
    }

    public Set<TeamSingleSoftwareCard> getTeamSingleSoftwareCardSet() {
        return teamSingleSoftwareCardSet;
    }

    public void setTeamSingleSoftwareCardSet(Set<TeamSingleSoftwareCard> teamSingleSoftwareCardSet) {
        this.teamSingleSoftwareCardSet = teamSingleSoftwareCardSet;
    }

    public Set<JoinTeamCardRequest> getJoinTeamCardRequestSet() {
        return joinTeamCardRequestSet;
    }

    public void setJoinTeamCardRequestSet(Set<JoinTeamCardRequest> joinTeamCardRequestSet) {
        this.joinTeamCardRequestSet = joinTeamCardRequestSet;
    }

    public Set<PendingNotification> getPendingNotificationSet() {
        return pendingNotificationSet;
    }

    public void setPendingNotificationSet(Set<PendingNotification> pendingNotificationSet) {
        this.pendingNotificationSet = pendingNotificationSet;
    }

    public void addChannel(Channel channel) {
        this.getChannels().add(channel);
    }

    public void removeChannel(Channel channel) {
        this.getChannels().remove(channel);
    }

    public void addPending_channel(Channel channel) {
        this.getPending_channels().add(channel);
    }

    public void removePending_channel(Channel channel) {
        this.getPending_channels().remove(channel);
    }

    public void addProfile(Profile profile) {
        this.getProfiles().add(profile);
    }

    public void removeProfile(Profile profile) {
        this.getProfiles().remove(profile);
    }

    public static TeamUser createOwner(String email, String username, Date arrivalDate, String teamKey, Team team) throws HttpServletException {
        TeamUserRole teamUserRole = new TeamUserRole(TeamUserRole.Role.OWNER.getValue());
        TeamUser owner = new TeamUser(email, username, arrivalDate, teamKey, team, teamUserRole);
        owner.setState(2);
        return owner;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("email", this.getEmail());
        res.put("username", this.username);
        res.put("disabled", this.disabled);
        res.put("role", this.getTeamUserRole().getRoleValue());
        res.put("invitation_sent", this.getTeamUserStatus().isInvitation_sent());
        res.put("creation_date", this.getCreation_date().getTime());
        res.put("arrival_date", this.getArrival_date() == null ? JSONObject.NULL : this.getArrival_date().getTime());
        res.put("first_name", this.getUser() == null ? "" : this.getUser().getPersonalInformation().getFirst_name());
        res.put("last_name", this.getUser() == null ? "" : this.getUser().getPersonalInformation().getLast_name());
        res.put("departure_date", this.getDepartureDate() == null ? JSONObject.NULL : this.getDepartureDate().getTime());
        res.put("team_id", this.getTeam().getDb_id());
        res.put("state", this.getState());
        JSONArray channel_ids = new JSONArray();
        for (Channel channel : this.getChannels())
            channel_ids.put(channel.getDb_id());
        res.put("room_ids", channel_ids);
        JSONArray teamCards = new JSONArray();
        this.getTeamCardReceivers().stream().map(TeamCardReceiver::getTeamCard).sorted((t1, t2) -> Long.compare(t2.getCreation_date().getTime(), t1.getCreation_date().getTime())).distinct().forEach(teamCard -> teamCards.put(teamCard.getDb_id()));
        res.put("team_card_ids", teamCards);
        res.put("admin_id", this.getAdmin_id() == null ? JSONObject.NULL : this.getAdmin_id());
        return res;
    }

    public JSONObject getWebSocketJson() {
        JSONObject res = new JSONObject();
        res.put("team_user", this.getJson());
        return res;
    }

    public void edit(JSONObject editJson) {
        String username = (String) editJson.get("username");
        if (username != null)
            this.username = username;
    }

    public boolean isTeamAdmin() {
        return this.getTeamUserRole().isAdmin();
    }

    public boolean isTeamOwner() {
        return this.getTeamUserRole().isOwner();
    }

    public void validateRegistration(String deciphered_teamKey, String userPublicKey, DataBaseConnection db) throws HttpServletException {
        if (this.isVerified())
            throw new HttpServletException(HttpStatus.BadRequest, "TeamUser already registered");
        DatabaseRequest request = db.prepareRequest("UDPATE teamUsers SET teamKey = ? WHERE id = ?;");
        this.teamKey = RSA.Encrypt(deciphered_teamKey, userPublicKey);
        request.setString(this.teamKey);
        request.setInt(this.db_id);
        request.set();
    }

    public void finalizeRegistration(String userKey, String userPrivateKey, HibernateQuery hibernateQuery) throws HttpServletException {
        String teamKey = RSA.Decrypt(this.getTeamKey(), userPrivateKey);
        this.setTeamKey(AES.encrypt(teamKey, userKey));
        this.setState(2);
        hibernateQuery.saveOrUpdateObject(this);
    }

    public void lastRegistrationStep(WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) throws HttpServletException {
        NotificationFactory.getInstance().createTeamUserRegisteredNotification(this, this.getTeam().getTeamUserWithId(this.getAdmin_id()), userWebSocketManager, hibernateQuery);
        if (this.getTeamCardReceivers().isEmpty())
            return;
        //Profile profile = this.getOrCreateProfile(hibernateQuery);
        for (TeamCardReceiver teamCardReceiver : this.getTeamCardReceivers()) {
            Channel channel = teamCardReceiver.getTeamCard().getChannel();
            Profile profile = this.getOrCreateProfile(channel, hibernateQuery);
            App app = teamCardReceiver.getApp();
            app.setProfile(profile);
            app.setPosition(profile.getSize());
            hibernateQuery.saveOrUpdateObject(app);
            profile.addApp(app);
        }
    }

    public String getDecipheredTeamKey(String userKey) throws HttpServletException {
        if (this.isVerified() && !this.isDisabled())
            return AES.decrypt(this.getTeamKey(), userKey);
        throw new HttpServletException(HttpStatus.Forbidden, "You are not allowed to decipher the team key");
    }

    public boolean isVerified() {
        return this.state == 2;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void editUsername(String username) {
        if (username.equals(this.getUsername()))
            return;
        this.username = username;
    }

    public boolean isRegistered() {
        return this.getUser() != null && this.getUser().getUserStatus().isRegistered();
    }

    public boolean isSuperior(TeamUser teamUserToModify) {
        return this.getTeamUserRole().isSuperior(teamUserToModify.getTeamUserRole());
    }

    public void transferOwnershipTo(TeamUser new_teamUser_owner) throws HttpServletException {
        new_teamUser_owner.getTeamUserRole().setRole(TeamUserRole.Role.OWNER);
        if (!this.getTeam().isValidFreemium())
            this.getTeamUserRole().setRole(TeamUserRole.Role.MEMBER);
        else
            this.getTeamUserRole().setRole(TeamUserRole.Role.ADMINISTRATOR);
    }

    public JSONObject getOrigin() {
        JSONObject res = new JSONObject();
        res.put("team_id", this.getTeam().getDb_id());
        return res;
    }

    public void cipheringStep(String userKey, String userPrivateKey, HibernateQuery hibernateQuery) throws HttpServletException {
        if (this.getState() == 1) {
            this.finalizeRegistration(userKey, userPrivateKey, hibernateQuery);
            hibernateQuery.saveOrUpdateObject(this);
        } else if (this.getState() == 2 && this.isDisabled()) {
            String deciphered_teamKey = RSA.Decrypt(this.getTeamKey(), userPrivateKey);
            this.setTeamKey(AES.encrypt(deciphered_teamKey, userKey));
            this.setDisabled(false);
            hibernateQuery.saveOrUpdateObject(this);
        }
    }

    /**
     * if you have a team profile, it returns it
     * if you don't and you didn't already had one, create it
     * if you don't have any profile, create it
     * if you don't and you already had one, returns any profile of you team space
     *
     * @param hibernateQuery
     * @return Profile profile
     * @throws HttpServletException
     */
    /* public Profile getOrCreateProfile(HibernateQuery hibernateQuery) throws HttpServletException {
        if (this.getUser() == null || !this.getUser().getUserStatus().isRegistered())
            throw new HttpServletException(HttpStatus.InternError);
        Profile profile;
        if (this.getProfile() == null) {
            int column_size = Math.toIntExact(this.getUser().getProfileSet().stream().filter(profile1 -> profile1.getColumn_index().equals(2)).count());
            profile = new Profile(this.getUser(), 2, column_size, new ProfileInformation(this.getTeam().getName()));
            hibernateQuery.saveOrUpdateObject(profile);
            this.getUser().addProfile(profile);
            this.getUser().moveProfile(profile.getDb_id(), 2, 0, hibernateQuery);
            this.setProfile(profile);
            this.getTeamUserStatus().setProfile_created(true);
            hibernateQuery.saveOrUpdateObject(this);
        }
        return this.getProfile();
    } */

    /**
     * if you have a team profile, it returns it
     * if you don't and you didn't already had one, create it
     * if you don't have any profile, create it
     * if you don't and you already had one, returns any profile of you team space
     *
     * @param channel
     * @param hibernateQuery
     * @return Profile profile
     * @throws HttpServletException
     */
    public Profile getOrCreateProfile(Channel channel, HibernateQuery hibernateQuery) throws HttpServletException {
        if (this.getUser() == null || !this.getUser().getUserStatus().isRegistered())
            throw new HttpServletException(HttpStatus.InternError);
        Profile profile = this.getProfiles().stream().filter(profile1 -> channel.equals(profile1.getChannel())).findFirst().orElse(null);
        if (profile == null) {
            int column = ThreadLocalRandom.current().nextInt(0, 4);
            int column_size = Math.toIntExact(this.getUser().getProfileSet().stream().filter(profile1 -> profile1.getColumn_index().equals(column)).count());
            profile = new Profile(this.getUser(), column, column_size, new ProfileInformation(channel.getName()));
            hibernateQuery.saveOrUpdateObject(profile);
            profile.setTeamUser(this);
            profile.setChannel(channel);
            hibernateQuery.saveOrUpdateObject(profile);
            this.getUser().addProfile(profile);
            this.getUser().moveProfile(profile.getDb_id(), column, 0, hibernateQuery);
            this.getTeamUserStatus().setProfile_created(true);
            hibernateQuery.saveOrUpdateObject(this);
        }
        return profile;
    }

    public Profile createTeamProfile(Channel channel, HibernateQuery hibernateQuery) throws HttpServletException {
        return this.getOrCreateProfile(channel, hibernateQuery);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamUser teamUser = (TeamUser) o;

        return db_id.equals(teamUser.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public void removeTeamCardReceiver(TeamCardReceiver teamCardReceiver) {
        this.getTeamCardReceivers().remove(teamCardReceiver);
    }

    public void addTeamCardReceiver(TeamCardReceiver teamCardReceiver) {
        this.getTeamCardReceivers().add(teamCardReceiver);
    }

    public void removeTeamSingleCardToFill(TeamSingleCard teamSingleCard) {
        this.getTeamSingleCardToFillSet().remove(teamSingleCard);
    }

    public void removeTeamSingleSoftwareCardToFill(TeamSingleSoftwareCard teamSingleSoftwareCard) {
        this.getTeamSingleSoftwareCardSet().remove(teamSingleSoftwareCard);
    }

    public void addTeamSingleCardToFill(TeamSingleCard teamSingleCard) {
        this.getTeamSingleCardToFillSet().add(teamSingleCard);
    }

    public void addTeamSingleSoftwareCardToFill(TeamSingleSoftwareCard teamSingleSoftwareCard) {
        this.getTeamSingleSoftwareCardSet().add(teamSingleSoftwareCard);
    }

    public TeamUser getAdmin() throws HttpServletException {
        if (this.getAdmin_id() == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This user does not have admin");
        return this.getTeam().getTeamUserWithId(this.getAdmin_id());
    }
}
