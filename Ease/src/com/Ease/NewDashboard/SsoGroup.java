package com.Ease.NewDashboard;

import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "ssoGroups")
public class SsoGroup {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ssoGroup", cascade = CascadeType.ALL)
    @MapKeyJoinColumn(name = "db_id")
    private Map<Integer, SsoApp> ssoAppMap;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;


    public SsoGroup() {

    }

    public SsoGroup(Account account) {
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("empty", this.getAccount() == null);
        if (this.getAccount() != null)
            res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
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