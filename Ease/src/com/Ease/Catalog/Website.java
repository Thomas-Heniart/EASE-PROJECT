package com.Ease.Catalog;

import com.Ease.Context.Variables;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.persistence.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    public String getLogo() {
        return "/resources/websites/" + this.getFolder() + "/logo.png";
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
        /* loginWith part */

        /* end loginWith part */
        res.put("integration_date", this.getWebsiteAttributes().getAddedDate().getTime());
        return res;
    }

    /* @TODO to be replaced ASAP do it with a JSON*/

    public Map<String, String> getNeededInfos(ServletManager sm) throws HttpServletException {
        try {
            Map<String, String> res = new HashMap<>();
            for (WebsiteInformation information : this.getWebsiteInformationList()) {
                String info_name = information.getInformation_name();
                String value = sm.getServletParam(info_name, false);
                if (value == null || value.isEmpty())
                    throw new HttpServletException(HttpStatus.BadRequest, "Wrong info: " + info_name + ".");
                if (info_name.equals("password")) {
                    //Mettre un param keyDate dans le post si besoin de decrypter en RSA. Correspond à la private key RSA,
                    String keyDate = sm.getServletParam("keyDate", true);
                    if (keyDate != null && !keyDate.equals(""))
                        value = RSA.Decrypt(value, Integer.parseInt(keyDate));
                }
                res.put(info_name, value);
            }
            return res;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, "Oops, please contact us at thomas@ease.space");
        }
    }

    public Map<String, String> getNeededInfosForEdition(ServletManager sm) {
        return null;
    }

    /* For current version of askInfo */
    public JSONObject getConnectionJson() throws HttpServletException {
        JSONParser parser = new JSONParser();
        try {
            JSONObject a = (JSONObject) parser.parse(new FileReader(Variables.PROJECT_PATH + "/resources/websites/" + this.getFolder() + "/connect.json"));
            a.put("loginUrl", this.getLogin_url());
            a.put("website_name", this.getName());
            a.put("siteSrc", this.getFolder());
            a.put("img", Variables.URL_PATH + this.getLogo());
            return a;
        } catch (IOException | ParseException e) {
            throw new HttpServletException(HttpStatus.InternError, "Sorry, fuck you");
        }
    }

    public Integer getSsoId() {
        return  this.getSso() == null ? -1 : this.getSso().getDb_id();
    }
}
