package com.Ease.Team;

import com.Ease.Catalog.Website;
import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Metrics.ClickOnApp;
import com.Ease.Metrics.TeamMetrics;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.DateUtils;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Slack.SlackAPIWrapper;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teams")
public class Team {

    private static final String DEFAULT_CHANNEL_NAME = "openspace";

    public static final Map<Integer, String> plansMap = new HashMap<>();
    private static final Map<String, Integer> inverse_plansMap = new HashMap<>();
    public static final int MAX_MEMBERS = 15;

    static {
        plansMap.put(0, "FreePlan");
        inverse_plansMap.put("FreePlan", 0);
        plansMap.put(1, "EaseFreemium");
        inverse_plansMap.put("EaseFreemium", 1);
        plansMap.put(2, "Pro");
        inverse_plansMap.put("Pro", 2);
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "name")
    private String name;

    @Column(name = "customer_id")
    private String customer_id;

    @Column(name = "subscription_id")
    private String subscription_id;

    @Column(name = "subscription_date")
    private Date subscription_date;

    @Column(name = "card_entered")
    private boolean card_entered = false;

    @Column(name = "invite_people")
    private boolean invite_people = false;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "company_size")
    private Integer company_size;

    @Column(name = "invitations_sent")
    private boolean invitations_sent = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "onboarding_status_id")
    private OnboardingStatus onboardingStatus = new OnboardingStatus();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @MapKey(name = "db_id")
    private Map<Integer, TeamUser> teamUsers = new ConcurrentHashMap<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @MapKey(name = "db_id")
    private Map<Integer, Channel> channels = new ConcurrentHashMap<>();

    @ManyToMany(mappedBy = "teams")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Website> teamWebsites = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TeamCard> teamCardSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @MapKey(name = "email")
    private Map<String, InvitedFriend> invitedFriendMap = new ConcurrentHashMap<>();

    @Transient
    private Customer customer;

    @Transient
    private Subscription subscription;

    @Transient
    private Channel default_channel;

    public Team(String name, Integer company_size) {
        this.name = name;
        this.company_size = company_size;
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

    public Integer getCompany_size() {
        return company_size;
    }

    public void setCompany_size(Integer company_size) {
        this.company_size = company_size;
    }

    public boolean isInvitations_sent() {
        return invitations_sent;
    }

    public void setInvitations_sent(boolean invitations_sent) {
        this.invitations_sent = invitations_sent;
    }

    public synchronized Set<TeamCard> getTeamCardSet() {
        return teamCardSet;
    }

    public void setTeamCardSet(Set<TeamCard> teamCardSet) {
        this.teamCardSet = teamCardSet;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public OnboardingStatus getOnboardingStatus() {
        return onboardingStatus;
    }

    public void setOnboardingStatus(OnboardingStatus onboardingStatus) {
        this.onboardingStatus = onboardingStatus;
    }

    public TeamCard getTeamCard(Integer id) throws HttpServletException {
        TeamCard teamCard = this.getTeamCardSet().stream().filter(teamCard1 -> teamCard1.getDb_id().equals(id)).findFirst().orElse(null);
        if (teamCard == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This teamCard does not exist");
        return teamCard;
    }

    public void addTeamCard(TeamCard teamCard) {
        this.getTeamCardSet().add(teamCard);
        Stream<TeamCard> stream = this.getTeamCardSet().stream().filter(teamCard1 -> teamCard1.getTeamCardReceiverMap().size() > 0);
        long count = stream.count();
        if (count == 15L || count == 30L) {
            StringBuilder stringBuilder = new StringBuilder("*✅Company with ")
                    .append(count)
                    .append(" cards*\nThis company has now ")
                    .append(count)
                    .append(" cards on its team space (with tags) : ")
                    .append(this.getName())
                    .append(" (")
                    .append(this.getTeamUserOwner().getEmail())
                    .append(")")
                    .append("\n=======\n=======\n=======");
            try {
                SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
            } catch (IOException | SlackApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeTeamCard(TeamCard teamCard) {
        this.getTeamCardSet().remove(teamCard);
    }

    public void removeTeamCards(Set<TeamCard> teamCardSet) {
        this.getTeamCardSet().removeAll(teamCardSet);
    }

    public boolean isFreemium() throws HttpServletException {
        return this.getSubscription().getPlan().getId().equals("EaseFreemium") || this.getSubscription().getPlan().getId().equals("Pro");
    }

    public boolean isValidFreemium() throws HttpServletException {
        return this.isFreemium() && (this.isCard_entered() || (this.getSubscription().getTrialEnd() != null && this.getSubscription().getTrialEnd() * 1000 > new Date().getTime()) || this.getCustomer().getAccountBalance() < 0);
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

    public synchronized Set<Website> getTeamWebsites() {
        return teamWebsites;
    }

    public void setTeamWebsites(Set<Website> teamWebsites) {
        this.teamWebsites = teamWebsites;
    }

    public synchronized Map<String, InvitedFriend> getInvitedFriendMap() {
        return invitedFriendMap;
    }

    public void setInvitedFriendMap(Map<String, InvitedFriend> invitedFriendMap) {
        this.invitedFriendMap = invitedFriendMap;
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
        TeamUser teamUser = this.getTeamUsers().get(teamUser_id);
        if (teamUser == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This teamUser does not exist");
        return teamUser;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.getTeamUsers().put(teamUser.getDb_id(), teamUser);
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

    public InvitedFriend getInvitedFriend(String email) {
        return this.getInvitedFriendMap().get(email);
    }

    public void addInvitedFriend(InvitedFriend invitedFriend) {
        this.getInvitedFriendMap().put(invitedFriend.getEmail(), invitedFriend);
    }

    public void removeInvitedFriend(InvitedFriend invitedFriend) {
        this.getInvitedFriendMap().remove(invitedFriend.getEmail());
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
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("company_size", this.getCompany_size());
        Integer plan_id = this.getPlan_id();
        /* Hack for sergii frontend */
        if (plan_id == 2)
            plan_id = 1;
        res.put("plan_id", plan_id);
        res.put("onboarding_step", this.getOnboardingStatus().getStep());
        res.put("payment_required", this.isBlocked());
        res.put("show_invite_people_popup", !this.isInvitations_sent() && this.getTeamCardSet().size() >= 8 && DateUtils.isOutdated(this.getSubscription_date(), 0, 1));
        res.put("extra_members", this.getInvitedFriendMap().size());
        return res;
    }

    public JSONObject getWebSockeetJson() throws HttpServletException {
        JSONObject res = new JSONObject();
        res.put("team", this.getJson());
        return res;
    }

    public JSONObject getJson() throws HttpServletException {
        JSONObject res = this.getSimpleJson();
        JSONArray rooms = new JSONArray();
        JSONArray teamUsers = new JSONArray();
        for (Channel channel : this.getChannels().values())
            rooms.put(channel.getJson());
        for (TeamUser teamUser : this.getTeamUsers().values())
            teamUsers.put(teamUser.getJson());
        res.put("rooms", rooms);
        res.put("team_users", teamUsers);
        return res;
    }

    public void editName(String name) {
        if (name.equals(this.getName()))
            return;
        this.name = name;
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
            if (teamUser.isVerified() && !teamUser.getTeamCardReceivers().isEmpty())
                res++;
        }
        return res;
    }

    public void updateSubscription(Map<String, Object> teamProperties) {
        try {
            if (this.subscription_id == null || this.subscription_id.equals(""))
                return;
            this.initializeStripe(teamProperties);
            if (this.getPlan_id() == 2)
                return;
            int activeSubscriptions = Math.toIntExact(this.getTeamUsers().values().stream().filter(teamUser -> teamUser.isVerified() && !teamUser.getTeamCardReceivers().isEmpty()).count());
            System.out.println("Team: " + this.getName() + " has " + activeSubscriptions + " active subscriptions.");
            this.initializeStripe(teamProperties);
            if (this.getSubscription().getQuantity() <= activeSubscriptions) {
                Map<String, Object> updateParams = new HashMap<>();
                updateParams.put("quantity", activeSubscriptions);
                this.getSubscription().update(updateParams);
                this.getSubscription().setQuantity(activeSubscriptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeStripe(Map<String, Object> teamProperties) {
        try {
            Customer customer = (Customer) teamProperties.get("customer");
            if (customer == null) {
                customer = Customer.retrieve(this.getCustomer_id());
                teamProperties.put("customer", customer);
            }
            this.setCustomer(customer);
            Subscription subscription = (Subscription) teamProperties.get("subscription");
            if (subscription == null) {
                subscription = Subscription.retrieve(this.getSubscription_id());
                teamProperties.put("subscription", subscription);
            }
            this.setSubscription(subscription);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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

    public void checkFreeTrialEnd() {
        MailJetBuilder mailJetBuilder;
        if (this.card_entered)
            return;
        try {
            if (!this.isFreemium() || this.getSubscription_id() == null || this.getCustomer_id() == null)
                return;
            String link = Variables.URL_PATH + "#/teams/" + this.getDb_id() + "/" + this.getDefaultChannel().getDb_id() + "/settings/payment";
            Long trialEnd = this.getSubscription().getTrialEnd() * 1000;
            if (DateUtils.isInDays(new Date(trialEnd), 5)) {
                System.out.println(this.getName() + " trial will end in 5 days.");
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(208643);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addTo(this.getTeamUserOwner().getEmail());
                mailJetBuilder.addVariable("teamName", this.getName());
                mailJetBuilder.addVariable("link", link);
                mailJetBuilder.sendEmail();
            } else if (DateUtils.isInDays(new Date(trialEnd), 1)) {
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

    public synchronized boolean hasTeamUserWithUsername(String username) {
        return this.getTeamUsers().values().stream().anyMatch(teamUser -> teamUser.getUsername().equals(username));
    }

    public boolean hasTeamUserWithEmail(String email) {
        return this.getTeamUsers().values().stream().anyMatch(teamUser -> teamUser.getEmail().equals(email));
    }

    public JSONArray getAverageOfClick(int year, int week_of_year, HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        double day_one = 0.;
        double day_two = 0.;
        double day_three = 0.;
        double day_four = 0.;
        double day_five = 0.;
        double day_six = 0.;
        double day_seven = 0.;
        TeamMetrics teamMetrics = TeamMetrics.getMetrics(this.getDb_id(), year, week_of_year, hibernateQuery);
        int teamUsers_size = teamMetrics.getPeople_invited();
        if (teamUsers_size == 0) {
            res.put(0);
            res.put(0);
            res.put(0);
            res.put(0);
            res.put(0);
            res.put(0);
            res.put(0);
        } else {
            hibernateQuery.queryString("SELECT c FROM ClickOnApp c WHERE c.team_id = :team_id AND c.year = :year AND c.week_of_year = :week_of_year");
            hibernateQuery.setParameter("team_id", this.getDb_id());
            hibernateQuery.setParameter("year", year);
            hibernateQuery.setParameter("week_of_year", week_of_year);
            List<ClickOnApp> metrics = hibernateQuery.list();
            for (ClickOnApp clickOnAppMetric : metrics) {
                day_one += clickOnAppMetric.getDay_one();
                day_two += clickOnAppMetric.getDay_two();
                day_three += clickOnAppMetric.getDay_three();
                day_four += clickOnAppMetric.getDay_four();
                day_five += clickOnAppMetric.getDay_five();
                day_six += clickOnAppMetric.getDay_six();
                day_seven += clickOnAppMetric.getDay_seven();
            }
            res.put(day_one / teamUsers_size);
            res.put(day_two / teamUsers_size);
            res.put(day_three / teamUsers_size);
            res.put(day_four / teamUsers_size);
            res.put(day_five / teamUsers_size);
            res.put(day_six / teamUsers_size);
            res.put(day_seven / teamUsers_size);
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;
        return db_id.equals(team.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
