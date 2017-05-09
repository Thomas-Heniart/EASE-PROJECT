package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.User.User;
import com.Ease.Utils.*;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.*;

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
    protected User user;

    @Transient
    protected com.Ease.Dashboard.User.User dashboard_user;

    @Column(name = "firstName")
    protected String firstName;

    @Column(name = "lastName")
    protected String lastName;

    @Column(name = "email")
    protected String email;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    protected Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permissions_id")
    protected TeamUserPermissions teamUserPermissions;

    @Column(name = "username")
    protected String username;

    @Column(name = "arrivalDate")
    protected Date arrivalDate;

    @Column(name = "departureDate")
    protected Date departureDate;

    @ManyToMany
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = { @JoinColumn(name = "team_user_id") }, inverseJoinColumns = { @JoinColumn(name = "channel_id") })
    protected List<Channel> channels = new LinkedList<>();


    /**
     * TODO Use hibernate for apps then update code avout sharedApps
     */
    @Transient
    protected List<SharedApp> sharedApps = new LinkedList<>();

    @Transient
    private List<ShareableApp> shareableApps = new LinkedList<>();

    public TeamUser(User user, String firstName, String lastName, String email, String username, Date departureDate, Team team, TeamUserPermissions teamUserPermissions, List<Channel> channels) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.departureDate = departureDate;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
        this.channels = channels;
    }

    public TeamUser(User user, String firstName, String lastName, String email, String username, Team team, TeamUserPermissions teamUserPermissions, List<Channel> channels) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
        this.channels = channels;
    }

    public TeamUser(User user, String firstName, String lastName, String email, String username, Team team, TeamUserPermissions teamUserPermissions) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
    }

    public TeamUser(String firstName, String lastName, String email, String username, Team team, TeamUserPermissions teamUserPermissions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public TeamUserPermissions getTeamUserPermissions() {
        return teamUserPermissions;
    }

    public void setTeamUserPermissions(TeamUserPermissions teamUserPermissions) {
        this.teamUserPermissions = teamUserPermissions;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public static TeamUser createAdminUser(String firstName, String lastName, String email, String username, Team team) {
        TeamUserPermissions permissions = new TeamUserPermissions(TeamUserPermissions.Role.ADMINISTRATOR.getValue());
        return new TeamUser(firstName, lastName, email, username, team, permissions);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.db_id);
        res.put("firstName", this.firstName);
        res.put("lastName", this.lastName);
        res.put("email", this.email);
        res.put("username", this.username);
        res.put("role", this.teamUserPermissions.getRole());
        res.put("arrivalDate", this.arrivalDate.toString());
        if (departureDate != null)
            res.put("departureDate", this.departureDate);
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
        return this.getTeamUserPermissions().hasAdminPermissions();
    }

    public void loadSharedApps(ServletManager sm) throws GeneralException {
        this.sharedApps = App.loadSharedApps(this.db_id, sm);
    }

    public void loadShareableApps(ServletManager sm) throws GeneralException {
        this.shareableApps = App.loadShareableApps(this.db_id, sm);
    }
}
