package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import javax.servlet.Servlet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Column(name = "verified")
    protected boolean verified;

    @Transient
    protected String deciphered_teamKey;

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

    @Column(name = "jobTitle")
    protected String jobTitle;

    @Transient
    protected DateFormat dateFormat = new SimpleDateFormat("MMMM dd, HH:mm", Locale.US);

    @ManyToMany
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = {@JoinColumn(name = "team_user_id")}, inverseJoinColumns = {@JoinColumn(name = "channel_id")})
    protected List<Channel> channels = new LinkedList<>();


    /**
     * TODO Use hibernate for apps then update code about sharedApps
     */
    @Transient
    protected List<SharedApp> sharedApps = new LinkedList<>();

    @Transient
    Map<Integer, SharedApp> sharedAppMap = new HashMap<>();

    @Transient
    private List<ShareableApp> shareableApps = new LinkedList<>();

    public TeamUser(User user, String firstName, String lastName, String email, String username, String teamKey, Boolean verified, Date departureDate, Team team, TeamUserPermissions teamUserPermissions, List<Channel> channels) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.teamKey = teamKey;
        this.verified = verified;
        this.departureDate = departureDate;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
        this.channels = channels;
        this.arrivalDate = new Date();
    }

    public TeamUser(User user, String firstName, String lastName, String email, String username, String teamKey, Boolean verified, Team team, TeamUserPermissions teamUserPermissions, List<Channel> channels) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.teamKey = teamKey;
        this.verified = verified;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
        this.channels = channels;
        this.arrivalDate = new Date();
    }

    public TeamUser(User user, String firstName, String lastName, String email, String username, String teamKey, Boolean verified, Team team, TeamUserPermissions teamUserPermissions) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.teamKey = teamKey;
        this.verified = verified;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
        this.arrivalDate = new Date();
    }

    public TeamUser(String firstName, String lastName, String email, String username, String teamKey, Boolean verified, Team team, TeamUserPermissions teamUserPermissions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.teamKey = teamKey;
        this.verified = verified;
        this.team = team;
        this.teamUserPermissions = teamUserPermissions;
        this.arrivalDate = new Date();
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
        this.user_id = dashboard_user.getDBid();
    }

    public void setDashboard_user(com.Ease.Dashboard.User.User user, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE teamUsers SET user_id = ? WHERE id = ?");
        request.setInt(user.getDBid());
        request.setInt(this.db_id);
        request.set();
        this.setDashboard_user(user);
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

    /*public void setTeamKey(String teamPrivateKey) {
        System.out.println("old teamPrivateKey: " + this.teamPrivateKey);
        System.out.println("new teamPrivateKey: " + teamPrivateKey);
        this.teamPrivateKey = teamPrivateKey;
    }*/

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

    public static TeamUser createAdminUser(String firstName, String lastName, String email, String username, String teamKey, Team team) throws GeneralException {
        TeamUserPermissions permissions = new TeamUserPermissions(TeamUserPermissions.Role.ADMINISTRATOR.getValue());
        return new TeamUser(firstName, lastName, email, username, teamKey, true, team, permissions);
    }

    public List<SharedApp> getSharedApps() {
        return sharedApps;
    }

    public JSONObject getJson() throws GeneralException {
        JSONObject res = new JSONObject();
        res.put("id", this.db_id);
        res.put("firstName", this.firstName);
        res.put("lastName", this.lastName);
        res.put("email", this.email);
        res.put("username", this.username);
        res.put("role", this.teamUserPermissions.getPermissions());
        res.put("arrivalDate", this.arrivalDate.toString());
        res.put("departureDate", "null");
        if (departureDate != null)
            res.put("departureDate", this.departureDate);
        JSONArray sharedApps = new JSONArray();
        for (SharedApp sharedApp : this.sharedApps) {
            sharedApps.add(sharedApp.getSharedJSON());
        }
        res.put("sharedApps", sharedApps);
        JSONArray shareableApps = new JSONArray();
        for (ShareableApp shareableApp : this.shareableApps)
            shareableApps.add(shareableApp.getShareableJson());
        res.put("shareableApps", shareableApps);
        res.put("verified", this.verified);
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

    public void addSharedApp(SharedApp app) {
        this.sharedApps.add(app);
        this.sharedAppMap.put(((App) app).getSingleId(), app);
    }

    public void addShareableApp(ShareableApp app) {
        this.shareableApps.add(app);
    }

    public void validateRegistration(String deciphered_teamKey, String userPublicKey, DataBaseConnection db) throws GeneralException {
        if (this.isVerified())
            throw new GeneralException(ServletManager.Code.ClientError, "TeamUser already registered");
        DatabaseRequest request = db.prepareRequest("UDPATE teamUsers SET teamKey = ? WHERE id = ?;");
        this.teamKey = RSA.Encrypt(deciphered_teamKey, userPublicKey);
        request.setString(this.teamKey);
        request.setInt(this.db_id);
        request.set();
    }

    public void finalizeRegistration(ServletManager sm) throws GeneralException {
        if (this.isVerified() && !this.isVerified())
            throw new GeneralException(ServletManager.Code.ClientError, "You shouldn't be there");
        this.deciphered_teamKey = RSA.Decrypt(this.teamKey, this.getDashboard_user().getKeys().getPrivateKey());
        this.teamKey = this.getDashboard_user().encrypt(this.deciphered_teamKey);
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE teamUsers SET teamKey = ?, verified = 1 WHERE id = ?;");
        request.setString(this.teamKey);
        request.setInt(this.db_id);
        request.set();
    }

    public void decipher_teamKey() throws GeneralException {
        this.deciphered_teamKey = this.getDashboard_user().decrypt(this.teamKey);
    }

    public boolean isVerified() {
        return this.verified;
    }

    public SharedApp getSharedAppWithId(Integer app_id) throws GeneralException {
        SharedApp sharedApp = this.sharedAppMap.get(app_id);
        if (sharedApp == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This app does not exist.");
        return sharedApp;
    }

    public JSONObject getSimpleJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getDb_id());
        jsonObject.put("username", this.getUsername());
        jsonObject.put("email", this.getEmail());
        jsonObject.put("first_name", this.getFirstName());
        jsonObject.put("last_name", this.getLastName());
        jsonObject.put("arrival_date", this.dateFormat.format(this.getArrivalDate()));
        jsonObject.put("departure_date", "Undefined");
        if (this.getDepartureDate() != null)
            jsonObject.put("departure_date", this.dateFormat.format(this.getDepartureDate()));
        jsonObject.put("role", this.getTeamUserPermissions().getPermissions());
        return jsonObject;
    }

    public void editFirstName(String firstName) {
        if (firstName.equals(this.getFirstName()))
            return;
        this.firstName = firstName;
    }

    public void editLastName(String lastName) {
        if (firstName.equals(this.getLastName()))
            return;
        this.lastName = lastName;
    }

    public void editUsername(String username) {
        if (firstName.equals(this.getUsername()))
            return;
        this.username = username;
    }
}
