package com.Ease.Team;

import com.Ease.Catalog.Website;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Table(name = "teams")
public class Team {

    private static final String DEFAULT_CHANNEL_NAME = "openspace";

    public static final Map<Integer, String> plansMap = new HashMap<>();
    private static final Map<String, Integer> inverse_plansMap = new HashMap<>();

    static {
        plansMap.put(0, "FreePlan");
        inverse_plansMap.put("FreePlan", 0);
        plansMap.put(1, "EaseFreemium");
        inverse_plansMap.put("EaseFreemium", 1);
    }

    public static List<Team> loadTeams(ServletContext context, DataBaseConnection db) throws HttpServletException {
        HibernateQuery query = new HibernateQuery();
        query.queryString("SELECT t FROM Team t WHERE t.active = true");
        List<Team> teams = query.list();
        for (Team team : teams) {
            team.lazyInitialize();
            team.getAppManager().setShareableApps(App.loadShareableAppsForTeam(team, context, db));
            for (ShareableApp shareableApp : team.getAppManager().getShareableApps().values()) {
                List<SharedApp> sharedApps = App.loadSharedAppsForShareableApp(shareableApp, team, context, db);
                shareableApp.setSharedApps(sharedApps);
                team.getAppManager().setSharedApps(sharedApps);
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
    @MapKey(name = "db_id")
    protected Map<Integer, TeamUser> teamUsers = new ConcurrentHashMap<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, orphanRemoval = true)
    @MapKey(name = "db_id")
    protected Map<Integer, Channel> channels = new ConcurrentHashMap<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teamAndWebsiteMap", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "website_id"))
    protected Set<Website> teamWebsites = ConcurrentHashMap.newKeySet();

    @Transient
    protected Map<Integer, TeamUser> teamUsersWaitingForVerification = new ConcurrentHashMap<>();

    @Transient
    protected AppManager appManager = new AppManager();

    @Transient
    private int activeSubscriptions;

    @Transient
    private WebSocketManager webSocketManager = new WebSocketManager();

    @Transient
    private Channel default_channel;

    @Transient
    private Subscription subscription;

    @Transient
    private Customer customer;

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

    public Subscription getSubscription() throws HttpServletException {
        try {
            if (subscription == null)
                subscription = Subscription.retrieve(this.getSubscription_id());
            return subscription;
        } catch (StripeException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Customer getCustomer() throws HttpServletException {
        try {
            if (customer == null)
                customer = Customer.retrieve(this.getCustomer_id());
            return customer;
        } catch (StripeException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
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

    public boolean isFreemium() throws HttpServletException {
        return this.getSubscription().getPlan().getId().equals("EaseFreemium");
    }

    public boolean isValidFreemium() throws HttpServletException {
        return this.isFreemium() && (this.isCard_entered() || (this.getSubscription().getTrialEnd() * 1000 > new Date().getTime()) || this.getCustomer().getAccountBalance() < 0);
    }

    public synchronized Map<Integer, TeamUser> getTeamUsers() {
        if (teamUsers == null)
            teamUsers = new ConcurrentHashMap<>();
        return teamUsers;
    }

    public void setTeamUsers(Map<Integer, TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public synchronized Map<Integer, Channel> getChannels() {
        if (channels == null)
            channels = new ConcurrentHashMap<>();
        return channels;
    }

    public void setChannels(Map<Integer, Channel> channels) {
        this.channels = channels;
    }

    public Set<Website> getTeamWebsites() {
        return teamWebsites;
    }

    public void setTeamWebsites(Set<Website> teamWebsites) {
        this.teamWebsites = teamWebsites;
    }

    public AppManager getAppManager() {
        return appManager;
    }

    public void lazyInitialize() {
        for (Map.Entry<Integer, TeamUser> entry : this.getTeamUsers().entrySet()) {
            TeamUser teamUser = entry.getValue();
            if (!teamUser.isVerified())
                this.teamUsersWaitingForVerification.put(teamUser.getDb_id(), teamUser);
        }
    }

    public Channel getChannelWithId(Integer channel_id) throws HttpServletException {
        Channel channel = this.getChannels().get(channel_id);
        if (channel == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This channel does not exist");
        return channel;
    }

    public synchronized List<Channel> getChannelsForTeamUser(TeamUser teamUser) {
        List<Channel> channels = new LinkedList<>();
        for (Map.Entry<Integer, Channel> entry : this.getChannels().entrySet()) {
            Channel channel = entry.getValue();
            if (channel.getTeamUsers().contains(teamUser))
                channels.add(channel);
        }
        return channels;
    }

    public TeamUser getTeamUserWithId(Integer teamUser_id) throws HttpServletException {
        TeamUser teamUser = this.teamUsers.get(teamUser_id);
        if (teamUser == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This teamUser does not exist");
        return teamUser;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.getTeamUsers().put(teamUser.getDb_id(), teamUser);
        if (!teamUser.isVerified())
            this.teamUsersWaitingForVerification.put(teamUser.getDb_id(), teamUser);
    }

    public void addChannel(Channel channel) {
        this.getChannels().put(channel.getDb_id(), channel);
    }

    public Channel getDefaultChannel() throws HttpServletException {
        if (this.default_channel == null) {
            for (Map.Entry<Integer, Channel> entry : this.getChannels().entrySet()) {
                Channel channel = entry.getValue();
                if (channel.getName().equals(DEFAULT_CHANNEL_NAME))
                    this.default_channel = channel;
            }
        }
        return this.default_channel;
    }

    public void removeTeamUser(TeamUser teamUser) {
        this.getTeamUsers().remove(teamUser.getDb_id());
    }

    public void removeChannel(Channel channel) {
        this.getChannels().remove(channel.getDb_id());
    }

    public void addTeamWebsite(Website website) {
        this.getTeamWebsites().add(website);
    }

    public void removeTeamWebsite(Website website) {
        this.getTeamWebsites().remove(website);
    }

    public void edit(JSONObject editJson) {
        String name = (String) editJson.get("name");
        if (name != null)
            this.name = name;
    }

    public Map<String, String> getAdministratorsUsernameAndEmail() {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<Integer, TeamUser> entry : this.getTeamUsers().entrySet()) {
            TeamUser teamUser = entry.getValue();
            if (teamUser.isTeamAdmin())
                res.put(teamUser.getUsername(), teamUser.getEmail());
        }
        return res;
    }

    public Integer getPlan_id() throws HttpServletException {
        return inverse_plansMap.get(this.getSubscription().getPlan().getId());
    }

    public JSONObject getSimpleJson() throws HttpServletException {
        JSONObject res = new JSONObject();
        res.put("id", this.db_id);
        res.put("name", this.name);
        Integer plan_id = this.getPlan_id();
        res.put("plan_id", plan_id);
        res.put("payment_required", this.isBlocked());
        return res;
    }

    public JSONObject getJson() throws HttpServletException {
        JSONObject res = this.getSimpleJson();
        JSONArray rooms = new JSONArray();
        JSONArray teamUsers = new JSONArray();
        for (Channel channel : this.getChannels().values())
            rooms.add(channel.getJson());
        for (TeamUser teamUser : this.getTeamUsers().values())
            teamUsers.add(teamUser.getJson());
        res.put("rooms", rooms);
        res.put("team_users", teamUsers);
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
        if (!this.teamUsersWaitingForVerification.containsKey(teamUser.getDb_id()))
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
        for (ShareableApp shareableApp : this.getAppManager().getShareableApps().values()) {
            if (channel != shareableApp.getChannel())
                continue;
            jsonArray.add(shareableApp.getShareableJson());
        }
        return jsonArray;
    }

    public JSONArray getShareableAppsForTeamUser(Integer teamUser_id) throws HttpServletException {
        TeamUser teamUser = this.getTeamUserWithId(teamUser_id);
        JSONArray jsonArray = new JSONArray();
        for (ShareableApp shareableApp : this.getAppManager().getShareableApps().values()) {
            if (!shareableApp.getTeamUser_tenants().contains(teamUser))
                continue;
            App app = (App) shareableApp;
            if (app.isLinkApp()) {
                SharedApp sharedApp = shareableApp.getSharedAppForTeamUser(teamUser);
                if (sharedApp == null)
                    continue;
                if (sharedApp.isPinned())
                    jsonArray.add(shareableApp.getShareableJson());
            } else
                jsonArray.add(shareableApp.getShareableJson());
        }
        return jsonArray;
    }

    public void decipherApps(String deciphered_teamKey) throws HttpServletException {
        try {
            for (ShareableApp shareableApp : this.getAppManager().getShareableApps().values()) {
                App app = (App) shareableApp;
                if (app.isClassicApp())
                    ((ClassicApp) app).getAccount().decipherWithTeamKeyIfNeeded(deciphered_teamKey);
                for (SharedApp sharedApp : shareableApp.getSharedApps().values()) {
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
        for (Map.Entry<Integer, Channel> entry : this.getChannels().entrySet()) {
            Channel channel = entry.getValue();
            if (channel.getName().equals(name))
                return channel;
        }
        return null;
    }

    public Integer getActiveTeamUserNumber() {
        int res = 0;
        for (Map.Entry<Integer, TeamUser> entry : this.getTeamUsers().entrySet()) {
            TeamUser teamUser = entry.getValue();
            if (teamUser.isActive_subscription())
                res++;
        }
        return res;
    }

    public void updateSubscription() {
        if (this.subscription_id == null || this.subscription_id.equals(""))
            return;
        this.activeSubscriptions = 0;
        this.getTeamUsers().forEach((id, teamUser) -> {
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
            if (this.getSubscription().getQuantity() != activeSubscriptions) {
                Map<String, Object> updateParams = new HashMap<>();
                updateParams.put("quantity", activeSubscriptions);
                this.getSubscription().update(updateParams);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBlocked() {
        try {
            return this.isFreemium() && !card_entered && (this.getSubscription().getTrialEnd() == null || new Date().getTime() > this.getSubscription().getTrialEnd() * 1000) && this.getCustomer().getAccountBalance() >= 0;
        } catch (HttpServletException e) {
            e.printStackTrace();
            return true;
        }
    }

    public Integer increaseAccountBalance(Integer amount, HibernateQuery hibernateQuery) throws HttpServletException {
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
                Customer customer = this.getCustomer();
                Map<String, Object> customerParams = new HashMap<>();
                Integer account_balance = Math.toIntExact((customer.getAccountBalance() == null) ? 0 : customer.getAccountBalance());
                Integer team_account_balance = account_balance - amount - credit;
                customerParams.put("account_balance", team_account_balance);
                customer.update(customerParams);
                customer.setAccountBalance(Long.valueOf(team_account_balance));
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

    public Channel createDefaultChannel(TeamUser owner) {
        this.default_channel = new Channel(this, DEFAULT_CHANNEL_NAME, "Company-wide apps and tools sharing", owner);
        return this.default_channel;
    }

    public TeamUser getTeamUserOwner() {
        for (Map.Entry<Integer, TeamUser> entry : this.getTeamUsers().entrySet()) {
            TeamUser teamUser = entry.getValue();
            if (teamUser.isTeamOwner())
                return teamUser;
        }
        return null;
    }

    public void checkFreeTrialEnd(DataBaseConnection db) {
        MailJetBuilder mailJetBuilder;
        if (this.card_entered)
            return;
        try {
            if (!this.isFreemium())
                return;
            String link = Variables.URL_PATH + "teams#/teams/" + this.getDb_id() + "/" + this.getDefaultChannel().getDb_id() + "/settings/payment";
            Long trialEnd = this.getSubscription().getTrialEnd() * 1000;
            if (DateComparator.isInDays(new Date(trialEnd), 5)) {
                System.out.println(this.getName() + " trial will end in 5 days.");
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(208643);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addTo(this.getTeamUserOwner().getEmail());
                mailJetBuilder.addVariable("teamName", this.getName());
                mailJetBuilder.addVariable("link", link);
                mailJetBuilder.sendEmail();
            } else if (DateComparator.isInDays(new Date(trialEnd), 1)) {
                System.out.println(this.getName() + " trial will end in 1 day.");
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(208644);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addVariable("teamName", this.getName());
                mailJetBuilder.addTo(this.getTeamUserOwner().getEmail());
                mailJetBuilder.addVariable("link", link);
                mailJetBuilder.sendEmail();
            }
            for (ShareableApp shareableApp : this.getAppManager().getShareableApps().values()) {
                ((App) shareableApp).setDisabled(this.isBlocked(), db);
                for (SharedApp sharedApp : shareableApp.getSharedApps().values()) {
                    if (!this.isBlocked() && sharedApp.getTeamUser_tenant().isDisabled())
                        continue;
                    ((App) sharedApp).setDisabled(this.isBlocked(), db);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkDepartureDates(Date date, DataBaseConnection db) {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd");
            for (TeamUser teamUser : this.getTeamUsers().values()) {
                if (teamUser.getDepartureDate() == null)
                    continue;
                if (teamUser.isDisabled()) {
                    System.out.println(this.getAppManager().getSharedAppsForTeamUser(teamUser).size());
                    for (SharedApp sharedApp : this.getAppManager().getSharedAppsForTeamUser(teamUser))
                        ((App) sharedApp).setDisabled(true, db);
                }
                if (DateComparator.isInDays(teamUser.getDepartureDate(), 3)) {
                    calendar.setTime(teamUser.getDepartureDate());
                    String suffixe = "th";
                    switch (calendar.get(Calendar.DAY_OF_MONTH)) {
                        case 1: {
                            suffixe = "st";
                            break;
                        }
                        case 2: {
                            suffixe = "nd";
                            break;
                        }
                        case 3: {
                            suffixe = "rd";
                            break;
                        }
                    }
                    String formattedDate = simpleDateFormat.format(teamUser.getDepartureDate()) + suffixe;
                    this.getTeamUserWithId(teamUser.getAdmin_id()).addNotification("Reminder: the departure of @" + teamUser.getUsername() + " is planned on next " + formattedDate + ".", "@" + teamUser.getDb_id() + "/flexPanel", "/resources/notifications/user_departure.png", date, db);

                }
            }
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }
}
