package com.Ease.Catalog;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.File.FileUtils;
import com.Ease.Utils.HttpServletException;
import org.json.JSONObject;

public class SoftwareFactory {
    private static SoftwareFactory ourInstance = new SoftwareFactory();

    public static SoftwareFactory getInstance() {
        return ourInstance;
    }

    private SoftwareFactory() {
    }

    public Software createSoftwareAndLogo(String name, String folder, String logo_url, JSONObject connectionInformation, HibernateQuery hibernateQuery) throws HttpServletException {
        Software software = new Software(name, folder);
        if (logo_url != null && logo_url.startsWith("https://logo.clearbit.com/"))
            software.setLogo_url(logo_url);
        else
            FileUtils.createSoftwareLogo(folder, name);
        hibernateQuery.saveOrUpdateObject(software);
        for (Object object : connectionInformation.keySet()) {
            String key = String.valueOf(object);
            JSONObject properties = connectionInformation.getJSONObject(key);
            SoftwareConnectionInformation softwareConnectionInformation = new SoftwareConnectionInformation(software, key, properties.getString("information_type"), properties.getInt("priority"));
            hibernateQuery.saveOrUpdateObject(softwareConnectionInformation);
            software.addSoftwareConnectionInformation(softwareConnectionInformation);
        }
        return software;
    }
}