package com.Ease.Context;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.HttpServletException;

import java.util.List;
import java.util.TimerTask;

public class WebsiteScheduledTask extends TimerTask {

    private Catalog catalog;

    WebsiteScheduledTask(Catalog catalog) {
        super();
        this.catalog = catalog;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT websites.id FROM websites JOIN websiteAttributes ON (websites.website_attributes_id = websiteAttributes.id) WHERE addedDate <= CURRENT_TIMESTAMP - INTERVAL 4 DAY AND new = 1;");
            List<Object> rs = hibernateQuery.list();
            rs.forEach((Object obj) -> {
                Integer id = (Integer) obj;
                Website website;
                try {
                    website = catalog.getWebsiteWithId(id, hibernateQuery);
                    System.out.println(website.getName() + " not new anymore");
                    website.getWebsiteAttributes().setNew_website(false);
                    hibernateQuery.saveOrUpdateObject(website.getWebsiteAttributes());
                } catch (HttpServletException e) {
                    e.printStackTrace();
                }
            });
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}
