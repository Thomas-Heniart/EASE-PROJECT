package com.Ease.Catalog;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.File.FileUtils;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;

import java.util.HashSet;
import java.util.Set;

public class WebsiteFactory {
    private static WebsiteFactory ourInstance = new WebsiteFactory();

    public static WebsiteFactory getInstance() {
        return ourInstance;
    }

    private WebsiteFactory() {
    }

    public Website createWebsiteAndLogo(String url, String name, String img_url, HibernateQuery hibernateQuery) throws HttpServletException {
        String folder = url.split("//")[1].split("/")[0].replaceAll("\\.", "_");
        WebsiteAttributes websiteAttributes = new WebsiteAttributes(true);
        Website website = new Website(url, folder, folder, url, websiteAttributes);
        if (img_url == null || img_url.equals("") || !img_url.startsWith("https://logo.clearbit.com/")) {
            FileUtils.createWebsiteLogo(folder, name);
        } else {
            if (img_url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid img_url");
            websiteAttributes.setLogo_url(img_url);
        }
        WebsiteInformation loginInformation = new WebsiteInformation("login", "text", 0, "Login", "fa-user-o", website);
        WebsiteInformation passwordInformation = new WebsiteInformation("password", "password", 1, "Password", "fa-lock", website);
        Set<WebsiteInformation> websiteInformationSet = new HashSet<>();
        websiteInformationSet.add(loginInformation);
        websiteInformationSet.add(passwordInformation);
        website.setWebsiteInformationList(websiteInformationSet);
        hibernateQuery.saveOrUpdateObject(websiteAttributes);
        hibernateQuery.saveOrUpdateObject(website);
        return website;
    }
}
