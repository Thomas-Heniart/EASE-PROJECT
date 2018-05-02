package com.Ease.Team.TeamCard;

import com.Ease.NewDashboard.Account;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamCards")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TeamCard {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation_date;

    @OneToMany(mappedBy = "teamCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @MapKey(name = "db_id")
    private Map<Integer, TeamCardReceiver> teamCardReceiverMap = new ConcurrentHashMap<>();

    @OneToMany(mappedBy = "teamCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @MapKey(name = "db_id")
    private Map<Integer, JoinTeamCardRequest> joinTeamCardRequestMap = new ConcurrentHashMap<>();

    @ManyToOne
    @JoinColumn(name = "team_user_sender_id")
    private TeamUser teamUser_sender;

    public TeamCard() {

    }

    public TeamCard(String name, Team team, Channel channel, String description) {
        this.name = name;
        this.team = team;
        this.channel = channel;
        this.description = description;
        this.creation_date = new Date();
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        if (this.channel != null)
            this.channel.removeTeamCard(this);
        this.channel = channel;
        if (this.channel != null)
            this.channel.addTeamCard(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public synchronized Map<Integer, TeamCardReceiver> getTeamCardReceiverMap() {
        return teamCardReceiverMap;
    }

    public void setTeamCardReceiverMap(Map<Integer, TeamCardReceiver> teamCardReceiverMap) {
        this.teamCardReceiverMap = teamCardReceiverMap;
    }

    public synchronized Map<Integer, JoinTeamCardRequest> getJoinTeamCardRequestMap() {
        return joinTeamCardRequestMap;
    }

    public void setJoinTeamCardRequestMap(Map<Integer, JoinTeamCardRequest> joinTeamCardRequestMap) {
        this.joinTeamCardRequestMap = joinTeamCardRequestMap;
    }

    public TeamUser getTeamUser_sender() {
        return teamUser_sender;
    }

    public void setTeamUser_sender(TeamUser teamUser_sender) {
        this.teamUser_sender = teamUser_sender;
    }

    public abstract String getLogo();

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("logo", this.getLogo());
        res.put("type", this.getType());
        res.put("sub_type", this.getSubtype());
        res.put("creation_date", this.getCreation_date().getTime());
        res.put("team_id", this.getTeam().getDb_id());
        res.put("channel_id", this.getChannel().getDb_id());
        res.put("description", this.getDescription());
        res.put("team_user_sender_id", this.getTeamUser_sender() == null ? JSONObject.NULL : this.getTeamUser_sender().getDb_id());
        JSONArray receivers = new JSONArray();
        this.getTeamCardReceiverMap().values().stream().sorted(Comparator.comparingInt(TeamCardReceiver::getDb_id)).forEach(c -> receivers.put(c.getCardJson()));
        res.put("receivers", receivers);
        if (!this.isTeamLinkCard()) {
            JSONArray requests = new JSONArray();
            for (JoinTeamCardRequest joinTeamCardRequest : this.getJoinTeamCardRequestMap().values())
                requests.put(joinTeamCardRequest.getJson());
            res.put("requests", requests);
        }
        return res;
    }

    public JSONObject getWebSocketJson() {
        JSONObject res = new JSONObject();
        res.put("team_card", this.getJson());
        return res;
    }

    public abstract String getType();

    public abstract String getSubtype();

    public void addTeamCardReceiver(TeamCardReceiver teamCardReceiver) {
        this.getTeamCardReceiverMap().put(teamCardReceiver.getDb_id(), teamCardReceiver);
    }

    public void removeTeamCardReceiver(TeamCardReceiver teamCardReceiver) {
        this.getTeamCardReceiverMap().remove(teamCardReceiver.getDb_id());
    }

    public void removeTeamCardReceiver(Integer team_card_receiver_id) {
        this.getTeamCardReceiverMap().remove(team_card_receiver_id);
    }

    public synchronized boolean containsTeamUser(TeamUser teamUser_receiver) {
        return this.getTeamCardReceiverMap().values().stream().filter(teamCardReceiver -> teamCardReceiver.getTeamUser().equals(teamUser_receiver)).findFirst().orElse(null) != null;
    }

    public boolean isTeamWebsiteCard() {
        return false;
    }

    public boolean isTeamSoftwareCard() {
        return false;
    }

    public boolean isTeamSingleCard() {
        return false;
    }

    public boolean isTeamLinkCard() {
        return false;
    }

    public boolean isTeamEnterpriseCard() {
        return false;
    }

    public TeamCardReceiver getTeamCardReceiver(Integer id) throws HttpServletException {
        TeamCardReceiver teamCardReceiver = this.getTeamCardReceiverMap().get(id);
        if (teamCardReceiver == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No team card receiver with this id");
        return teamCardReceiver;
    }

    public TeamCardReceiver getTeamCardReceiver(TeamUser teamUser) {
        return this.getTeamCardReceiverMap().values().stream().filter(teamCardReceiver1 -> teamCardReceiver1.getTeamUser().equals(teamUser)).findFirst().orElse(null);
    }

    public JoinTeamCardRequest getJoinTeamCardRequest(TeamUser teamUser) {
        return this.getJoinTeamCardRequestMap().values().stream().filter(joinTeamCardRequest -> joinTeamCardRequest.getTeamUser().equals(teamUser)).findFirst().orElse(null);
    }

    public JoinTeamCardRequest getJoinTeamCardRequest(Integer id) throws HttpServletException {
        JoinTeamCardRequest joinTeamCardRequest = this.getJoinTeamCardRequestMap().get(id);
        if (joinTeamCardRequest == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such request");
        return joinTeamCardRequest;
    }

    public void addJoinTeamCardRequest(JoinTeamCardRequest joinTeamCardRequest) {
        this.getJoinTeamCardRequestMap().put(joinTeamCardRequest.getDb_id(), joinTeamCardRequest);
    }

    public void removeJoinTeamCardRequest(JoinTeamCardRequest joinTeamCardRequest) {
        this.getJoinTeamCardRequestMap().remove(joinTeamCardRequest.getDb_id());
    }

    public void decipher(String teamKey) throws HttpServletException {
        for (TeamCardReceiver teamCardReceiver : this.getTeamCardReceiverMap().values()) {
            teamCardReceiver.getApp().decipher(null, teamKey);
        }
    }

    public Integer getPassword_reminder_interval() {
        return -1;
    }

    public Account getAccount() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamCard teamCard = (TeamCard) o;

        return db_id.equals(teamCard.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public abstract String getMetricName();

    public Collection<TeamUser> getTeamUsers() {
        return this.getTeamCardReceiverMap().values().stream().map(TeamCardReceiver::getTeamUser).collect(Collectors.toList());
    }

    public void calculatePasswordScore() throws NoSuchAlgorithmException {
        if (this.getAccount() == null)
            return;
        this.setPasswordScore(this.getAccount().calculatePasswordScore());
    }

    public abstract Integer getPasswordScore();

    protected abstract void setPasswordScore(Integer score);

    public abstract void setLastPasswordScoreAlertDate(Date date);

    public StringBuilder passwordExportCsvString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.getAccount() == null)
            return stringBuilder;
        return stringBuilder.append(this.getName());
    }
}