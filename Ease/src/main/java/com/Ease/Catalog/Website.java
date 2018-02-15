package com.Ease.Catalog;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Onboarding.OnboardingRoom;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamWebsiteCard;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @Column(name = "logo_version")
    private Integer logo_version = 0;

    @OneToOne(cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "website_attributes_id")
    private WebsiteAttributes websiteAttributes;

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<WebsiteInformation> websiteInformationList = ConcurrentHashMap.newKeySet();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sso")
    private Sso sso;

    @ManyToMany
    @JoinTable(name = "websiteAndSignInWebsiteMap", joinColumns = @JoinColumn(name = "signIn_website_id"), inverseJoinColumns = @JoinColumn(name = "website_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Website> signIn_websites = ConcurrentHashMap.newKeySet();

    @ManyToMany
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "websiteAndSignInWebsiteMap", joinColumns = @JoinColumn(name = "website_id"), inverseJoinColumns = @JoinColumn(name = "signIn_website_id"))
    private Set<Website> connectWith_websites = ConcurrentHashMap.newKeySet();

    @ManyToMany
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "teamAndWebsiteMap", joinColumns = @JoinColumn(name = "website_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebsiteRequest> websiteRequests = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebsiteCredentials> websiteCredentials = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL)
    private Set<WebsiteApp> websiteAppSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL)
    private Set<TeamWebsiteCard> teamWebsiteCardSet = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<WebsiteAlternativeUrl> websiteAlternativeUrlSet = ConcurrentHashMap.newKeySet();

    @ManyToMany(mappedBy = "websiteSet")
    private Set<OnboardingRoom> onboardingRoomSet = ConcurrentHashMap.newKeySet();


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

    public Integer getLogo_version() {
        return logo_version;
    }

    public void setLogo_version(Integer logo_version) {
        this.logo_version = logo_version;
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

    public Set<TeamWebsiteCard> getTeamWebsiteCardSet() {
        return teamWebsiteCardSet;
    }

    public void setTeamWebsiteCardSet(Set<TeamWebsiteCard> teamWebsiteCardSet) {
        this.teamWebsiteCardSet = teamWebsiteCardSet;
    }

    public Set<WebsiteAlternativeUrl> getWebsiteAlternativeUrlSet() {
        return websiteAlternativeUrlSet;
    }

    public void setWebsiteAlternativeUrlSet(Set<WebsiteAlternativeUrl> websiteAlternativeUrlSet) {
        this.websiteAlternativeUrlSet = websiteAlternativeUrlSet;
    }

    public Set<OnboardingRoom> getOnboardingRoomSet() {
        return onboardingRoomSet;
    }

    public void setOnboardingRoomSet(Set<OnboardingRoom> onboardingRoomSet) {
        this.onboardingRoomSet = onboardingRoomSet;
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

    public void addWebsiteAlternativeUrl(WebsiteAlternativeUrl websiteAlternativeUrl) {
        this.getWebsiteAlternativeUrlSet().add(websiteAlternativeUrl);
    }

    public void removeWebsiteAlternativeUrl(WebsiteAlternativeUrl websiteAlternativeUrl) {
        this.getWebsiteAlternativeUrlSet().remove(websiteAlternativeUrl);
    }

    public WebsiteAlternativeUrl getWebsiteAlternativeUrl(String url) {
        return this.getWebsiteAlternativeUrlSet().stream().filter(websiteAlternativeUrl -> websiteAlternativeUrl.getUrl().startsWith(url)).findFirst().orElse(null);
    }

    public void addOnboardingRoom(OnboardingRoom onboardingRoom) {
        this.getOnboardingRoomSet().add(onboardingRoom);
    }

    public void removeOnboardingRoom(OnboardingRoom onboardingRoom) {
        this.getOnboardingRoomSet().remove(onboardingRoom);
    }

    public String getLogo() {
        return this.getWebsiteAttributes().getLogo_url() == null ? ("/resources/websites/" + this.getFolder() + "/logo.png?v=" + this.getLogo_version()) : this.getWebsiteAttributes().getLogo_url();
    }

    public Map<String, String> getInformationNeeded(JSONObject information) throws HttpServletException {
        Map<String, String> res = new ConcurrentHashMap<>();
        System.out.println("Get information needed");
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList()) {
            System.out.println("Get information needed loop");
            String value = information.optString(websiteInformation.getInformation_name());
            if (value == null || value.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter " + websiteInformation.getInformation_name());
            if (value.length() >= 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + websiteInformation.getInformation_name());
            res.put(websiteInformation.getInformation_name(), value);
        }
        return res;
    }

    public Map<String, String> getInformationFromJson(JSONObject information) throws HttpServletException {
        Map<String, String> res = new ConcurrentHashMap<>();
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList()) {
            String value = information.optString(websiteInformation.getInformation_name());
            if (value != null && !value.equals("") && value.length() <= 255)
                res.put(websiteInformation.getInformation_name(), value);
        }
        return res;
    }

    public JSONObject getAllCredentialsFromJson(JSONObject account_information) throws HttpServletException {
        JSONObject res = new JSONObject();
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList()) {
            String value = account_information.optString(websiteInformation.getInformation_name());
            if (value == null || value.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter: " + websiteInformation.getInformation_name());
            res.put(websiteInformation.getInformation_name(), value);
        }
        if (res.length() != this.getWebsiteInformationList().size())
            throw new HttpServletException(HttpStatus.BadRequest, "Some credentials are missing");
        return res;
    }

    public JSONObject getPresentCredentialsFromJson(JSONObject account_information) {
        JSONObject res = new JSONObject();
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList()) {
            String value = account_information.optString(websiteInformation.getInformation_name());
            if (value == null || value.equals(""))
                continue;
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
        res.put("landing_url", this.getWebsite_homepage());
        res.put("login_url", this.getLogin_url());
        return res;
    }

    public JSONObject getCatalogJson() {
        JSONObject res = this.getJson();
        res.put("category_id", this.getCategory() == null ? null : this.getCategory().getDb_id());
        res.put("sso_id", this.getSso() == null ? null : this.getSso().getDb_id());
        JSONArray signIn_websites_ids = new JSONArray();
        for (Website website : this.getSignIn_websites())
            signIn_websites_ids.put(website.getDb_id());
        res.put("signIn_websites", signIn_websites_ids);
        JSONArray connectWith_websites_ids = new JSONArray();
        for (Website website : this.getConnectWith_websites())
            connectWith_websites_ids.put(website.getDb_id());
        res.put("connectWith_websites", connectWith_websites_ids);
        res.put("integration_date", this.getWebsiteAttributes().getAddedDate().getTime());
        return res;
    }

    /* For current version of askInfo */
    public JSONObject getConnectionJson() throws HttpServletException {
        try {
            if (!this.getWebsiteAttributes().isIntegrated())
                throw new HttpServletException(HttpStatus.BadRequest, "Please, wait until we integrate this website");
            StringBuilder stringBuilder = new StringBuilder();
            Files.lines(Paths.get(Variables.WEBSITES_FOLDER_PATH + this.getFolder() + "/connect.json"), StandardCharsets.UTF_8).forEach(stringBuilder::append);
            JSONObject a = new JSONObject(stringBuilder.toString());
            a.put("loginUrl", this.getLogin_url());
            a.put("website_name", this.getName());
            a.put("siteSrc", this.getFolder());
            a.put("img", Variables.URL_PATH + this.getLogo());
            return a;
        } catch (IOException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public Integer getSsoId() {
        return this.getSso() == null ? -1 : this.getSso().getDb_id();
    }

    public JSONObject getRequestJson() {
        JSONObject res = new JSONObject();
        for (WebsiteRequest websiteRequest : this.getWebsiteRequests()) {
            res = websiteRequest.getJson();
            res.put("credentials", !this.getWebsiteCredentials().isEmpty());
        }
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

    public void addTeamWebsiteCard(TeamWebsiteCard teamWebsiteCard) {
        this.getTeamWebsiteCardSet().add(teamWebsiteCard);
    }

    public void addWebsiteAlternativeUrl(String login_url, HibernateQuery hibernateQuery) {
        WebsiteAlternativeUrl websiteAlternativeUrl = new WebsiteAlternativeUrl(this, login_url);
        hibernateQuery.saveOrUpdateObject(websiteAlternativeUrl);
        this.addWebsiteAlternativeUrl(websiteAlternativeUrl);
    }

    public int matchUrl(String subdomain, String domain, String path) throws MalformedURLException {
        try {
            Set<URL> urls = new HashSet<>();
            urls.add(new URL(this.getLogin_url()));
            for (WebsiteAlternativeUrl websiteAlternativeUrl : this.getWebsiteAlternativeUrlSet())
                urls.add(new URL(websiteAlternativeUrl.getUrl()));
            int max_val = 0;
            for (URL aLoginUrl : urls) {
                String login_url_host = aLoginUrl.getHost();
                String login_domain = "";
                String login_path = aLoginUrl.getPath();
                String login_subdomain = "";
                if (login_path.equals("/"))
                    login_path = "";
                String[] login_url_parsed = login_url_host.split("\\.");
                if (login_url_parsed.length == 2) {
                    login_domain += login_url_parsed[0] + "." + login_url_parsed[1];
                } else {
                    login_domain += login_url_parsed[login_url_parsed.length - 2] + "." + login_url_parsed[login_url_parsed.length - 1];
                    for (int i = 0; i < login_url_parsed.length - 2; i++)
                        login_subdomain += login_url_parsed[i];
                    if (login_subdomain.equals("www"))
                        login_subdomain = "";
                }
                if (login_domain.equals(domain)) {
                    if (login_subdomain.equals(subdomain)) {
                        if (login_path.equals(path))
                            return 3;
                        max_val = 2;
                    } else if (max_val < 2)
                        max_val = 1;
                }
            }
            return max_val;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean matchInformationSet(Set<String> informationSet) {
        if (this.getWebsiteInformationList().size() < informationSet.size())
            return false;
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList()) {
            if (!websiteInformation.getInformation_name().equals("login") && !websiteInformation.getInformation_name().equals("password"))
                continue;
            if (!informationSet.contains(websiteInformation.getInformation_name()))
                return false;
        }
        return true;
    }
}
