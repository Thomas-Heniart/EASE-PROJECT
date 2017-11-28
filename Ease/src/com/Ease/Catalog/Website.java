package com.Ease.Catalog;

import com.Ease.Context.Variables;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.persistence.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "websites")
public class Website {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "login_url")
    private String login_url;

    @Column(name = "website_name")
    private String name;

    @Column(name = "folder")
    private String folder;

    @Column(name = "website_homepage")
    private String website_homepage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "website_attributes_id")
    private WebsiteAttributes websiteAttributes;

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebsiteInformation> websiteInformationList = ConcurrentHashMap.newKeySet();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sso")
    private Sso sso;

    @ManyToMany
    @JoinTable(name = "websiteAndSignInWebsiteMap", joinColumns = @JoinColumn(name = "signIn_website_id"), inverseJoinColumns = @JoinColumn(name = "website_id"))
    private Set<Website> signIn_websites = ConcurrentHashMap.newKeySet();

    @ManyToMany
    @JoinTable(name = "websiteAndSignInWebsiteMap", joinColumns = @JoinColumn(name = "website_id"), inverseJoinColumns = @JoinColumn(name = "signIn_website_id"))
    private Set<Website> connectWith_websites = ConcurrentHashMap.newKeySet();

    @ManyToMany
    @JoinTable(name = "teamAndWebsiteMap", joinColumns = @JoinColumn(name = "website_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebsiteRequest> websiteRequests = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebsiteCredentials> websiteCredentials = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebsiteApp> websiteAppSet = ConcurrentHashMap.newKeySet();


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

    public Set<Website> getSignIn_websites() {
        return signIn_websites;
    }

    public void setSignIn_websites(Set<Website> signIn_websites) {
        this.signIn_websites = signIn_websites;
    }

    public Set<Website> getConnectWith_websites() {
        return connectWith_websites;
    }

    public void setConnectWith_websites(Set<Website> connectWith_websites) {
        this.connectWith_websites = connectWith_websites;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<WebsiteRequest> getWebsiteRequests() {
        return websiteRequests;
    }

    public void setWebsiteRequests(Set<WebsiteRequest> websiteRequests) {
        this.websiteRequests = websiteRequests;
    }

    public Set<WebsiteCredentials> getWebsiteCredentials() {
        return websiteCredentials;
    }

    public void setWebsiteCredentials(Set<WebsiteCredentials> websiteCredentials) {
        this.websiteCredentials = websiteCredentials;
    }

    public Set<WebsiteApp> getWebsiteAppSet() {
        return websiteAppSet;
    }

    public void setWebsiteAppSet(Set<WebsiteApp> websiteAppSet) {
        this.websiteAppSet = websiteAppSet;
    }

    public void addConnectWith_website(Website website) {
        this.getConnectWith_websites().add(website);
    }

    public void addSignIn_website(Website website) {
        this.getSignIn_websites().add(website);
    }

    public void addTeam(Team team) {
        this.getTeams().add(team);
    }

    public void removeTeam(Team team) {
        this.getTeams().remove(team);
    }

    public void addWebsiteRequest(WebsiteRequest websiteRequest) {
        this.getWebsiteRequests().add(websiteRequest);
    }

    public void removeWebsiteRequest(WebsiteRequest websiteRequest) {
        this.getWebsiteRequests().remove(websiteRequest);
    }

    public void addWebsiteCredentials(WebsiteCredentials websiteCredentials) {
        this.getWebsiteCredentials().add(websiteCredentials);
    }

    public void removeWebsiteCredentials(WebsiteCredentials websiteCredentials) {
        this.getWebsiteCredentials().remove(websiteCredentials);
    }

    public void addWebsiteApp(WebsiteApp websiteApp) {
        this.getWebsiteAppSet().add(websiteApp);
    }

    public void removeWebsiteApp(WebsiteApp websiteApp) {
        this.getWebsiteAppSet().remove(websiteApp);
    }

    public String getLogo() {
        return "/resources/websites/" + this.getFolder() + "/logo.png";
    }

    public Map<String, String> getInformationNeeded(JSONObject information) throws HttpServletException {
        Map<String, String> res = new ConcurrentHashMap<>();
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList()) {
            String value = (String) information.get(websiteInformation.getInformation_name());
            if (value == null || value.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter " + websiteInformation.getInformation_name());
            if (value.length() >= 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + websiteInformation.getInformation_name());
            res.put(websiteInformation.getInformation_name(), value);
        }
        return res;
    }

    public Map<String, String> getInformationFromJson(JSONObject information) {
        Map<String, String> res = new ConcurrentHashMap<>();
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList()) {
            String value = (String) information.get(websiteInformation.getInformation_name());
            if (value != null && !value.equals("") && value.length() <= 255)
                res.put(websiteInformation.getInformation_name(), value);
        }
        return res;
    }

    public JSONObject getJson() {
        JSONObject res = this.getSimpleJson();
        JSONObject information = new JSONObject();
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList())
            information.put(websiteInformation.getInformation_name(), websiteInformation.getJson());
        res.put("information", information);
        return res;
    }

    public JSONObject getSimpleJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("logo", this.getLogo());
        res.put("pinneable", this.getWebsiteAttributes().isIntegrated());
        return res;
    }

    public JSONObject getCatalogJson() {
        JSONObject res = this.getJson();
        res.put("landing_url", this.getWebsite_homepage());
        res.put("category_id", this.getCategory() == null ? null : this.getCategory().getDb_id());
        res.put("sso_id", this.getSso() == null ? null : this.getSso().getDb_id());
        JSONArray signIn_websites_ids = new JSONArray();
        for (Website website : this.getSignIn_websites())
            signIn_websites_ids.add(website.getDb_id());
        res.put("signIn_websites", signIn_websites_ids);
        JSONArray connectWith_websites_ids = new JSONArray();
        for (Website website : this.getConnectWith_websites())
            connectWith_websites_ids.add(website.getDb_id());
        res.put("connectWith_websites", connectWith_websites_ids);
        res.put("integration_date", this.getWebsiteAttributes().getAddedDate().getTime());
        return res;
    }

    /* For current version of askInfo */
    public JSONObject getConnectionJson() throws HttpServletException {
        if (!this.getWebsiteAttributes().isIntegrated())
            throw new HttpServletException(HttpStatus.BadRequest, "Please, wait until we integrate this website");
        JSONParser parser = new JSONParser();
        try {
            JSONObject a = (JSONObject) parser.parse(new FileReader(Variables.WEBSITES_FOLDER_PATH + this.getFolder() + "/connect.json"));
            a.put("loginUrl", this.getLogin_url());
            a.put("website_name", this.getName());
            a.put("siteSrc", this.getFolder());
            a.put("img", Variables.URL_PATH + this.getLogo());
            return a;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new HttpServletException(HttpStatus.InternError);
        }
    }

    public Integer getSsoId() {
        return this.getSso() == null ? -1 : this.getSso().getDb_id();
    }

    public JSONObject getRequestJson() {
        JSONObject res = new JSONObject();
        for (WebsiteRequest websiteRequest : this.getWebsiteRequests())
            res = websiteRequest.getJson();
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Website website = (Website) o;

        return db_id.equals(website.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
