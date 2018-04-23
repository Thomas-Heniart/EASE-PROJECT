package com.Ease.NewDashboard;

import com.Ease.Catalog.Software;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "softwareApps")
@PrimaryKeyJoinColumn(name = "id")
public class SoftwareApp extends App {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "software_id")
    private Software software;

    public SoftwareApp() {

    }

    public SoftwareApp(AppInformation appInformation, Software software) {
        super(appInformation);
        this.software = software;
    }

    public SoftwareApp(AppInformation appInformation, Software software, Account account) {
        super(appInformation);
        this.account = account;
        this.software = software;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    @Override
    public String getLogo() {
        return this.getSoftware().getLogo();
    }

    @Override
    public String getType() {
        return "softwareApp";
    }

    @Override
    public boolean isSoftwareApp() {
        return true;
    }

    @Override
    public void decipher(String symmetric_key, String team_key) throws HttpServletException {
        if (this.getAccount() == null)
            return;
        this.getAccount().decipher(team_key == null ? symmetric_key : team_key);
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("software", this.getSoftware().getJson());
        res.put("empty", this.isEmpty());
        res.put("account_information", new JSONObject());
        res.put("stronger_password_asked", this.getAccount() != null && this.getAccount().isStrongerPasswordAsked());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("password_reminder_interval", this.getAccount().getReminder_interval());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
    }

    @Override
    public JSONObject getRestJson() {
        JSONObject res = super.getRestJson();
        res.put("software", this.getSoftware().getJson());
        res.put("empty", this.isEmpty());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("password_reminder_interval", this.getAccount().getReminder_interval());
        res.put("account_information", this.getAccount().getJson());
        return res;
    }
}