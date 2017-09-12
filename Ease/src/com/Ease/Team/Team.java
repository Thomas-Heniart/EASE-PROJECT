package com.Ease.Team;

import com.Ease.Context.Variables;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.*;
import com.Ease.websocketV1.WebSocketManager;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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

    private static final String DEFAULT_CHANNEL_NAME = "openspace";

    public static List<Team> loadTeams(ServletContext context, DataBaseConnection db) throws HttpServletException {
        HibernateQuery query = new HibernateQuery();
        //query.querySQLString("SELECT id FROM teams");
        query.queryString("SELECT t FROM Team t WHERE t.active = true");
        List<Team> teams = new LinkedList<>();
        teams = query.list();
        /* for (Object team_id : query.list()) {
            Integer id = (Integer) team_id;
            Team team = (Team) query.get(Team.class, new Integer(id));
            team.lazyInitialize(db);
            team.getAppManager().setShareableApps(App.loadShareableAppsForTeam(team, context, db));
            for (ShareableApp shareableApp : team.getAppManager().getShareableApps()) {
                List<SharedApp> sharedApps = App.loadSharedAppsForShareableApp(shareableApp, context, db);
                shareableApp.setSharedApps(sharedApps);
                team.getAppManager().setSharedApps(sharedApps);
            }
            teams.add(team);
        } */
        for (Team team : teams) {
            team.lazyInitialize();
            team.getAppManager().setShareableApps(App.loadShareableAppsForTeam(team, context, db));
            for (ShareableApp shareableApp : team.getAppManager().getShareableApps()) {
                List<SharedApp> sharedApps = App.loadSharedAppsForShareableApp(shareableApp, context, db);
                shareableApp.setSharedApps(sharedApps);
                team.getAppManager().setSharedApps(sharedApps);
            }
            for (Channel channel : team.getChannels()) {
                if (!channel.getTeamUsers().isEmpty()) {

                }
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

    @Column(name = "customer_id")
    protected String customer_id;

    @Column(name = "subscription_id")
    protected String subscription_id;

    @Column(name = "subscription_date")
    protected Date subscription_date;

    @Column(name = "card_entered")
    protected boolean card_entered = false;

    @Column(name = "invite_people")
    protected boolean invite_people = false;

    @Column(name = "active")
    protected boolean active = true;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, orphanRemoval = true)
    protected List<TeamUser> teamUsers = new LinkedList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, orphanRemoval = true)
    protected List<Channel> channels = new LinkedList<>();

    @Transient
    protected Map<Integer, Channel> channelIdMap = new HashMap<>();

    @Transient
    protected Map<Integer, TeamUser> teamUserIdMap = new HashMap<>();

    @Transient
    protected List<TeamUser> teamUsersWaitingForVerification = new LinkedList<>();

    @Transient
    protected AppManager appManager = new AppManager();

    @Transient
    private int activeSubscriptions;

    @Transient
    private WebSocketManager webSocketManager = new WebSocketManager();

    @Transient
    private Channel default_channel;

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

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public boolean isCard_entered() {
        return card_entered;
    }

    public void setCard_entered(boolean card_entered) {
        this.card_entered = card_entered;
    }

    public Date getSubscription_date() {
        return subscription_date;
    }

    public void setSubscription_date(Date subscription_date) {
        this.subscription_date = subscription_date;
    }

    public boolean invite_people() {
        return invite_people;
    }

    public void setInvite_people(boolean invite_people) {
        this.invite_people = invite_people;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<TeamUser> getTeamUsers() {
        if (this.teamUsers == null)
            this.teamUsers = new LinkedList<>();
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public List<Channel> getChannels() {
        if (channels == null)
            channels = new LinkedList<>();
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public AppManager getAppManager() {
        return appManager;
    }

    public void lazyInitialize() {
        for (TeamUser teamUser : this.getTeamUsers()) {
            this.teamUserIdMap.put(teamUser.getDb_id(), teamUser);
            if (!teamUser.isVerified())
                this.teamUsersWaitingForVerification.add(teamUser);
        }
        for (Channel channel : this.getChannels())
            this.channelIdMap.put(channel.getDb_id(), channel);
    }

    public Channel getChannelWithId(Integer channel_id) throws HttpServletException {
        Channel channel = this.channelIdMap.get(channel_id);
        if (channel == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This channel does not exist");
        return channel;
    }

    public List<Channel> getChannelsForTeamUser(TeamUser teamUser) {
        List<Channel> channels = new LinkedList<>();
        for (Channel channel : this.getChannels()) {
            if (channel.getTeamUsers().contains(teamUser))
                channels.add(channel);
        }
        return channels;
    }

    public TeamUser getTeamUserWithId(Integer teamUser_id) throws HttpServletException {
        TeamUser teamUser = this.teamUserIdMap.get(teamUser_id);
        if (teamUser == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This teamUser does not exist");
        return teamUser;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.getTeamUsers().add(teamUser);
        this.teamUserIdMap.put(teamUser.getDb_id(), teamUser);
        if (!teamUser.isVerified())
            this.teamUsersWaitingForVerification.add(teamUser);
    }

    public void addChannel(Channel channel) {
        this.getChannels().add(channel);
        this.channelIdMap.put(channel.getDb_id(), channel);
    }

    public Channel getDefaultChannel() throws HttpServletException {
        if (this.default_channel == null) {
            for (Channel channel : this.getChannels()) {
                if (channel.getName().equals(DEFAULT_CHANNEL_NAME))
                    this.default_channel = channel;
            }
        }
        System.out.println("Default channel is null: " + (this.default_channel == null));
        return this.default_channel;
    }

    public void removeTeamUser(TeamUser teamUser) {
        this.teamUserIdMap.remove(teamUser.getDb_id());
        this.getTeamUsers().remove(teamUser);
    }

    public void removeChannel(Channel channel) {
        this.channelIdMap.remove(channel.getDb_id());
        this.getChannels().remove(channel);
    }

    public void edit(JSONObject editJson) {
        String name = (String) editJson.get("name");
        if (name != null)
            this.name = name;
    }

    public JSONObject getJson() throws HttpServletException {
        return this.getSimpleJson();
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
        res.put("valid_subscription", !this.isBlocked());
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
        for (ShareableApp shareableApp : this.getAppManager().getShareableApps()) {
            if (channel != shareableApp.getChannel())
                continue;
            jsonArray.add(shareableApp.getShareableJson());
        }
        return jsonArray;
    }

    public JSONArray getShareableAppsForTeamUser(Integer teamUser_id) throws HttpServletException {
        TeamUser teamUser = this.getTeamUserWithId(teamUser_id);
        JSONArray jsonArray = new JSONArray();
        for (ShareableApp shareableApp : this.getAppManager().getShareableApps()) {
            if (!shareableApp.getTeamUser_tenants().contains(teamUser))
                continue;
            jsonArray.add(shareableApp.getShareableJson());
        }
        return jsonArray;
    }

    public void decipherApps(String deciphered_teamKey) throws HttpServletException {
        try {
            for (ShareableApp shareableApp : this.getAppManager().getShareableApps()) {
                App app = (App) shareableApp;
                if (app.isClassicApp())
                    ((ClassicApp) app).getAccount().decipherWithTeamKeyIfNeeded(deciphered_teamKey);
                for (SharedApp sharedApp : shareableApp.getSharedApps()) {
                    App app1 = (App) sharedApp;
                    if (app1.isClassicApp())
                        ((ClassicApp) app1).getAccount().decipherWithTeamKeyIfNeeded(deciphered_teamKey);
                }
            }
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError);
        }
    }

    public Channel getChannelNamed(String name) {
        for (Channel channel : this.getChannels()) {
            if (channel.getName().equals(name))
                return channel;
        }
        return null;
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

    public Integer getActiveTeamUserNumber() {
        int res = 0;
        for (TeamUser teamUser : this.getTeamUsers()) {
            if (teamUser.isActive_subscription())
                res++;
        }
        return res;
    }

    public void updateSubscription() {
        if (this.subscription_id == null || this.subscription_id.equals(""))
            return;
        this.activeSubscriptions = 0;
        this.getTeamUsers().forEach(teamUser -> {
            if (!this.getAppManager().getShareableAppsForTeamUser(teamUser).isEmpty())
                teamUser.setActive_subscription(true);
            for (SharedApp sharedApp : this.getAppManager().getSharedAppsForTeamUser(teamUser)) {
                if (!((App) sharedApp).isReceived())
                    continue;
                teamUser.setActive_subscription(true);
                break;
            }
            if (teamUser.isActive_subscription())
                activeSubscriptions++;
        });
        System.out.println("Team: " + this.getName() + " has " + activeSubscriptions + " active subscriptions.");
        try {
            Subscription subscription = Subscription.retrieve(this.subscription_id);
            if (subscription.getQuantity() != activeSubscriptions) {
                Map<String, Object> updateParams = new HashMap<>();
                updateParams.put("quantity", activeSubscriptions);
                subscription.update(updateParams);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    public boolean isBlocked() {
        return (this.subscription_date == null) || (!card_entered && DateComparator.isOutdated(this.subscription_date, 30));
    }

    public Integer increaseAccountBalance(Integer amount, HibernateQuery hibernateQuery) {
        hibernateQuery.querySQLString("SELECT credit FROM teamCredit WHERE team_id = ?");
        hibernateQuery.setParameter(1, this.getDb_id());
        Integer credit = (Integer) hibernateQuery.getSingleResult();
        if (this.getCustomer_id() == null) {
            if (credit != null) {
                hibernateQuery.querySQLString("UPDATE teamCredit SET credit = ? WHERE team_id = ?;");
                amount += credit;
                hibernateQuery.setParameter(1, amount);
            } else {
                hibernateQuery.querySQLString("INSERT INTO teamCredit VALUES (NULL, ?, ?);");
                hibernateQuery.setParameter(1, amount);
            }
            hibernateQuery.setParameter(2, this.getDb_id());
            hibernateQuery.executeUpdate();
            return amount;
        } else {
            try {
                if (credit != null) {
                    hibernateQuery.querySQLString("DELETE FROM teamCredit WHERE team_id = ?;");
                    hibernateQuery.setParameter(1, this.getDb_id());
                    hibernateQuery.executeUpdate();
                } else
                    credit = 0;
                Customer customer = Customer.retrieve(this.getCustomer_id());
                Map<String, Object> customerParams = new HashMap<>();
                Integer account_balance = Math.toIntExact((customer.getAccountBalance() == null) ? 0 : customer.getAccountBalance());
                Integer team_account_balance = account_balance - amount - credit;
                customerParams.put("account_balance", team_account_balance);
                customer.update(customerParams);
                return team_account_balance;
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            } catch (CardException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public WebSocketManager getWebSocketManager() {
        return webSocketManager;
    }

    public Channel createDefaultChannel(Integer owner_id) {
        this.default_channel = new Channel(this, DEFAULT_CHANNEL_NAME, "Company-wide apps and tools sharing", owner_id);
        return this.default_channel;
    }

    public TeamUser getTeamUserOwner() {
        for (TeamUser teamUser : this.getTeamUsers()) {
            if (teamUser.isTeamOwner())
                return teamUser;
        }
        return null;
    }

    public void checkFreeTrialEnd() {
        MailJetBuilder mailJetBuilder;
        if (this.card_entered)
            return;
        try {
            String link = Variables.URL_PATH + "teams#/teams/" + this.getDb_id() + "/" + this.getDefaultChannel().getDb_id() + "/settings/payment";
            if (DateComparator.isEqualsAfter(this.subscription_date, 25) && !this.card_entered) {
                System.out.println(this.getName() + " trial will end in 5 days.");
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(208643);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addTo(this.getTeamUserOwner().getEmail());
                mailJetBuilder.addVariable("teamName", this.getName());
                mailJetBuilder.addVariable("link", link);
                mailJetBuilder.sendEmail();
            } else if (DateComparator.isEqualsAfter(this.subscription_date, 29) && !this.card_entered) {
                System.out.println(this.getName() + " trial will end in 1 day.");
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(208644);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addVariable("teamName", this.getName());
                mailJetBuilder.addTo(this.getTeamUserOwner().getEmail());
                mailJetBuilder.addVariable("link", link);
                mailJetBuilder.sendEmail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
