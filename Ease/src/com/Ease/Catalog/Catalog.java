package com.Ease.Catalog;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONArray;

import java.util.Collection;
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

    public Map<Integer, Sso> getSsoMap() {
        return ssoMap;
    }

    public Map<Integer, Website> getWebsiteMap() {
        return websiteMap;
    }

    public Map<Integer, Category> getCategoryMap() {
        return categoryMap;
    }

    public Website getWebsiteWithId(Integer id) throws HttpServletException {
        Website website = this.getWebsiteMap().get(id);
        if (website == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No websites corresponding to this id.");
        return website;
    }

    public Collection<Website> getWebsites() {
        return this.getWebsiteMap().values();
    }

    public JSONArray getCatalogWebsites() {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites())
            res.add(website.getCatalogJson());
        return res;
    }

    public JSONArray getPublicCatalogWebsites() {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites()) {
            if (!website.getWebsiteAttributes().isPublic_website() || !website.getWebsiteAttributes().isIntegrated())
                continue;
            res.add(website.getCatalogJson());
        }
        return res;
    }

    public Website getWebsiteWithName(String name) throws HttpServletException {
        String name_lowercase = name.toLowerCase();
        for (Website website : this.getWebsites()) {
            if (website.getName().toLowerCase().equals(name_lowercase))
                return website;
        }
        throw new HttpServletException(HttpStatus.BadRequest, "This website does not exist");
    }

    public JSONArray getCategoriesJson() {
        JSONArray res = new JSONArray();
        for (Category category : this.getCategories())
            res.add(category.getJson());
        return res;
    }

    private Collection<Category> getCategories() {
        return this.getCategoryMap().values();
    }

    public JSONArray getSsoListJson() {
        JSONArray res = new JSONArray();
        for (Sso sso : this.getSsoList())
            res.add(sso.getJson());
        return res;
    }

    private Collection<Sso> getSsoList() {
        return this.getSsoMap().values();
    }

    public Sso getSsoWithId(Integer id) throws HttpServletException {
        Sso sso = this.getSsoMap().get(id);
        if (sso == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This sso does not exist");
        return sso;
    }

    public void removeWebsite(Integer id) {
        this.getWebsiteMap().remove(id);
    }

    public Category getCategoryWithId(Integer id) throws HttpServletException {
        Category category = this.getCategoryMap().get(id);
        if (category == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such category");
        return category;
    }
}
