package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Notification.Notification;
import com.Ease.Notification.TeamNotification;
import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import javax.servlet.ServletContext;
import java.util.*;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Table(name = "teams")
public class Team {

    public static List<Team> loadTeams(ServletContext context, DataBaseConnection db) throws HttpServletException {
        HibernateQuery query = new HibernateQuery();
        query.queryString("SELECT t FROM Team t");
        List<Team> teams = query.list();
        for (Team team : teams) {
            team.lazyInitialize(db);
            team.setShareableApps(App.loadShareableAppsForTeam(team, context, db));
            for (ShareableApp shareableApp : team.getShareableApps()) {
                shareableApp.setSharedApps(App.loadSharedAppsForShareableApp(shareableApp, context, db));
                /* if (shareableApp.getChannel() != null) {
                    shareableApp.getChannel().setSharedApps(shareableApp.getSharedApps());
                } */
            }
        }

        query.commit();
        return teams;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    protected String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    protected List<TeamUser> teamUsers = new LinkedList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    protected List<Channel> channels = new LinkedList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    protected Set<TeamNotification> teamNotifications = new HashSet<>();

    @Transient
    protected Map<Integer, Channel> channelIdMap = new HashMap<>();

    @Transient
    protected Map<Integer, TeamUser> teamUserIdMap = new HashMap<>();

    @Transient
    protected List<TeamUser> teamUsersWaitingForVerification = new LinkedList<>();

    @Transient
    protected List<ShareableApp> shareableApps = new LinkedList<>();

    @Transient
    protected Map<Integer, ShareableApp> shareableAppMap = new HashMap<>();

    public Team(String name, List<TeamUser> teamUsers, List<Channel> channels) {
        this.name = name;
        this.teamUsers = teamUsers;
        this.channels = channels;
    }

    public Team(String name) {
        this.name = name;
    }

    public Team() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public Set<TeamNotification> getTeamNotifications() {
        return teamNotifications;
    }

    public void setTeamNotifications(Set<TeamNotification> teamNotifications) {
        this.teamNotifications = teamNotifications;
    }

    public List<ShareableApp> getShareableApps() {
        return shareableApps;
    }

    public void setShareableApps(List<ShareableApp> shareableApps) {
        for (ShareableApp shareableApp : shareableApps)
            this.addShareableApp(shareableApp);
    }

    public void lazyInitialize(DataBaseConnection db) {
        for (Channel channel : this.getChannels())
            this.channelIdMap.put(channel.getDb_id(), channel);
        for (TeamUser teamUser : this.teamUsers) {
            this.teamUserIdMap.put(teamUser.getDb_id(), teamUser);
            if (!teamUser.isVerified())
                this.teamUsersWaitingForVerification.add(teamUser);
        }
    }

    public Channel getChannelWithId(Integer channel_id) throws HttpServletException {
        Channel channel = this.channelIdMap.get(channel_id);
        if (channel == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This channel does not exist");
        return channel;
    }

    public TeamUser getTeamUserWithId(Integer teamUser_id) throws HttpServletException {
        TeamUser teamUser = this.teamUserIdMap.get(teamUser_id);
        if (teamUser == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This teamUser does not exist");
        return teamUser;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.teamUsers.add(teamUser);
        this.teamUserIdMap.put(teamUser.getDb_id(), teamUser);
        if (!teamUser.isVerified())
            this.teamUsersWaitingForVerification.add(teamUser);
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
        this.channelIdMap.put(channel.getDb_id(), channel);
    }

    public ShareableApp getShareableAppWithId(Integer db_id) throws HttpServletException {
        ShareableApp shareableApp = this.shareableAppMap.get(db_id);
        if (shareableApp == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This shareable app does not exist.");
        return shareableApp;
    }

    /* @TODO For the moment we use single_id but it will be replaced by db_id in the future */
    public void addShareableApp(ShareableApp shareableApp) {
        this.shareableApps.add(shareableApp);
        this.shareableAppMap.put(Integer.valueOf(((App) shareableApp).getDBid()), shareableApp);
    }

    public Channel getGeneralChannel() throws HttpServletException {
        for (Channel channel : this.getChannels()) {
            if (channel.getName().equals("General"))
                return channel;
        }
        throw new HttpServletException(HttpStatus.BadRequest, "No general channel");
    }

    public void removeTeamUser(TeamUser teamUser) {
        this.teamUserIdMap.remove(teamUser.getDb_id());
        this.teamUsers.remove(teamUser);
    }

    public void removeChannel(Channel channel) {
        this.channelIdMap.remove(channel.getDb_id());
        this.channels.remove(channel);
    }

    public void edit(JSONObject editJson) {
        String name = (String) editJson.get("name");
        if (name != null)
            this.name = name;
    }

    public JSONObject getJson() throws HttpServletException {
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("id", this.db_id);
        JSONArray channels = new JSONArray();
        for (Channel channel : this.getChannels())
            channels.add(channel.getJson());
        res.put("channels", channels);
        JSONArray teamUsers = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers())
            teamUsers.add(teamUser.getJson());
        res.put("teamUsers", teamUsers);
        JSONArray shareableApps = new JSONArray();
        for (ShareableApp shareableApp : this.shareableApps)
            shareableApps.add(shareableApp.getShareableJson());
        res.put("shareableApps", shareableApps);
        return res;
    }

    public Map<String, String> getAdministratorsUsernameAndEmail() {
        Map<String, String> res = new HashMap<>();
        for (TeamUser teamUser : this.getTeamUsers()) {
            if (teamUser.isTeamAdmin())
                res.put(teamUser.getUsername(), teamUser.getEmail());
        }
        return res;
    }

    public JSONObject getSimpleJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.db_id);
        res.put("name", this.name);
        return res;
    }

    public void askVerificationForTeamUser(TeamUser teamUser, String code) {
        for (Map.Entry<String, String> usernameAndEmail : this.getAdministratorsUsernameAndEmail().entrySet()) {
            String username = usernameAndEmail.getKey();
            String email = usernameAndEmail.getValue();
            SendGridMail sendGridMail = new SendGridMail("Benjamin @Ease", "benjamin@ease.space");
            sendGridMail.sendTeamUserVerificationEmail(username, email, teamUser.getUsername(), code);
        }
    }

    public void validateTeamUserRegistration(String deciphered_teamKey, TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        if (!this.teamUsersWaitingForVerification.contains(teamUser))
            throw new HttpServletException(HttpStatus.BadRequest, "teamUser already validated");
        try {
            DatabaseRequest request = db.prepareRequest("SELECT userKeys.publicKey FROM (userKeys JOIN users ON userKeys.id = users.key_id) JOIN teamUsers ON users.id = teamUsers.user_id WHERE teamUsers.id = ?;");
            request.setInt(teamUser.getDb_id());
            DatabaseResult rs = request.get();
            rs.next();
            String userPublicKey = rs.getString(1);
            teamUser.validateRegistration(deciphered_teamKey, userPublicKey, db);
            this.teamUsersWaitingForVerification.remove(teamUser);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError);
        }

    }

