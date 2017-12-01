package com.Ease.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.ProfileInformation;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.TeamSingleCard;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Table(name = "teamUsers")
public class TeamUser {

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "firstName")
    protected String firstName;

    @Column(name = "lastName")
    protected String lastName;

    @Column(name = "email")
    protected String email;

    @Column(name = "teamKey")
    private String teamKey;

    @Column(name = "state")
    protected Integer state = 0;

    @Column(name = "active")
    protected boolean active;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamUserRole_id")
    private TeamUserRole teamUserRole;

    @Column(name = "username")
    private String username;

    @Column(name = "arrivalDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalDate;

    @Column(name = "departureDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureDate;

    @Column(name = "jobTitle")
    private String jobTitle;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "admin_id")
    private Integer admin_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private TeamUserStatus teamUserStatus;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "disabled_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date disabledDate;

    @Column(name = "invitation_code")
    private String invitation_code;

    @ManyToMany(mappedBy = "teamUsers")
    private Set<Channel> channels = ConcurrentHashMap.newKeySet();

    @ManyToMany(mappedBy = "pending_teamUsers")
    private Set<Channel> pending_channels = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamCardReceiver> teamCardReceivers = ConcurrentHashMap.newKeySet();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "teamUser_filler")
    private Set<TeamSingleCard> teamSingleCardToFillSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JoinTeamCardRequest> joinTeamCardRequestSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "teamUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PendingNotification> pendingNotificationSet = ConcurrentHashMap.newKeySet();

    public TeamUser(String firstName, String lastName, String email, String username, Date arrivalDate, String teamKey, Team team, TeamUserRole teamUserRole) throws HttpServletException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.teamKey = teamKey;
        this.team = team;
        this.teamUserRole = teamUserRole;
        this.arrivalDate = arrivalDate;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public static TeamUser createOwner(String firstName, String lastName, String email, String username, Date arrivalDate, String teamKey, Team team) throws HttpServletException {
        TeamUserRole teamUserRole = new TeamUserRole(TeamUserRole.Role.OWNER.getValue());
        TeamUser owner = new TeamUser(firstName, lastName, email, username, arrivalDate, teamKey, team, teamUserRole);
        owner.setState(2);
        return owner;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.db_id);
        res.put("first_name", this.firstName);
        res.put("last_name", this.lastName);
        res.put("email", this.email);
        res.put("username", this.username);
        res.put("disabled", this.disabled);
        res.put("role", this.teamUserRole.getRoleValue());
        res.put("invitation_sent", this.getTeamUserStatus().isInvitation_sent());
        res.put("arrival_date", arrivalDate.getTime());
        res.put("departure_date", (departureDate == null) ? null : departureDate.getTime());
        res.put("state", this.state);
        res.put("phone_number", this.getPhone_number());
        JSONArray channel_ids = new JSONArray();
        for (Channel channel : this.getChannels())
            channel_ids.add(channel.getDb_id());
        res.put("room_ids", channel_ids);
        JSONArray teamCards = new JSONArray();
        this.getTeamCardReceivers().stream().map(TeamCardReceiver::getTeamCard).sorted((t1, t2) -> Long.compare(t2.getCreation_date().getTime(), t1.getCreation_date().getTime())).distinct().forEach(teamCard -> teamCards.add(teamCard.getDb_id()));
        res.put("team_card_ids", teamCards);
        return res;
    }

    public void edit(JSONObject editJson) {
        String firstName = (String) editJson.get("firstName");
        String lastName = (String) editJson.get("lastName");
        String username = (String) editJson.get("username");
        if (lastName != null)
            this.lastName = lastName;
        if (firstName != null)
            this.firstName = firstName;
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

    public void lastRegistrationStep(String keyUser, String teamKey, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) throws HttpServletException {
        this.setTeamKey(AES.encrypt(teamKey, keyUser));
        this.setState(2);
        hibernateQuery.saveOrUpdateObject(this);
        if (this.getTeamCardReceivers().isEmpty())
            return;
        Profile profile = this.getOrCreateProfile(hibernateQuery);
        this.getTeamCardReceivers().stream().map(TeamCardReceiver::getApp).forEach(app -> {
            app.setProfile(profile);
            app.setPosition(profile.getSize());
            hibernateQuery.saveOrUpdateObject(app);
            profile.addApp(app);
        });
        NotificationFactory.getInstance().createTeamUserRegisteredNotification(this, this.getTeam().getTeamUserWithId(this.getAdmin_id()), userWebSocketManager, hibernateQuery);
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

    public void editFirstName(String firstName) {
        if (firstName.equals(this.getFirstName()))
            return;
        this.firstName = firstName;
    }

    public void editLastName(String lastName) {
        if (lastName.equals(this.getLastName()))
            return;
        this.lastName = lastName;
    }

    public void editUsername(String username) {
        if (username.equals(this.getUsername()))
            return;
        this.username = username;
    }

    public boolean isRegistered() {
        return this.getUser() != null;
    }

    public boolean isSuperior(TeamUser teamUserToModify) {
        return this.getTeamUserRole().isSuperior(teamUserToModify.getTeamUserRole());
    }

    public void transferOwnershipTo(TeamUser new_teamUser_owner) throws HttpServletException {
        new_teamUser_owner.getTeamUserRole().setRole(TeamUserRole.Role.OWNER);
        if (!team.isValidFreemium())
            this.getTeamUserRole().setRole(TeamUserRole.Role.MEMBER);
        else
            this.getTeamUserRole().setRole(TeamUserRole.Role.ADMINISTRATOR);
    }

    public void delete(DataBaseConnection db) throws HttpServletException {
        for (Channel channel : this.getChannels())
            channel.removeTeamUser(this);
        for (Channel channel : this.getPending_channels())
            channel.removePendingTeamUser(this);
        if (this.getUser() != null)
            this.getUser().getTeamUsers().remove(this);
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
    public synchronized Profile getOrCreateProfile(HibernateQuery hibernateQuery) throws HttpServletException {
        if (this.getUser() == null)
            throw new HttpServletException(HttpStatus.InternError);
        Profile profile = null;
        if (!this.getTeamUserStatus().isProfile_created()) {
            int column_size = Math.toIntExact(this.getUser().getProfileSet().stream().filter(profile1 -> profile1.getColumn_index().equals(2)).count());
            profile = new Profile(this.getUser(), 2, column_size, new ProfileInformation(this.getTeam().getName()));
            hibernateQuery.saveOrUpdateObject(profile);
            this.getUser().addProfile(profile);
            this.getUser().moveProfile(profile.getDb_id(), 2, 0, hibernateQuery);
            this.setProfile(profile);
            this.getTeamUserStatus().setProfile_created(true);
            hibernateQuery.saveOrUpdateObject(this);
            return this.getProfile();
        } else {
            if (this.getUser().getProfileSet().isEmpty()) {
                profile = new Profile(this.getUser(), 2, 0, new ProfileInformation(this.getTeam().getName()));
                hibernateQuery.saveOrUpdateObject(profile);
                this.getUser().addProfile(profile);
                this.setProfile(profile);
                hibernateQuery.saveOrUpdateObject(this);
                return this.getProfile();
            } else if (this.getProfile() == null) {
                return this.getUser().getProfileSet().stream().findAny().get();
            } else
                return this.getProfile();
        }
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
}
