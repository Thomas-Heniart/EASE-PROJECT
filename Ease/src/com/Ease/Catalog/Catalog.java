package com.Ease.Catalog;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 24/04/2017.
 */
public class Catalog {
    protected Map<Integer, Sso> ssoMap = new ConcurrentHashMap<>();
    protected Map<Integer, Website> websiteMap = new ConcurrentHashMap<>();
    protected Map<Integer, Category> categoryMap = new ConcurrentHashMap<>();

    public Catalog() {
    }

    public void populate() throws GeneralException, HttpServletException {
        HibernateQuery query = new HibernateQuery();
        query.queryString("SELECT w FROM Website w INNER JOIN w.websiteAttributes wa ORDER BY wa.addedDate");
        List<Website> websites = query.list();
        for (Website website : websites)
            this.websiteMap.put(website.getDb_id(), website);

        query.queryString("SELECT s FROM Sso s");
        List<Sso> ssoList = query.list();
        for (Sso sso : ssoList)
            this.ssoMap.put(sso.getDb_id(), sso);
        query.queryString("SELECT c FROM Category c");
        List<Category> categories = query.list();
        for (Category category : categories)
            this.categoryMap.put(category.getDb_id(), category);
        query.commit();
        System.out.println("Catalog websites size: " + websiteMap.size());
        System.out.println("Catalog categories size: " + categoryMap.size());
        System.out.println("Catalog sso size: " + ssoMap.size());
    }
}
