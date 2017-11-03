package com.Ease.Team.TeamCard;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "teamCards")
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class TeamCard {
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

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation_date;

    @OneToMany(mappedBy = "teamCard", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKey(name = "db_id")
    private Map<Integer, TeamCardReceiver> teamCardReceiverMap = new ConcurrentHashMap<>();

    public TeamCard() {

    }

    public TeamCard(Team team, Channel channel) {
        this.team = team;
        this.channel = channel;
        this.creation_date = new Date();
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
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
        this.channel = channel;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Map<Integer, TeamCardReceiver> getTeamCardReceiverMap() {
        return teamCardReceiverMap;
    }

    public void setTeamCardReceiverMap(Map<Integer, TeamCardReceiver> teamCardReceiverMap) {
        this.teamCardReceiverMap = teamCardReceiverMap;
    }

    public abstract String getName();

    public abstract String getLogo();

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("logo", this.getLogo());
        res.put("type", this.getType());
        res.put("creation_date", this.getCreation_date().getTime());
        JSONArray receivers = new JSONArray();
        this.getTeamCardReceiverMap().values().stream().sorted(Comparator.comparingInt(TeamCardReceiver::getDb_id)).forEach(c -> receivers.add(c.getJson()));
        res.put("receivers", receivers);
        return res;
    }

    public abstract String getType();

    public void addTeamCardReceiver(TeamCardReceiver teamCardReceiver) {
        this.getTeamCardReceiverMap().put(teamCardReceiver.getDb_id(), teamCardReceiver);
    }

    public void removeTeamCardReceiver(TeamCardReceiver teamCardReceiver) {
        this.getTeamCardReceiverMap().remove(teamCardReceiver.getDb_id());
    }

    public void removeTeamCardReceiver(Integer teamCard_receiver_id) {
        this.getTeamCardReceiverMap().remove(teamCard_receiver_id);
    }

    public boolean containsTeamUser(TeamUser teamUser_receiver) {
        return this.getTeamCardReceiverMap().values().stream().filter(teamCardReceiver -> teamCardReceiver.getTeamUser() == teamUser_receiver).findFirst().orElse(null) != null;
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
}