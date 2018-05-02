package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSoftwareCard;
import com.Ease.Team.TeamCard.TeamWebsiteCard;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamEnterpriseCardReceivers")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamEnterpriseCardReceiver extends TeamCardReceiver {

    @Column(name = "password_score")
    private Integer passwordScore;

    @Column(name = "lastPasswordScoreAlertDate")
    private Date lastPasswordScoreAlertDate;

    public TeamEnterpriseCardReceiver() {

    }

    public TeamEnterpriseCardReceiver(App app, TeamCard teamCard, TeamUser teamUser) {
        super(app, teamCard, teamUser);
    }

    public Integer getPasswordScore() {
        return passwordScore;
    }

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
        return "teamEnterpriseApp";
    }

    @Override
    public JSONObject getCardJson() {
        JSONObject res = super.getCardJson();
        Account account = this.getApp().getAccount();
        res.put("account_information", new JSONObject());
        res.put("empty", this.isEmpty());
        res.put("password_score", this.getPasswordScore() == null ? JSONObject.NULL : this.getPasswordScore());
        res.put("last_password_score_alert_date", this.getLastPasswordScoreAlertDate() == null ? JSONObject.NULL : this.getLastPasswordScoreAlertDate().getTime());
        res.put("stronger_password_asked", account != null && account.isStrongerPasswordAsked());
        if (account == null)
            return res;
        res.put("account_information", account.getJsonWithoutPassword());
        res.put("last_update_date", account.getLast_update().getTime());
        return res;
    }

    private boolean isEmpty() {
        Account account = this.getApp().getAccount();
        if (account == null)
            return true;
        TeamCard teamCard = this.getTeamCard();
        if (teamCard.isTeamWebsiteCard()) {
            TeamWebsiteCard teamWebsiteCard = (TeamWebsiteCard) teamCard;
            return !account.satisfyWebsite(teamWebsiteCard.getWebsite());
        } else if (teamCard.isTeamSoftwareCard()) {
            TeamSoftwareCard teamSoftwareCard = (TeamSoftwareCard) teamCard;
            return !account.satisfySoftware(teamSoftwareCard.getSoftware());
        }
        return false;
    }

    public void decipher(String decipheredTeamKey) throws HttpServletException {
        App app = this.getApp();
        if (app.getAccount() == null || app.getAccount().getDeciphered_private_key() != null)
            return;
        app.getAccount().decipher(decipheredTeamKey);
    }

    public void calculatePasswordScore() throws NoSuchAlgorithmException {
        if (this.getApp() == null || this.getApp().getAccount() == null)
            return;
        this.setPasswordScore(this.getApp().getAccount().calculatePasswordScore());
    }
}