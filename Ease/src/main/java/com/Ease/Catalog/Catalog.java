package com.Ease.Catalog;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by thomas on 24/04/2017.
 */
public class Catalog {
    public Catalog() {
    }

    public Website getWebsiteWithId(Integer id, HibernateQuery hibernateQuery) throws HttpServletException {
        Website website = (Website) hibernateQuery.get(Website.class, id);
        if (website == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No websites corresponding to this id.");
        return website;
    }

    public Website optWebsiteWithId(Integer id, HibernateQuery hibernateQuery) {
        return (Website) hibernateQuery.get(Website.class, id);
    }

    public Collection<Website> getWebsites(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT w FROM Website w ORDER BY w.db_id DESC");
        hibernateQuery.cacheQuery();
        return hibernateQuery.list();
    }

    public JSONArray getCatalogWebsites(HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites(hibernateQuery))
            res.put(website.getCatalogJson());
        return res;
    }

    public JSONArray getPublicCatalogWebsites(Set<Team> teams, HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites(hibernateQuery)) {
            if ((!website.getWebsiteAttributes().isPublic_website() && (website.getTeams().isEmpty() || website.getTeams().stream().noneMatch(teams::contains))) || !website.getWebsiteAttributes().isIntegrated())
                continue;
            res.put(website.getCatalogJson());
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
            res.put(category.getJson());
        return res;
    }

    public List<Category> getCategories(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT c FROM Category c");
        return hibernateQuery.list();
    }

    public JSONArray getSsoListJson(HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Sso sso : this.getSsoList(hibernateQuery))
            res.put(sso.getJson());
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
        if ((!website.getWebsiteAttributes().isPublic_website() && (website.getTeams().isEmpty() || website.getTeams().stream().noneMatch(teams::contains))) || !website.getWebsiteAttributes().isIntegrated())
            throw new HttpServletException(HttpStatus.BadRequest, "This website is not public");
        return website;
    }

    public Website optPublicWebsiteWithId(Integer id, HibernateQuery hibernateQuery, Set<Team> teams) throws HttpServletException {
        Website website = this.optWebsiteWithId(id, hibernateQuery);
        if (website == null)
            return null;
        if ((!website.getWebsiteAttributes().isPublic_website() && (website.getTeams().isEmpty() || website.getTeams().stream().noneMatch(teams::contains))) || !website.getWebsiteAttributes().isIntegrated())
            throw new HttpServletException(HttpStatus.BadRequest, "This website is not public");
        return website;
    }

    public JSONArray getWebsiteRequests(HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        for (Website website : this.getWebsites(hibernateQuery)) {
            if (website.getWebsiteRequests().isEmpty())
                continue;
            res.put(website.getRequestJson());
        }
        return res;
    }

    public Website getWebsiteWithUrl(String url, JSONObject connection_information, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT w FROM Website w LEFT JOIN w.websiteAlternativeUrlSet AS url WHERE w.website_homepage LIKE CONCAT(:url, '%') OR w.login_url LIKE CONCAT(:url, '%') OR url.url LIKE CONCAT(:url, '%') ORDER BY w.db_id ASC");
        hibernateQuery.setParameter("url", url);
        Website website = (Website) hibernateQuery.getSingleResult();
        if (website == null || website.getFolder().equals("undefined") || website.getWebsiteInformationList().size() != connection_information.length())
            return null;
        for (WebsiteInformation websiteInformation : website.getWebsiteInformationList()) {
            JSONObject tmp = connection_information.getJSONObject(websiteInformation.getInformation_name());
            if (tmp == null)
                return null;
        }
        return website;
    }

    public Software getSoftwareWithFolderOrName(String name, String folder, JSONObject connection_information, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT s FROM Software s WHERE s.name = :name OR s.folder = :folder");
        hibernateQuery.setParameter("name", name);
        hibernateQuery.setParameter("folder", folder);
        Software software = (Software) hibernateQuery.getSingleResult();
        if (software == null || software.getSoftwareConnectionInformationSet().size() != connection_information.length())
            return null;
        for (SoftwareConnectionInformation softwareConnectionInformation : software.getSoftwareConnectionInformationSet()) {
            JSONObject tmp = connection_information.getJSONObject(softwareConnectionInformation.getInformation_name());
            if (tmp == null)
                return null;
        }
        return software;
    }

    public Website getPublicWebsiteWithUrl(String url, Set<String> information_names, HibernateQuery hibernateQuery) throws HttpServletException {
        try {
            URL aUrl = new URL(url);
            String[] url_parsed = aUrl.getHost().split("\\.");
            String subdomain = "";
            String domain = "";
            String path = aUrl.getPath();
            if (path.equals("/"))
                path = "";
            if (url_parsed.length < 2)
                return null;
            if (url_parsed.length == 2)
                domain += url_parsed[0] + "." + url_parsed[1];
            else {
                domain += url_parsed[url_parsed.length - 2] + "." + url_parsed[url_parsed.length - 1];
                for (int i = 0; i < url_parsed.length - 2; i++)
                    subdomain += url_parsed[i];
                if (subdomain.equals("www"))
                    subdomain = "";
            }
            hibernateQuery.queryString("SELECT w FROM Website w WHERE w.login_url LIKE :domain ORDER BY w.db_id ASC");
            hibernateQuery.setParameter("domain", "%" + domain + "%");
            hibernateQuery.cacheQuery();
            List<Website> websites = hibernateQuery.list();
            int last_value = 0;
            Website last_website = null;
            for (Website website : websites) {
                System.out.println(website.getDb_id());
                int match_value = website.matchUrl(subdomain, domain, path);
                if (match_value == 3) {
                    if (website.matchInformationSet(information_names))
                        return website;
                } else if (match_value > 0 && match_value > last_value) {
                    if (website.matchInformationSet(information_names)) {
                        last_value = match_value;
                        last_website = website;
                    }
                }
            }
            return last_website;
        } catch (MalformedURLException e) {
            throw new HttpServletException(HttpStatus.BadRequest, "This is not a valid url");
        }
    }

    public List<Website> getPublicCatalogWebsites(HibernateQuery hibernateQuery) {
        return this.getWebsites(hibernateQuery).stream().filter(website -> website.getWebsiteAttributes().isPublic_website()).collect(Collectors.toList());
    }
}
