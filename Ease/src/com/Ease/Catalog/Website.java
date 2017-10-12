package com.Ease.Catalog;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "Websites")
public class Website {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "login_url")
    protected String login_url;

    @Column(name = "website_name")
    protected String name;

    @Column(name = "folder")
    protected String folder;

    @Column(name = "website_homepage")
    protected String website_homepage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "website_attributes_id")
    protected WebsiteAttributes websiteAttributes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id")
    protected Set<WebsiteInformation> websiteInformationList = ConcurrentHashMap.newKeySet();

    @ManyToOne
    @JoinColumn(name = "category_id")
    protected Category category;

    @ManyToOne
    @JoinColumn(name = "sso")
    protected Sso sso;

    public Website(String login_url, String name, String folder, String website_homepage, WebsiteAttributes websiteAttributes) {
        this.login_url = login_url;
        this.name = name;
        this.folder = folder;
        this.website_homepage = website_homepage;
        this.websiteAttributes = websiteAttributes;
    }

    public Website() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getWebsite_homepage() {
        return website_homepage;
    }

    public void setWebsite_homepage(String website_homepage) {
        this.website_homepage = website_homepage;
    }

    public WebsiteAttributes getWebsiteAttributes() {
        return websiteAttributes;
    }

    public void setWebsiteAttributes(WebsiteAttributes websiteAttributes) {
        this.websiteAttributes = websiteAttributes;
    }

    public Set<WebsiteInformation> getWebsiteInformationList() {
        return websiteInformationList;
    }

    public void setWebsiteInformationList(Set<WebsiteInformation> websiteInformationList) {
        this.websiteInformationList = websiteInformationList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Sso getSso() {
        return sso;
    }

    public void setSso(Sso sso) {
        this.sso = sso;
    }

}
