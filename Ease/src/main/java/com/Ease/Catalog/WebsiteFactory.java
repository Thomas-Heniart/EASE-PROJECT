package com.Ease.Catalog;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.File.FileUtils;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class WebsiteFactory {
    private static WebsiteFactory ourInstance = new WebsiteFactory();

    public static WebsiteFactory getInstance() {
        return ourInstance;
    }

    private WebsiteFactory() {
    }

    public Website createWebsiteAndLogo(String email, String url, String name, String img_url, JSONObject connection_information, HibernateQuery hibernateQuery) throws HttpServletException {
        String folder = url.split("//")[1].split("/")[0].replaceAll("\\W", "_");
        WebsiteAttributes websiteAttributes = new WebsiteAttributes(true);
        Website website = new Website(url, folder, folder, url, websiteAttributes);
        if (img_url == null || img_url.equals("") || !img_url.startsWith("https://logo.clearbit.com/")) {
            FileUtils.createWebsiteLogo(folder, name);
        } else {
            if (img_url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid img_url");
            websiteAttributes.setLogo_url(img_url);
        }
        Set<WebsiteInformation> websiteInformationSet = new HashSet<>();
        for (Object object : connection_information.keySet()) {
            String key = (String) object;
            JSONObject information = connection_information.getJSONObject(key);
            WebsiteInformation websiteInformation = new WebsiteInformation(key, information.getString("type"), information.getInt("priority"), information.getString("placeholder"), "", website);
            websiteInformationSet.add(websiteInformation);
        }
        hibernateQuery.saveOrUpdateObject(websiteAttributes);
        hibernateQuery.saveOrUpdateObject(website);
        website.addWebsiteRequest(this.createWebsiteRequest(email, website, hibernateQuery));
        return website;
    }

    public WebsiteRequest createWebsiteRequest(String email, Website website, HibernateQuery hibernateQuery) {
        WebsiteRequest websiteRequest = new WebsiteRequest(website.getLogin_url(), email, website);
        hibernateQuery.saveOrUpdateObject(websiteRequest);
        return websiteRequest;
    }
}
