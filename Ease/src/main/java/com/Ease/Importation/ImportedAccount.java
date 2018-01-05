package com.Ease.Importation;

import com.Ease.Catalog.Website;
import com.Ease.User.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "IMPORTED_ACCOUNT")
public class ImportedAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @OneToOne
    @JoinColumn(name = "website_id")
    private Website website;

    @Column(name = "name")
    private String name;

    @JsonManagedReference
    @JsonProperty("account_information")
    @OneToMany(mappedBy = "importedAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "name")
    private Map<String, ImportedAccountInformation> importedAccountInformationMap = new ConcurrentHashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public ImportedAccount() {

    }

    public ImportedAccount(String url, Website website, String name, User user) {
        this.url = url;
        this.website = website;
        this.name = name;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, ImportedAccountInformation> getImportedAccountInformationMap() {
        return importedAccountInformationMap;
    }

    public void setImportedAccountInformationMap(Map<String, ImportedAccountInformation> importedAccountInformationMap) {
        this.importedAccountInformationMap = importedAccountInformationMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImportedAccount that = (ImportedAccount) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("url", this.getUrl() == null ? "" : this.getUrl());
        res.put("website_id", this.getWebsite() == null ? -1 : this.getWebsite().getDb_id());
        res.put("name", this.getName());
        JSONObject account_information = new JSONObject();
        this.getImportedAccountInformationMap().forEach((s, importedAccountInformation) -> account_information.put(s, importedAccountInformation.getJson()));
        res.put("account_information", account_information);
        return res;
    }

    public ImportedAccountInformation getImportedAccountInformation(String name) {
        return this.getImportedAccountInformationMap().get(name);
    }

    public void removeImportedAccountInformation(String name) {
        this.getImportedAccountInformationMap().remove(name);
    }

    public void addImportedAccountInformation(ImportedAccountInformation importedAccountInformation) {
        this.getImportedAccountInformationMap().put(importedAccountInformation.getName(), importedAccountInformation);
    }
}
