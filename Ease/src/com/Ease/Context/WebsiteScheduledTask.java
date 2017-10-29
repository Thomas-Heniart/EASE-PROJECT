package com.Ease.Context;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.HttpServletException;

import java.util.List;
import java.util.TimerTask;

public class WebsiteScheduledTask extends TimerTask {

    private Catalog catalog;

    public WebsiteScheduledTask(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void run() {
        try {
            HibernateQuery hibernateQuery = new HibernateQuery();
            hibernateQuery.queryString("SELECT websites.id FROM websites JOIN websiteAttributes ON (websites.website_attributes_id = websiteAttributes.id) WHERE addedDate <= CURRENT_TIMESTAMP - INTERVAL 4 DAY AND new = 1;");
            List<Object> rs = hibernateQuery.list();
            rs.forEach((Object obj) -> {
                Integer id = (Integer) obj;
                Website website = null;
                try {
                    website = catalog.getWebsiteWithId(id);
                    System.out.println(website.getName() + " not new anymore");
                    website.getWebsiteAttributes().setNew_website(false);
                    hibernateQuery.saveOrUpdateObject(website.getWebsiteAttributes());
                } catch (HttpServletException e) {
                    e.printStackTrace();
                }
            });
            hibernateQuery.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
