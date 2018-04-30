package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
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
@Table(name = "teamSingleCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamSingleCard extends TeamWebsiteCard {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "teamUser_filler_id")
    private TeamUser teamUser_filler;

    @Column(name = "magicLink")
    private String magicLink;

    @Column(name = "magicLinkExpirationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date magicLinkExpirationDate;

    public TeamSingleCard() {

    }

    public TeamSingleCard(String name, Team team, Channel channel, String description, Website website, Integer password_reminder_interval, Account account, TeamUser teamUser_filler) {
        super(name, team, channel, description, website, password_reminder_interval);
        this.account = account;
        this.teamUser_filler = teamUser_filler;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public TeamUser getTeamUser_filler() {
        return teamUser_filler;
    }

    public void setTeamUser_filler(TeamUser teamUser_filler) {
        this.teamUser_filler = teamUser_filler;
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

    public void decipherAccount(String symmetric_key) throws HttpServletException {
        if (this.getAccount().getDeciphered_private_key() != null)
            return;
        this.getAccount().decipher(symmetric_key);
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("empty", this.getAccount() == null || !this.getAccount().satisfyWebsite((this.getWebsite())));
        res.put("account_information", new JSONObject());
        res.put("team_user_filler_id", this.getTeamUser_filler() == null ? -1 : this.getTeamUser_filler().getDb_id());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        res.put("magic_link", this.getMagicLink() == null ? "" : this.getMagicLink());
        res.put("magic_link_expiration_date", this.getMagicLinkExpirationDate() == null ? JSONObject.NULL : this.getMagicLinkExpirationDate().getTime());
        return res;
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
                .append(this.getWebsite().getLogin_url())
                .append(",")
                .append(this.getAccount().passwordExportCsvString());
    }
}