    public void editName(String name) {
        if (name.equals(this.getName()))
            return;
        this.name = name;
    }

    public JSONArray getShareableAppsForChannel(Integer channel_id) throws HttpServletException {
        Channel channel = this.getChannelWithId(channel_id);
        JSONArray jsonArray = new JSONArray();
        for (ShareableApp shareableApp : this.getShareableApps()) {
            if (channel != shareableApp.getChannel())
                continue;
            jsonArray.add(shareableApp.getShareableJson());
        }
        return jsonArray;
    }

    public JSONArray getShareableAppsForTeamUser(Integer teamUser_id) throws HttpServletException {
        TeamUser teamUser = this.getTeamUserWithId(teamUser_id);
        JSONArray jsonArray = new JSONArray();
        for (ShareableApp shareableApp : this.getShareableApps()) {
            if (!shareableApp.getTeamUser_tenants().contains(teamUser))
                continue;
            jsonArray.add(shareableApp.getShareableJson());
        }
        return jsonArray;
    }

    public void decipherApps(String deciphered_teamKey) throws HttpServletException {
        try {
            for (ShareableApp shareableApp : this.getShareableApps()) {
                App app = (App) shareableApp;
                if (app.isClassicApp())

                    ((ClassicApp) app).getAccount().decipherWithTeamKeyIfNeeded(deciphered_teamKey);

                else if (app.isEmpty()) {
                    for (SharedApp sharedApp : shareableApp.getSharedApps()) {
                        App app1 = (App) sharedApp;
                        if (app1.isClassicApp())
                            ((ClassicApp) app1).getAccount().decipherWithTeamKeyIfNeeded(deciphered_teamKey);
                    }
                }
            }
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError);
        }
    }

    public void removeShareableApp(ShareableApp shareableApp) {
        this.shareableApps.remove(shareableApp);
        this.shareableAppMap.remove(Integer.valueOf(((App) shareableApp).getDBid()));
    }
}
