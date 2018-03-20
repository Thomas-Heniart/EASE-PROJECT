package com.Ease.Update;

import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EASE_UPDATE")
public class Update {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id")
    private Website website;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_card_id")
    private TeamCard teamCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_user_id")
    private TeamUser teamUser;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "update_account_id")
    private UpdateAccount updateAccount;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    public Update() {

    }

    public Update(User user, Website website) {
        this.user = user;
        this.website = website;
    }

    public Update(User user, Website website, App app) {
        this.user = user;
        this.website = website;
        this.app = app;
    }

    public Update(User user, Website website, App app, TeamCard teamCard) {
        this.user = user;
        this.website = website;
        this.app = app;
        this.teamCard = teamCard;
    }

    public Update(User user, Website website, TeamCard teamCard, TeamUser teamUser) {
        this.user = user;
        this.website = website;
        this.teamCard = teamCard;
        this.teamUser = teamUser;
    }

    public Update(User user, String url) {
        this.user = user;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public TeamCard getTeamCard() {
        return teamCard;
    }

    public void setTeamCard(TeamCard teamCard) {
        this.teamCard = teamCard;
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public void setTeamUser(TeamUser teamUser) {
        this.teamUser = teamUser;
    }

    public UpdateAccount getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(UpdateAccount updateAccount) {
        this.updateAccount = updateAccount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void decipher(String private_key) throws HttpServletException {
        this.getUpdateAccount().decipher(private_key);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("url", this.getUrl() == null ? JSONObject.NULL : this.getUrl());
        res.put("website_id", this.getWebsite() == null ? -1 : this.getWebsite().getDb_id());
        res.put("app_id", this.getApp() == null ? -1 : this.getApp().getDb_id());
        res.put("team_card_id", this.getTeamCard() == null ? -1 : this.getTeamCard().getDb_id());
        res.put("team_id", this.getTeamCard() == null ? -1 : this.getTeamCard().getTeam().getDb_id());
        res.put("team_user_id", this.getTeamUser() == null ? -1 : this.getTeamUser().getDb_id());
        res.put("account_information", this.getUpdateAccount() == null ? JSONObject.NULL : this.getUpdateAccount().getJson());
        res.put("type", this.getType());
        return res;
    }

    public boolean accountMatch(JSONObject account_information) {
        return this.getUpdateAccount().match(account_information);
    }

    public void edit(JSONObject account_information, String publicKey) throws HttpServletException {
        this.getUpdateAccount().edit(account_information, publicKey);
    }

    public boolean passwordMatch(JSONObject account_information) {
        return this.getUpdateAccount().passwordMatch(account_information);
    }

    public JSONObject getAccountInformation() {
        return this.getUpdateAccount().getAccountInformation();
    }

    private String getType() {
        if (this.getApp() == null && this.getTeamCard() == null)
            return "NewAccountUpdate";
        if (this.getApp() != null && this.getApp().isEmpty())
            return "AccountUpdate";
        return "PasswordUpdate";
    }
}