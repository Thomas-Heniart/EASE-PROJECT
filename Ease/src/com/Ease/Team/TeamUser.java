package com.Ease.Team;

import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.NewDashboard.User.User;
import com.Ease.Notification.Notification;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.websocketV1.WebSocketMessageFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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

    /* @ManyToOne
    @JoinColumn(name = "user_id") */
    @Transient
    protected User user;

    /* To remove when we we migrate on hibernate */
    @Transient
    protected com.Ease.Dashboard.User.User dashboard_user;

    /* To remove when we we migrate on hibernate */
    @Column(name = "user_id")
    protected String user_id;

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

    @Transient
    protected String deciphered_teamKey;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    protected Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamUserRole_id")
    protected TeamUserRole teamUserRole;

    @Column(name = "username")
    protected String username;

    @Column(name = "arrivalDate")
    protected Date arrivalDate;

    @Column(name = "departureDate")
    protected Date departureDate;

    @Column(name = "jobTitle")
    protected String jobTitle;

    @Transient
    protected DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "admin_id")
    protected Integer admin_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    protected TeamUserStatus teamUserStatus;

    @Column(name = "phone_number")
    protected String phone_number;

    @Column(name = "disabled_date")
    private Date disabledDate;

    public TeamUser(String firstName, String lastName, String email, String username, Date arrivalDate, String teamKey, Team team, TeamUserRole teamUserRole) {
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

    public TeamUser() {
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

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public com.Ease.Dashboard.User.User getDashboard_user() {
        return dashboard_user;
    }

    public void setDashboard_user(com.Ease.Dashboard.User.User dashboard_user) {
        this.dashboard_user = dashboard_user;
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

    public String getDeciphered_teamKey() throws GeneralException {
        if (this.deciphered_teamKey == null)
            this.decipher_teamKey();
        return deciphered_teamKey;
    }

    public void setDeciphered_teamKey(String deciphered_teamKey) {
        this.deciphered_teamKey = deciphered_teamKey;
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

    public boolean isDisabled() {
        return disabled;
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

    public static TeamUser createOwner(String firstName, String lastName, String email, String username, Date arrivalDate, String teamKey, Team team) throws GeneralException {
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
        res.put("arrival_date", this.dateFormat.format(arrivalDate));
        res.put("departure_date", "");
        if (departureDate != null)
            res.put("departure_date", this.dateFormat.format(this.departureDate));
        res.put("state", this.state);
        res.put("phone_number", this.getPhone_number());
        JSONArray channel_ids = new JSONArray();
        for (Channel channel : this.getTeam().getChannelsForTeamUser(this))
            channel_ids.add(channel.getDb_id());
        res.put("channel_ids", channel_ids);
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
        try {
            if (this.isVerified())
                throw new HttpServletException(HttpStatus.BadRequest, "TeamUser already registered");
            DatabaseRequest request = db.prepareRequest("UDPATE teamUsers SET teamKey = ? WHERE id = ?;");
            this.teamKey = RSA.Encrypt(deciphered_teamKey, userPublicKey);
            request.setString(this.teamKey);
            request.setInt(this.db_id);
            request.set();
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void finalizeRegistration() throws HttpServletException {
        try {
            this.deciphered_teamKey = RSA.Decrypt(this.teamKey, this.getDashboard_user().getKeys().getPrivateKey());
            this.teamKey = this.getDashboard_user().encrypt(this.deciphered_teamKey);
            this.state = 2;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void decipher_teamKey() throws GeneralException {
        this.deciphered_teamKey = this.getDashboard_user().decrypt(this.teamKey);
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
        return (this.getUser_id() != null && !this.getUser_id().equals(""));
    }

    public boolean isSuperior(TeamUser teamUserToModify) {
        return this.getTeamUserRole().isSuperior(teamUserToModify.getTeamUserRole());
    }

    public void transferOwnershipTo(TeamUser new_teamUser_owner) throws HttpServletException {
        new_teamUser_owner.getTeamUserRole().setRole(TeamUserRole.Role.OWNER);
        this.getTeamUserRole().setRole(TeamUserRole.Role.ADMINISTRATOR);
    }

    public void delete(DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            Team team = this.getTeam();
            List<ShareableApp> shareableAppsToRemove = new LinkedList<>();
            for (ShareableApp shareableApp : team.getAppManager().getShareableApps()) {
                if (shareableApp.getTeamUser_owner() == this)
                    shareableAppsToRemove.add(shareableApp);
            }
            team.getAppManager().removeShareableApps(shareableAppsToRemove, db);
            team.getAppManager().removeSharedAppsForTeamUser(this, db);
            for (Channel channel : this.getTeam().getChannels())
                channel.removeTeamUser(this, db);
            DatabaseRequest request = db.prepareRequest("DELETE FROM pendingTeamInvitations WHERE teamUser_id = ?;");
            request.setInt(this.getDb_id());
            request.set();
            request = db.prepareRequest("DELETE FROM pendingTeamUserVerifications WHERE teamUser_id = ?;");
            request.setInt(this.getDb_id());
            request.set();
            request = db.prepareRequest("DELETE FROM pendingJoinChannelRequests WHERE teamUser_id = ?;");
            request.setInt(this.getDb_id());
            request.set();
            request = db.prepareRequest("DELETE FROM pendingJoinAppRequests WHERE team_user_id = ?;");
            request.setInt(this.getDb_id());
            request.set();
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.db_id);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TeamUser))
            return false;
        TeamUser teamUser = (TeamUser) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.db_id, teamUser.db_id);
        return eb.isEquals();
    }

    public List<SharedApp> getSharedApps() {
        return this.getTeam().getAppManager().getSharedAppsForTeamUser(this);
    }

    public void disconnect() {
        this.dashboard_user = null;
        this.deciphered_teamKey = null;
    }

    public JSONObject getOrigin() {
        JSONObject res = new JSONObject();
        res.put("team_id", this.getTeam().getDb_id());
        return res;
    }

    public void addNotification(String content, String url, String icon, Date timestamp, DataBaseConnection db) throws HttpServletException {
        if (this.dashboard_user == null)
            Notification.createNotification(content, url, icon, this.getUser_id(), timestamp, db);
        else {
            Notification notification = this.dashboard_user.getNotificationManager().addNotification(content, url, icon, timestamp, db);
            this.dashboard_user.getWebSocketManager().sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        }

    }
}
