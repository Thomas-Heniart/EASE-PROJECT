package com.Ease.Catalog;

import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "websiteCredentials")
public class WebsiteCredentials {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "website_id", nullable = false)
    private Website website;

    @ManyToOne
    @JoinColumn(name = "serverPublicKey_id", nullable = false)
    private ServerPublicKey serverPublicKey;

    public WebsiteCredentials() {

    }

    public WebsiteCredentials(String login, String password, Website website, ServerPublicKey serverPublicKey) {
        this.login = login;
        this.password = password;
        this.website = website;
        this.serverPublicKey = serverPublicKey;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public ServerPublicKey getServerPublicKey() {
        return serverPublicKey;
    }

    public void setServerPublicKey(ServerPublicKey serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("login", this.getLogin());
        res.put("password", this.getPassword());
        res.put("publicKey", this.getServerPublicKey().getPublicKey());
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebsiteCredentials that = (WebsiteCredentials) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
