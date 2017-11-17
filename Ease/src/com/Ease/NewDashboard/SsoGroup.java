package com.Ease.NewDashboard;

import com.Ease.Catalog.Sso;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "ssoGroups")
public class SsoGroup {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @OneToMany(mappedBy = "ssoGroup", cascade = CascadeType.ALL)
    @MapKey(name = "db_id")
    private Map<Integer, SsoApp> ssoAppMap = new ConcurrentHashMap<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sso_id")
    private Sso sso;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    public SsoGroup() {

    }

    public SsoGroup(User user, Sso sso, Account account) {
        this.user = user;
        this.sso = sso;
        this.account = account;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Map<Integer, SsoApp> getSsoAppMap() {
        return ssoAppMap;
    }

    public void setSsoAppMap(Map<Integer, SsoApp> ssoAppMap) {
        this.ssoAppMap = ssoAppMap;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sso getSso() {
        return sso;
    }

    public void setSso(Sso sso) {
        this.sso = sso;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void addSsoApp(SsoApp ssoApp) {
        this.getSsoAppMap().put(ssoApp.getDb_id(), ssoApp);
    }

    public void removeSsoApp(App app) {
        this.getSsoAppMap().remove(app.getDb_id());
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("sso_id", this.getSso().getDb_id());
        res.put("empty", this.getAccount() == null);
        if (this.getAccount() != null)
            res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
    }

    public void decipher(String symmetric_key) throws HttpServletException {
        if (this.getAccount() == null)
            return;
        this.getAccount().decipher(symmetric_key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SsoGroup ssoGroup = (SsoGroup) o;

        return db_id.equals(ssoGroup.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}