package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Software;
import com.Ease.Context.Variables;
import com.Ease.NewDashboard.Account;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamSingleSoftwareCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamSingleSoftwareCard extends TeamSoftwareCard {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "teamUser_filler_id")
    private TeamUser teamUser_filler_test;

    @Column(name = "magicLink")
    private String magicLink;

    @Column(name = "magicLinkExpirationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date magicLinkExpirationDate;

    @Column(name = "password_score")
    private Integer passwordScore;

    @Column(name = "lastPasswordScoreAlertDate")
    private Date lastPasswordScoreAlertDate;

    public TeamSingleSoftwareCard() {

    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software) {
        super(name, team, channel, description, software);
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, Account account) {
        super(name, team, channel, description, software);
        this.account = account;
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, Integer password_reminder_interval, Account account) {
        super(name, team, channel, description, software, password_reminder_interval);
        this.account = account;
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, TeamUser teamUser_filler_test) {
        super(name, team, channel, description, software);
        this.teamUser_filler_test = teamUser_filler_test;
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, Integer password_reminder_interval, TeamUser teamUser_filler_test) {
        super(name, team, channel, description, software, password_reminder_interval);
        this.teamUser_filler_test = teamUser_filler_test;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public TeamUser getTeamUser_filler_test() {
        return teamUser_filler_test;
    }

    public void setTeamUser_filler_test(TeamUser teamUser_filler) {
        this.teamUser_filler_test = teamUser_filler;
    }

    public String getMagicLink() {
        return magicLink;
    }

    public void setMagicLink(String magicLink) {
        this.magicLink = magicLink;
    }

    public Date getMagicLinkExpirationDate() {
        return magicLinkExpirationDate;
    }

    public void setMagicLinkExpirationDate(Date magicLinkExpirationDate) {
        this.magicLinkExpirationDate = magicLinkExpirationDate;
    }

    public Integer getPasswordScore() {
        return passwordScore;
    }

    @Override
    public void setPasswordScore(Integer passwordScore) {
        this.passwordScore = passwordScore;
    }

    public Date getLastPasswordScoreAlertDate() {
        return lastPasswordScoreAlertDate;
    }

    public void setLastPasswordScoreAlertDate(Date lastPasswordScoreAlertDate) {
        this.lastPasswordScoreAlertDate = lastPasswordScoreAlertDate;
    }

    @Override
    public String getType() {
        return "teamSingleCard";
    }

    @Override
    public boolean isTeamSingleCard() {
        return true;
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("empty", this.getAccount() == null || !this.getAccount().satisfySoftware((this.getSoftware())));
        res.put("account_information", new JSONObject());
        res.put("team_user_filler_id", this.getTeamUser_filler_test() == null ? -1 : this.getTeamUser_filler_test().getDb_id());
        res.put("stronger_password_asked", this.getAccount() != null && this.getAccount().isStrongerPasswordAsked());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        res.put("magic_link", this.getMagicLink() == null ? "" : this.getMagicLink());
        res.put("magic_link_expiration_date", this.getMagicLinkExpirationDate() == null ? JSONObject.NULL : this.getMagicLinkExpirationDate().getTime());
        res.put("password_score", this.getPasswordScore() == null ? JSONObject.NULL : this.getPasswordScore());
        res.put("last_password_score_alert_date", this.getLastPasswordScoreAlertDate() == null ? JSONObject.NULL : this.getLastPasswordScoreAlertDate().getTime());
        return res;
    }

    @Override
    public void decipher(String symmetric_key) throws HttpServletException {
        if (this.getAccount() == null || this.getAccount().getDeciphered_private_key() != null)
            return;
        this.getAccount().decipher(symmetric_key);
        super.decipher(symmetric_key);
    }

    public void generateMagicLink() {
        this.magicLink = Variables.URL_PATH + "#/fill?card_id=" + this.getDb_id() + "&uuid=" + UUID.randomUUID().toString();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 1);
        this.magicLinkExpirationDate = now.getTime();
    }

    @Override
    public StringBuilder passwordExportCsvString() {
        StringBuilder passwordExportCsvString = super.passwordExportCsvString();
        if (this.getAccount() == null)
            return passwordExportCsvString;
        return passwordExportCsvString
                .append(",")
                .append(",")
                .append(this.getAccount().passwordExportCsvString());
    }

}