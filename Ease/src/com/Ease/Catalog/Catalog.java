package com.Ease.Catalog;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONArray;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by thomas on 24/04/2017.
 */
public class Catalog {
    public Catalog() {
    }

    public Website getWebsiteWithId(Integer id, HibernateQuery hibernateQuery) throws HttpServletException {
        hibernateQuery.queryString("SELECT w FROM Website w WHERE w.db_id = :id");
        hibernateQuery.setParameter("id", id);
        Website website = (Website) hibernateQuery.getSingleResult();
        if (website == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No websites corresponding to this id.");
        return website;
    }

    public Collection<Website> getWebsites(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT w FROM Website w ORDER BY w.db_id DESC");
        return hibernateQuery.list();
    }

    public JSONArray getCatalogWebsites(HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites(hibernateQuery))
            res.add(website.getCatalogJson());
        return res;
    }

    public JSONArray getPublicCatalogWebsites(Set<Team> teams, HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites(hibernateQuery)) {
            if ((!website.getWebsiteAttributes().isPublic_website() && !website.getTeams().isEmpty() && website.getTeams().stream().noneMatch(teams::contains)) || !website.getWebsiteAttributes().isIntegrated())
                continue;
            res.add(website.getCatalogJson());
        }
        return res;
    }

    public Website getWebsiteWithName(String name, HibernateQuery hibernateQuery) throws HttpServletException {
        String name_lowercase = name.toLowerCase();
        for (Website website : this.getWebsites(hibernateQuery)) {
            if (website.getName().toLowerCase().equals(name_lowercase))
                return website;
        }
        throw new HttpServletException(HttpStatus.BadRequest, "This website does not exist");
    }

    public JSONArray getCategoriesJson(HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Category category : this.getCategories(hibernateQuery))
            res.add(category.getJson());
        return res;
    }

    public List<Category> getCategories(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT c FROM Category c");
        return hibernateQuery.list();
    }

    public JSONArray getSsoListJson(HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Sso sso : this.getSsoList(hibernateQuery))
            res.add(sso.getJson());
        return res;
    }

    private Collection<Sso> getSsoList(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT s FROM Sso s");
        return hibernateQuery.list();
    }

    public Sso getSsoWithId(Integer id, HibernateQuery hibernateQuery) throws HttpServletException {
        hibernateQuery.queryString("SELECT s FROM Sso s where s.db_id = :id");
        hibernateQuery.setParameter("id", id);
        Sso sso = (Sso) hibernateQuery.getSingleResult();
        if (sso == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This sso does not exist");
        return sso;
    }

    public Category getCategoryWithId(Integer id, HibernateQuery hibernateQuery) throws HttpServletException {
        hibernateQuery.queryString("SELECT c FROM Category c WHERE c.db_id = :id");
        hibernateQuery.setParameter("id", id);
        Category category = (Category) hibernateQuery.getSingleResult();
        if (category == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such category");
        return category;
    }

    public Website getPublicWebsiteWithId(Integer id, HibernateQuery hibernateQuery, Set<Team> teams) throws HttpServletException {
        Website website = this.getWebsiteWithId(id, hibernateQuery);
        if (!website.getWebsiteAttributes().isPublic_website() && website.getTeams().stream().noneMatch(teams::contains))
            throw new HttpServletException(HttpStatus.BadRequest, "This website is not public");
        return website;
    }

    public JSONArray getWebsiteRequests(HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites(hibernateQuery)) {
            if (website.getWebsiteRequests().isEmpty())
                continue;
            res.add(website.getRequestJson());
        }
        return res;
    }
}
