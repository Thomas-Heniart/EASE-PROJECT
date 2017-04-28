package com.Ease.Website;

import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.ServletManagerHibernate;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    protected String loginUrl;

    @Column(name = "website_name")
    protected String websiteName;

    @Column(name = "folder")
    protected String folder;

    @OneToOne
    @JoinColumn(name = "sso")
    protected Sso sso;

    @Column(name = "noLogin")
    protected Boolean noLogin;

    @Column(name = "website_homepage")
    protected String website_homepage;

    @Column(name = "ratio")
    protected Integer ratio;

    @Column(name = "position")
    protected Integer position;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "website_attributes_id")
    protected WebsiteAttributes websiteAttributes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id")
    protected List<WebsiteInformation> websiteInformationList;

    public Website(String loginUrl, String websiteName, String folder, Sso sso, Boolean noLogin, String website_homepage, Integer ratio, Integer position, WebsiteAttributes websiteAttributes) {
        this.loginUrl = loginUrl;
        this.websiteName = websiteName;
        this.folder = folder;
        this.sso = sso;
        this.noLogin = noLogin;
        this.website_homepage = website_homepage;
        this.ratio = ratio;
        this.position = position;
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

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Sso getSso() {
        return sso;
    }

    public void setSso(Sso sso) {
        this.sso = sso;
    }

    public Boolean getNoLogin() {
        return noLogin;
    }

    public void setNoLogin(Boolean noLogin) {
        this.noLogin = noLogin;
    }

    public String getWebsite_homepage() {
        return website_homepage;
    }

    public void setWebsite_homepage(String website_homepage) {
        this.website_homepage = website_homepage;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public WebsiteAttributes getWebsiteAttributes() {
        return websiteAttributes;
    }

    public void setWebsiteAttributes(WebsiteAttributes websiteAttributes) {
        this.websiteAttributes = websiteAttributes;
    }

    public List<WebsiteInformation> getWebsiteInformationList() {
        return websiteInformationList;
    }

    public void setWebsiteInformationList(List<WebsiteInformation> websiteInformationList) {
        this.websiteInformationList = websiteInformationList;
    }

    public Map<String, String> getNeededInformation(ServletManagerHibernate sm) throws GeneralException {
        Map<String, String> infos = new HashMap<String, String>();
        for (WebsiteInformation info : this.websiteInformationList) {
            String info_name = info.getInformation_name();
            String value = sm.getServletParam(info_name, false);
            if (value == null || value.isEmpty()) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong info: " + info_name + ".");
            }
            if (info_name.equals("password")) {
                //Mettre un param keyDate dans le post si besoin de decrypter en RSA. Correspond à la private key RSA,
                String keyDate = sm.getServletParam("keyDate", true);
                if (keyDate != null && !keyDate.equals("")) {
                    value = RSA.Decrypt(value, Integer.parseInt(keyDate));
                }
                value = sm.getUser().getKeys().encrypt(value);
            }
            infos.put(info_name, value);
        }
        return infos;
    }

    public List<String> getWebsiteInformationNames() {
        List<String> websiteInformationNames = new LinkedList<String>();
        for (WebsiteInformation websiteInformation : this.getWebsiteInformationList())
            websiteInformationNames.add(websiteInformation.getInformation_name());
        return websiteInformationNames;
    }
}
